/*
 * Copyright (C) 2009 - 2020 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.bonitasoft.connectors.google.calendar.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Event.Gadget;
import com.google.api.services.calendar.model.Event.Reminders;
import com.google.api.services.calendar.model.Event.Source;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

public abstract class BuildEventConnector extends CalendarConnector {

    public static final String INPUT_START_TIME = "startTime";

    public static final String INPUT_SOURCE_URL = "sourceUrl";

    public static final String INPUT_SOURCE_TITLE = "sourceTitle";

    public static final String INPUT_REMINDER_USE_DEFAULT = "reminderUseDefault";

    public static final String INPUT_REMINDER_OVERRIDES = "reminderOverrides";

    public static final String INPUT_RECURRENCE = "recurrence";

    public static final String INPUT_ORIGINAL_START_TIME_ZONE = "originalStartTimeZone";

    public static final String INPUT_ORIGINAL_START_TIME = "originalStartTime";

    public static final String INPUT_LOCATION = "location";

    public static final String INPUT_GUESTS_CAN_SEE_OTHER_GUESTS = "guestsCanSeeOtherGuests";

    public static final String INPUT_GUESTS_CAN_INVITE_OTHERS = "guestsCanInviteOthers";

    public static final String INPUT_GADGET_WIDTH = "gadgetWidth";

    public static final String INPUT_GADGET_TYPE = "gadgetType";

    public static final String INPUT_GADGET_TITLE = "gadgetTitle";

    public static final String INPUT_GADGET_ICON_LINK = "gadgetIconLink";

    public static final String INPUT_SEQUENCE = "sequence";

    public static final String INPUT_ORIGINAL_START_DATE = "originalStartDate";

    public static final String INPUT_GADGET_LINK = "gadgetLink";

    public static final String INPUT_GADGET_HEIGHT = "gadgetHeight";

    public static final String INPUT_START_TIME_ZONE = "startTimeZone";

    public static final String INPUT_STATUS = "status";

    public static final String INPUT_SUMMARY = "summary";

    public static final String INPUT_TRANSPARENCY = "transparency";

    public static final String INPUT_GADGET_DISPLAY = "gadgetDisplay";

    public static final String INPUT_GADGET_PREFERENCES = "gadgetPreferences";

    public static final String INPUT_VISIBILITY = "visibility";

    public static final String INPUT_END_TIME_ZONE = "endTimeZone";

    public static final String INPUT_END_TIME = "endTime";

    public static final String INPUT_DESCRIPTION = "description";

    public static final String INPUT_COLOR_ID = "colorId";

    public static final String INPUT_ATTENDEES_EMAILS = "attendeesEmails";

    public static final String INPUT_ANYONE_CAN_ADD_SELF = "anyoneCanAddSelf";

    public static final String INPUT_END_DATE = "endDate";

    public static final String INPUT_START_DATE = "startDate";

    public static final String INPUT_ALL_DAY = "allDay";

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);

    private static final String TIME_FORMAT_PATTERN = "HH:ss";

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(TIME_FORMAT_PATTERN);

    private static final List<String> AVAILABLE_TZ_IDS = Arrays.asList(TimeZone.getAvailableIDs());
    
    private static final DateTimeFormatter RFC3339_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneId.of("UTC"));

    protected List<String> checkStartDate() {
        final List<String> errors = new ArrayList<String>();
        if (getStartDate() != null && isDateFormatKO(DATE_FORMAT, getStartDate())) {
            errors.add("Start Date format is wrong. It must be like " + DATE_FORMAT_PATTERN + " and it is now: " + getStartDate());
        }
        if (getStartTime() != null && isDateFormatKO(TIME_FORMAT, getStartTime())) {
            errors.add("Start Time format is wrong. It must be like " + TIME_FORMAT_PATTERN + " and it is now: " + getStartTime());
        }
        if (getStartTime() != null && (getStartTimeZone() == null || getStartTimeZone().isEmpty())) {
            errors.add("Start Timezone must be specified when Start Time is specified");
            return errors;
        }
        if (getStartTimeZone() != null && !AVAILABLE_TZ_IDS.contains(getStartTimeZone())) {
            errors.add("Specified Start Timezone is not supported. It is now: " + getStartTimeZone() + " and should be one of: "
                    + AVAILABLE_TZ_IDS.toString());
            return errors;
        }
 
        if (getStartDate() != null) {
            // checks the start EventDateTime can be built properly
            try {
                buildEventDateTime(getStartDate(), getStartTime(), getStartTimeZone());
            } catch (final ParseException e) {
                errors.add("Error while parsing start date: " + e.getMessage());
            }
        }
        return errors;
    }

    protected List<String> checkEndDate() {
        final List<String> errors = new ArrayList<String>();
        if (getEndDate() != null && isDateFormatKO(DATE_FORMAT, getEndDate())) {
            errors.add("End Date format is wrong. It must be like " + DATE_FORMAT_PATTERN + " and it is now: " + getEndDate());
        }
        if (getEndTime() != null && isDateFormatKO(TIME_FORMAT, getEndTime())) {
            errors.add("End Time format is wrong. It must be like " + TIME_FORMAT_PATTERN + " and it is now: " + getEndTime());
        }
        if (getEndTime() != null && (getEndTimeZone() == null || getEndTimeZone().isEmpty())) {
            errors.add("End Timezone must be specified when End Time is specified");
            return errors;
        }
        if (getEndTimeZone() != null && !AVAILABLE_TZ_IDS.contains(getEndTimeZone())) {
            errors.add("Specified End Timezone is not supported. It is now: " + getEndTimeZone() + " and should be one of: "
                    + AVAILABLE_TZ_IDS.toString());
            return errors;
        }
        // checks the end EventDateTime can be built properly
        if (getEndDate() != null) {
            try {
                buildEventDateTime(getEndDate(), getEndTime(), getEndTimeZone());
            } catch (final ParseException e) {
                errors.add("Error while parsing end date: " + e.getMessage());
            }
        }
        return errors;
    }

    protected void buildEvent(final Event event) throws ParseException {
        if (getStartDate() != null) {
            event.setStart(buildEventDateTime(getStartDate(), getStartTime(), getStartTimeZone()));
        }
        if (getEndDate() != null) {
            event.setEnd(buildEventDateTime(getEndDate(), getEndTime(), getEndTimeZone()));
        }

        if (getSummary() != null) {
            event.setSummary(getSummary());
        }
        if (getDescription() != null) {
            event.setDescription(getDescription());
        }
        if (getLocation() != null) {
            event.setLocation(getLocation());
        }
        if (getAnyoneCanAddSelf() != null) {
            event.setAnyoneCanAddSelf(getAnyoneCanAddSelf());
        }
        if (getColorId() != null) {
            event.setColorId(getColorId());
        }
        if (getGadgetTitle() != null) {
            final Gadget gadget = new Gadget();
            gadget.setTitle(getGadgetTitle());
            gadget.setLink(getGadgetLink());
            gadget.setIconLink(getGadgetIconLink());
            gadget.setType(getGadgetType());
            if (getGadgetPreferences() != null) {
                gadget.setPreferences(getGadgetPreferences());
            }
            if (getGadgetWidth() != null) {
                gadget.setWidth(getGadgetWidth());
            }
            if (getGadgetHeight() != null) {
                gadget.setHeight(getGadgetHeight());
            }
            if (getGadgetDisplay() != null) {
                gadget.setDisplay(getGadgetDisplay());
            }
            event.setGadget(gadget);
        }
        if (getGuestsCanInviteOthers() != null) {
            event.setGuestsCanInviteOthers(getGuestsCanInviteOthers());
        }
        if (getGuestsCanSeeOtherGuests() != null) {
            event.setGuestsCanSeeOtherGuests(getGuestsCanSeeOtherGuests());
        }
        if (getId() != null) {
            event.setId(getId());
        }
        if (getSequence() != null) {
            event.setSequence(getSequence());
        }
        if (getSourceTitle() != null) {
            final Source source = new Source();
            source.setTitle(getSourceTitle());
            source.setUrl(getSourceUrl());
            event.setSource(source);
        }
        if (getStatus() != null) {
            event.setStatus(getStatus());
        }
        if (getTransparency() != null) {
            event.setTransparency(getTransparency());
        }
        if (getVisibility() != null) {
            event.setVisibility(getVisibility());
        }
        if (getOriginalStartDate() != null) {
            event.setOriginalStartTime(buildEventDateTime(getOriginalStartDate(), getOriginalStartTime(), getOriginalStartTimeZone()));
        }
        if (getRecurrence() != null) {
            event.setRecurrence(getRecurrence());
        }
        if (getAttendeesEmails() != null) {
            final List<EventAttendee> attendees = new ArrayList<EventAttendee>();
            for (final String attendeeEmail : getAttendeesEmails()) {
                final EventAttendee eventAttendee = new EventAttendee();
                eventAttendee.setEmail(attendeeEmail);
                attendees.add(eventAttendee);
            }
            event.setAttendees(attendees);
        }
        if (getReminderUseDefault() != null || getReminderOverrides() != null) {
            final Reminders reminders = new Reminders();
            if (getReminderUseDefault() != null) {
                reminders.setUseDefault(getReminderUseDefault());
            } else if (getReminderOverrides() != null) {
                reminders.setUseDefault(false);
            } else {
                reminders.setUseDefault(true);
            }
            if (getReminderOverrides() != null) {
                final List<EventReminder> overrides = new ArrayList<EventReminder>();
                for (final String override : getReminderOverrides()) {
                    final String[] overrideSplit = override.split(":");
                    final String method = overrideSplit[0];
                    final Integer minutes = Integer.valueOf(overrideSplit[1]);
                    final EventReminder eventReminder = new EventReminder();
                    eventReminder.setMethod(method);
                    eventReminder.setMinutes(minutes);
                    overrides.add(eventReminder);
                }
                reminders.setOverrides(overrides);
            }
            event.setReminders(reminders);
        }
    }

    protected ZonedDateTime getDate(final ZoneId zoneId, final String date, final String time) {
        final int year = Integer.valueOf(date.substring(0, 4));
        final int month = Integer.valueOf(date.substring(5, 7));
        final int day = Integer.valueOf(date.substring(8, 10));
        final int hours = Integer.valueOf(time.substring(0, 2));
        final int minutes = Integer.valueOf(time.substring(3, 5));
        
        return ZonedDateTime.of(year, month, day, hours, minutes, 0, 0,zoneId);
    }

    protected EventDateTime buildEventDateTime(final String date, final String dateTime, final String timeZone) throws ParseException {
        final EventDateTime edt = new EventDateTime();
        if (getAllDay() != null && getAllDay()) {
            edt.setDate(new DateTime(date));
        } else {
            final ZonedDateTime localDate = getDate(ZoneId.of(timeZone), date, dateTime);
            edt.setDateTime(DateTime.parseRfc3339(localDate.format(RFC3339_FORMATTER)));
        }
        return edt;
    }

    protected boolean isDateFormatKO(final SimpleDateFormat simpleDateFormat, final String stringToParse) {
        try {
            simpleDateFormat.parse(stringToParse);
            return false;
        } catch (final ParseException e) {
            return true;
        }
    }

    protected Boolean getAllDay() {
        return (Boolean) getInputParameter(INPUT_ALL_DAY);
    }

    protected String getStartDate() {
        return (String) getInputParameter(INPUT_START_DATE);
    }

    protected String getEndDate() {
        return (String) getInputParameter(INPUT_END_DATE);
    }

    protected Boolean getAnyoneCanAddSelf() {
        return (Boolean) getInputParameter(INPUT_ANYONE_CAN_ADD_SELF);
    }

    @SuppressWarnings("unchecked")
    protected List<String> getAttendeesEmails() {
        return (List<String>) getInputParameter(INPUT_ATTENDEES_EMAILS);
    }

    protected String getColorId() {
        return (String) getInputParameter(INPUT_COLOR_ID);
    }

    protected String getDescription() {
        return (String) getInputParameter(INPUT_DESCRIPTION);
    }

    protected String getEndTime() {
        return (String) getInputParameter(INPUT_END_TIME);
    }

    protected String getEndTimeZone() {
        return (String) getInputParameter(INPUT_END_TIME_ZONE);
    }

    protected String getGadgetDisplay() {
        return (String) getInputParameter(INPUT_GADGET_DISPLAY);
    }

    protected Integer getGadgetHeight() {
        return (Integer) getInputParameter(INPUT_GADGET_HEIGHT);
    }

    protected String getGadgetIconLink() {
        return (String) getInputParameter(INPUT_GADGET_ICON_LINK);
    }

    protected String getGadgetLink() {
        return (String) getInputParameter(INPUT_GADGET_LINK);
    }

    protected Map<String, String> getGadgetPreferences() {
        return getGadgetPreferencesAsMap();
    }

    protected String getGadgetTitle() {
        return (String) getInputParameter(INPUT_GADGET_TITLE);
    }

    protected String getGadgetType() {
        return (String) getInputParameter(INPUT_GADGET_TYPE);
    }

    protected Integer getGadgetWidth() {
        return (Integer) getInputParameter(INPUT_GADGET_WIDTH);
    }

    protected Boolean getGuestsCanInviteOthers() {
        return (Boolean) getInputParameter(INPUT_GUESTS_CAN_INVITE_OTHERS);
    }

    protected Boolean getGuestsCanSeeOtherGuests() {
        return (Boolean) getInputParameter(INPUT_GUESTS_CAN_SEE_OTHER_GUESTS);
    }

    protected String getLocation() {
        return (String) getInputParameter(INPUT_LOCATION);
    }

    protected String getOriginalStartDate() {
        return (String) getInputParameter(INPUT_ORIGINAL_START_DATE);
    }

    protected String getOriginalStartTime() {
        return (String) getInputParameter(INPUT_ORIGINAL_START_TIME);
    }

    protected String getOriginalStartTimeZone() {
        return (String) getInputParameter(INPUT_ORIGINAL_START_TIME_ZONE);
    }

    @SuppressWarnings("unchecked")
    protected List<String> getRecurrence() {
        return (List<String>) getInputParameter(INPUT_RECURRENCE);
    }

    @SuppressWarnings("unchecked")
    protected List<String> getReminderOverrides() {
        return (List<String>) getInputParameter(INPUT_REMINDER_OVERRIDES);
    }

    protected Boolean getReminderUseDefault() {
        return (Boolean) getInputParameter(INPUT_REMINDER_USE_DEFAULT);
    }

    protected Integer getSequence() {
        return (Integer) getInputParameter(INPUT_SEQUENCE);
    }

    protected String getSourceTitle() {
        return (String) getInputParameter(INPUT_SOURCE_TITLE);
    }

    protected String getSourceUrl() {
        return (String) getInputParameter(INPUT_SOURCE_URL);
    }

    protected String getStartTime() {
        return (String) getInputParameter(INPUT_START_TIME);
    }

    protected String getStartTimeZone() {
        return (String) getInputParameter(INPUT_START_TIME_ZONE);
    }

    protected String getStatus() {
        return (String) getInputParameter(INPUT_STATUS);
    }

    protected String getSummary() {
        return (String) getInputParameter(INPUT_SUMMARY);
    }

    protected String getTransparency() {
        return (String) getInputParameter(INPUT_TRANSPARENCY);
    }

    protected String getVisibility() {
        return (String) getInputParameter(INPUT_VISIBILITY);
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getGadgetPreferencesAsMap() {
        final List<List<Object>> inputPreferences = (List<List<Object>>) getInputParameter(INPUT_GADGET_PREFERENCES);
        final Map<String, String> result = new HashMap<String, String>();
        if (inputPreferences != null) {
            for (final List<Object> rows : inputPreferences) {
                if (rows.size() == 2) {
                    final Object keyContent = rows.get(0);
                    final Object valueContent = rows.get(1);
                    if (keyContent != null && valueContent != null) {
                        final String key = keyContent.toString();
                        final String value = valueContent.toString();
                        result.put(key, value);
                    }
                }
            }
        }
        return result;
    }

}
