package org.bonitasoft.connectors.google.calendar;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Move;
import com.google.api.services.calendar.model.Event;

public class MoveEventConnector extends CalendarConnector {

    public static final String INPUT_DEST_CALENDAR_ID = "destCalendarId";

    public static final String OUTPUT_ID = "id";

    public static final String OUTPUT_ICAL_UID = "iCalUID";

    public static final String OUTPUT_HTML_LINK = "htmlLink";

    public static final String OUTPUT_HANGOUT_LINK = "hangoutLink";

    public static final String OUTPUT_ETAG = "etag";

    public static final String OUTPUT_EVENT = "event";

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
        setOutputParameter(OUTPUT_EVENT, movedEvent);
        setOutputParameter(OUTPUT_ETAG, movedEvent.getEtag());
        setOutputParameter(OUTPUT_HANGOUT_LINK, movedEvent.getHangoutLink());
        setOutputParameter(OUTPUT_HTML_LINK, movedEvent.getHtmlLink());
        setOutputParameter(OUTPUT_ICAL_UID, movedEvent.getICalUID());
        setOutputParameter(OUTPUT_ID, movedEvent.getId());
    }

    private String getDestCalendarId() {
        return (String) getInputParameter(INPUT_DEST_CALENDAR_ID);
    }

}
