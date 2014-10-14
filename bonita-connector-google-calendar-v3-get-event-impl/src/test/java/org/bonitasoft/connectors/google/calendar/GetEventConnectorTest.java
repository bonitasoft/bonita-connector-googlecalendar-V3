package org.bonitasoft.connectors.google.calendar;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.api.services.calendar.Calendar;

public class GetEventConnectorTest {

    @Test
    public void should_DoJobWithCalendar_Get_right_event_based_on_id() throws Exception {

        // Given
        GetEventConnector spyGetEventConnector = new GetEventConnector();
        String calendarId = "Calendar Identifier";
        String id = "Event Identifier";

        Map<String, Object> inputParameters = new HashMap<String, Object>();
        inputParameters.put(CalendarConnector.CALENDAR_ID, calendarId);
        inputParameters.put(CalendarConnector.INPUT_ID, id);
        spyGetEventConnector.setInputParameters(inputParameters);

        Calendar mockCalendarService = Mockito.mock(Calendar.class);
        Calendar.Events mockedEvents = Mockito.mock(Calendar.Events.class);
        Mockito.when(mockCalendarService.events()).thenReturn(mockedEvents);

        Calendar.Events.Get get = Mockito.mock(Calendar.Events.Get.class);
        Mockito.when(mockedEvents.get(calendarId, id)).thenReturn(get);

        // When
        spyGetEventConnector.doJobWithCalendar(mockCalendarService);

        // Then
        Mockito.verify(mockedEvents).get(calendarId, id);
        Mockito.verify(get).execute();
    }

    @Test
    public void should_DoJobWithCalendar_set_optional_parameters() throws Exception {

        // Given
        GetEventConnector spyGetEventConnector = new GetEventConnector();

        Map<String, Object> inputParameters = new HashMap<String, Object>();
        String tz = "timezone";
        int maxAttendees = 42;
        boolean prettyPrint = true;
        inputParameters.put(GetEventConnector.INPUT_TIME_ZONE, tz);
        inputParameters.put(GetEventConnector.INPUT_MAX_ATTENDEES, maxAttendees);
        inputParameters.put(GetEventConnector.INPUT_PRETTY_PRINT, prettyPrint);
        spyGetEventConnector.setInputParameters(inputParameters);

        Calendar mockCalendarService = Mockito.mock(Calendar.class);
        Calendar.Events mockedEvents = Mockito.mock(Calendar.Events.class);
        Mockito.when(mockCalendarService.events()).thenReturn(mockedEvents);

        Calendar.Events.Get mockGet = Mockito.mock(Calendar.Events.Get.class);
        Mockito.when(mockedEvents.get(null, null)).thenReturn(mockGet);

        // When
        spyGetEventConnector.doJobWithCalendar(mockCalendarService);

        // Then
        Mockito.verify(mockGet).setMaxAttendees(maxAttendees);
        Mockito.verify(mockGet).setTimeZone(tz);
        Mockito.verify(mockGet).setPrettyPrint(prettyPrint);

    }

}
