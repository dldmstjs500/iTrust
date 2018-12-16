<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction" %>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean" %>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO" %>

<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO" %>
<%@page import="edu.ncsu.csc.itrust.exception.DBException" %>
<%@page import="edu.ncsu.csc.itrust.messages.MessagesFilter" %>
<%@page import="java.sql.SQLException" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="edu.ncsu.csc.itrust.exception.ITrustException" %>
<%@ page import="java.text.ParseException" %>

<script src="/iTrust/DataTables/media/js/jquery.dataTables.min.js" type="text/javascript"></script>
<script type="text/javascript">
    jQuery.fn.dataTableExt.oSort['lname-asc'] = function (x, y) {
        var a = x.split(" ");
        var b = y.split(" ");
        return ((a[1] < b[1]) ? -1 : ((a[1] > b[1]) ? 1 : 0));
    };

    jQuery.fn.dataTableExt.oSort['lname-desc'] = function (x, y) {
        var a = x.split(" ");
        var b = y.split(" ");
        return ((a[1] < b[1]) ? 1 : ((a[1] > b[1]) ? -1 : 0));
    };
</script>
<script type="text/javascript">
    $(document).ready(function () {
        $("#mailbox").dataTable({
            "aaColumns": [[2, 'dsc']],
            "aoColumns": [{"sType": "lname"}, null, null, {"bSortable": false}],
            "sPaginationType": "full_numbers"
        });
    });
</script>
<style type="text/css" title="currentStyle">
    @import "/iTrust/DataTables/media/css/demo_table.css";
</style>

<%
    boolean outbox = (Boolean) session.getAttribute("outbox");
    boolean isHCP = (Boolean) session.getAttribute("isHCP");

    String pageName = outbox ? "messageOutbox.jsp" : "messageInbox.jsp";
    String messageNameType = outbox ? "Receiver" : "Sender";
    String messageTimeType = outbox ? "Sent" : "Received";

    PersonnelDAO personnelDAO = new PersonnelDAO(prodDAO);
    PatientDAO patientDAO = new PatientDAO(prodDAO);

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    ViewMyMessagesAction action = new ViewMyMessagesAction(prodDAO, loggedInMID.longValue());

%>

<%
    // Get the list of messages for the specified user.
    List<MessageBean> messages = outbox ? action.getAllMySentMessages() : action.getAllMyMessages();

    boolean isTestingFilter = request.getParameter("testFilter") != null;
    boolean saveFilter = request.getParameter("saveFilter") != null;
    MessagesFilter filter;
    String filterString;

    // Obtain filter, either based on stored filter or testing filter. Filter messages against it.
    if (!saveFilter && !isTestingFilter) {
        filterString = isHCP ? personnelDAO.getMessageFilter(loggedInMID.longValue()) : patientDAO.getMessageFilter(loggedInMID.longValue());
        filter = MessagesFilter.fromFilterString(filterString);
    }
    else {
        filter = MessagesFilter.builder()
                .name(request.getParameter("sender").replace(",", ""))
                .subject(request.getParameter("subject").replace(",", ""))
                .includedWords(request.getParameter("includedWords").replace(",", ""))
                .excludedWords(request.getParameter("excludedWords").replace(",", ""))
                .startDate(request.getParameter("startDate").replace(",", ""))
                .endDate(request.getParameter("endDate"))
                .build();
        filterString = filter.convertToFilterString();
        // Save the filter if requested.
        if (saveFilter && isHCP) {
            personnelDAO.saveMessageFilter(filterString, loggedInMID.longValue());
        }
        else if (saveFilter) {
            patientDAO.saveMessageFilter(filterString, loggedInMID.longValue());
        }
    }

    // Use filter to filter the messages
    try {
        messages = action.filterMessagesBidir(messages, filterString, outbox);
    } catch (Exception e) {
        messages = new ArrayList<>();
    }
    session.setAttribute("messages", messages);
%>
<legend></legend>
<h3>Filter <%=isTestingFilter ? " (Testing filter)" : ""%></h3>
<form method="post" action="<%=pageName%>?filter=true">
    <table>
        <tr style="text-align: right;">
            <td>
                <label for="sender"><%=messageNameType%>: </label>
                <input type="text" name="sender" id="sender"
                       value="<%= StringEscapeUtils.escapeHtml("" + filter.getName() ) %>"/>
            </td>
            <td style="padding-left: 10px; padding-right: 10px;">
                <label for="hasWords">Has the words: </label>
                <input type="text" name="includedWords" id="includedWords"
                       value="<%= StringEscapeUtils.escapeHtml("" + filter.getIncludedWords()) %>"/>
            </td>
            <td>
                <label for="startDate">Start Date: </label>
                <input type="text" name="startDate" id="startDate"
                       value="<%= StringEscapeUtils.escapeHtml("" + filter.getStartDate()) %>"/>
                <input type="button" value="Select Date" onclick="displayDatePicker('startDate');"/>
            </td>
        </tr>
        <tr style="text-align: right;">
            <td>
                <label for="subject">Subject: </label>
                <input type="text" name="subject" id="subject"
                       value="<%= StringEscapeUtils.escapeHtml("" + filter.getSubject()) %>"/>
            </td>
            <td style="padding-left: 10px; padding-right: 10px;">
                <label for="notWords">Does not have the words: </label>
                <input type="text" name="excludedWords" id="excludedWords"
                       value="<%= StringEscapeUtils.escapeHtml("" + filter.getExcludedWords()) %>"/>
            </td>
            <td>
                <label for="endDate">End Date: </label>
                <input type="text" name="endDate" id="endDate"
                       value="<%= StringEscapeUtils.escapeHtml("" + filter.getEndDate()) %>"/>
                <input type="button" value="Select Date" onclick="displayDatePicker('endDate');"/>
            </td>
        </tr>
        <tr style="text-align: center;">
            <td colspan="3">
                <input type="submit" name="testFilter" value="Test Filter"/>
                <input type="submit" name="saveFilter" value="Save"/>
                <input type="submit" name="cancel" value="Cancel"/>
            </td>
        </tr>
    </table>
</form>

<c:if test="${!messages.isEmpty()}">
    <table id="mailbox" class="display fTable">
        <thead>
        <tr>
            <th><%=messageNameType%>
            </th>
            <th>Subject</th>
            <th><%=messageTimeType%>
            </th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <%
            int index=-1;
            for(MessageBean message : messages) {
                String style = "";
                if(message.getRead() == 0) {
                    style = "style=\"font-weight: bold;\"";
                }

                if(!outbox || message.getOriginalMessageId()==0){
                    index ++;
        %>
        <tr <%=style%>>
            <td><%= StringEscapeUtils.escapeHtml("" + ( action.getMessageToString(outbox, message))) %></td>
            <td><%= StringEscapeUtils.escapeHtml("" + ( message.getSubject() )) %></td>
            <td><%= StringEscapeUtils.escapeHtml("" + ( dateFormat.format(message.getSentDate()) )) %></td>
            <td><a href="<%= outbox?"viewMessageOutbox.jsp?msg=" + StringEscapeUtils.escapeHtml("" + ( index )):"viewMessageInbox.jsp?msg=" + StringEscapeUtils.escapeHtml("" + ( index )) %>">Read</a></td>
        </tr>
        <%
                }

            }
        %>
        </tbody>
    </table>
</c:if>
<c:if test="${messages.isEmpty()}">
    <div>
        <i>You have no messages</i>
    </div>
</c:if>
