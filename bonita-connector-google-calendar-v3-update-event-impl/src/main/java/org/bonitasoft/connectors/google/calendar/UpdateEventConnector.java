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
        ensureIdInputIsSpecified(errors);
        return errors;
    }

    @Override
    protected void doJobWithCalendar(final Calendar calendarService) throws Exception {
        final Event event = calendarService.events().get(getCalendarId(), getId()).execute();

        buildEvent(event);

        final Update update = calendarService.events().update(getCalendarId(), event.getId(), event);

        setCommonInputs(update);
        if (getSendNotifications() != null) {
            update.setSendNotifications(getSendNotifications());
        }
        if (getMaxAttendees() != null) {
            update.setMaxAttendees(getMaxAttendees());
        }

        final Event updatedEvent = update.execute();

        setOutputParameters(updatedEvent);
    }

}
