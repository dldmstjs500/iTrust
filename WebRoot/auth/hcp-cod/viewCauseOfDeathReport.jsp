<%@ page import="edu.ncsu.csc.itrust.exception.FormValidationException" %>
<%@ page import="edu.ncsu.csc.itrust.beans.CauseOfDeathReportBean" %>
<%@ page import="edu.ncsu.csc.itrust.action.ViewCauseOfDeathReportAction" %>
<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>

<%@include file="/global.jsp" %>
<%
    pageTitle = "iTrust - View Cause of Death Report";
%>
<%@include file="/header.jsp" %>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%
    //log the page view
    loggingAction.logEvent(TransactionType.DEATH_TRENDS_VIEW, loggedInMID.longValue(), 0, "");

    //get form data
    String startYear = request.getParameter("startYear");
    String endYear = request.getParameter("endYear");
    String gender = request.getParameter("gender");
    long loggedinmid = loggedInMID.longValue();


    //Get the cause of death action that will do the logic to pull data
    ViewCauseOfDeathReportAction codAction = new ViewCauseOfDeathReportAction(prodDAO);
    CauseOfDeathReportBean codBean = null;
    //If all the data is entered, try to build the report (catch the error if fails to validate)
    if(startYear != null && endYear != null && gender != null) {
        try {
            codBean = codAction.getReport(startYear, endYear, gender, loggedinmid);
        } catch (FormValidationException e) {
            e.printHTML(pageContext.getOut());
        }
    }

    //Initialize values
    if(startYear == null)
        startYear = "";
    if(endYear == null)
        endYear = "";
    if(gender == null)
        gender = "";
%>


<br />
<form action="viewCauseOfDeathReport.jsp" method="post" id="formMain">
    <br>
    Gender:
    <select name="gender" style="font-size:10" >
        <option value="All" <%= gender.equals("All") ? "selected" : "" %>>All</option>
        <option value="Male" <%= gender.equals("Male") ? "selected" : "" %>>Male</option>
        <option value="Female" <%= gender.equals("Female") ? "selected" : "" %>>Female</option>>
    </select>
    <br>
    Start Year:
    <input name="startYear" value="<%= StringEscapeUtils.escapeHtml("" + (startYear)) %>" size="10">
    <br>
    End Year:
    <input name="endYear" value="<%= StringEscapeUtils.escapeHtml("" + (endYear)) %>" size="10">
    <br>
    <input type="submit" id="view_report" value="View Report">
</form>
<br />

<% if (codBean != null) { %>
<table class="fTable" align="center" id="causeOfDeathTable">
    <tr>
        <th>Patients</th>
        <th>Code</th>
        <th>Description</th>
        <th>Count</th>
    </tr>
    <tr style="text-align:center;">
        <td>All</td>
        <td><%= codBean.getAllcode1() %></td>
        <td><%= codBean.getAllname1() %></td>
        <td><%= codBean.getAllcount1() %></td>
    </tr>
    <tr style="text-align:center;">
        <td>All</td>
        <td><%= codBean.getAllcode2() %></td>
        <td><%= codBean.getAllname2() %></td>
        <td><%= codBean.getAllcount2() %></td>
    </tr>
    <tr style="text-align:center;">
        <td>Yours</td>
        <td><%= codBean.getHcpcode1() %></td>
        <td><%= codBean.getHcpname1() %></td>
        <td><%= codBean.getHcpcount1() %></td>
    </tr>
    <tr style="text-align:center;">
        <td>Yours</td>
        <td><%= codBean.getHcpcode2() %></td>
        <td><%= codBean.getHcpname2() %></td>
        <td><%= codBean.getHcpcount2() %></td>
    </tr>
</table>
<br/>
<% } %>
<%@include file="/footer.jsp" %>