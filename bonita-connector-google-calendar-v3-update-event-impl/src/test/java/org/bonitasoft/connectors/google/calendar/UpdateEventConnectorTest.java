package org.bonitasoft.connectors.google.calendar;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdateEventConnectorTest {

    @Test
    public void should_DoJobWithCalendar_Update_right_event_based_on_id() throws Exception {

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
