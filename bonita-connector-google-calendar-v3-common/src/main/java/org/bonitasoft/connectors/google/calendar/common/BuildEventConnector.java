package org.bonitasoft.connectors.google.calendar.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
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

    private static final String START_TIME = "startTime";

    private static final String SOURCE_URL = "sourceUrl";

    private static final String SOURCE_TITLE = "sourceTitle";

    private static final String REMINDER_USE_DEFAULT = "reminderUseDefault";

    private static final String REMINDER_OVERRIDES = "reminderOverrides";

    private static final String RECURRENCE = "recurrence";

    private static final String ORIGINAL_START_TIME_ZONE = "originalStartTimeZone";

    private static final String ORIGINAL_START_TIME = "originalStartTime";

    private static final String LOCATION = "location";

    private static final String ID = "id";

    private static final String GUESTS_CAN_SEE_OTHER_GUESTS = "guestsCanSeeOtherGuests";

    private static final String GUESTS_CAN_INVITE_OTHERS = "guestsCanInviteOthers";

    private static final String GADGET_WIDTH = "gadgetWidth";

    private static final String GADGET_TYPE = "gadgetType";

    private static final String GADGET_TITLE = "gadgetTitle";

    private static final String GADGET_ICON_LINK = "gadgetIconLink";

    private static final String SEQUENCE = "sequence";

    private static final String ORIGINAL_START_DATE = "originalStartDate";

    private static final String GADGET_LINK = "gadgetLink";

    private static final String GADGET_HEIGHT = "gadgetHeight";

    private static final String START_TIME_ZONE = "startTimeZone";

    private static final String STATUS = "status";

    private static final String SUMMARY = "summary";

    private static final String TRANSPARENCY = "transparency";

    private static final String GADGET_DISPLAY = "gadgetDisplay";

    private static final String GADGET_PREFERENCES = "gadgetPreferences";

    private static final String VISIBILITY = "visibility";

    private static final String END_TIME_ZONE = "endTimeZone";

    private static final String END_TIME = "endTime";

    private static final String DESCRIPTION = "description";

    private static final String COLOR_ID = "colorId";

    private static final String ATTENDEES_EMAILS = "attendeesEmails";

    private static final String ANYONE_CAN_ADD_SELF = "anyoneCanAddSelf";

    private static final String END_DATE = "endDate";

    private static final String START_DATE = "startDate";

    private static final String ALL_DAY = "allDay";

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);

    private static final String TIME_FORMAT_PATTERN = "HH:ss";

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(TIME_FORMAT_PATTERN);

    private static final List<String> AVAILABLE_TZ_IDS = Arrays.asList(TimeZone.getAvailableIDs());

    protected List<String> checkStartDate() {
        final List<String> errors = new ArrayList<String>();
        if (getStartDate() != null && isDateFormatKO(DATE_FORMAT, getStartDate())) {
            errors.add("Start Date format is wrong. It must be like " + DATE_FORMAT_PATTERN + " and it is now: " + getStartDate());
        }
        if (getStartTime() != null && isDateFormatKO(TIME_FORMAT, getStartTime())) {
            errors.add("Start Time format is wrong. It must be like " + TIME_FORMAT_PATTERN + " and it is now: " + getStartTime());
        }
        if (getStartTimeZone() != null && !AVAILABLE_TZ_IDS.contains(getStartTimeZone())) {
            errors.add("Specified Start Timezone is not supported. It is now: " + getStartTimeZone() + " and should be one of: "
                    + AVAILABLE_TZ_IDS.toString());
        }
        if (getStartTime() != null && getStartTimeZone() == null) {
            errors.add("Start Timezone must be specified when Start Time is specified");
        }
        if (getStartDate() != null) {
            // checks the start EventDateTime can be built properly
            try {
                buildEventDateTime(getStartDate(), getStartTime(), getStartTimeZone());
            } catch (ParseException e) {
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
        if (getEndTime() != null && getEndTimeZone() == null) {
            errors.add("End Timezone must be specified when End Time is specified");
        }
        if (getEndTimeZone() != null && !AVAILABLE_TZ_IDS.contains(getEndTimeZone())) {
            errors.add("Specified End Timezone is not supported. It is now: " + getEndTimeZone() + " and should be one of: "
                    + AVAILABLE_TZ_IDS.toString());
        }
        // checks the end EventDateTime can be built properly
        if (getEndDate() != null) {
            try {
                buildEventDateTime(getEndDate(), getEndTime(), getEndTimeZone());
            } catch (ParseException e) {
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

    protected Date getDate(final TimeZone timeZone, final String date, final String time) {
        final int year = Integer.valueOf(date.substring(0, 4));
        final int month = Integer.valueOf(date.substring(5, 7));
        final int day = Integer.valueOf(date.substring(8, 10));
        final int hours = Integer.valueOf(time.substring(0, 2));
        final int minutes = Integer.valueOf(time.substring(3, 5));

        final GregorianCalendar gCal = new GregorianCalendar(timeZone);
        gCal.set(GregorianCalendar.YEAR, year);
        gCal.set(GregorianCalendar.MONTH, month - 1);
        gCal.set(GregorianCalendar.DAY_OF_MONTH, day);
        gCal.set(GregorianCalendar.HOUR_OF_DAY, hours);
        gCal.set(GregorianCalendar.MINUTE, minutes);
        gCal.set(GregorianCalendar.SECOND, 0);
        return gCal.getTime();
    }

    protected EventDateTime buildEventDateTime(final String date, final String dateTime, final String timeZone) throws ParseException {
        final EventDateTime edt = new EventDateTime();
        if (getAllDay() != null && getAllDay()) {
            edt.setDate(new DateTime(date));
        } else {
            final Date localDate = getDate(TimeZone.getTimeZone(timeZone), date, dateTime);
            edt.setDateTime(new DateTime(localDate, TimeZone.getTimeZone(timeZone)));
        }
        return edt;
    }

    protected boolean isDateFormatKO(final SimpleDateFormat simpleDateFormat, final String stringToParse) {
        try {
            simpleDateFormat.parse(stringToParse);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    protected Boolean getAllDay() {
        return (Boolean) getInputParameter(ALL_DAY);
    }

    protected String getStartDate() {
        return (String) getInputParameter(START_DATE);
    }

    protected String getEndDate() {
        return (String) getInputParameter(END_DATE);
    }

    protected Boolean getAnyoneCanAddSelf() {
        return (Boolean) getInputParameter(ANYONE_CAN_ADD_SELF);
    }

    @SuppressWarnings("unchecked")
    protected List<String> getAttendeesEmails() {
        return (List<String>) getInputParameter(ATTENDEES_EMAILS);
    }

    protected String getColorId() {
        return (String) getInputParameter(COLOR_ID);
    }

    protected String getDescription() {
        return (String) getInputParameter(DESCRIPTION);
    }

    protected String getEndTime() {
        return (String) getInputParameter(END_TIME);
    }

    protected String getEndTimeZone() {
        return (String) getInputParameter(END_TIME_ZONE);
    }

    protected String getGadgetDisplay() {
        return (String) getInputParameter(GADGET_DISPLAY);
    }

    protected Integer getGadgetHeight() {
        return (Integer) getInputParameter(GADGET_HEIGHT);
    }

    protected String getGadgetIconLink() {
        return (String) getInputParameter(GADGET_ICON_LINK);
    }

    protected String getGadgetLink() {
        return (String) getInputParameter(GADGET_LINK);
    }

    protected Map<String, String> getGadgetPreferences() {
        return getGadgetPreferencesAsMap();
    }

    protected String getGadgetTitle() {
        return (String) getInputParameter(GADGET_TITLE);
    }

    protected String getGadgetType() {
        return (String) getInputParameter(GADGET_TYPE);
    }

    protected Integer getGadgetWidth() {
        return (Integer) getInputParameter(GADGET_WIDTH);
    }

    protected Boolean getGuestsCanInviteOthers() {
        return (Boolean) getInputParameter(GUESTS_CAN_INVITE_OTHERS);
    }

    protected Boolean getGuestsCanSeeOtherGuests() {
        return (Boolean) getInputParameter(GUESTS_CAN_SEE_OTHER_GUESTS);
    }

    protected String getId() {
        return (String) getInputParameter(ID);
    }

    protected String getLocation() {
        return (String) getInputParameter(LOCATION);
    }

    protected String getOriginalStartDate() {
        return (String) getInputParameter(ORIGINAL_START_DATE);
    }

    protected String getOriginalStartTime() {
        return (String) getInputParameter(ORIGINAL_START_TIME);
    }

    protected String getOriginalStartTimeZone() {
        return (String) getInputParameter(ORIGINAL_START_TIME_ZONE);
    }

    @SuppressWarnings("unchecked")
    protected List<String> getRecurrence() {
        return (List<String>) getInputParameter(RECURRENCE);
    }

    @SuppressWarnings("unchecked")
    protected List<String> getReminderOverrides() {
        return (List<String>) getInputParameter(REMINDER_OVERRIDES);
    }

    protected Boolean getReminderUseDefault() {
        return (Boolean) getInputParameter(REMINDER_USE_DEFAULT);
    }

    protected Integer getSequence() {
        return (Integer) getInputParameter(SEQUENCE);
    }

    protected String getSourceTitle() {
        return (String) getInputParameter(SOURCE_TITLE);
    }

    protected String getSourceUrl() {
        return (String) getInputParameter(SOURCE_URL);
    }

    protected String getStartTime() {
        return (String) getInputParameter(START_TIME);
    }

    protected String getStartTimeZone() {
        return (String) getInputParameter(START_TIME_ZONE);
    }

    protected String getStatus() {
        return (String) getInputParameter(STATUS);
    }

    protected String getSummary() {
        return (String) getInputParameter(SUMMARY);
    }

    protected String getTransparency() {
        return (String) getInputParameter(TRANSPARENCY);
    }

    protected String getVisibility() {
        return (String) getInputParameter(VISIBILITY);
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getGadgetPreferencesAsMap() {
        final List<List<Object>> inputPreferences = (List<List<Object>>) getInputParameter(GADGET_PREFERENCES);
        final Map<String, String> result = new HashMap<String, String>();
        if (inputPreferences != null) {
            for (List<Object> rows : inputPreferences) {
                if (rows.size() == 2) {
                    Object keyContent = rows.get(0);
                    Object valueContent = rows.get(1);
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

    // private void printGetters(final Class<?> clazz, final Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    // final Method[] methods = clazz.getDeclaredMethods();
    // System.err.println("Getters of: " + clazz);
    // if (methods != null) {
    // for (final Method method : methods) {
    // if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
    // System.err.println(method.getName() + " : " + method.invoke(object, (Object[]) null));
    // }
    // }
    // }
    // if (clazz.getSuperclass() != null) {
    // printGetters(clazz.getSuperclass(), object);
    // }
    // }
}
