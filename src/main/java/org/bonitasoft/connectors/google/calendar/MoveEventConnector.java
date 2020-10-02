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

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Move;
import com.google.api.services.calendar.model.Event;

public class MoveEventConnector extends CalendarConnector {

    public static final String INPUT_DEST_CALENDAR_ID = "destCalendarId";

    @Override
    protected List<String> checkParameters() {
        final List<String> errors = new ArrayList<String>();
        ensureIdInputIsSpecified(errors);
        if (getDestCalendarId() == null) {
            errors.add("Destination Calendar Id must be set.");
        }
        return errors;
    }

    @Override
    protected void doJobWithCalendarEvents(final Calendar.Events events) throws Exception {
        final Move move = events.move(getCalendarId(), getId(), getDestCalendarId());

        setCommonInputs(move);
        if (getSendNotifications() != null) {
            move.setSendNotifications(getSendNotifications());
        }

        final Event movedEvent = move.execute();
        setOutputParameters(movedEvent);
    }

    private String getDestCalendarId() {
        return (String) getInputParameter(INPUT_DEST_CALENDAR_ID);
    }

}
