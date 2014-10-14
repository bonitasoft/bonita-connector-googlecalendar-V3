package org.bonitasoft.connectors.google.calendar;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Move;
import com.google.api.services.calendar.model.Event;

public class MoveEventConnector extends CalendarConnector {

    public static final String INPUT_DEST_CALENDAR_ID = "destCalendarId";

    @Override
    protected List<String> checkParameters() {
        final List<String> errors = new ArrayList<String>();
        ensureIdInputIsSpecified(errors);
        if (getDestCalendarId() == null) {
            errors.add("Destination Calendar Id must be set.");
        }
        return errors;
    }

    @Override
    protected void doJobWithCalendar(final Calendar calendarService) throws Exception {
        final Move move = calendarService.events().move(getCalendarId(), getId(), getDestCalendarId());

        setCommonInputs(move);
        if (getSendNotifications() != null) {
            move.setSendNotifications(getSendNotifications());
        }

        final Event movedEvent = move.execute();
        setOutputParameters(movedEvent);
    }

    private String getDestCalendarId() {
        return (String) getInputParameter(INPUT_DEST_CALENDAR_ID);
    }

}
