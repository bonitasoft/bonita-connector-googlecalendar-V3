/*
 * Copyright (C) 2009 - 2020 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.bonitasoft.connectors.google.calendar;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.google.calendar.common.BuildEventConnector;
import org.bonitasoft.engine.connector.ConnectorException;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Update;
import com.google.api.services.calendar.model.Event;

public class UpdateEventConnector extends BuildEventConnector {

    @Override
    protected List<String> checkParameters() {
        final List<String> errors = new ArrayList<String>();
        ensureIdInputIsSpecified(errors);
        return errors;
    }

    @Override
    protected void doJobWithCalendarEvents(final Calendar.Events events) throws ConnectorException {
        try {
            Event event = events.get(getCalendarId(), getId()).execute();
            buildEvent(event);
            final Update update = events.update(getCalendarId(), event.getId(), event);

            setCommonInputs(update);
            if (getSendNotifications() != null) {
                update.setSendNotifications(getSendNotifications());
            }
            if (getMaxAttendees() != null) {
                update.setMaxAttendees(getMaxAttendees());
            }
            setOutputParameters(update.execute());
        } catch (IOException | ParseException e) {
            throw new ConnectorException(e);
        }
    }

}
