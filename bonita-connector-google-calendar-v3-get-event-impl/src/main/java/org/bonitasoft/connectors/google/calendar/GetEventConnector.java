package org.bonitasoft.connectors.google.calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Get;
import com.google.api.services.calendar.model.Event;

public class GetEventConnector extends CalendarConnector {

    private static final List<String> AVAILABLE_TZ_IDS = Arrays.asList(TimeZone.getAvailableIDs());

    public static final String INPUT_TIME_ZONE = "timeZone";

    @Override
    protected List<String> checkParameters() {
        final List<String> errors = new ArrayList<String>();

        ensureIdInputIsSpecified(errors);

        if (getTimeZone() != null && !AVAILABLE_TZ_IDS.contains(getTimeZone())) {
            errors.add("Specified Timezone is not supported. It is now: " + getTimeZone() + " and should be one of: "
                    + AVAILABLE_TZ_IDS.toString());
        }
        return errors;
    }

    @Override
    protected void doJobWithCalendar(final Calendar calendarService) throws Exception {
        final Get get = calendarService.events().get(getCalendarId(), getId());

        setCommonInputs(get);
        setSpecificOptionalInputs(get);

        final Event event = get.execute();

        setOutputParameters(event);

    }

    private void setSpecificOptionalInputs(Get get) {
        if (getMaxAttendees() != null) {
            get.setMaxAttendees(getMaxAttendees());
        }
        if (getTimeZone() != null) {
            get.setTimeZone(getTimeZone());
        }
    }

    private String getTimeZone() {
        return (String) getInputParameter(INPUT_TIME_ZONE);
    }

}
