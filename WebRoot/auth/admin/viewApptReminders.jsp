<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewDiagnosisStatisticsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - Reminder homepage";

    String view = request.getParameter("viewSelect");
%>

<%@include file="/header.jsp" %>

<%
    if (view != null && !view.equalsIgnoreCase("")) {
        if (view.equalsIgnoreCase("send")) {
%>
<form action="viewApptReminders.jsp" method="post" id="formSelectFlow">
    <select name="viewSelect" style="font-size:10" >
        <option selected="selected" value="send"> Send </option>
        <option value="view"> View </option>
    </select>
    <input type="submit" id="select_View" value="Go">
</form>
<%@include file="viewSendReminders.jsp" %>
<%
} else if(view.equalsIgnoreCase("view")){
%>
<form action="viewApptReminders.jsp" method="post" id="formSelectFlow">
    <select name="viewSelect" style="font-size:10" >
        <option value="send"> Send </option>
        <option selected="selected" value="view"> View </option>
    </select>
    <input type="submit" id="select_View" value="Go">
</form>
<%@include file="viewMyReminders.jsp" %>
<%
} }else {
%>

<form action="viewApptReminders.jsp" method="post" id="formSelectFlow">
    <select name="viewSelect" style="font-size:10" >
        <option value="">-- None Selected --</option>
        <option value="send"> Send </option>
        <option value="view"> View </option>
    </select>
    <input type="submit" id="select_View" value="Go">
</form>
<% } %>

<%@include file="/footer.jsp" %>