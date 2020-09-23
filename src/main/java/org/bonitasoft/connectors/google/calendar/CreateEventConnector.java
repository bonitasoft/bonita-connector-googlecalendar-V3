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
        if (getStartDate() == null || getStartDate().isEmpty()) {
            errors.add("Start Date must be set.");
        }
        errors.addAll(checkStartDate());

        // END DATE
        if (getEndDate() == null || getEndDate().isEmpty()) {
            errors.add("End Date must be set.");
        }
        errors.addAll(checkEndDate());
        return errors;
    }

    @Override
    protected void doJobWithCalendarEvents(final Calendar.Events events) throws Exception {
        final Event eventToInsert = new Event();
        buildEvent(eventToInsert);
        final Insert insert = events.insert(getCalendarId(), eventToInsert);

        setCommonInputs(insert);
        setSpecificInputs(insert);

        final Event insertedEvent = insert.execute();

        setOutputParameters(insertedEvent);
    }

    private void setSpecificInputs(Insert insert) {
        if (getMaxAttendees() != null) {
            insert.setMaxAttendees(getMaxAttendees());
        }
        if (getSendNotifications() != null) {
            insert.setSendNotifications(getSendNotifications());
        }
    }
}
