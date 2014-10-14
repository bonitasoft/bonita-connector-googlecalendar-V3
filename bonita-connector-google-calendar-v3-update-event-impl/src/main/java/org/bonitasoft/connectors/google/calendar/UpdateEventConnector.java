package org.bonitasoft.connectors.google.calendar;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.google.calendar.common.BuildEventConnector;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Update;
import com.google.api.services.calendar.model.Event;

public class UpdateEventConnector extends BuildEventConnector {

    public static final String INPUT_SEND_NOTIFICATIONS = "sendNotifications";

    public static final String INPUT_PRETTY_PRINT = "prettyPrint";

    public static final String INPUT_MAX_ATTENDEES = "maxAttendees";

    public static final String OUTPUT_ID = "id";

    public static final String OUTPUT_ICAL_UID = "iCalUID";

    public static final String OUTPUT_HTML_LINK = "htmlLink";

    public static final String OUTPUT_HANGOUT_LINK = "hangoutLink";

    public static final String OUTPUT_ETAG = "etag";

    public static final String OUTPUT_EVENT = "event";

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

        buildEvent(event);

        final Update update = calendarService.events().update(getCalendarId(), event.getId(), event);

        if (getPrettyPrint() != null) {
            update.setPrettyPrint(getPrettyPrint());
        }
        if (getSendNotifications() != null) {
            update.setSendNotifications(getSendNotifications());
        }
        if (getMaxAttendees() != null) {
            update.setMaxAttendees(getMaxAttendees());
        }

        final Event updatedEvent = update.execute();
        setOutputParameter(OUTPUT_EVENT, updatedEvent);
        setOutputParameter(OUTPUT_ETAG, updatedEvent.getEtag());
        setOutputParameter(OUTPUT_HANGOUT_LINK, updatedEvent.getHangoutLink());
        setOutputParameter(OUTPUT_HTML_LINK, updatedEvent.getHtmlLink());
        setOutputParameter(OUTPUT_ICAL_UID, updatedEvent.getICalUID());
        setOutputParameter(OUTPUT_ID, updatedEvent.getId());
    }

    protected Integer getMaxAttendees() {
        return (Integer) getInputParameter(INPUT_MAX_ATTENDEES);
    }

    protected Boolean getPrettyPrint() {
        return (Boolean) getInputParameter(INPUT_PRETTY_PRINT);
    }

    protected Boolean getSendNotifications() {
        return (Boolean) getInputParameter(INPUT_SEND_NOTIFICATIONS);
    }
}
