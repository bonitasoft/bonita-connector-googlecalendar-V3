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
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.bonitasoft.connectors.google.calendar.common.CalendarConnector;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.Get;
import com.google.api.services.calendar.model.Event;

public class GetEventConnector extends CalendarConnector {

    private static final List<String> AVAILABLE_TZ_IDS = Arrays.asList(TimeZone.getAvailableIDs());

    public static final String INPUT_TIME_ZONE = "timeZone";

    @Override
    protected List<String> checkParameters() {
        final List<String> errors = new ArrayList<String>();

        ensureIdInputIsSpecified(errors);

        if (getTimeZone() != null && !AVAILABLE_TZ_IDS.contains(getTimeZone())) {
            errors.add("Specified Timezone is not supported. It is now: " + getTimeZone() + " and should be one of: "
                    + AVAILABLE_TZ_IDS.toString());
        }
        return errors;
    }

    @Override
    protected void doJobWithCalendarEvents(final Calendar.Events events) throws Exception {
        final Get get = events.get(getCalendarId(), getId());

        setCommonInputs(get);
        setSpecificOptionalInputs(get);

        final Event event = get.execute();

        setOutputParameters(event);

    }

    private void setSpecificOptionalInputs(Get get) {
        if (getMaxAttendees() != null) {
            get.setMaxAttendees(getMaxAttendees());
        }
        if (getTimeZone() != null) {
            get.setTimeZone(getTimeZone());
        }
    }

    private String getTimeZone() {
        return (String) getInputParameter(INPUT_TIME_ZONE);
    }

}
