<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.Date"%>
<%@ page import="java.sql.Timestamp" %>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="java.util.List"%>
<%@ page import="edu.ncsu.csc.itrust.beans.ApptBean" %>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - View Message";
%>

<%@include file="/header.jsp" %>

<%
    ViewMyApptsAction action = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
    String msgParameter = request.getParameter("msg");
    int msgIndex = 0;
    try {
        msgIndex = Integer.parseInt(msgParameter);
    } catch (NumberFormatException nfe) {
        response.sendRedirect("viewMyReminders.jsp");
    }
    List<MessageBean> messages = (List<MessageBean>) session.getAttribute("messages");

    MessageBean original = (MessageBean) messages.get(msgIndex);
        //session.setAttribute("message", original);
%>
<div>
    <table width="99%">
        <tr>
            <td><b>To:</b> <%= StringEscapeUtils.escapeHtml("" + action.getName(original.getTo())) %></td>
        </tr>
        <tr>
            <td><b>From:</b> <%= StringEscapeUtils.escapeHtml("System Reminder") %></td>
        </tr>
        <tr>
            <td><b>Date &amp; Time:</b> <%= StringEscapeUtils.escapeHtml("" + ( original.getSentDate() )) %></td>
        </tr>
        <tr>
            <td><b>Subject:</b> <%= StringEscapeUtils.escapeHtml(""+ original.getSubject()) %></td>
        </tr>
        <tr>
            <td><b>Message:</b> <%= StringEscapeUtils.escapeHtml("" + original.getBody()) %></td>
        </tr>
        <tr>
            <td colspan="2"><a href="viewApptReminders.jsp">Back</a></td>
        </tr>
    </table>
</div>


<%@include file="/footer.jsp" %>
