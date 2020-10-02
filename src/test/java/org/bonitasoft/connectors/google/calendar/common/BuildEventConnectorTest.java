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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.assertj.core.api.Assertions;
import org.bonitasoft.engine.connector.ConnectorValidationException;
import org.junit.jupiter.api.Test;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;

/**
 * Created by Nicolas Chabanoles on 14/10/14.
 */
class BuildEventConnectorTest {


    private class DoNothingBuildEventCalendarConnector extends BuildEventConnector {
        @Override
        protected void doJobWithCalendarEvents(Calendar.Events events) throws Exception {

        }

        @Override
        protected List<String> checkParameters() {
            return Collections.emptyList();
        }
    }

    @Test
    void should_have_timezone_set_if_startDate_and_endDate_are_set() throws ConnectorValidationException {
        // Given
        BuildEventConnector connector = new DoNothingBuildEventCalendarConnector();

        // When
        Map<String, Object> defaultConfiguration = DefaultConnectorConfiguration.defaultConfiguration();
        defaultConfiguration.put(BuildEventConnector.INPUT_START_TIME_ZONE, null);
        defaultConfiguration.put(BuildEventConnector.INPUT_END_TIME_ZONE, null);
        connector.setInputParameters(defaultConfiguration);
        
        // Then
        assertThat(connector.checkStartDate()).contains("Start Timezone must be specified when Start Time is specified");
        assertThat(connector.checkEndDate()).contains("End Timezone must be specified when End Time is specified");
    }


    @Test
    void should_getDate_parse_date_according_to_timezone() {
        // Given
        BuildEventConnector connector = new DoNothingBuildEventCalendarConnector();
        ZoneId tz = ZoneId.of("America/Los_Angeles");
        String date = "2014-12-31";
        String time = "00:00";

        // When
        ZonedDateTime dateTime = connector.getDate(tz, date, time);

        // Then
        assertThat(dateTime)
            .hasToString("2014-12-31T00:00-08:00[America/Los_Angeles]");
        assertThat(ZonedDateTime.ofInstant(dateTime.toInstant(), ZoneId.of("Europe/Paris")))
            .hasToString("2014-12-31T09:00+01:00[Europe/Paris]");
    }

    @Test
    void should_buildEvent_use_input_parameters() throws Exception {

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
