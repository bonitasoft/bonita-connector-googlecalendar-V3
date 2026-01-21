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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarRequest;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;

public abstract class CalendarConnector extends AbstractConnector {

    enum AuthMode {
        JSON, P12
    }

    public static final String APPLICATION_NAME = "applicationName";
    public static final String CALENDAR_ID = "calendarId";
    public static final String AUTH_MODE = "authMode";
    public static final String SERVICE_ACCOUNT_ID = "serviceAccountId";
    public static final String SERVICE_ACCOUNT_JSON_TOKEN = "serviceAccountJsonToken";
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

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Override
    public void validateInputParameters() throws ConnectorValidationException {
        final List<String> errors = new ArrayList<>();
        validateAuthenticationInputs(errors);

        if (getServiceAccountId() == null || getServiceAccountId().isEmpty()) {
            errors.add("Service Account ID must be set.");
        }

        if (getCalendarId() == null || getCalendarId().isEmpty()) {
            errors.add("CalendarId must be set.");
        }
        errors.addAll(checkParameters());
        if (!errors.isEmpty()) {
            throw new ConnectorValidationException(this, errors);
        }
    }

    private void validateAuthenticationInputs(final List<String> errors) throws ConnectorValidationException {
        if (getAuthMode() == null) {
            throw new ConnectorValidationException(this, "Authentication mode must be set.");
        } else {
            try {
                AuthMode.valueOf(getAuthMode());
            } catch (IllegalArgumentException e) {
                throw new ConnectorValidationException(this, "Supported authentication mode are: "
                        + Stream.of(AuthMode.values())
                                .map(AuthMode::name)
                                .collect(Collectors.joining(", ")));
            }
        }

        AuthMode authMode = AuthMode.valueOf(getAuthMode());
        if (authMode == AuthMode.P12) {
            validateP12AuthInputs(errors);
        } else if (authMode == AuthMode.JSON
                && (getServiceAccountJsonToken() == null || getServiceAccountJsonToken().isEmpty())) {
            errors.add("A Service Account credential using a Json token must be set.");
        }
    }

    private void validateP12AuthInputs(final List<String> errors) {
        if (getServiceAccountP12File() == null || getServiceAccountP12File().isEmpty()) {
            errors.add("A Service Account credential using a p12 file (legacy) must be set.");
        } else {
            final File p12File = new File(getServiceAccountP12File());
            if (!p12File.exists()) {
                errors.add(
                        "Service Account P12 File refers to a non existing file: " + getServiceAccountP12File() + ".");
            } else if (p12File.isDirectory()) {
                errors.add("Service Account P12 File refers to a directory and not a file: "
                        + getServiceAccountP12File() + ".");
            }
        }
    }

    @Override
    protected void executeBusinessLogic() throws ConnectorException {
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            try {
                AuthMode authMode = AuthMode.valueOf(getAuthMode());
                HttpRequestInitializer requestInitializer = null;
                if (authMode == AuthMode.JSON) {
                    // Load client secrets.
                    try (InputStream jsonTokenStream = new ByteArrayInputStream(
                            getServiceAccountJsonToken().getBytes())) {
                        requestInitializer = new HttpCredentialsAdapter(
                                ServiceAccountCredentials.fromStream(jsonTokenStream)
                                        .toBuilder()
                                        .setServiceAccountUser(getServiceAccountUser())
                                        .build()
                                        .createScoped(CalendarScopes.CALENDAR));
                    }
                } else if (authMode == AuthMode.P12) {
                    requestInitializer = new GoogleCredential.Builder()
                            .setTransport(httpTransport)
                            .setJsonFactory(JSON_FACTORY)
                            .setServiceAccountId(getServiceAccountId())
                            .setServiceAccountUser(getServiceAccountUser())
                            // variable p12File is a String w/ path to the .p12 file name
                            .setServiceAccountPrivateKeyFromP12File(new java.io.File(getServiceAccountP12File()))
                            .build()
                            .createScoped(Collections.singleton(CalendarScopes.CALENDAR));
                }
                Calendar calendar = new Calendar.Builder(httpTransport, JSON_FACTORY, requestInitializer)
                        .setApplicationName(getApplicationName())
                        .build();
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

    protected abstract void doJobWithCalendarEvents(final Calendar.Events events) throws ConnectorException;

    protected abstract List<String> checkParameters();

    public String getCalendarId() {
        return (String) getInputParameter(CALENDAR_ID);
    }

    public String getAuthMode() {
        return (String) getInputParameter(AUTH_MODE);
    }

    public String getApplicationName() {
        return (String) getInputParameter(APPLICATION_NAME);
    }

    public String getServiceAccountId() {
        return (String) getInputParameter(SERVICE_ACCOUNT_ID);
    }

    public String getServiceAccountJsonToken() {
        return (String) getInputParameter(SERVICE_ACCOUNT_JSON_TOKEN);
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

    protected Integer getMaxAttendees() {
        return (Integer) getInputParameter(INPUT_MAX_ATTENDEES);
    }

    protected String getId() {
        return (String) getInputParameter(INPUT_ID);
    }

    protected Boolean getSendNotifications() {
        return (Boolean) getInputParameter(INPUT_SEND_NOTIFICATIONS);
    }

    protected void setCommonInputs(CalendarRequest<Event> request) {
        if (getPrettyPrint() != null) {
            request.setPrettyPrint(getPrettyPrint());
        }
    }

    protected void setOutputParameters(Event event) {
        if (event != null) {
            setOutputParameter(OUTPUT_EVENT, event.toString());
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
