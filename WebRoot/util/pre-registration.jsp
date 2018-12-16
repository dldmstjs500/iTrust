<%@page import="edu.ncsu.csc.itrust.action.ResetPasswordAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Pre-registered Patient";
%>

<%@include file="/header.jsp"%>

<h1>Pre-registered Patient</h1>

<%--jsp logics--%>

<%
	ResetPasswordAction action = new ResetPasswordAction(prodDAO);
	if (action.isMaxedOut(request.getRemoteAddr())) {
		response.sendRedirect("/iTrust/auth/forward?error=Too+many+attempts!");
	}

	long mid = action.checkMID(request.getParameter("mid"));
	String role = null;

	try {
		role = action.checkRole(mid, request.getParameter("role"));
	} catch (ITrustException e) {
%>
<span >User does not exist with this role and mid.</span>
<%
	}
%>

<%--post this form on the webpage--%>

<form action="/iTrust/util/pre-registration.jsp" method="post">
<table>
<%
	if (mid == 0 || role == null) {
%>
	<tr>
		<td colspan=2><b>Please enter your name, email, and password</b></td>
	</tr>
	<tr>
		<td>Name:</td>
		<td>
			<input type=TEXT maxlength=20 name="Name">
		</td>
	</tr>
	<tr>
		<td>Email:</td>
		<td>
			<input type=TEXT maxlength=30 name="Email">
		</td>
	</tr>
	<tr>
		<td>Password:</td>
		<td>
			<input type=TEXT maxlength=30 name="Password">
		</td>
	</tr>
	<tr>
		<td>Confirm Password:</td>
		<td>
			<input type=TEXT maxlength=30 name="Confirmpassword">
		</td>
	</tr>
	<tr>
		<td colspan=2 align=center>
			<input type="submit" value="Submit">
		</td>
	</tr>

<%
	} else {
		String answer = action.checkAnswerNull(request
		.getParameter("answer"));
		if (answer == null) {
	try {
%>
	<tr>
		<td colspan=2>
			<b><%=StringEscapeUtils.escapeHtml("" + (action.getSecurityQuestion(mid)))%></b>
		</td>
	</tr>
	<tr>
		<td>Answer:</td>
		<td><input type=password maxlength=50 name="answer"> <input
			type=hidden name="mid" value="<%=StringEscapeUtils.escapeHtml("" + (mid))%>"> <input type=hidden
			name="role" value="<%=StringEscapeUtils.escapeHtml("" + (role))%>"></td>
	</tr>
	<tr>
		<td>New Password:</td>
		<td><input type=password maxlength=20 name="password"></td>
	</tr>
	<tr>
		<td>Confirm:</td>
		<td><input type=password maxlength=20 name="confirmPassword"></td>
	</tr>
	<tr>
		<td colspan=2 align=center><input type="submit" value="Submit"></td>
	</tr>

<%
	} catch (ITrustException e) {
%>
	<tr>
		<td>
			<font color='red'>This user has not set a security question/answer.</font>
		</td>
	</tr>
<%
			}
			
		} else {
			String password = request.getParameter("password");
			String confirmPassword = request.getParameter("confirmPassword");
			String confirm = "";
			try {
				confirm = action.resetPassword(mid, role, answer, 
				                               password, confirmPassword, 
				                               request.getRemoteAddr());
				
				if(confirm.contains("Password changed")) {
					loggingAction.logEvent(TransactionType.PASSWORD_RESET, mid, mid, "");
				}
			} catch (FormValidationException e) {
				e.printHTML(pageContext.getOut());
%>
	<tr>
		<td>
			<a href="resetPassword.jsp">
				<h2>Please try again</h2>
			</a>
		</td>
	</tr>
<%
			}
%>
	<tr>
		<td>
			<%= StringEscapeUtils.escapeHtml("" + (confirm)) %>
		</td>
	</tr>

<%
		}
	}
%>
</table>
</form>

<%@include file="/footer.jsp" %>

