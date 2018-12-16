<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@ page import="edu.ncsu.csc.itrust.dao.mysql.MessageDAO" %>
<%
    pageTitle = "iTrust - View Reminders";
    long systemReminder = 9000000009L;
    ViewMyMessagesAction action = new ViewMyMessagesAction(prodDAO, systemReminder);
    List<MessageBean> messages = action.getAllMySentMessages();
%>
<h3>View Reminders</h3>

<table id="mailbox" class="display fTable">
    <thead>
    <tr>
        <th>Subject</th>
        <th>Name</th>
        <th>Timestamp</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <%
        if(messages != null){
            int index = -1;
            session.setAttribute("messages", messages);
            for(MessageBean messageBean : messages) {
                index++;
    %>
    <tr>
        <td><%= StringEscapeUtils.escapeHtml("" + messageBean.getSubject()) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (action.getName(messageBean.getTo()))) %></td>
        <td><%= StringEscapeUtils.escapeHtml("" + (messageBean.getSentDate() )) %></td>
        <td><a href="<%= "viewMessageBox.jsp?msg=" + StringEscapeUtils.escapeHtml("" + ( index ))%>">Read</a></td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>
