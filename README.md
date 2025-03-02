This repository handles the CRUD operations for the Translation module.
**Setup Instructions**
  Install MariaDB: Set up MariaDB on your local machine and create a database named translation_db.
  Run the Application: You can run the application with the following command:

**Authentication**
When you directly hit the endpoint, you might encounter an "Unauthorized" error. To authenticate the user, use the following curl command to log in first:

curl --location 'http://localhost:8080/api/auth/login' \
--header 'Content-Type: application/json' \
--data '{"username": "translation_app", "password": "123"}'

This end ponit give you the token. by using that token you can hit api's for know token expiry time is approximately 1 day but you can adjust according to your need.

I have done as much code coverage as I could in given time, which is not 90 percent, but the remaining functionality is working.
