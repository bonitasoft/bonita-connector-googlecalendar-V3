package org.bonitasoft.connectors.google.calendar.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

                doJobWithCalendar(calendar);
            } finally {
                httpTransport.shutdown();
            }
        } catch (Exception e) {
            throw new ConnectorException(e);
        }
    }

    protected abstract void doJobWithCalendar(final Calendar calendarService) throws Exception;

    protected abstract List<String> checkParameters();

    public String getCalendarId() {
        return (String) getInputParameter("calendarId");
    }

    public String getApplicationName() {
        return (String) getInputParameter("applicationName");
    }

    public String getServiceAccountId() {
        return (String) getInputParameter("serviceAccountId");
    }

    public String getServiceAccountP12File() {
        return (String) getInputParameter("serviceAccountP12File");
    }

    public String getServiceAccountUser() {
        return (String) getInputParameter("serviceAccountUser");
    }
}
