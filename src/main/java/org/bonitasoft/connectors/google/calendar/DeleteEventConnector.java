package org.bonitasoft.connectors.google.calendar;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Delete;
import com.google.api.services.calendar.Calendar.Events.Get;
import com.google.api.services.calendar.model.Event;

public class DeleteEventConnector extends CalendarConnector {

    @Override
    protected List<String> checkParameters() {
        final List<String> errors = new ArrayList<String>();
        ensureIdInputIsSpecified(errors);
        return errors;
    }

    @Override
    protected void doJobWithCalendarEvents(final Calendar.Events events) throws Exception {
        final Get get = events.get(getCalendarId(), getId());

        setCommonInputs(get);
        if (getMaxAttendees() != null) {
            get.setMaxAttendees(getMaxAttendees());
        }

        final Event event = get.execute();

        final Delete delete = events.delete(getCalendarId(), getId());

        delete.execute();

        setOutputParameters(event);
    }

}
