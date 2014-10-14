package org.bonitasoft.connectors.google.calendar;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Delete;
import com.google.api.services.calendar.model.Event;

public class DeleteEventConnector extends CalendarConnector {

    public static final String INPUT_SEND_NOTIFICATIONS = "sendNotifications";

    public static final String INPUT_PRETTY_PRINT = "prettyPrint";

    public static final String INPUT_ID = "id";

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
        if (getId() == null) {
            errors.add("Event Id must be set.");
        }
        return errors;
    }

    @Override
    protected void doJobWithCalendar(final Calendar calendarService) throws Exception {
        final Event event = calendarService.events().get(getCalendarId(), getId()).execute();

        final Delete delete = calendarService.events().delete(getCalendarId(), getId());
        if (getPrettyPrint() != null) {
            delete.setPrettyPrint(getPrettyPrint());
        }
        if (getSendNotifications() != null) {
            delete.setSendNotifications(getSendNotifications());
        }
        delete.execute();

        setOutputParameters(event);
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

    private String getId() {
        return (String) getInputParameter(INPUT_ID);
    }

    protected Boolean getPrettyPrint() {
        return (Boolean) getInputParameter(INPUT_PRETTY_PRINT);
    }

    protected Boolean getSendNotifications() {
        return (Boolean) getInputParameter(INPUT_SEND_NOTIFICATIONS);
    }
}
