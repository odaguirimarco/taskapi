# Task Management API

This is a RESTful API for managing tasks. It uses Spring Boot, Spring Security, and an H2 in-memory database (for testing purposes).

## Prerequisites

* Java 17 or later
* Maven or Gradle
* Postman or `curl`

## Running the Application

1. **Clone the Repository:**

    ```bash
    git clone https://github.com/odaguirimarco/taskapi
    cd taskapi
    ```

2.  **Build the Application:**

    * Open a terminal or command prompt and navigate to the extracted project directory.
    * **Maven:**

        ```bash
        ./mvnw clean install
        ```

    * **Gradle:**

        ```bash
        ./gradlew clean build
        ```

3.  **Run the Application:**

    * **Maven:**

        ```bash
        ./mvnw spring-boot:run
        ```

    * **Gradle:**

        ```bash
        ./gradlew bootRun
        ```

    The application will start on `http://localhost:8080`.

## Testing the API with Postman or `curl`

### 1. Retrieve the CSRF Token

First, retrieve the CSRF token. This is necessary for POST, PUT, and DELETE requests.

* **`curl`:**

    ```bash
    curl http://localhost:8080/csrf
    ```

    * Extract the `token` value from the JSON response.

* **Postman:**

    * Make a `GET` request to `http://localhost:8080/csrf`.
    * Extract the `token` value from the JSON response.

### 2. Create a Task (POST)

* **`curl`:**

    ```bash
    curl -v -u user:password -H "Content-Type: application/json" -H "X-CSRF-TOKEN: <YOUR_CSRF_TOKEN>" -X POST -d '{
      "title": "First Task",
      "description": "This is the first Task description",
      "dueDate": "2024-04-07",
      "status": "TO_DO"
    }' http://localhost:8080/api/tasks
    ```

    * Replace `<YOUR_CSRF_TOKEN>` with the token from step 1.

* **Postman:**

    * URL: `http://localhost:8080/api/tasks`
    * Method: `POST`
    * Authorization: Basic Auth (username: `user`, password: `password`)
    * Headers:
        * `Content-Type: application/json`
        * `X-CSRF-TOKEN: <YOUR_CSRF_TOKEN>` (replace with token from step 1)
    * Body (raw, JSON):

        ```json
        {
          "title": "First Task",
          "description": "This is the first Task description",
          "dueDate": "2024-04-07",
          "status": "TO_DO"
        }
        ```

### 3. Get All Tasks (GET)

* **`curl`:**

    ```bash
    curl -v http://localhost:8080/api/tasks
    ```

* **Postman:**

    * URL: `http://localhost:8080/api/tasks`
    * Method: `GET`

### 4. Get a Task by ID (GET)

* **`curl`:**

    ```bash
    curl -v http://localhost:8080/api/tasks/<TASK_ID>
    ```

    * Replace `<TASK_ID>` with the ID of the task you want to retrieve.

* **Postman:**

    * URL: `http://localhost:8080/api/tasks/<TASK_ID>` (replace `<TASK_ID>`)
    * Method: `GET`

### 5. Update a Task (PUT)

* **`curl`:**

    ```bash
    curl -v -u user:password -H "Content-Type: application/json" -H "X-CSRF-TOKEN: <YOUR_CSRF_TOKEN>" -X PUT -d '{
      "title": "Updated Task",
      "description": "Updated task description",
      "dueDate": "2025-04-08",
      "status": "IN_PROGRESS"
    }' http://localhost:8080/api/tasks/<TASK_ID>
    ```

    * Replace `<TASK_ID>` and `<YOUR_CSRF_TOKEN>`.

* **Postman:**

    * URL: `http://localhost:8080/api/tasks/<TASK_ID>` (replace `<TASK_ID>`)
    * Method: `PUT`
    * Authorization: Basic Auth (username: `user`, password: `password`)
    * Headers:
        * `Content-Type: application/json`
        * `X-CSRF-TOKEN: <YOUR_CSRF_TOKEN>` (replace with token from step 1)
    * Body (raw, JSON):

        ```json
        {
          "title": "Updated Task",
          "description": "Updated task description",
          "dueDate": "2025-04-08",
          "status": "IN_PROGRESS"
        }
        ```

### 6. Delete a Task (DELETE)

* **`curl`:**

    ```bash
    curl -v -u user:password -H "X-CSRF-TOKEN: <YOUR_CSRF_TOKEN>" -X DELETE http://localhost:8080/api/tasks/<TASK_ID>
    ```

    * Replace `<TASK_ID>` and `<YOUR_CSRF_TOKEN>`.

* **Postman:**

    * URL: `http://localhost:8080/api/tasks/<TASK_ID>` (replace `<TASK_ID>`)
    * Method: `DELETE`
    * Authorization: Basic Auth (username: `user`, password: `password`)
    * Headers:
        * `X-CSRF-TOKEN: <YOUR_CSRF_TOKEN>` (replace with token from step 1)

## Security

* The API uses HTTP Basic Authentication.
* CSRF protection is enabled for POST, PUT, and DELETE requests.
* GET requests do not require authentication or CSRF tokens.

## Database

* The application uses an H2 in-memory database for testing.
* The database is cleared when the application restarts.

## Notes

* Replace placeholders like `<TASK_ID>`, and `<YOUR_CSRF_TOKEN>` with your actual values.
* For production, use a persistent database and a more secure authentication mechanism.
* For production, never disable CSRF protection.