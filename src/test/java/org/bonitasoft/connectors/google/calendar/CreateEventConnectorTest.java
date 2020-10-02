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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;
import org.junit.jupiter.api.Test;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;

class CreateEventConnectorTest {

    @Test
    void should_DoJobWithCalendar_insert_event_on_right_calendar() throws Exception {

        // Given
        CreateEventConnector connector = new CreateEventConnector();
        String calendarId = "Calendar Identifier";
        Map<String, Object> inputParameters = Collections.singletonMap(CalendarConnector.CALENDAR_ID, (Object) calendarId);

        connector.setInputParameters(inputParameters);

        Calendar.Events mockedEvents = mock(Calendar.Events.class);

        Calendar.Events.Insert insert = mock(Calendar.Events.Insert.class);
        when(mockedEvents.insert(eq(calendarId), any(Event.class))).thenReturn(insert);

        // When
        connector.doJobWithCalendarEvents(mockedEvents);

        // Then
        verify(insert).execute();
    }

}
