<%@page import ="edu.ncsu.csc.itrust.action.ViewTransactionLogs" %>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.beans.TransactionBean"%>
<%@page import="java.util.List"%>
<%@include file="/global.jsp"%>

<%
    boolean viewSummary = request.getParameter("summary") != null;
    if (viewSummary) {
        // If the user wants to view the summary info, dispatch them there.
        RequestDispatcher dispatcher = request.getRequestDispatcher("summaryTransactionResult.jsp");
        dispatcher.forward(request, response);
    }
    pageTitle = "iTrust - Transaction Logs Result";
%>

<%@include file="/header.jsp"%>
<%
    ViewTransactionLogs action = new ViewTransactionLogs(prodDAO);

    String loggingRole = request.getParameter("logging");
    String secondary = request.getParameter("secondary");
    String startdate = request.getParameter("startdate");

    String enddate = request.getParameter("enddate");

    int code = Integer.parseInt(request.getParameter("transactionType"));
    Date start = null;
    Date end = null;
    if(!(startdate.compareTo("") == 0 && enddate.compareTo("") == 0)) {
        startdate = startdate.trim();
        enddate = enddate.trim();
        SimpleDateFormat frmt = new SimpleDateFormat(
                "MM/dd/yyyy hh:mm a");
        start = frmt.parse(startdate + " 00:00 AM");
        end = frmt.parse(enddate + " 00:00 AM");
    }
    if(loggingRole.compareTo("all") == 0){
            loggingRole = "";
    }
    if(secondary.compareTo("all") == 0) {
            secondary = "";
    }
    List<TransactionBean> results = action.getRequestTransactions(loggingRole, secondary, start, end, code);

%>
<h1>Transaction Logs Results</h1>
<ul style="list-style-type: none;">
    <%
        if(request.getParameter("submit")!= null){
    %>
        <table class="fTable" border=1 align="left">
            <tr>
                <th>Logged-In User Role</th>
                <th>Secondary User Role</th>
                <th>Transaction Type</th>
                <th>Additional Info</th>
                <th>Time Stamp</th>
            </tr>
    <%
            int index = 0;
            for (TransactionBean req : results) {
                out.print("<tr id=\"tableRow" + index + "\">");
                out.print("<td>" + req.getLoggedInRole() + "</td>");
                out.print("<td>" + req.getSecondaryRole() + "</td>");
                out.print("<td>" + req.getTransactionType().name() + "</td>");
                out.print("<td>" + req.getAddedInfo() + "</td>");
                out.print("<td>" + req.getTimeLogged() + "</td>");
                out.println("</tr>");
                index ++;
            }

        }else{
            out.println("<h1>Something went wrong! :/</h1>");
        }
    %>
</ul>

<%@include file="/footer.jsp"%>