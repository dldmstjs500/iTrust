<%@page import ="edu.ncsu.csc.itrust.action.ViewTransactionLogs" %>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.beans.TransactionBean"%>
<%@page import="edu.ncsu.csc.itrust.enums.Role"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.ArrayList" %>
<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - Transaction Logs Result Summary";
%>

<%@include file="/header.jsp"%>
<%
    ViewTransactionLogs action = new ViewTransactionLogs(prodDAO);

    String loggingRole = request.getParameter("logging");
    String secondary = request.getParameter("secondary");
    String startdate = request.getParameter("startdate");
    String enddate = request.getParameter("enddate");

    int startMonth = 6;
    int startYear = 2007;
    String endDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
    int endMonth = Integer.parseInt(endDate.substring(0,2));
    int endYear = Integer.parseInt(endDate.substring(6,10));

    int code = Integer.parseInt(request.getParameter("transactionType"));
    Date start = null;
    Date end = null;
    if(!(startdate.compareTo("") == 0 && enddate.compareTo("") == 0)) {
        startdate = startdate.trim();
        enddate = enddate.trim();

        startMonth = Integer.parseInt(startdate.substring(0,2));
        startYear = Integer.parseInt(startdate.substring(6,10));
        endMonth = Integer.parseInt(enddate.substring(0,2));
        endYear = Integer.parseInt(enddate.substring(6,10));

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

    // 1. the x axis shows the roles for the logged-in user field and the y axis shows the total number of target transaction logs for each role for the logged-in user field
    // 2. the x axis shows the roles for the secondary user field and the y axis shows the total number of target transaction logs for each role for the secondary user field
    List<String> roles = new ArrayList<>();
    List<Integer> loggedInCount = new ArrayList<>();
    List<Integer> secondaryCount = new ArrayList<>();
    List<String> types = new ArrayList<>();
    List<Integer> typeCount = new ArrayList<>();
    for (Role role : Role.values()) {
        roles.add(role.getUserRolesString());
        loggedInCount.add(action.getNumForLoggedIn(results, role.getUserRolesString()));
        secondaryCount.add(action.getNumForSecondary(results, role.getUserRolesString()));
    }
    // 3. the x axis shows a specific month of a specific year and the y axis shows the total number of target transaction logs for each specific month of specific year
    // (the left most bar corresponds the least recent month/year in the target transaction logs and the right most bar corresponds the most recent month/year in the target transaction logs)
    List<String> months = new ArrayList<>();
    List<Integer> monthlyCount = new ArrayList<>();
    for (int i = startYear; i < endYear + 1; i++){
        for (int j = 1; j < 13; j++){
            if(i == startYear && j < startMonth) continue;
            if(i == endYear && j > endMonth) break;
            months.add(j + "/" + i);
            monthlyCount.add(action.getNumForMonth(results, i, j));
        }
    }
    // 4. the x axis shows the transaction types and the y axis shows the total number of target transaction logs for each transaction type
    for (TransactionType type : TransactionType.values()) {
        types.add(type.name());
        typeCount.add(action.getNumForType(results, type.getCode()));
    }
%>
<h1>Transaction Logs Result Summary</h1>
<ul style="list-style-type: none;">
    <%
        String msg = "";
        if(request.getParameter("summary")!= null){
            out.println("<div id=\"first_div\"></div>");
            out.println("<div id=\"second_div\"></div>");
            out.println("<div id=\"monthly_div\"></div>");
            out.println("<div id=\"type_div\"></div>");
        }else{
            out.println("<h1>Something went wrong!:/</h1>");
        }
    %>
</ul>


<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">

    // Load the Visualization API and the corechart package.
    google.charts.load('current', {'packages':['corechart']});

    // Set a callback to run when the Google Visualization API is loaded.
    google.charts.setOnLoadCallback(drawLoggedInChart);
    google.charts.setOnLoadCallback(drawSecondaryChart);
    google.charts.setOnLoadCallback(drawMonthlyChart);
    google.charts.setOnLoadCallback(drawTypeChart);

    // Callback that creates and populates a data table,
    // instantiates the pie chart, passes in the data and
    // draws it.
    function drawLoggedInChart() {

        // Create the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Role');
        data.addColumn('number', 'Transaction Logs Count');
        data.addRows([
            <%for (int i = 0; i < roles.size(); i++){%>
                ['<%= roles.get(i) %>', <%= loggedInCount.get(i) %>],
            <%}%>
        ]);

        // Set chart options
        var options = {'title':'Logged-In Role vs Transaction Logs',
            'width':800,
            'height':300,
            legend: 'none',};

        // Instantiate and draw our chart, passing in some options.
        var chart = new google.visualization.ColumnChart(document.getElementById('first_div'));
        chart.draw(data, options);
    }

    function drawSecondaryChart() {

        // Create the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Role');
        data.addColumn('number', 'Transaction Logs Count');
        data.addRows([
            <%for (int i = 0; i < roles.size(); i++){%>
            ['<%= roles.get(i) %>', <%= secondaryCount.get(i) %>],
            <%}%>
        ]);

        // Set chart options
        var options = {'title':'Secondary Role vs Transaction Logs',
            'width':800,
            'height':300,
            legend: 'none',};

        // Instantiate and draw our chart, passing in some options.
        var chart = new google.visualization.ColumnChart(document.getElementById('second_div'));
        chart.draw(data, options);
    }

    function drawMonthlyChart() {

        // Create the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Month/Year');
        data.addColumn('number', 'Transaction Logs Count');
        data.addRows([
            <%for (int i = 0; i < months.size(); i++){%>
            ['<%= months.get(i) %>', <%= monthlyCount.get(i) %>],
            <%}%>
        ]);

        // Set chart options
        var options = {'title':'Monthly Log Count',
            'width':800,
            'height':300,
            legend: 'none',};

        // Instantiate and draw our chart, passing in some options.
        var chart = new google.visualization.ColumnChart(document.getElementById('monthly_div'));
        chart.draw(data, options);
    }

    function drawTypeChart() {

        // Create the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Type');
        data.addColumn('number', 'Count');
        data.addRows([
            <%for (int i = 0; i < types.size(); i++){%>
            ['<%= types.get(i) %>', <%= typeCount.get(i) %>],
            <%}%>
        ]);

        // Set chart options
        var options = {'title':'Transaction Type vs. Transaction Count',
            'width':800,
            'height':300,
            legend: 'none',};

        // Instantiate and draw our chart, passing in some options.
        var chart = new google.visualization.ColumnChart(document.getElementById('type_div'));
        chart.draw(data, options);
    }

</script>
<%@include file="/footer.jsp"%>