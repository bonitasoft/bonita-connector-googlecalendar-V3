package org.bonitasoft.connectors.google.calendar.common;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.connector.Connector;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;

public class EventConnectorTest {

    protected Map<String, Object> getInputParameters() {
        final Map<String, Object> inputParameters = new HashMap<String, Object>();
        inputParameters.put("applicationName", "My Application/1.0");

        // rd.user
        inputParameters.put("serviceAccountId", "831328470067-dn65s17e861m08mc94820ffc2u2fju9e@developer.gserviceaccount.com");
        inputParameters.put("serviceAccountP12File", "/Volumes/Data/charles/Downloads/rd-test-project-34c7f11c6d40.p12");
        inputParameters.put("serviceAccountUser", "rd.user@bonitasoft.com");
        inputParameters.put("calendarId", "rd.user@bonitasoft.com");

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

}
