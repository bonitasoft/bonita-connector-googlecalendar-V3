package org.bonitasoft.connectors.google.calendar;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Move;
import com.google.api.services.calendar.model.Event;

public class MoveEventConnector extends CalendarConnector {

    @Override
    protected List<String> checkParameters() {
        final List<String> errors = new ArrayList<String>();
        if (getId() == null) {
            errors.add("Event Id must be set.");
        }
        if (getDestCalendarId() == null) {
            errors.add("Destination Calendar Id must be set.");
        }
        return errors;
    }

    @Override
    protected void doJobWithCalendar(final Calendar calendarService) throws Exception {
        final Move move = calendarService.events().move(getCalendarId(), getId(), getDestCalendarId());

        if (getPrettyPrint() != null) {
            move.setPrettyPrint(getPrettyPrint());
        }
        if (getSendNotifications() != null) {
            move.setSendNotifications(getSendNotifications());
        }

        final Event movedEvent = move.execute();
        setOutputParameter("event", movedEvent);
        setOutputParameter("etag", movedEvent.getEtag());
        setOutputParameter("hangoutLink", movedEvent.getHangoutLink());
        setOutputParameter("htmlLink", movedEvent.getHtmlLink());
        setOutputParameter("iCalUID", movedEvent.getICalUID());
        setOutputParameter("id", movedEvent.getId());
    }

    private String getDestCalendarId() {
        return (String) getInputParameter("destCalendarId");
    }

    private String getId() {
        return (String) getInputParameter("id");
    }

    protected Boolean getPrettyPrint() {
        return (Boolean) getInputParameter("prettyPrint");
    }

    protected Boolean getSendNotifications() {
        return (Boolean) getInputParameter("sendNotifications");
    }
}
