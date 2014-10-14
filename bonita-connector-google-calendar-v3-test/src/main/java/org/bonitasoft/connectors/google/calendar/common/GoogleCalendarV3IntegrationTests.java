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
        System.setProperty("gg.calendar.v3.destCalendarId", "charles.souillard@bonitasoft.com");
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

    protected String getDesCalendarId() {
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

        // get
        getEvent(basicInputParameters, getCalendarId(), createdEvent.getId(), summary);

        // update
        final String updatedSummary = summary + "-updated-" + getCurrentTime();
        final Map<String, Object> updateInputParameters = new HashMap<String, Object>(basicInputParameters);
        updateInputParameters.put(CalendarConnector.INPUT_ID, createdEvent.getId());
        updateInputParameters.put(BuildEventConnector.INTPUT_SUMMARY, updatedSummary);
        final UpdateEventConnector connector = new UpdateEventConnector();
        final Map<String, Object> updateOutputParameters = executeConnector(connector, updateInputParameters, PRINT_OUTPUT);
        final Event updatedEvent = (Event) updateOutputParameters.get(UpdateEventConnector.OUTPUT_EVENT);
        Assert.assertNotNull(updatedEvent);
        Assert.assertEquals(updatedSummary, updatedEvent.getSummary());

        // get
        getEvent(basicInputParameters, getCalendarId(), createdEvent.getId(), updatedSummary);

        // move

        // get

        // delete
    }

    protected void getEvent(final Map<String, Object> basicInputParameters, final String calendarId, final String eventId, final String expectedSummary)
            throws ConnectorValidationException, ConnectorException {
        final Map<String, Object> getInputParameters = new HashMap<String, Object>(basicInputParameters);
        getInputParameters.put(CalendarConnector.CALENDAR_ID, getCalendarId());
        getInputParameters.put(GetEventConnector.INPUT_ID, eventId);
        final GetEventConnector getEventConnector = new GetEventConnector();
        final Map<String, Object> getOutputParameters = executeConnector(getEventConnector, getInputParameters, PRINT_OUTPUT);
        final Event retrievedEvent = (Event) getOutputParameters.get(GetEventConnector.OUTPUT_EVENT);
        Assert.assertNotNull(retrievedEvent);
        Assert.assertEquals(expectedSummary, retrievedEvent.getSummary());
    }

    protected void deleteEvent(final String calendarId, final String eventId) throws ConnectorValidationException, ConnectorException {
        final Map<String, Object> inputParameters = getInputParameters();

        inputParameters.put("calendarId", calendarId);
        inputParameters.put("id", eventId);

        final DeleteEventConnector connector = new DeleteEventConnector();
        executeConnector(connector, inputParameters, PRINT_OUTPUT);
    }

    protected void moveEvent(final String calendarId, final String eventId, final String destCalendarId) throws ConnectorValidationException,
            ConnectorException {
        final Map<String, Object> moveInputParameters = getInputParameters();

        moveInputParameters.put("id", eventId);
        moveInputParameters.put("destCalendarId", destCalendarId);

        final MoveEventConnector connector = new MoveEventConnector();
        executeConnector(connector, moveInputParameters, PRINT_OUTPUT);
    }

}
