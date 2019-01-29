package org.bonitasoft.connectors.google.calendar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;
import org.junit.Test;

import com.google.api.services.calendar.Calendar;

public class GetEventConnectorTest {

    @Test
    public void should_DoJobWithCalendar_Get_right_event_based_on_id() throws Exception {

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
    public void should_DoJobWithCalendar_set_optional_parameters() throws Exception {

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
