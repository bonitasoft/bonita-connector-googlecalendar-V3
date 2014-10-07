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

    @Override
    protected List<String> checkParameters() {
        final List<String> errors = new ArrayList<String>();
        if (getId() == null) {
            errors.add("Event Id must be set.");
        }
        if (getTimeZone() != null && !AVAILABLE_TZ_IDS.contains(getTimeZone())) {
            errors.add("Specified Timezone is not supported. It is now: " + getTimeZone() + " and should be one of: "
                    + AVAILABLE_TZ_IDS.toString());
        }
        return errors;
    }

    @Override
    protected void doJobWithCalendar(final Calendar calendarService) throws Exception {
        final Get get = calendarService.events().get(getCalendarId(), getId());

        if (getPrettyPrint() != null) {
            get.setPrettyPrint(getPrettyPrint());
        }
        if (getMaxAttendees() != null) {
            get.setMaxAttendees(getMaxAttendees());
        }
        if (getTimeZone() != null) {
            get.setTimeZone(getTimeZone());
        }

        final Event event = get.execute();
        setOutputParameter("event", event);
        setOutputParameter("etag", event.getEtag());
        setOutputParameter("hangoutLink", event.getHangoutLink());
        setOutputParameter("htmlLink", event.getHtmlLink());
        setOutputParameter("iCalUID", event.getICalUID());
        setOutputParameter("id", event.getId());
        setOutputParameter("status", event.getStatus());
        setOutputParameter("summary", event.getSummary());
        setOutputParameter("description", event.getDescription());
        setOutputParameter("location", event.getLocation());
        setOutputParameter("transparency", event.getTransparency());
        setOutputParameter("visibility", event.getVisibility());
        setOutputParameter("sequence", event.getSequence());
        setOutputParameter("anyoneCanAddSelf", event.getAnyoneCanAddSelf());
        setOutputParameter("guestsCanInviteOthers", event.getGuestsCanInviteOthers());
        setOutputParameter("guestsCanModify", event.getGuestsCanModify());
        setOutputParameter("guestsCanSeeOtherGuests", event.getGuestsCanSeeOtherGuests());

    }

    private String getTimeZone() {
        return (String) getInputParameter("timeZone");
    }

    protected Integer getMaxAttendees() {
        return (Integer) getInputParameter("maxAttendees");
    }

    private String getId() {
        return (String) getInputParameter("id");
    }

    protected Boolean getPrettyPrint() {
        return (Boolean) getInputParameter("prettyPrint");
    }

}
