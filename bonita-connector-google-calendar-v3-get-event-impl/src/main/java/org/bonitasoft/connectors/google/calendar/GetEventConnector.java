package org.bonitasoft.connectors.google.calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Get;
import com.google.api.services.calendar.model.Event;

public class GetEventConnector extends CalendarConnector {

    private static final List<String> AVAILABLE_TZ_IDS = Arrays.asList(TimeZone.getAvailableIDs());

    public static final String INPUT_TIME_ZONE = "timeZone";

    public static final String OUTPUT_EVENT = "event";
    public static final String OUTPUT_ETAG = "etag";
    public static final String OUTPUT_HANGOUT_LINK = "hangoutLink";
    public static final String OUTPUT_HTML_LINK = "htmlLink";
    public static final String OUTPUT_I_CAL_UID = "iCalUID";
    public static final String OUTPUT_ID = "id";
    public static final String OUTPUT_STATUS = "status";
    public static final String OUTPUT_SUMMARY = "summary";
    public static final String OUTPUT_DESCRIPTION = "description";
    public static final String OUTPUT_LOCATION = "location";
    public static final String OUTPUT_TRANSPARENCY = "transparency";
    public static final String OUTPUT_VISIBILITY = "visibility";
    public static final String OUTPUT_SEQUENCE = "sequence";
    public static final String OUTPUT_ANYONE_CAN_ADD_SELF = "anyoneCanAddSelf";
    public static final String OUTPUT_GUESTS_CAN_INVITE_OTHERS = "guestsCanInviteOthers";
    public static final String OUTPUT_GUESTS_CAN_MODIFY = "guestsCanModify";
    public static final String OUTPUT_GUESTS_CAN_SEE_OTHER_GUESTS = "guestsCanSeeOtherGuests";

    @Override
    protected List<String> checkParameters() {
        final List<String> errors = new ArrayList<String>();

        ensureIdInputIsSpecified(errors);

        if (getTimeZone() != null && !AVAILABLE_TZ_IDS.contains(getTimeZone())) {
            errors.add("Specified Timezone is not supported. It is now: " + getTimeZone() + " and should be one of: "
                    + AVAILABLE_TZ_IDS.toString());
        }
        return errors;
    }

    @Override
    protected void doJobWithCalendar(final Calendar calendarService) throws Exception {
        final Get get = calendarService.events().get(getCalendarId(), getId());

        setCommonInputs(get);
        setSpecificOptionalInputs(get);

        final Event event = get.execute();

        setOutputParameters(event);

    }

    private void setSpecificOptionalInputs(Get get) {
        if (getMaxAttendees() != null) {
            get.setMaxAttendees(getMaxAttendees());
        }
        if (getTimeZone() != null) {
            get.setTimeZone(getTimeZone());
        }
    }

    protected void setOutputParameters(Event event) {
        setOutputParameter(OUTPUT_EVENT, event);
        setOutputParameter(OUTPUT_ETAG, event.getEtag());
        setOutputParameter(OUTPUT_HANGOUT_LINK, event.getHangoutLink());
        setOutputParameter(OUTPUT_HTML_LINK, event.getHtmlLink());
        setOutputParameter(OUTPUT_I_CAL_UID, event.getICalUID());
        setOutputParameter(OUTPUT_ID, event.getId());
        setOutputParameter(OUTPUT_STATUS, event.getStatus());
        setOutputParameter(OUTPUT_SUMMARY, event.getSummary());
        setOutputParameter(OUTPUT_DESCRIPTION, event.getDescription());
        setOutputParameter(OUTPUT_LOCATION, event.getLocation());
        setOutputParameter(OUTPUT_TRANSPARENCY, event.getTransparency());
        setOutputParameter(OUTPUT_VISIBILITY, event.getVisibility());
        setOutputParameter(OUTPUT_SEQUENCE, event.getSequence());
        setOutputParameter(OUTPUT_ANYONE_CAN_ADD_SELF, event.getAnyoneCanAddSelf());
        setOutputParameter(OUTPUT_GUESTS_CAN_INVITE_OTHERS, event.getGuestsCanInviteOthers());
        setOutputParameter(OUTPUT_GUESTS_CAN_MODIFY, event.getGuestsCanModify());
        setOutputParameter(OUTPUT_GUESTS_CAN_SEE_OTHER_GUESTS, event.getGuestsCanSeeOtherGuests());
    }

    private String getTimeZone() {
        return (String) getInputParameter(INPUT_TIME_ZONE);
    }

}
