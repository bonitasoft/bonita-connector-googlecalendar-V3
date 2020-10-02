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
package org.bonitasoft.connectors.google.calendar.common;


public interface EventStatus {
    
    /**
     *   Uses the default visibility for events on the calendar. This is the default value.
     */
    public static final String DEFAULT = "default";
    /**
     *  The event is public and event details are visible to all readers of the calendar.
     */
    public static final String PUBLIC = "public";
    /**
     *  The event is private and only event attendees may view event details.
     */
    public static final String PRIVATE = "private";
    /**
     *  The event is private. This value is provided for compatibility reasons.
     */
    public static final String CONFIDENTIAL = "confidential";
    
}
