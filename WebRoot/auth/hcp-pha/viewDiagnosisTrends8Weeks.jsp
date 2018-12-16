<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewDiagnosisStatisticsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%
    //log the page view
    loggingAction.logEvent(TransactionType.DIAGNOSIS_TRENDS_VIEW, loggedInMID.longValue(), 0, "");

    ViewDiagnosisStatisticsAction diagnoses = new ViewDiagnosisStatisticsAction(prodDAO);
    ArrayList<DiagnosisStatisticsBean> dsBeans= new ArrayList<DiagnosisStatisticsBean>();


    //get form data
    String startDate = request.getParameter("startDate");
    //String endDate = request.getParameter("endDate");


    String zipCode = request.getParameter("zipCode");

    String icdCode = request.getParameter("icdCode");

    long x0 = 0, x1 = 0, x2 = 0, x3 = 0, x4 = 0, x5 = 0, x6 = 0, x7 = 0;
    long y0 = 0, y1 = 0, y2 = 0, y3 = 0, y4 = 0, y5 = 0, y6 = 0, y7 = 0;

    if (zipCode == null)
        zipCode = "";
    if (startDate == null)
        startDate = "";
    if (icdCode == null)
        icdCode = "";

    //try to get the statistics. If there's an error, print it. If null is returned, it's the first page load
    try{
        dsBeans = diagnoses.getEightWeeksCount(startDate, icdCode, zipCode);
        if(dsBeans.size()==8){
            x0 = dsBeans.get(0).getRegionStats();
            y0 = dsBeans.get(0).getZipStats();
            x1 = dsBeans.get(1).getRegionStats();
            y1 = dsBeans.get(1).getZipStats();
            x2 = dsBeans.get(2).getRegionStats();
            y2 = dsBeans.get(2).getZipStats();
            x3 = dsBeans.get(3).getRegionStats();
            y3 = dsBeans.get(3).getZipStats();
            x4 = dsBeans.get(4).getRegionStats();
            y4 = dsBeans.get(4).getZipStats();
            x5 = dsBeans.get(5).getRegionStats();
            y5 = dsBeans.get(5).getZipStats();
            x6 =  dsBeans.get(6).getRegionStats();
            y6 = dsBeans.get(6).getZipStats();
            x7 = dsBeans.get(7).getRegionStats();
            y7 = dsBeans.get(7).getZipStats();
        }

    } catch(FormValidationException e){
        e.printHTML(pageContext.getOut());
    }



%>
<br />
<form action="viewDiagnosisStatistics.jsp" method="post" id="formMain">
    <input type="hidden" name="viewSelect" value="trends2" />
    <table class="fTable" align="center" id="diagnosisStatisticsSelectionTable">
        <tr>
            <th colspan="4">Diagnosis Statistics</th>
        </tr>
        <tr class="subHeader">
            <td>Diagnosis:</td>
            <td>
                <select name="icdCode" style="font-size:10" >
                    <option value="">-- None Selected --</option>
                    <%for(DiagnosisBean diag : diagnoses.getDiagnosisCodes()) { %>
                    <%if (diag.getICDCode().equals(icdCode)) { %>
                    <option selected="selected" value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
                        - <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
                    <% } else { %>
                    <option value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
                        - <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
                    <% } %>
                    <%}%>
                </select>
            </td>
            <td>Zip Code:</td>
            <td ><input name="zipCode" value="<%= StringEscapeUtils.escapeHtml(zipCode) %>" /></td>
        </tr>
        <tr class="subHeader">
            <td>Specific Date:</td>
            <td>
                <input name="startDate" value="<%= StringEscapeUtils.escapeHtml("" + (startDate)) %>" size="10">
                <input type=button value="Select Date" onclick="displayDatePicker('startDate');">
            </td>
        </tr>
        <tr>
            <td colspan="4" style="text-align: center;"><input type="submit" id="select_diagnosis" value="View Statistics"></td>
        </tr>
    </table>

</form>
<% if (dsBeans.size()>0) {  %>
<div id="chart_div"></div>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript" >
    google.charts.load('current', {'packages': ['corechart']});

    function drawChart() {

        var data = google.visualization.arrayToDataTable([
                ["Week","Regional Stats","Zipcode Stats"],
                ["First Week",  <%=x0%>, <%=y0%>],
                ["Second Week", <%=x1%>, <%=y1%>],
                ["Thirth Week", <%=x2%>, <%=y2%>],
                ["Fourth Week", <%=x3%>, <%=y3%>],
                ["Fifth Week", <%=x4%>, <%=y4%>],
                ["Sixth Week", <%=x5%>, <%=y5%>],
                ["Seven Week", <%=x6%>, <%=y6%>],
                ["Eight Week", <%=x7%>, <%=y7%>],
            ]);

            var options = {'title':'Diagonosis Trends In Eight Weeks',
                'height':300};

            var chart = new google.visualization.BarChart(document.getElementById('chart_div'));
            chart.draw(data, options);

    }

    google.charts.setOnLoadCallback(drawChart);
</script>

<%} %>

<br />
<br />
<br />
