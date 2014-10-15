package org.bonitasoft.connectors.google.calendar;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateEventConnectorTest {

    @Test
    public void should_DoJobWithCalendar_insert_event_on_right_calendar() throws Exception {

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
