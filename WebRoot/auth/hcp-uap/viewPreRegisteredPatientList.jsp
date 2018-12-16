<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientOfficeVisitHistoryAction"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Pre-Registered Patients";
%>

<%@include file="/header.jsp" %>

<%
ViewPatientOfficeVisitHistoryAction action = new ViewPatientOfficeVisitHistoryAction(prodDAO, loggedInMID.longValue());

String patientString = "";
if (request.getParameter("patient") != null) {
	patientString = request.getParameter("patient");
	int patientIndex = Integer.parseInt(patientString);
	List<PatientBean> patientsfirst = (List<PatientBean>) session.getAttribute("patients");
	if(request.getParameter("r").equals("1"))
	    action.registerPatient(patientsfirst.get(patientIndex));
	else
		action.deregisterPatient(patientsfirst.get(patientIndex));
	session.removeAttribute("patients");
}
	List<PatientBean> patients = action.getPreRegisteredPatients();


%>
<h2>PreRegistered Patients</h2>
<table class="display fTable" id="patientList" align="center">
	
	<thead>


	<tr class="">
		<th>Patient</th>
		<th>Address</th>
		<th>Register</th>
		<th>Deregister</th>
	</tr>
	</thead>
	<tbody>
	<%
		int index = 0;
		for (PatientBean bean : patients) {
	%>
	<tr>
		<td >
			<a href="editPHR.jsp?patient=<%= StringEscapeUtils.escapeHtml("" + (index)) %>">
		
		
			<%= StringEscapeUtils.escapeHtml("" + (bean.getFullName())) %>
		
		
			</a>
			</td>
		<td ><%= StringEscapeUtils.escapeHtml("" + (bean.getStreetAddress1() +" " +bean.getStreetAddress2())) %></td>
		<td><a href="viewPreRegisteredPatientList.jsp?r=1&patient=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" ><input id="regButton<%= StringEscapeUtils.escapeHtml(index+"") %>" type="button" value="Register" ></a></td>
		<td><a href="viewPreRegisteredPatientList.jsp?r=0&patient=<%= StringEscapeUtils.escapeHtml("" + (index)) %>" ><input id="deButton<%= StringEscapeUtils.escapeHtml(index+"") %>" type="button" value="Deregister" ></a></td>

	</tr>
	<%
			index ++;
		}
		session.setAttribute("patients", patients);
	%>
	</tbody>
</table>
<br />
<br />

<%@include file="/footer.jsp" %>
