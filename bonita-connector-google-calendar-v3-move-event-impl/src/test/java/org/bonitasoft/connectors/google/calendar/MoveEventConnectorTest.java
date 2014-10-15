package org.bonitasoft.connectors.google.calendar;

import com.google.api.services.calendar.Calendar;
import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MoveEventConnectorTest {

    @Test
    public void should_DoJobWithCalendar_Delete_right_event_based_on_id() throws Exception {

        // Given
        MoveEventConnector connector = new MoveEventConnector();
        String calendarId = "Calendar Identifier";
        String id = "Event Identifier";
        String calendarDestId = "Another Calendar";

        Map<String, Object> inputParameters = new HashMap<String, Object>();
        inputParameters.put(CalendarConnector.CALENDAR_ID, calendarId);
        inputParameters.put(CalendarConnector.INPUT_ID, id);
        inputParameters.put(MoveEventConnector.INPUT_DEST_CALENDAR_ID,calendarDestId);
        connector.setInputParameters(inputParameters);

        Calendar.Events mockedEvents = mock(Calendar.Events.class);

        Calendar.Events.Move mockRequest = mock(Calendar.Events.Move.class);
        when(mockedEvents.move(calendarId, id, calendarDestId)).thenReturn(mockRequest);

        // When
        connector.doJobWithCalendarEvents(mockedEvents);

        // Then
        Mockito.verify(mockRequest).execute();
    }

}
