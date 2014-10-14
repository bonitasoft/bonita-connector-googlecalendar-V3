package org.bonitasoft.connectors.google.calendar.common;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.bonitasoft.connectors.google.calendar.CreateEventConnector;
import org.bonitasoft.connectors.google.calendar.DeleteEventConnector;
import org.bonitasoft.connectors.google.calendar.GetEventConnector;
import org.bonitasoft.connectors.google.calendar.MoveEventConnector;
import org.bonitasoft.connectors.google.calendar.UpdateEventConnector;
import org.bonitasoft.engine.connector.Connector;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;
import org.junit.After;
import org.junit.Test;

public class GoogleCalendarV3IntegrationTests {

    public static final String APPLICATION_NAME = GoogleCalendarV3IntegrationTests.class.getName();

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
        inputParameters.put("applicationName", APPLICATION_NAME);
        inputParameters.put("serviceAccountId", getServiceAccountId());
        inputParameters.put("serviceAccountP12File", getServiceAccountP12File());

        if (getServiceAccountUser() != null) {
            inputParameters.put("serviceAccountUser", getServiceAccountUser());
        }
        inputParameters.put("calendarId", getCalendarId());

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

    @Test
    public void shoudWork() throws Exception {
        createEvent();
    }

    protected String createEvent() throws ConnectorValidationException, ConnectorException {
        final Map<String, Object> inputParameters = getInputParameters();
        // Main
        inputParameters.put("startDate", "2014-10-15");
        inputParameters.put("startTime", "14:00");
        inputParameters.put("startTimeZone", "Europe/Paris");
        inputParameters.put("endDate", "2014-10-15");
        inputParameters.put("endTime", "10:00");
        inputParameters.put("endTimeZone", "America/Los_Angeles");
        inputParameters.put("allDay", false);

        inputParameters.put("summary", "Test-" + getCurrentTime());

        final CreateEventConnector connector = new CreateEventConnector();
        final Map<String, Object> outputParameters = executeConnector(connector, inputParameters, true);

        Assert.assertNotNull(outputParameters.get("event"));
        
        return 
    }

    protected void deleteEvent(final String calendarId, final String eventId) throws ConnectorValidationException, ConnectorException {
        final Map<String, Object> inputParameters = getInputParameters();

        inputParameters.put("calendarId", calendarId);
        inputParameters.put("id", eventId);

        final DeleteEventConnector connector = new DeleteEventConnector();
        executeConnector(connector, inputParameters, true);
    }

    protected void getEvent(final String calendarId, final String eventId, final String timeZone) throws ConnectorValidationException, ConnectorException {
        final Map<String, Object> inputParameters = getInputParameters();

        inputParameters.put("calendarId", calendarId);
        inputParameters.put("id", eventId);
        inputParameters.put("timeZone", timeZone);

        final GetEventConnector connector = new GetEventConnector();
        executeConnector(connector, inputParameters, true);
    }

    protected void moveEvent(final String calendarId, final String eventId, final String destCalendarId) throws ConnectorValidationException,
            ConnectorException {
        final Map<String, Object> inputParameters = getInputParameters();

        inputParameters.put("calendarId", calendarId);
        inputParameters.put("id", eventId);
        inputParameters.put("destCalendarId", destCalendarId);

        final MoveEventConnector connector = new MoveEventConnector();
        executeConnector(connector, inputParameters, true);
    }

    protected void updateEvent(final String calendarId, final String eventId) throws ConnectorValidationException, ConnectorException {
        final Map<String, Object> inputParameters = getInputParameters();

        inputParameters.put("calendarId", calendarId);
        inputParameters.put("id", eventId);

        inputParameters.put("summary", "updatedSummary");
        inputParameters.put("location", "updatedLocation");

        final UpdateEventConnector connector = new UpdateEventConnector();
        executeConnector(connector, inputParameters, true);
    }
}
