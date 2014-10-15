package org.bonitasoft.connectors.google.calendar;

import com.google.api.services.calendar.Calendar;
import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeleteEventConnectorTest {

    @Test
    public void should_DoJobWithCalendar_Delete_right_event_based_on_id() throws Exception {

        // Given
        DeleteEventConnector connector = new DeleteEventConnector();
        String calendarId = "Calendar Identifier";
        String id = "Event Identifier";

        Map<String, Object> inputParameters = new HashMap<String, Object>();
        inputParameters.put(CalendarConnector.CALENDAR_ID, calendarId);
        inputParameters.put(CalendarConnector.INPUT_ID, id);
        connector.setInputParameters(inputParameters);

        Calendar.Events mockedEvents = mock(Calendar.Events.class);

        Calendar.Events.Delete mockDeleteRequest = mock(Calendar.Events.Delete.class);
        when(mockedEvents.delete(calendarId, id)).thenReturn(mockDeleteRequest);

        Calendar.Events.Get mockGetRequest = mock(Calendar.Events.Get.class);
        when(mockedEvents.get(calendarId, id)).thenReturn(mockGetRequest);

        // When
        connector.doJobWithCalendarEvents(mockedEvents);

        // Then
        verify(mockDeleteRequest).execute();
        verify(mockGetRequest).execute();
    }

}
