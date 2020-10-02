# Bonita Google Calendar Connetor (API V3)

This is the implementation of the Bonita Google Calendar based on the Google API V3.

## Steps to get the credentials from Google

### Prerequistes

You must have a G Suite account to properly use this connector.

### Service Account creation

Go to https://console.developers.google.com

Create a project
   - Set a project **Name** and a **Location**
   - This may take a few seconds to a few minutes

Go to “APIs & Services” section
   - Click on `+ ENABLE APIS AND SERVICES`
   - Search for Google Calendar API, and **Enable** it

Go to “APIs & Services” section and then into “Credentials” sub section

   - Click on `+ CREATE CREDENTIALS`and select **Service account**
   - Choose a service account **name** and **description** and click on `CREATE`
   - *Optional*, choose a **Roles** for the account permissions and lick on `CONTINUE`
   - *Optional*, you can configure users access to the service account, and click on `DONE`
   - In the Service Accounts table, click on the `modify` icon for your service account.
   - In Keys section, click on `ADD KEY` and `Create a new key`
   - Select the JSON type and click on `CREATE`
   - A download dialog should popup, the file contains the JSON token than should be used in the connector. Download it in a safe place.
   - In addition, you should enable `G Suite Domain-wide delegation` for the service account to be able to modify your collaborators calendar.

Open your own Google Apps Domain

Go to http://admin.google.com and choose “Security” section, then Advanced Settings and then Manage OAuth Client access

   - copy the CLIENT ID from developer console value and paste it in the Client Name input text field
   - inside the API Scopes input text field, enter https://www.googleapis.com/auth/calendar
   - click Authorize
