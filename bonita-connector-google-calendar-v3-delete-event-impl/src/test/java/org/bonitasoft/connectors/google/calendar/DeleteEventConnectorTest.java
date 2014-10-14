package org.bonitasoft.connectors.google.calendar;

import com.google.api.services.calendar.Calendar;
import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class DeleteEventConnectorTest {

    @Test
    public void should_DoJobWithCalendar_Delete_right_event_based_on_id() throws Exception {

        // Given
        DeleteEventConnector spyDeleteEventConnector = new DeleteEventConnector();
        String calendarId = "Calendar Identifier";
        String id = "Event Identifier";

        Map<String, Object> inputParameters = new HashMap<String, Object>();
        inputParameters.put(CalendarConnector.CALENDAR_ID, calendarId);
        inputParameters.put(CalendarConnector.INPUT_ID, id);
        spyDeleteEventConnector.setInputParameters(inputParameters);

        Calendar mockCalendarService = Mockito.mock(Calendar.class);
        Calendar.Events mockedEvents = Mockito.mock(Calendar.Events.class);
        Mockito.when(mockCalendarService.events()).thenReturn(mockedEvents);

        Calendar.Events.Delete mockDeleteRequest = Mockito.mock(Calendar.Events.Delete.class);
        Mockito.when(mockedEvents.delete(calendarId, id)).thenReturn(mockDeleteRequest);

        Calendar.Events.Get mockGetRequest = Mockito.mock(Calendar.Events.Get.class);
        Mockito.when(mockedEvents.get(calendarId, id)).thenReturn(mockGetRequest);

        // When
        spyDeleteEventConnector.doJobWithCalendar(mockCalendarService);

        // Then
        Mockito.verify(mockedEvents).get(calendarId, id);
        Mockito.verify(mockDeleteRequest).execute();
        Mockito.verify(mockGetRequest).execute();
    }

}
