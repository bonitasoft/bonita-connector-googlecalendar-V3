# Bonita Google Calendar Connetor (API V3)
![](https://github.com/bonitasoft/bonita-connector-googlecalendar-V3/workflows/Build/badge.svg)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=bonitasoft_bonita-connector-googlecalendar-V3&metric=alert_status)](https://sonarcloud.io/dashboard?id=bonita-connector-googlecalendar-V3)
[![GitHub release](https://img.shields.io/github/v/release/bonitasoft/bonita-connector-google-calendar-v3?color=blue&label=Release)](https://github.com/bonitasoft/bonita-connector-googlecalendar-V3/releases)
[![Maven Central](https://img.shields.io/maven-central/v/org.bonitasoft.connectors/bonita-connector-google-calendar-v3.svg?label=Maven%20Central&color=orange)](https://search.maven.org/search?q=g:%22org.bonitasoft.connectors%22%20AND%20a:%22bonita-connector-google-calendar-v3%22)
[![License: GPL v2](https://img.shields.io/badge/License-GPL%20v2-yellow.svg)](https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html)

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

## Build

__Clone__ or __fork__ this repository, then at the root of the project run:

`./mvnw`

## Release

In order to create a new release push a `release-<version>` branch with the desired version in pom.xml.
Update the `master` with the next SNAPSHOT version.

## Contributing

We would love you to contribute, pull requests are welcome! Please see the [CONTRIBUTING.md](CONTRIBUTING.md) for more information.

## License

The sources and documentation in this project are released under the [GPLv2 License](LICENSE)


