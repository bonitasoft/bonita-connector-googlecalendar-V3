package org.bonitasoft.connectors.google.calendar.common;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.*;

/**
 * Created by Nicolas Chabanoles on 14/10/14.
 */
public class BuildEventConnectorTest {


    private class DoNothingBuildEventCalendarConnector extends BuildEventConnector {
        @Override
        protected void doJobWithCalendarEvents(Calendar.Events events) throws Exception {

        }

        @Override
        protected List<String> checkParameters() {
            return null;
        }
    }




    @Test
    public void should_getDate_parse_date_according_to_timezone() {
        // Given
        BuildEventConnector connector = new DoNothingBuildEventCalendarConnector();
        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        String date = "2014-12-31";
        String time = "00:00";

        // When
        Date dateTime = connector.getDate(tz, date, time);

        // Then
        java.util.Calendar calendar = java.util.Calendar.getInstance(tz);
        calendar.set(2014, java.util.Calendar.DECEMBER,31,0,0,0 );

        Date lastDayOf2014 = calendar.getTime();

        Assertions.assertThat(dateTime.getTime()).isEqualTo(lastDayOf2014.getTime());
    }

    @Test
    public void should_buildEvent_use_input_parameters() throws Exception {

        // Given
        BuildEventConnector connector = new DoNothingBuildEventCalendarConnector();

        String tz = TimeZone.getDefault().getID();
        String date = "2014-12-31";
        String time = "00:00";
        String summary = "Event Summary";
        String description = "Event description";
        String location = "A good place to live";
        boolean anyoneCanAddSelf = false;
        String colorId = "Green";
        String gadgetTitle = "Weather Gadget";
        Boolean guestsCanInviteOthers = false;
        Boolean guestsCanSeeOtherGuests = true;
        String eventId = "Event Id";
        Integer sequence = 42;
        String status = "OK";
        String transparency = "Crystal clear";
        String visibility = "Now you can see me.";
        List<String> recurrence = Arrays.asList("now", "then");
        List<String> attendeesEmail  = Arrays.asList("no-reply@my-company.com");
        String sourceTitle = "Source Title";
        boolean useDefaultReminders = true;

        Map<String, Object> inputParameters = new HashMap<String, Object>();
        inputParameters.put(BuildEventConnector.INPUT_START_DATE, date);
        inputParameters.put(BuildEventConnector.INPUT_START_TIME, time);
        inputParameters.put(BuildEventConnector.INPUT_START_TIME_ZONE, tz);
        inputParameters.put(BuildEventConnector.INPUT_END_DATE, date);
        inputParameters.put(BuildEventConnector.INPUT_END_TIME, time);
        inputParameters.put(BuildEventConnector.INPUT_END_TIME_ZONE, tz);
        inputParameters.put(BuildEventConnector.INPUT_SUMMARY, summary);
        inputParameters.put(BuildEventConnector.INPUT_DESCRIPTION, description);
        inputParameters.put(BuildEventConnector.INPUT_LOCATION, location);
        inputParameters.put(BuildEventConnector.INPUT_ANYONE_CAN_ADD_SELF, anyoneCanAddSelf);
        inputParameters.put(BuildEventConnector.INPUT_COLOR_ID, colorId);
        inputParameters.put(BuildEventConnector.INPUT_GADGET_TITLE, gadgetTitle);
        inputParameters.put(BuildEventConnector.INPUT_GUESTS_CAN_INVITE_OTHERS, guestsCanInviteOthers);
        inputParameters.put(BuildEventConnector.INPUT_GUESTS_CAN_SEE_OTHER_GUESTS, guestsCanSeeOtherGuests);
        inputParameters.put(BuildEventConnector.INPUT_ID, eventId);
        inputParameters.put(BuildEventConnector.INPUT_SEQUENCE, sequence);
        inputParameters.put(BuildEventConnector.INPUT_SOURCE_TITLE, sourceTitle);
        inputParameters.put(BuildEventConnector.INPUT_STATUS, status);
        inputParameters.put(BuildEventConnector.INPUT_TRANSPARENCY, transparency);
        inputParameters.put(BuildEventConnector.INPUT_VISIBILITY, visibility);
        inputParameters.put(BuildEventConnector.INPUT_ORIGINAL_START_DATE, date);
        inputParameters.put(BuildEventConnector.INPUT_ORIGINAL_START_TIME, time);
        inputParameters.put(BuildEventConnector.INPUT_ORIGINAL_START_TIME_ZONE, tz);
        inputParameters.put(BuildEventConnector.INPUT_RECURRENCE, recurrence);
        inputParameters.put(BuildEventConnector.INPUT_ATTENDEES_EMAILS, attendeesEmail);
        inputParameters.put(BuildEventConnector.INPUT_REMINDER_USE_DEFAULT, useDefaultReminders);
        connector.setInputParameters(inputParameters);

        // When
        Event event = new Event();
        connector.buildEvent(event);

        // Then
        Assertions.assertThat(event.getStart()).isNotNull();
        Assertions.assertThat(event.getEnd()).isNotNull();
        Assertions.assertThat(event.getSummary()).isEqualTo(summary);
        Assertions.assertThat(event.getDescription()).isEqualTo(description);
        Assertions.assertThat(event.getLocation()).isEqualTo(location);
        Assertions.assertThat(event.getAnyoneCanAddSelf()).isEqualTo(anyoneCanAddSelf);
        Assertions.assertThat(event.getColorId()).isEqualTo(colorId);
        Assertions.assertThat(event.getGadget()).isNotNull();
        Assertions.assertThat(event.getGuestsCanInviteOthers()).isEqualTo(guestsCanInviteOthers);
        Assertions.assertThat(event.getGuestsCanSeeOtherGuests()).isEqualTo(guestsCanSeeOtherGuests);
        Assertions.assertThat(event.getId()).isEqualTo(eventId);
        Assertions.assertThat(event.getSequence()).isEqualTo(sequence);
        Assertions.assertThat(event.getSource()).isNotNull();
        Assertions.assertThat(event.getStatus()).isEqualTo(status);
        Assertions.assertThat(event.getTransparency()).isEqualTo(transparency);
        Assertions.assertThat(event.getVisibility()).isEqualTo(visibility);
        Assertions.assertThat(event.getOriginalStartTime()).isNotNull();
        Assertions.assertThat(event.getRecurrence()).isEqualTo(recurrence);
        Assertions.assertThat(event.getAttendees().get(0).getEmail()).isEqualTo(attendeesEmail.get(0));
        Assertions.assertThat(event.getReminders()).isNotNull();

    }
}
