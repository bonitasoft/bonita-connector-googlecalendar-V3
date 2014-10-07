Bonita BPM Google Calendar Connetor (API V3)
================================

This is the implementation of the Bonita BPM Google Calendar based on the Google API V3.

Steps to get the credentials from Google
-------------------------

Go to https://developers.google.com/console/

Create a project
   - this may take a few seconds to a few minutes

Go to “APIS & AUTH” section and then into “APIs” sub section
   - enable Calendar API row by turning it from `OFF` to `ON`

Go to “APIS & AUTH” section and then into “Credentials” sub section

   - click on the red button called “Create New Client ID”
   - select “Service Account” Application type
   - click "Create Client ID"
   - click “Generate new P12 key” and locate the downloaded file in a folder of your choice (in case you had a name in your project name, this file name will contain spaces, remove them.)
   - copy the full path to this file as it is an input parameter of the connector (`p12key`).
   - copy the EMAIL  ADDRESS as it is an input parameter of the connector (`service account ID`)
   - copy the CLIENT ID as it will be used in your Google Apps Admin Security configuration

Open your own Google Apps Domain

Go to http://admin.google.com and choose “Security” section, then Advanced Settings and then Manage OAuth Client access

   - copy the CLIENT ID from developer console value and paste it in the Client Name input text field
   - inside the API Scopes input text field, enter https://www.googleapis.com/auth/calendar
   - click Authorize
