package org.bonitasoft.connectors.google.calendar;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Delete;

public class DeleteEventConnector extends CalendarConnector {

    public static final String INPUT_SEND_NOTIFICATIONS = "sendNotifications";

    public static final String INPUT_PRETTY_PRINT = "prettyPrint";

    public static final String INPUT_ID = "id";

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
        final Delete delete = calendarService.events().delete(getCalendarId(), getId());
        if (getPrettyPrint() != null) {
            delete.setPrettyPrint(getPrettyPrint());
        }
        if (getSendNotifications() != null) {
            delete.setSendNotifications(getSendNotifications());
        }
        delete.execute();
    }

    private String getId() {
        return (String) getInputParameter(INPUT_ID);
    }

    protected Boolean getPrettyPrint() {
        return (Boolean) getInputParameter(INPUT_PRETTY_PRINT);
    }

    protected Boolean getSendNotifications() {
        return (Boolean) getInputParameter(INPUT_SEND_NOTIFICATIONS);
    }
}
