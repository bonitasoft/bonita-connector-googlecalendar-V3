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
import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;
import org.bonitasoft.engine.connector.ConnectorException;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Delete;
import com.google.api.services.calendar.Calendar.Events.Get;
import com.google.api.services.calendar.model.Event;

public class DeleteEventConnector extends CalendarConnector {

    @Override
    protected List<String> checkParameters() {
        final List<String> errors = new ArrayList<String>();
        ensureIdInputIsSpecified(errors);
        return errors;
    }

    @Override
    protected void doJobWithCalendarEvents(final Calendar.Events events) throws ConnectorException {
        try {
            Get get = events.get(getCalendarId(), getId());
            setCommonInputs(get);
            if (getMaxAttendees() != null) {
                get.setMaxAttendees(getMaxAttendees());
            }
            final Event event = get.execute();
            final Delete delete = events.delete(getCalendarId(), getId());
            delete.execute();
            setOutputParameters(event);
        } catch (IOException e) {
            throw new ConnectorException(e);
        }
    }

}
