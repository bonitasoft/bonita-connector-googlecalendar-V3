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
package org.bonitasoft.connectors.google.calendar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;
import org.junit.jupiter.api.Test;

import com.google.api.services.calendar.Calendar;

class GetEventConnectorTest {

    @Test
    void should_DoJobWithCalendar_Get_right_event_based_on_id() throws Exception {

        // Given
        GetEventConnector connector = new GetEventConnector();
        String calendarId = "Calendar Identifier";
        String id = "Event Identifier";

        Map<String, Object> inputParameters = new HashMap<String, Object>();
        inputParameters.put(CalendarConnector.CALENDAR_ID, calendarId);
        inputParameters.put(CalendarConnector.INPUT_ID, id);
        connector.setInputParameters(inputParameters);

        Calendar.Events mockedEvents = mock(Calendar.Events.class);

        Calendar.Events.Get get = mock(Calendar.Events.Get.class);
        when(mockedEvents.get(calendarId, id)).thenReturn(get);

        // When
        connector.doJobWithCalendarEvents(mockedEvents);

        // Then
        verify(get).execute();
    }

    @Test
    void should_DoJobWithCalendar_set_optional_parameters() throws Exception {

        // Given
        GetEventConnector connector = new GetEventConnector();

        Map<String, Object> inputParameters = new HashMap<String, Object>();
        String tz = "timezone";
        int maxAttendees = 42;
        boolean prettyPrint = true;
        inputParameters.put(GetEventConnector.INPUT_TIME_ZONE, tz);
        inputParameters.put(GetEventConnector.INPUT_MAX_ATTENDEES, maxAttendees);
        inputParameters.put(GetEventConnector.INPUT_PRETTY_PRINT, prettyPrint);
        connector.setInputParameters(inputParameters);

        Calendar.Events mockedEvents = mock(Calendar.Events.class);

        Calendar.Events.Get mockGet = mock(Calendar.Events.Get.class);
        when(mockedEvents.get(null, null)).thenReturn(mockGet);

        // When
        connector.doJobWithCalendarEvents(mockedEvents);

        // Then
        verify(mockGet).setMaxAttendees(maxAttendees);
        verify(mockGet).setTimeZone(tz);
        verify(mockGet).setPrettyPrint(prettyPrint);

    }

}
