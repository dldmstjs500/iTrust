<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.AddPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@ page import="edu.ncsu.csc.itrust.enums.Role" %>
<%@ page import="edu.ncsu.csc.itrust.RandomPassword" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Add Pre-Registered Patient";
%>

<%@include file="/header.jsp" %>

<%
boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");
String error = "";
if (formIsFilled) {
    error = "";
	PatientBean p = new BeanBuilder<PatientBean>().build(request.getParameterMap(), new PatientBean());

	String fullname;
	long newMID = 1L;
	try{
	    String newPass = p.getPassword();
	    String confirmPass = request.getParameter("confirmPassword");
		String containsLetter = "[a-zA-Z0-9]*[a-zA-Z]+[a-zA-Z0-9]*";
		String containsNumber = "[a-zA-Z0-9]*[0-9]+[a-zA-Z0-9]*";
		String fiveAlphanumeric = "[a-zA-Z0-9]{5,20}";

		//Make sure new passwords match
		if (!newPass.equals(confirmPass)) {
		    error = "Passwords don't match";
		    %>
<div align=center>
	<span class="iTrustMessage">Error: <%= StringEscapeUtils.escapeHtml("" + (error)) %></span>
</div>
<%
		}

		//Validate password. Must contain a letter, contain a number, and be a string of 5-20 alphanumeric characters
		else if(!(newPass.matches(containsLetter) && newPass.matches(containsNumber) && newPass.matches(fiveAlphanumeric))) {
			error =  "Password formatted incorrectly";
%>
<div align=center>
	<span class="iTrustMessage">Error: <%= StringEscapeUtils.escapeHtml("" + (error)) %></span>
</div>
<%
		}
		else {

			Long tempMID = new Long(0L);
			AddPatientAction pa = new AddPatientAction(prodDAO, tempMID.longValue());
			newMID = pa.addPreRegisteredPatient(p, request.getParameter("password"));
			session.setAttribute("pid", Long.toString(newMID));




			fullname = p.getFullName();

			loggingAction.logEvent(TransactionType.PATIENT_PREREGISTER, newMID, 0, "");


%>
	<div align=center>
		<div id="mid" name="<%= StringEscapeUtils.escapeHtml("" + (newMID)) %>"></div>
		<span class="iTrustMessage">New Pre-registered Patient <%= StringEscapeUtils.escapeHtml("" + (fullname)) %> successfully added!</span>
		<br />
		<table class="fTable">
			<tr>
				<th colspan=2>In order to use the iTrust medical system, an HCP needs to activate this account.</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">MID:</td>
				<td><%= StringEscapeUtils.escapeHtml("" + (newMID)) %></td>
			</tr>
		</table>
	</div>
<%
	}
	} catch(FormValidationException e){
%>
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
	</div>
<%
	}
}
%>

<div align=center>
<form action="addPreregisteredPatient.jsp" method="post">
	<input type="hidden" name="formIsFilled" value="true"> <br />
<br />
<div style="width: 50%; text-align:left;">Please enter in the name of the new pre-registered
patient, with a valid email address.</div>
<br />
<br />
<table class="fTable">
	<tr>
		<th colspan=2>Pre-Registered Patient Information</th>
	</tr>
	<tr>
		<td class="subHeaderVertical">First name:</td>
		<td><input type="text" name="firstName"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Last Name:</td>
		<td><input type="text" name="lastName">
	</tr>
	<tr>
		<td class="subHeaderVertical">Email:</td>
		<td><input type="text" name="email"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Password:</td>
		<td><input type="password" name="password"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Confirm Password:</td>
		<td><input type="password" name="confirmPassword"></td>
	</tr>

	<tr>
		<th colspan=2>Optional Information</th>
	</tr>

	<tr>
		<td class="subHeaderVertical">Address:</td>
		<td><input type="text" name="streetAddress1"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">City:</td>
		<td><input type="text" name="city"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">State: </td>
		<td><select name="state">
		<% 	String[] x = {"AK","AL","AR","AZ","CA","CO","CT","DE","DC","FL","GA","HI","IA","ID","IL","IN","KS","KY","LA","MA","MD","ME","MI","MN","MO","MS","MT","NC","ND","NE","NH","NJ","NM","NV","NY","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT","VA","VT","WA","WI","WV","WY"};
			for(String state: x)
			{%>
                <option value="<%=state%>"><%=state%></option>
			<%}%>
        </select></td>

	</tr>
	<tr>
		<td class="subHeaderVertical">zip:</td>
		<td><input type="text" name="zip"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Phone:</td>
		<td><input type="text" name="phone"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Insurance Name:</td>
		<td><input type="text" name="icName"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Insurance Address:</td>
		<td><input type="text" name="icAddress1"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Insurance City:</td>
		<td><input type="text" name="icCity"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Insurance State: </td>
        <td><select name="icState">
        <% 	for(String state: x)
            {%>
        <option value="<%=state%>"><%=state%></option>
        <%}%>
        </select></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Insurance zip:</td>
		<td><input type="text" name="icZip"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Insurance Phone:</td>
		<td><input type="text" name="icPhone"></td>
	</tr>
	<tr>
	<td class="subHeaderVertical">Height:</td>
	<td><input type="text" name="height"></td>
</tr>
	<tr>
	<td class="subHeaderVertical">Weight:</td>
	<td><input type="text" name="weight"></td>
</tr>
	<tr>
	<td class="subHeaderVertical">Smoker?: type in yes or no</td>
	<td><input type="text" name="smoker"></td>
</tr>

</table>
<br />
<input type="submit" style="font-size: 16pt; font-weight: bold;" value="Add pre-registered patient">
</form>
<br />
</div>
<%@include file="/footer.jsp" %>
