# logApiV2
This repository contains the implementation of an API designed to act as a bridge between a database and a webpage. The API facilitates secure, efficient, and scalable communication between the backend and the frontend, ensuring data is seamlessly retrieved, processed, and displayed.


Overview

This API is designed to manage feeding records for the app Premature. It interacts with Firebase Firestore for data storage, allowing you to add and retrieve feeding records via RESTful endpoints. The API is built with Spring Boot and connects to Firestore for persistent data management.

Endpoints

1. Add a Feeding Record
	•	Method: POST
	•	Endpoint: /api/feeding
	•	Description: Adds a new feeding record to the Firestore collection feedingRecords.
	•	Request Body:

{
  "feedingTime": "2025-01-19T10:30:00",
  "feedingMethod": "bottle",
  "weight": 3.2,
  "date": "2025-01-19",
  "pee": true,
  "poop": true,
  "comments": "Baby feeding well."
}


	•	cURL Command:

curl -X POST http://localhost:8080/api/feeding \
-H "Content-Type: application/json" \
-d '{
  "feedingTime": "2025-01-19T10:30:00",
  "feedingMethod": "bottle",
  "weight": 3.2,
  "date": "2025-01-19",
  "pee": true,
  "poop": true,
  "comments": "Baby feeding well."
}'

2. Get All Feeding Records
	•	Method: GET
	•	Endpoint: /api/feeding
	•	Description: Fetches all feeding records from the Firestore collection feedingRecords.
	•	cURL Command:

curl -X GET http://localhost:8080/api/feeding


	•	Recommended for Pretty Output:
Install jq for formatted JSON output:
	•	Install jq using Homebrew (macOS):

brew install jq


	•	Command with jq:

curl -X GET http://localhost:8080/api/feeding | jq

Setup Instructions

Prerequisites
	1.	Java Development Kit (JDK): Ensure JDK 17 or higher is installed.
	2.	Maven: Ensure Maven is installed for building and running the project.
	3.	Firebase Service Account JSON:
	•	The firebase-service-account.json file is NOT included in the repository for security reasons.
	•	Obtain the service account JSON file from the Firebase Console:
	1.	Navigate to your Firebase project.
	2.	Go to Project Settings > Service Accounts.
	3.	Click “Generate New Private Key” and download the JSON file.
	•	Place the file at:

src/main/resources/firebase-service-account.json



Code Configuration

In LogApiApplication.java, update the file path for the service account JSON if needed:

FirebaseOptions options = FirebaseOptions.builder()
    .setCredentials(GoogleCredentials.fromStream(
        new FileInputStream("src/main/resources/firebase-service-account.json")
    ))
    .build();
FirebaseApp.initializeApp(options);

Build and Run

Build the Project

Use Maven to build the project:

mvn clean install

Run the Application

Start the Spring Boot application:

mvn spring-boot:run

The API will run on http://localhost:8080 by default.

Error Handling

Common Issues:
	1.	Missing Firebase Service Account JSON File:
	•	Ensure the JSON file is in the correct location and the file path in LogApiApplication.java is accurate.
	2.	Empty Firestore Collection:
	•	Manually add documents to the feedingRecords collection via the Firebase Console to test retrieval.
	3.	Formatted Output for curl:
	•	Use jq or another JSON formatter to view the response in a readable format.

Dependencies

The following dependencies are required:
	•	Spring Boot Starter Web
	•	Spring Boot Starter Validation
	•	Firebase Admin SDK

Feel free to reach out if you need additional help setting up or running the API!