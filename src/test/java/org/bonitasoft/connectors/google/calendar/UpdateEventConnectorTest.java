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
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;

class UpdateEventConnectorTest {

    @Test
    void should_DoJobWithCalendar_Update_right_event_based_on_id() throws Exception {

        // Given
        UpdateEventConnector connector = new UpdateEventConnector();
        String calendarId = "Calendar Identifier";
        String id = "Event Identifier";

        Map<String, Object> inputParameters = new HashMap<String, Object>();
        inputParameters.put(CalendarConnector.CALENDAR_ID, calendarId);
        inputParameters.put(CalendarConnector.INPUT_ID, id);
        connector.setInputParameters(inputParameters);

        Calendar.Events mockedEvents = mock(Calendar.Events.class);

        Calendar.Events.Get mockGetRequest = mock(Calendar.Events.Get.class);
        when(mockedEvents.get(calendarId, id)).thenReturn(mockGetRequest);
        Event event = new Event();
        when(mockGetRequest.execute()).thenReturn(event);

        Calendar.Events.Update mockUpdateRequest = mock(Calendar.Events.Update.class);
        when(mockedEvents.update(calendarId, id, event)).thenReturn(mockUpdateRequest);


        // When
        connector.doJobWithCalendarEvents(mockedEvents);

        // Then
        Mockito.verify(mockUpdateRequest).execute();
    }

}
