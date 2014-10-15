package org.bonitasoft.connectors.google.calendar;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CreateEventConnectorTest {

    @Test
    public void should_DoJobWithCalendar_insert_event_on_right_calendar() throws Exception {

        // Given
        CreateEventConnector connector = new CreateEventConnector();
        String calendarId = "Calendar Identifier";
        Map<String, Object> inputParameters = Collections.singletonMap(CalendarConnector.CALENDAR_ID, (Object) calendarId);

        connector.setInputParameters(inputParameters);

        Calendar mockCalendarService = Mockito.mock(Calendar.class);
        Calendar.Events mockedEvents = Mockito.mock(Calendar.Events.class);
        Mockito.when(mockCalendarService.events()).thenReturn(mockedEvents);

        Calendar.Events.Insert insert = Mockito.mock(Calendar.Events.Insert.class);
        Mockito.when(mockedEvents.insert(Mockito.eq(calendarId), Mockito.any(Event.class))).thenReturn(insert);

        // When
        connector.doJobWithCalendar(mockCalendarService);

        // Then
        Mockito.verify(insert).execute();
    }

}
