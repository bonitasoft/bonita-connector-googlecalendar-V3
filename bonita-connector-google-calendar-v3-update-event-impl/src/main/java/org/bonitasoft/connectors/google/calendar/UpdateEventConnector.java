package org.bonitasoft.connectors.google.calendar;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.google.calendar.common.BuildEventConnector;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Update;
import com.google.api.services.calendar.model.Event;

public class UpdateEventConnector extends BuildEventConnector {

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
        setOutputParameter("event", updatedEvent);
        setOutputParameter("etag", updatedEvent.getEtag());
        setOutputParameter("hangoutLink", updatedEvent.getHangoutLink());
        setOutputParameter("htmlLink", updatedEvent.getHtmlLink());
        setOutputParameter("iCalUID", updatedEvent.getICalUID());
        setOutputParameter("id", updatedEvent.getId());
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
