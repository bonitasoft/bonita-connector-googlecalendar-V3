package org.bonitasoft.connectors.google.calendar.common;


public interface EventVisibility {
    
    /**
     *  The event is confirmed. This is the default status.
     */
    public static final String CONFIRMED = "confirmed";
    /**
     *  The event is tentatively confirmed.
     */
    public static final String TENTATIVE = "tentative";
    /**
     *  The event is cancelled (deleted). 
     */
    public static final String CANCELLED = "cancelled";

}
