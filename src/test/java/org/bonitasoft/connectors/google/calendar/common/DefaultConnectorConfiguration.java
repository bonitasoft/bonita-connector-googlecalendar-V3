package org.bonitasoft.connectors.google.calendar.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DefaultConnectorConfiguration {
    
    public static final Map<String, Object> defaultConfiguration(){
        Map<String, Object> inputParameters = new HashMap<String, Object>();
        inputParameters.put(CalendarConnector.AUTH_MODE, "JSON");
        inputParameters.put(CalendarConnector.SERVICE_ACCOUNT_ID, "anId");
        inputParameters.put(CalendarConnector.SERVICE_ACCOUNT_JSON_TOKEN, "aToken");
        inputParameters.put(CalendarConnector.CALENDAR_ID, "calendarId");
        inputParameters.put(BuildEventConnector.INPUT_START_DATE, "2014-12-31");
        inputParameters.put(BuildEventConnector.INPUT_START_TIME, "00:00");
        inputParameters.put(BuildEventConnector.INPUT_START_TIME_ZONE, TimeZone.getDefault().getID());
        inputParameters.put(BuildEventConnector.INPUT_END_DATE, "2014-12-31");
        inputParameters.put(BuildEventConnector.INPUT_END_TIME, "00:00");
        inputParameters.put(BuildEventConnector.INPUT_END_TIME_ZONE, TimeZone.getDefault().getID());
        inputParameters.put(BuildEventConnector.INPUT_SUMMARY, "Event Summary");
        inputParameters.put(BuildEventConnector.INPUT_DESCRIPTION, "Event description");
        inputParameters.put(BuildEventConnector.INPUT_LOCATION, "A good place to live");
        inputParameters.put(BuildEventConnector.INPUT_ANYONE_CAN_ADD_SELF, Boolean.FALSE);
        inputParameters.put(BuildEventConnector.INPUT_COLOR_ID, "Green");
        inputParameters.put(BuildEventConnector.INPUT_GADGET_TITLE, "Weather Gadget");
        inputParameters.put(BuildEventConnector.INPUT_GUESTS_CAN_INVITE_OTHERS, Boolean.FALSE);
        inputParameters.put(BuildEventConnector.INPUT_GUESTS_CAN_SEE_OTHER_GUESTS, Boolean.TRUE);
        inputParameters.put(BuildEventConnector.INPUT_ID, "Event Id");
        inputParameters.put(BuildEventConnector.INPUT_SEQUENCE, 42);
        inputParameters.put(BuildEventConnector.INPUT_SOURCE_TITLE, "Source Title");
        inputParameters.put(BuildEventConnector.INPUT_STATUS, "OK");
        inputParameters.put(BuildEventConnector.INPUT_TRANSPARENCY, EventTransparency.TRANSPARENT);
        inputParameters.put(BuildEventConnector.INPUT_VISIBILITY, EventVisibility.TENTATIVE);
        inputParameters.put(BuildEventConnector.INPUT_ORIGINAL_START_DATE, "2014-12-31");
        inputParameters.put(BuildEventConnector.INPUT_ORIGINAL_START_TIME, "00:00");
        inputParameters.put(BuildEventConnector.INPUT_ORIGINAL_START_TIME_ZONE, TimeZone.getDefault().getID());
        inputParameters.put(BuildEventConnector.INPUT_RECURRENCE, Arrays.asList("now", "then"));
        inputParameters.put(BuildEventConnector.INPUT_ATTENDEES_EMAILS, Arrays.asList("no-reply@my-company.com"));
        inputParameters.put(BuildEventConnector.INPUT_REMINDER_USE_DEFAULT, Boolean.TRUE);
        return inputParameters;
    }

}
