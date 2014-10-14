package org.bonitasoft.connectors.google.calendar.common;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.connectors.google.calendar.CreateEventConnector;
import org.bonitasoft.connectors.google.calendar.DeleteEventConnector;
import org.bonitasoft.connectors.google.calendar.GetEventConnector;
import org.bonitasoft.connectors.google.calendar.MoveEventConnector;
import org.bonitasoft.connectors.google.calendar.UpdateEventConnector;
import org.bonitasoft.engine.connector.Connector;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.google.api.services.calendar.model.Event;

public class GoogleCalendarV3IntegrationTests {

    public static final String APPLICATION_NAME = GoogleCalendarV3IntegrationTests.class.getName();

    public static final boolean PRINT_OUTPUT = false;

    static {
        System.setProperty("gg.calendar.v3.serviceAccountId", "831328470067-dn65s17e861m08mc94820ffc2u2fju9e@developer.gserviceaccount.com");
        System.setProperty("gg.calendar.v3.serviceAccountP12File", "/Volumes/Data/charles/Downloads/rd-test-project-34c7f11c6d40.p12");
        System.setProperty("gg.calendar.v3.serviceAccountUser", "charles.souillard@bonitasoft.com");
        System.setProperty("gg.calendar.v3.calendarId", "charles.souillard@bonitasoft.com");
        System.setProperty("gg.calendar.v3.destCalendarId", "bonitasoft.com_c8taspg7u8qmijv809jce8usng@group.calendar.google.com");
    }

    @After
    public void clean() {
        // here we should clean all the evnts we previously created
    }

    protected String getValueFromSysProp(final String prop) {
        final String value = System.getProperty(prop);
        if (value == null) {
            throw new IllegalStateException("System Property '" + prop + "' is not set");
        }
        return value;
    }

    protected String getServiceAccountId() {
        return getValueFromSysProp("gg.calendar.v3.serviceAccountId");
    }

    protected String getServiceAccountP12File() {
        return getValueFromSysProp("gg.calendar.v3.serviceAccountP12File");
    }

    protected String getServiceAccountUser() {
        return getValueFromSysProp("gg.calendar.v3.serviceAccountUser");
    }

    protected String getCalendarId() {
        return getValueFromSysProp("gg.calendar.v3.calendarId");
    }

    protected String getDestCalendarId() {
        return getValueFromSysProp("gg.calendar.v3.destCalendarId");
    }

    protected Map<String, Object> getInputParameters() {
        final Map<String, Object> inputParameters = new HashMap<String, Object>();
        inputParameters.put(CalendarConnector.APPLICATION_NAME, APPLICATION_NAME);
        inputParameters.put(CalendarConnector.SERVICE_ACCOUNT_ID, getServiceAccountId());
        inputParameters.put(CalendarConnector.SERVICE_ACCOUNT_P12_FILE, getServiceAccountP12File());

        if (getServiceAccountUser() != null) {
            inputParameters.put(CalendarConnector.SERVICE_ACCOUNT_USER, getServiceAccountUser());
        }
        inputParameters.put(CalendarConnector.CALENDAR_ID, getCalendarId());

        return inputParameters;
    }

    protected Map<String, Object> executeConnector(final Connector connector, final Map<String, Object> inputParameters, final boolean printOutput)
            throws ConnectorValidationException,
            ConnectorException {
        connector.setInputParameters(inputParameters);
        connector.validateInputParameters();
        connector.connect();
        final Map<String, Object> outputParameters = connector.execute();
        connector.disconnect();

        if (printOutput) {
            System.err.println("\n\n\nOUTPUT PARAMETERS OF " + connector.toString() + "\n\n");
            for (Map.Entry<String, Object> output : outputParameters.entrySet()) {
                System.err.println(output.getKey() + ": ");
                System.err.println(output.getValue() + "\n");
            }
        }
        return outputParameters;
    }

    protected String getCurrentTime() {
        final GregorianCalendar gCal = new GregorianCalendar();
        gCal.setTime(new Date());
        final String currentTime = Integer.toString(gCal.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(gCal.get(Calendar.MINUTE)) + ":"
                + Integer.toString(gCal.get(Calendar.SECOND));
        return currentTime;
    }

    protected String getCurrentDay() {
        final GregorianCalendar gCal = new GregorianCalendar();
        gCal.setTime(new Date());
        final String currentDay = Integer.toString(gCal.get(Calendar.YEAR)) + "-" + Integer.toString(gCal.get(Calendar.MONTH) + 1) + "-"
                + Integer.toString(gCal.get(Calendar.DAY_OF_MONTH));
        return currentDay;
    }

    @Test
    public void shoudWork() throws Exception {
        final Map<String, Object> basicInputParameters = getInputParameters();

        final String currentDay = getCurrentDay();
        final String summary = "Test-" + currentDay + "-" + getCurrentTime();
        final String originalStartTime = "14:00";
        final String startTimeZone = "Europe/Paris";
        final String originalEndTime = "10:00";
        final String endTimeZone = "America/Los_Angeles";

        // create
        final Map<String, Object> createInputParameters = new HashMap<String, Object>(basicInputParameters);
        createInputParameters.put(CreateEventConnector.INTPUT_START_DATE, currentDay);
        createInputParameters.put(CreateEventConnector.INTPUT_START_TIME, originalStartTime);
        createInputParameters.put(CreateEventConnector.INTPUT_START_TIME_ZONE, startTimeZone);
        createInputParameters.put(CreateEventConnector.INTPUT_END_DATE, currentDay);
        createInputParameters.put(CreateEventConnector.INTPUT_END_TIME, originalEndTime);
        createInputParameters.put(CreateEventConnector.INTPUT_END_TIME_ZONE, endTimeZone);
        createInputParameters.put(CreateEventConnector.INTPUT_ALL_DAY, false);
        createInputParameters.put(CreateEventConnector.INTPUT_SUMMARY, summary);
        final CreateEventConnector createEventConnector = new CreateEventConnector();
        final Map<String, Object> createOutputParameters = executeConnector(createEventConnector, createInputParameters, PRINT_OUTPUT);
        final Event createdEvent = (Event) createOutputParameters.get(CreateEventConnector.OUTPUT_EVENT);
        Assert.assertNotNull(createdEvent);
        Assert.assertEquals(summary, createdEvent.getSummary());
        Assert.assertEquals("confirmed", createdEvent.getStatus());

        // get
        getEventAndCheckStatus(basicInputParameters, getCalendarId(), createdEvent.getId(), summary, "confirmed");

        // update
        final String updatedSummary = summary + "-updated-" + getCurrentTime();
        final Map<String, Object> updateInputParameters = new HashMap<String, Object>(basicInputParameters);
        updateInputParameters.put(CalendarConnector.INPUT_ID, createdEvent.getId());
        updateInputParameters.put(BuildEventConnector.INTPUT_SUMMARY, updatedSummary);
        final UpdateEventConnector updateConnector = new UpdateEventConnector();
        final Map<String, Object> updateOutputParameters = executeConnector(updateConnector, updateInputParameters, PRINT_OUTPUT);
        final Event updatedEvent = (Event) updateOutputParameters.get(UpdateEventConnector.OUTPUT_EVENT);
        Assert.assertNotNull(updatedEvent);
        Assert.assertEquals(updatedSummary, updatedEvent.getSummary());
        Assert.assertEquals("confirmed", updatedEvent.getStatus());

        // get
        getEventAndCheckStatus(basicInputParameters, getCalendarId(), createdEvent.getId(), updatedSummary, "confirmed");

        // move
        final Map<String, Object> moveInputParameters = new HashMap<String, Object>(basicInputParameters);
        moveInputParameters.put(CalendarConnector.INPUT_ID, updatedEvent.getId());
        moveInputParameters.put(MoveEventConnector.INPUT_DEST_CALENDAR_ID, getDestCalendarId());
        final MoveEventConnector moveConnector = new MoveEventConnector();
        final Map<String, Object> moveOutputParameters = executeConnector(moveConnector, moveInputParameters, PRINT_OUTPUT);
        final Event movedEvent = (Event) moveOutputParameters.get(MoveEventConnector.OUTPUT_EVENT);
        Assert.assertNotNull(movedEvent);
        Assert.assertEquals(updatedSummary, movedEvent.getSummary());
        // returns the event that was moved (original one, not the new event in the cal => google cancel the "old" one to keep trace of it)
        Assert.assertEquals("cancelled", movedEvent.getStatus());

        // verify get returns the "old" event (Google set it as cancelled to keep trace of it)
        final Event newEvent = getEventAndCheckStatus(basicInputParameters, getCalendarId(), updatedEvent.getId(), updatedSummary, "cancelled");

        // get
        getEventAndCheckStatus(basicInputParameters, getDestCalendarId(), newEvent.getId(), updatedSummary, "confirmed");

        // delete
        final Map<String, Object> deleteInputParameters = new HashMap<String, Object>(basicInputParameters);
        deleteInputParameters.put(CalendarConnector.CALENDAR_ID, getDestCalendarId());
        deleteInputParameters.put(CalendarConnector.INPUT_ID, newEvent.getId());
        final DeleteEventConnector deleteConnector = new DeleteEventConnector();
        final Map<String, Object> deleteOutputParameters = executeConnector(deleteConnector, deleteInputParameters, PRINT_OUTPUT);
        final Event deletedEvent = (Event) deleteOutputParameters.get(DeleteEventConnector.OUTPUT_EVENT);
        Assert.assertNotNull(deletedEvent);
        Assert.assertEquals(updatedSummary, deletedEvent.getSummary());
        // returns the event that has been deleted (google cancel the "old" one to keep trace of it)
        Assert.assertEquals("confirmed", deletedEvent.getStatus());

        // verify get returns the cancelled version of the deleted event
        getEventAndCheckStatus(basicInputParameters, getDestCalendarId(), deletedEvent.getId(), updatedSummary, "cancelled");
    }

    protected Event getEventAndCheckStatus(final Map<String, Object> basicInputParameters, final String calendarId, final String eventId,
            final String expectedSummary,
            final String expectedStatus)
            throws ConnectorValidationException, ConnectorException {
        final Map<String, Object> getInputParameters = new HashMap<String, Object>(basicInputParameters);
        getInputParameters.put(CalendarConnector.CALENDAR_ID, calendarId);
        getInputParameters.put(GetEventConnector.INPUT_ID, eventId);
        final GetEventConnector getEventConnector = new GetEventConnector();
        final Map<String, Object> getOutputParameters = executeConnector(getEventConnector, getInputParameters, PRINT_OUTPUT);
        final Event event = (Event) getOutputParameters.get(GetEventConnector.OUTPUT_EVENT);

        Assert.assertNotNull(event);
        Assert.assertEquals(eventId, event.getId());
        Assert.assertEquals(expectedSummary, event.getSummary());
        Assert.assertEquals(expectedStatus, event.getStatus());

        return event;
    }

}
