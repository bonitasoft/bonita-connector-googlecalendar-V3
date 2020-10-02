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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.MapEntry;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;
import org.junit.jupiter.api.Test;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;

/**
 * Created by Nicolas Chabanoles on 14/10/14.
 */
class CalendarConnectorTest {

    private class DoNothingCalendarConnector extends CalendarConnector {

        @Override
        protected void doJobWithCalendarEvents(Calendar.Events events) throws ConnectorException {
        }

        @Override
        protected List<String> checkParameters() {
            return null;
        }

        // Make outputs visible from outside world for testing purpose
        public Map<String, Object> getOutputs() {
            return super.getOutputParameters();
        }
    }

    @Test
    public void should_set_output_parameters() throws ConnectorValidationException {

        // Given
        Event eventResult = new Event();
        eventResult.setAnyoneCanAddSelf(true);
        EventAttendee attendee = new EventAttendee();
        attendee.setDisplayName("First attendee");
        eventResult.setAttendees(Arrays.asList(attendee));
        eventResult.setDescription("Event Description");
        eventResult.setEtag("Event etag");
        eventResult.setGuestsCanInviteOthers(false);
        eventResult.setGuestsCanSeeOtherGuests(true);
        eventResult.setGuestsCanModify(false);
        eventResult.setHangoutLink("Hangout link");
        eventResult.setHtmlLink("HTML link");
        eventResult.setICalUID("Ical07");
        eventResult.setId("Event Id");
        eventResult.setLocation("Some interesting place");
        eventResult.setSequence(42);
        eventResult.setStatus("OK");
        eventResult.setSummary("Event title");
        eventResult.setTransparency("Crystal clear");
        eventResult.setVisibility("Visible");

        MapEntry[] expectedOutputs = new MapEntry[] {
                MapEntry.entry(CalendarConnector.OUTPUT_ANYONE_CAN_ADD_SELF, eventResult.getAnyoneCanAddSelf()),
                MapEntry.entry(CalendarConnector.OUTPUT_EVENT, eventResult.toString()),
                MapEntry.entry(CalendarConnector.OUTPUT_DESCRIPTION, eventResult.getDescription()),
                MapEntry.entry(CalendarConnector.OUTPUT_ETAG, eventResult.getEtag()),
                MapEntry.entry(CalendarConnector.OUTPUT_GUESTS_CAN_INVITE_OTHERS, eventResult.getGuestsCanInviteOthers()),
                MapEntry.entry(CalendarConnector.OUTPUT_GUESTS_CAN_SEE_OTHER_GUESTS, eventResult.getGuestsCanSeeOtherGuests()),
                MapEntry.entry(CalendarConnector.OUTPUT_GUESTS_CAN_MODIFY, eventResult.getGuestsCanModify()),
                MapEntry.entry(CalendarConnector.OUTPUT_HANGOUT_LINK, eventResult.getHangoutLink()),
                MapEntry.entry(CalendarConnector.OUTPUT_HTML_LINK, eventResult.getHtmlLink()),
                MapEntry.entry(CalendarConnector.OUTPUT_I_CAL_UID, eventResult.getICalUID()),
                MapEntry.entry(CalendarConnector.OUTPUT_ID, eventResult.getId()),
                MapEntry.entry(CalendarConnector.OUTPUT_LOCATION, eventResult.getLocation()),
                MapEntry.entry(CalendarConnector.OUTPUT_SEQUENCE, eventResult.getSequence()),
                MapEntry.entry(CalendarConnector.OUTPUT_STATUS, eventResult.getStatus()),
                MapEntry.entry(CalendarConnector.OUTPUT_SUMMARY, eventResult.getSummary()),
                MapEntry.entry(CalendarConnector.OUTPUT_TRANSPARENCY, eventResult.getTransparency()),
                MapEntry.entry(CalendarConnector.OUTPUT_VISIBILITY, eventResult.getVisibility())
        };

        DoNothingCalendarConnector connector = new DoNothingCalendarConnector();
        
        // When
        connector.setOutputParameters(eventResult);
        Map<String, Object> outputs = connector.getOutputs();

        // Then
        Assertions.assertThat(outputs).contains(expectedOutputs);
        Assertions.assertThat(outputs).hasSize(expectedOutputs.length);
    }
}
