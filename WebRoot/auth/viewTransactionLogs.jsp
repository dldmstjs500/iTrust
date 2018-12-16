<%@page import="edu.ncsu.csc.itrust.enums.TransactionType"%>
<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - View Transaction Logs";
%>
<%@include file="/header.jsp" %>

<h1>View Transaction Logs</h1>
<form name ="form" method="post" action="viewTransactionResults.jsp" >
    <table>
        <tr>
            <td colspan=2><b>Please pick roles for a logged-in user and a secondary user.</b></td>
        </tr>
        <tr>
            <td>Role for Logged-In User:</td>
            <td>
                <select name="logging">
                    <option value="all">All</option>
                    <option value="admin">Admin</option>
                    <option value="tester">Tester</option>
                    <option value="patient">Patient</option>
                    <option value="hcp">HCP</option>
                    <option value="uap">UAP</option>
                    <option value="er">ER</option>
                    <option value="pha">PHA</option>
                    <option value="lt">LT</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>Role for Secondary User:</td>
            <td>
                <select name="secondary">
                    <option value="all">All</option>
                    <option value="admin">Admin</option>
                    <option value="tester">Tester</option>
                    <option value="patient">Patient</option>
                    <option value="hcp">HCP</option>
                    <option value="uap">UAP</option>
                    <option value="er">ER</option>
                    <option value="pha">PHA</option>
                    <option value="lt">LT</option>
                </select>
            </td>
        </tr>
        <tr>
            <td colspan=2><b>(Optional) Please pick a date or period.</b></td>
        </tr>
        <tr>
            <td>
                <p>Start Date:</p>
                <input name="startdate" onchange="singleDate();" id="startDate"
                       value="<%=StringEscapeUtils.escapeHtml("")%>" size="10">
                <input type="button" value="Select Date"
                       onclick="displayDatePicker('startdate');">
            </td>
            <td>
                <p>End Date:</p>
                <input name="enddate" onchange="singleDate();" id="endDate"
                       value="<%=StringEscapeUtils.escapeHtml("")%>" size="10">
                <input type="button" value="Select Date"
                       onclick="displayDatePicker('enddate');">
            </td>
        </tr>
        <tr>
            <td colspan=2><b>Please pick a transaction type.</b></td>
        </tr>
        <tr>
            <td>Transaction Type:
                <select name="transactionType">
                    <option value ="-1">All</option>
                    <%
                        for(TransactionType type : TransactionType.values()) {
                    %><option
                        value="<%=type.getCode()%>"><%=type.getDescription()%></option>
                    <%
                        }
                    %>
                </select>
            </td>
        </tr>
    </table>
    <tr>
        <td colspan=2 align=center>
            <input type="submit" name="submit" value='View' onclick="singleDate()">
        </td>
        <td colspan=2 align=center>
            <input type="submit" name="summary" value='Summarize' onclick="singleDate()">
        </td>
    </tr>
</form>

<script language="Javascript">
    function singleDate() {
        var start = document.getElementById("startDate");
        var end = document.getElementById("endDate");

        if (start.value != "" && (end.value == null || end.value == "")) {
            end.value = start.value;
        } else if (end.value != "" && (start.value == null || start.value == "")) {
            start.value = end.value;
        }
    }
</script>

</script>
<%@include file="/footer.jsp"%>