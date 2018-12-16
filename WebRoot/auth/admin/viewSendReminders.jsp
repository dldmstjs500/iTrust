<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<h3 id="check">Send Reminders</h3>
<form method="post" action="viewSendReminderResult.jsp">
    <table>
        <tr style="text-align: right;">
            <td>
                <p>Select the number of in-advance days:</p>
                <input type="text" name="number"/>
            </td>
        </tr>

        <tr style="text-align: center;">
            <td colspan="3">
                <input type="submit" name="sendReminders" value="Send Appointment Reminders"/>
            </td>
        </tr>
    </table>
</form>

