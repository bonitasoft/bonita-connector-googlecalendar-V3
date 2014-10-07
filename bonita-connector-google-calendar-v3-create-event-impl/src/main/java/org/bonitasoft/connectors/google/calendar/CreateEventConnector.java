package org.bonitasoft.connectors.google.calendar;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.google.calendar.common.BuildEventConnector;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Insert;
import com.google.api.services.calendar.model.Event;

public class CreateEventConnector extends BuildEventConnector {

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

        setOutputParameter("event", insertedEvent);
        setOutputParameter("etag", insertedEvent.getEtag());
        setOutputParameter("hangoutLink", insertedEvent.getHangoutLink());
        setOutputParameter("htmlLink", insertedEvent.getHtmlLink());
        setOutputParameter("iCalUID", insertedEvent.getICalUID());
        setOutputParameter("id", insertedEvent.getId());
    }

    protected Integer getMaxAttendees() {
        return (Integer) getInputParameter("maxAttendees");
    }

    protected Boolean getPrettyPrint() {
        return (Boolean) getInputParameter("prettyPrint");
    }

    protected Boolean getSendNotifications() {
        return (Boolean) getInputParameter("sendNotifications");
    }

}
