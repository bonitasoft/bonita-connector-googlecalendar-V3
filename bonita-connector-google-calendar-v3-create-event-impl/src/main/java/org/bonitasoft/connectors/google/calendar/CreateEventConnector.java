package org.bonitasoft.connectors.google.calendar;

import static org.bonitasoft.connectors.google.calendar.common.BuildEventConnector.ID;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.google.calendar.common.BuildEventConnector;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Insert;
import com.google.api.services.calendar.model.Event;

public class CreateEventConnector extends BuildEventConnector {

    public static final String SEND_NOTIFICATIONS = "sendNotifications";

    public static final String PRETTY_PRINT = "prettyPrint";

    public static final String MAX_ATTENDEES = "maxAttendees";

    public static final String I_CAL_UID = "iCalUID";

    public static final String HTML_LINK = "htmlLink";

    public static final String HANGOUT_LINK = "hangoutLink";

    public static final String ETAG = "etag";

    public static final String EVENT = "event";

    @Override
    public List<String> checkParameters() {
        final List<String> errors = new ArrayList<String>();

        // ALL DAY
        if (getAllDay() == null) {
            errors.add("All Day must be set.");
        }

        // START DATE
        if (getStartDate() == null) {
            errors.add("Start Date must be set.");
        }
        checkStartDate();

        // END DATE
        if (getEndDate() == null) {
            errors.add("End Date must be set.");
        }
        checkEndDate();
        return errors;
    }

    @Override
    protected void doJobWithCalendar(final Calendar calendarService) throws Exception {
        final Event eventToInsert = new Event();
        buildEvent(eventToInsert);
        final Insert insert = calendarService.events().insert(getCalendarId(), eventToInsert);
        if (getMaxAttendees() != null) {
            insert.setMaxAttendees(getMaxAttendees());
        }
        if (getPrettyPrint() != null) {
            insert.setPrettyPrint(getPrettyPrint());
        }
        if (getSendNotifications() != null) {
            insert.setSendNotifications(getSendNotifications());
        }

        final Event insertedEvent = insert.execute();

        setOutputParameter(EVENT, insertedEvent);
        setOutputParameter(ETAG, insertedEvent.getEtag());
        setOutputParameter(HANGOUT_LINK, insertedEvent.getHangoutLink());
        setOutputParameter(HTML_LINK, insertedEvent.getHtmlLink());
        setOutputParameter(I_CAL_UID, insertedEvent.getICalUID());
        setOutputParameter(ID, insertedEvent.getId());
    }

    protected Integer getMaxAttendees() {
        return (Integer) getInputParameter(MAX_ATTENDEES);
    }

    protected Boolean getPrettyPrint() {
        return (Boolean) getInputParameter(PRETTY_PRINT);
    }

    protected Boolean getSendNotifications() {
        return (Boolean) getInputParameter(SEND_NOTIFICATIONS);
    }

}
