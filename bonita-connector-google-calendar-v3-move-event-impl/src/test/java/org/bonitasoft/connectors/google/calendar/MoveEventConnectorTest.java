package org.bonitasoft.connectors.google.calendar;

import java.util.Map;

import org.bonitasoft.connectors.google.calendar.common.EventConnectorTest;
import org.junit.Ignore;
import org.junit.Test;

public class MoveEventConnectorTest extends EventConnectorTest {

    @Ignore
    @Test
    public void shoudWork() throws Exception {
        final Map<String, Object> inputParameters = getInputParameters();

        inputParameters.put("id", "hcgbqh6vdssmot7fvouv99e1r4");
        inputParameters.put("destCalendarId", "souillard.net_9l4uqbivp21lueicmbqt4ltar0@group.calendar.google.com");

        final MoveEventConnector connector = new MoveEventConnector();
        executeConnector(connector, inputParameters, true);
    }

}
