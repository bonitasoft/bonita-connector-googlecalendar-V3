package org.bonitasoft.connectors.google.calendar;

import java.util.Map;

import org.bonitasoft.connectors.google.calendar.common.EventConnectorTest;
import org.junit.Ignore;
import org.junit.Test;

public class GetEventConnectorTest extends EventConnectorTest {

    @Ignore
    @Test
    public void shoudWork() throws Exception {
        final Map<String, Object> inputParameters = getInputParameters();

        inputParameters.put("id", "2jsic3uj9qflfgaukop7ro24cs");
        inputParameters.put("timeZone", "Europe/Paris");

        final GetEventConnector connector = new GetEventConnector();
        executeConnector(connector, inputParameters, true);
    }

}
