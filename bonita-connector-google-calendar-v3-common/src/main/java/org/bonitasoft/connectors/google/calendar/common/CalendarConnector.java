package org.bonitasoft.connectors.google.calendar.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.api.services.calendar.CalendarRequest;
import com.google.api.services.calendar.model.Event;
import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential.Builder;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

public abstract class CalendarConnector extends AbstractConnector {

    public static final String APPLICATION_NAME = "applicationName";
    public static final String CALENDAR_ID = "calendarId";
    public static final String SERVICE_ACCOUNT_ID = "serviceAccountId";
    public static final String SERVICE_ACCOUNT_P12_FILE = "serviceAccountP12File";
    public static final String SERVICE_ACCOUNT_USER = "serviceAccountUser";

    public static final String INPUT_ID = "id";
    public static final String INPUT_PRETTY_PRINT = "prettyPrint";
    public static final String INPUT_MAX_ATTENDEES = "maxAttendees";
    public static final String INPUT_SEND_NOTIFICATIONS = "sendNotifications";

    public static final String OUTPUT_EVENT = "event";
    public static final String OUTPUT_ETAG = "etag";
    public static final String OUTPUT_HANGOUT_LINK = "hangoutLink";
    public static final String OUTPUT_HTML_LINK = "htmlLink";
    public static final String OUTPUT_I_CAL_UID = "iCalUID";
    public static final String OUTPUT_ID = "id";
    public static final String OUTPUT_STATUS = "status";
    public static final String OUTPUT_SUMMARY = "summary";
    public static final String OUTPUT_DESCRIPTION = "description";
    public static final String OUTPUT_LOCATION = "location";
    public static final String OUTPUT_TRANSPARENCY = "transparency";
    public static final String OUTPUT_VISIBILITY = "visibility";
    public static final String OUTPUT_SEQUENCE = "sequence";
    public static final String OUTPUT_ANYONE_CAN_ADD_SELF = "anyoneCanAddSelf";
    public static final String OUTPUT_GUESTS_CAN_INVITE_OTHERS = "guestsCanInviteOthers";
    public static final String OUTPUT_GUESTS_CAN_MODIFY = "guestsCanModify";
    public static final String OUTPUT_GUESTS_CAN_SEE_OTHER_GUESTS = "guestsCanSeeOtherGuests";

    @Override
    public void validateInputParameters() throws ConnectorValidationException {
        final List<String> errors = new ArrayList<String>();
        if (getServiceAccountId() == null) {
            errors.add("Service Account ID must be set.");
        } else if (getServiceAccountId().isEmpty()) {
            errors.add("Service Account ID must not be an empty String.");
        }
        if (getServiceAccountP12File() == null) {
            errors.add("Service Account P12 File must be set.");
        } else if (getServiceAccountP12File().isEmpty()) {
            errors.add("Service Account P12 File must not be en empty String.");
        } else {
            final File p12File = new File(getServiceAccountP12File());
            if (!p12File.exists()) {
                errors.add("Service Account P12 File refers to a non existing file: " + getServiceAccountP12File() + ".");
            } else if (p12File.isDirectory()) {
                errors.add("Service Account P12 File refers to a directory and not a file: " + getServiceAccountP12File() + ".");
            }
        }
        
        // CALENDAR ID
        if (getCalendarId() == null) {
            errors.add("CalendarId must be set.");
        }
        errors.addAll(checkParameters());
        if (!errors.isEmpty()) {
            throw new ConnectorValidationException(this, errors);
        }
    }

    @Override
    protected void executeBusinessLogic() throws ConnectorException {
        try {
            final HttpTransport httpTransport = new NetHttpTransport();
            try {
                final JsonFactory jsonFactory = new JacksonFactory();

                final Builder credentialBuilder = new Builder();
                credentialBuilder.setTransport(httpTransport);
                credentialBuilder.setJsonFactory(jsonFactory);
                credentialBuilder.setServiceAccountId(getServiceAccountId());
                credentialBuilder.setServiceAccountPrivateKeyFromP12File(new File(getServiceAccountP12File()));
                if (getServiceAccountUser() != null) {
                    credentialBuilder.setServiceAccountUser(getServiceAccountUser());
                }
                credentialBuilder.setServiceAccountScopes(Collections.singleton(CalendarScopes.CALENDAR));
                final Credential credential = credentialBuilder.build();

                final Calendar calendar = new Calendar.Builder(httpTransport, jsonFactory, credential).setApplicationName(getApplicationName()).build();

                doJobWithCalendarEvents(calendar.events());
            } finally {
                httpTransport.shutdown();
            }
        } catch (Exception e) {
            throw new ConnectorException(e);
        }
    }

    protected void ensureIdInputIsSpecified(List<String> errors) {
        if (getId() == null) {
            errors.add("Event Id must be set.");
        }
    }

    protected abstract void doJobWithCalendarEvents(final Calendar.Events events) throws Exception;

    protected abstract List<String> checkParameters();

    public String getCalendarId() {
        return (String) getInputParameter(CALENDAR_ID);
    }

    public String getApplicationName() {
        return (String) getInputParameter(APPLICATION_NAME);
    }

    public String getServiceAccountId() {
        return (String) getInputParameter(SERVICE_ACCOUNT_ID);
    }

    public String getServiceAccountP12File() {
        return (String) getInputParameter(SERVICE_ACCOUNT_P12_FILE);
    }

    public String getServiceAccountUser() {
        return (String) getInputParameter(SERVICE_ACCOUNT_USER);
    }

    protected Boolean getPrettyPrint() {
        return (Boolean) getInputParameter(INPUT_PRETTY_PRINT);
    }

    protected Integer getMaxAttendees() { return (Integer) getInputParameter(INPUT_MAX_ATTENDEES); }

    protected String getId() { return (String) getInputParameter(INPUT_ID); }

    protected Boolean getSendNotifications() { return (Boolean) getInputParameter(INPUT_SEND_NOTIFICATIONS); }

    protected void setCommonInputs(CalendarRequest<Event> request) {
        if (getPrettyPrint() != null) {
            request.setPrettyPrint(getPrettyPrint());
        }
    }

    protected void setOutputParameters(Event event) {
        if(event != null) {
            setOutputParameter(OUTPUT_EVENT, event);
            setOutputParameter(OUTPUT_ETAG, event.getEtag());
            setOutputParameter(OUTPUT_HANGOUT_LINK, event.getHangoutLink());
            setOutputParameter(OUTPUT_HTML_LINK, event.getHtmlLink());
            setOutputParameter(OUTPUT_I_CAL_UID, event.getICalUID());
            setOutputParameter(OUTPUT_ID, event.getId());
            setOutputParameter(OUTPUT_STATUS, event.getStatus());
            setOutputParameter(OUTPUT_SUMMARY, event.getSummary());
            setOutputParameter(OUTPUT_DESCRIPTION, event.getDescription());
            setOutputParameter(OUTPUT_LOCATION, event.getLocation());
            setOutputParameter(OUTPUT_TRANSPARENCY, event.getTransparency());
            setOutputParameter(OUTPUT_VISIBILITY, event.getVisibility());
            setOutputParameter(OUTPUT_SEQUENCE, event.getSequence());
            setOutputParameter(OUTPUT_ANYONE_CAN_ADD_SELF, event.getAnyoneCanAddSelf());
            setOutputParameter(OUTPUT_GUESTS_CAN_INVITE_OTHERS, event.getGuestsCanInviteOthers());
            setOutputParameter(OUTPUT_GUESTS_CAN_MODIFY, event.getGuestsCanModify());
            setOutputParameter(OUTPUT_GUESTS_CAN_SEE_OTHER_GUESTS, event.getGuestsCanSeeOtherGuests());
        }
    }
}
