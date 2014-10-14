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
        GetEventConnector spyGetEventConnector = Mockito.spy(new GetEventConnector());
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
    }

}
