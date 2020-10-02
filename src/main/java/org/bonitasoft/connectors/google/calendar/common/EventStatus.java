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
