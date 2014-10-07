package org.bonitasoft.connectors.google.calendar;

import java.util.Map;

import org.bonitasoft.connectors.google.calendar.common.EventConnectorTest;
import org.junit.Ignore;
import org.junit.Test;

public class DeleteEventConnectorTest extends EventConnectorTest {

    @Ignore
    @Test
    public void shoudWork() throws Exception {
        final Map<String, Object> inputParameters = getInputParameters();

        inputParameters.put("id", "hcgbqh6vdssmot7fvouv99e1r4");
        inputParameters.put("calendarId", "souillard.net_9l4uqbivp21lueicmbqt4ltar0@group.calendar.google.com");

        final DeleteEventConnector connector = new DeleteEventConnector();
        executeConnector(connector, inputParameters, true);
    }

}
