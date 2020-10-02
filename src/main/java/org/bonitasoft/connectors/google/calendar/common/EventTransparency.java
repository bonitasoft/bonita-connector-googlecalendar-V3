package org.bonitasoft.connectors.google.calendar.common;

public interface EventTransparency {

    /**
     * Default value. The event does block time on the calendar. This is equivalent to setting Show me as to Busy in the Calendar UI.
     */
    public static final String OPAQUE = "opaque";
    /**
     * The event does not block time on the calendar. This is equivalent to setting Show me as to Available in the Calendar UI.
     */
    public static final String TRANSPARENT = "transparent";

}
