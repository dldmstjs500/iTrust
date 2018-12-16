<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ApptDAO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@ page import="edu.ncsu.csc.itrust.beans.ApptBean" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction" %>
<%@ page import="edu.ncsu.csc.itrust.dao.mysql.MessageDAO" %>
<%@ page import="edu.ncsu.csc.itrust.beans.Email" %>
<%@ page import="edu.ncsu.csc.itrust.EmailUtil" %>
<%@include file="/global.jsp" %>
<%
    pageTitle = "iTrust - Reminders Result";
%>

<%
    String size= request.getParameter("number");
    String isSubmit = request.getParameter("sendReminders");
    ApptDAO apptDAO = prodDAO.getApptDAO();
    ViewMyApptsAction action = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
    MessageDAO messageDAO = prodDAO.getMessageDAO();
    PatientDAO patients = new PatientDAO(prodDAO);
    EmailUtil emailer = new EmailUtil(prodDAO);
%>
<%@include file="/header.jsp" %>
<%
if(isSubmit!=null){
    int n = Integer.parseInt(size);

    List<ApptBean> apptBeans = apptDAO.getAllApptForComingDays(n);
    if(apptBeans == null){
        %>
<p>There is no appointment avaiable. Please re enter the days in advance. </p>>
<tr>
    <td colspan="2"><a href="viewApptReminders.jsp">Back</a></td>
</tr>
        <%
    }else{

        %>
<table id="mailbox" class="display fTable">
    <thead>
    <tr>
        <th>Subject</th>
        <th>Name</th>
        <th>Timestamp</th>
    </tr>
    </thead>
    <tbody>
    <%
    session.setAttribute("apptBeans", apptBeans);
    int index = -1;
    for(ApptBean apptBean : apptBeans) {
        //calculate the days
        Timestamp tm = apptBean.getDate();
        Date upper = new Date(tm.getTime());
        Date cur = new Date();
        Calendar upperCal = Calendar.getInstance();
        Calendar curCal = Calendar.getInstance();
        upperCal.setTime(upper);
        curCal.setTime(cur);

        long days = (long)(upperCal.getTimeInMillis() - curCal.getTimeInMillis())/(1000*60*60*24);
        index++;

        String subject = "Reminder: upcoming appointment in " + days + " day(s)";
        String message = "You have an appointment on "+ apptBean.getDate()+ " with Dr." + action.getName(apptBean.getHcp());

        long systemReminder = 9000000009L;
        MessageBean messageBean = new MessageBean();
        messageBean.setTo(apptBean.getPatient());
        messageBean.setFrom(systemReminder);
        messageBean.setSentDate(tm);
        messageBean.setSubject(subject);
        messageBean.setBody(message);
        messageDAO.addMessage(messageBean);
        //send email
        Email myEmail = new Email();

        List<String> toList = new ArrayList<String>();
        PatientBean myPatient = patients.getPatient(apptBean.getPatient());
        toList.add(myPatient.getEmail());
        myEmail.setToList(toList);
        myEmail.setFrom("System Reminder");
        myEmail.setBody(message);
        myEmail.setSubject(subject);

        emailer.sendEmail(myEmail);

        %>
        <tr>
            <td><%= StringEscapeUtils.escapeHtml(subject) %></td>
            <td><%= StringEscapeUtils.escapeHtml("" + (action.getName(apptBean.getPatient()))) %></td>
            <td><%= StringEscapeUtils.escapeHtml("" + (apptBean.getDate() )) %></td>
        </tr>
        <%
    }

}
}
%>
</tbody>
</table>
<%@include file="/footer.jsp" %>