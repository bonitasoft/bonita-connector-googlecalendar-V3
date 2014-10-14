package org.bonitasoft.connectors.google.calendar;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.google.calendar.common.BuildEventConnector;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Insert;
import com.google.api.services.calendar.model.Event;

public class CreateEventConnector extends BuildEventConnector {

    public static final String OUTPUT_ICAL_UID = "iCalUID";

    public static final String OUTPUT_HTML_LINK = "htmlLink";

    public static final String OUTPUT_HANGOUT_LINK = "hangoutLink";

    public static final String OUTPUT_ETAG = "etag";

    public static final String OUTPUT_ID = "id";

    public static final String OUTPUT_EVENT = "event";

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
        setCommonInputs(insert);
        if (getSendNotifications() != null) {
            insert.setSendNotifications(getSendNotifications());
        }

        final Event insertedEvent = insert.execute();

        setOutputParameter(OUTPUT_EVENT, insertedEvent);
        setOutputParameter(OUTPUT_ETAG, insertedEvent.getEtag());
        setOutputParameter(OUTPUT_HANGOUT_LINK, insertedEvent.getHangoutLink());
        setOutputParameter(OUTPUT_HTML_LINK, insertedEvent.getHtmlLink());
        setOutputParameter(OUTPUT_ICAL_UID, insertedEvent.getICalUID());
        setOutputParameter(OUTPUT_ID, insertedEvent.getId());
    }
}
