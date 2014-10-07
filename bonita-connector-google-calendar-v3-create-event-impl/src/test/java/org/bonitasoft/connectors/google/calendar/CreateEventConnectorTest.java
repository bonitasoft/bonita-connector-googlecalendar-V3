package org.bonitasoft.connectors.google.calendar;

import java.util.Arrays;
import java.util.Map;

import org.bonitasoft.connectors.google.calendar.common.EventConnectorTest;
import org.junit.Ignore;
import org.junit.Test;

public class CreateEventConnectorTest extends EventConnectorTest {

    @Ignore
    @Test
    public void shoudWork() throws Exception {
        final Map<String, Object> inputParameters = getInputParameters();

        // Main
        inputParameters.put("startDate", "2014-09-05");
        inputParameters.put("startTime", "14:00");
        inputParameters.put("startTimeZone", "Europe/Paris");
        inputParameters.put("endDate", "2014-09-05");
        inputParameters.put("endTime", "10:00");
        inputParameters.put("endTimeZone", "America/Los_Angeles");
        inputParameters.put("allDay", false);

        // OPTIONAL 1
        inputParameters.put("description", "description of the test");
        inputParameters.put("summary", "Test-" + getCurrentTime());
        inputParameters.put("location", "location of the test");

        inputParameters.put("transparency", "transparent");
        inputParameters.put("visibility", "public");

        // OPTIONAL 2
        inputParameters.put("anyoneCanAddSelf", true);

        // inputParameters.put("attendeesEmails", Collections.singletonList("charles.souillard@bonitasoft.com"));
        inputParameters.put("sendNotifications", true);
        inputParameters.put("guestsCanInviteOthers", true);
        inputParameters.put("guestsCanSeeOtherGuests", true);

        inputParameters.put("sourceTitle", "Bonitasoft");
        inputParameters.put("sourceUrl", "http://www.bonitasoft.com");

        inputParameters.put("status", "confirmed");// confirmed, tentative, cancelled
        inputParameters.put("colorId", "3");
        // inputParameters.put("id", UUID.randomUUID().toString());

        // GADGET
        // // final Map<String, String> gadgetPreferences = new HashMap<String, String>();
        // // gadgetPreferences.put("Format", "0");
        // // gadgetPreferences.put("Days", "10");
        // // inputParameters.put("gadgetDisplay", "icon");// icon or chip
        // // inputParameters.put("gadgetIconLink", "https://www.thefreedictionary.com/favicon.ico");
        // // inputParameters.put("gadgetLink", "https://www.thefreedictionary.com/_/WoD/wod-module.xml");
        // // inputParameters.put("gadgetPreferences", gadgetPreferences);
        // // inputParameters.put("gadgetTitle", "Word of the Day");
        // // inputParameters.put("gadgetType", "application/x-google-gadgets+xml");
        // // inputParameters.put("gadgetHeight", 136);
        // // inputParameters.put("gadgetWidth", 300);
        //
        // // inputParameters.put("maxAttendees", 100);
        // // inputParameters.put("prettyPrint", true);

        // inputParameters.put("originalStartDate", "2014-07-29");
        // inputParameters.put("originalStartTime", "14:00");
        // inputParameters.put("originalStartTimeZone", "Europe/Paris");
        // inputParameters.put("recurrence", Collections.singletonList("RRULE:FREQ=YEARLY"));

        inputParameters.put("reminderOverrides", Arrays.asList("email:4", "popup:10"));
        // inputParameters.put("reminderUseDefault", true);

        // // inputParameters.put("sequence", 3228);

        final CreateEventConnector connector = new CreateEventConnector();
        final Map<String, Object> outputParameters = executeConnector(connector, inputParameters, true);
    }

}
