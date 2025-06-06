package com.premaCare.logApi.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.premaCare.logApi.model.FeedingRecord;
import com.premaCare.logApi.model.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class feedingController {

    private static final String USERS_COLLECTION_NAME = "users"; // Parent collection
    private static final String FEEDING_COLLECTION_NAME = "feedingRecords"; // Subcollection
    private static final String MESSAGE_COLLECTION_NAME = "messages"; // Subcollection
    private Firestore firestore;

    // Constructor to inject Firestore instance
    public feedingController() {
        this.firestore = FirestoreClient.getFirestore();
    }

    // Retrieve all feeding records for a specific user
    @GetMapping("/feedingRecords/{userId}")
    public ResponseEntity<List<FeedingRecord>> getAllRecords(@PathVariable String userId) {
        try {
            // Get the reference to the user's feedingRecords subcollection
            CollectionReference collection = firestore.collection(USERS_COLLECTION_NAME)
                    .document(userId)
                    .collection(FEEDING_COLLECTION_NAME);

            // Fetch all records from that subcollection
            ApiFuture<QuerySnapshot> querySnapshot = collection.get();

            List<FeedingRecord> records = querySnapshot.get().getDocuments().stream()
                    .map(doc -> {
                        FeedingRecord record = doc.toObject(FeedingRecord.class);
                        record.setId(doc.getId()); // Set Firestore document ID
                        return record;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(records);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // Retrieve total amount of milk for a specific date and user
    @GetMapping("/totalMilk/{userId}/{date}")
    public ResponseEntity<String> getTotalMilk(@PathVariable String userId, @PathVariable String date) {
        try {
            // Get the reference to the user's feedingRecords subcollection
            CollectionReference collection = firestore.collection(USERS_COLLECTION_NAME)
                    .document(userId)
                    .collection(FEEDING_COLLECTION_NAME);

            Query query = collection.whereEqualTo("date", date);

            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            // Sum the feedingAmount for the filtered records
            double totalMilk = querySnapshot.get().getDocuments().stream()
                    .mapToDouble(doc -> doc.toObject(FeedingRecord.class).getAmount())
                    .sum();
            String response = totalMilk + " Ml";

            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // Retrieve feeding records for a specific date and baby name
    @GetMapping("/feedingRecords/{userId}/{date}/{babyName}")
    public ResponseEntity<List<FeedingRecord>> getFeedingRecordsByDateAndName(
            @PathVariable String userId, @PathVariable String date, @PathVariable String babyName) {
        try {
            // Get the reference to the user's feedingRecords subcollection
            CollectionReference collection = firestore.collection(USERS_COLLECTION_NAME)
                    .document(userId)
                    .collection(FEEDING_COLLECTION_NAME);

            // Query Firestore for records matching the date and baby name
            Query query = collection
                    .whereEqualTo("date", date)
                    .whereArrayContains("babyNameKeywords", babyName); // Matches any part of the name

            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            List<FeedingRecord> records = querySnapshot.get().getDocuments().stream()
                    .map(doc -> {
                        FeedingRecord record = doc.toObject(FeedingRecord.class);
                        record.setId(doc.getId());
                        return record;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(records);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // Retrieve messages for a specific user
    @GetMapping("/messages/{userId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String userId) {
        try {
            // Get the reference to the user's messages subcollection
            CollectionReference collection = firestore.collection(USERS_COLLECTION_NAME)
                    .document(userId)
                    .collection(MESSAGE_COLLECTION_NAME);

            // Fetch all messages from that subcollection
            ApiFuture<QuerySnapshot> querySnapshot = collection.get();

            List<Message> messages = querySnapshot.get().getDocuments().stream()
                    .map(doc -> {
                        Message message = doc.toObject(Message.class);
                        message.setId(doc.getId()); // Set Firestore document ID
                        return message;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(messages);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // Send a message to a user
  // @PostMapping("/messages/{userId}")
  // public ResponseEntity<String> sendMessage(@PathVariable String userId, @RequestBody Message message) {
  //     try {
  //         // Get the reference to the user's messages subcollection
  //         CollectionReference collection = firestore.collection(USERS_COLLECTION_NAME)
  //                 .document(userId)
  //                 .collection(MESSAGE_COLLECTION_NAME);

  //         // Add the message to the Firestore subcollection
  //         ApiFuture<DocumentReference> future = collection.add(message);
  //         future.get(); // Wait for completion

  //         return ResponseEntity.ok("Message sent successfully");
  //     } catch (ExecutionException | InterruptedException e) {
  //         e.printStackTrace();
  //         return ResponseEntity.status(500).body("Failed to send message");
  //     }
  // }

    @PostMapping("/messages/{userId}")
    public ResponseEntity<String> sendMessage(@PathVariable String userId, @RequestBody Message message) {
        try {
            // Tving isRead til false for nye meldinger sendt via dette endepunktet.
            // Dette garanterer at alle nye meldinger fra sykepleier er uleste når de legges til i Firebase.
            message.setIsRead(false); // <--- KUN DENNE LINJEN ER BEHOLDT OG ENDRET

            // Hent referansen til brukerens meldings-underkolleksjon
            CollectionReference collection = firestore.collection(USERS_COLLECTION_NAME)
                    .document(userId)
                    .collection(MESSAGE_COLLECTION_NAME);

            // Legg til meldingen i Firestore-underkolleksjonen
            ApiFuture<DocumentReference> future = collection.add(message);
            future.get(); // Vent på fullførelse

            return ResponseEntity.ok("Message sent successfully");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send message");
        }
    }

    @GetMapping("/searchChild")
    public ResponseEntity<List<Map<String, Object>>> searchChild(@RequestParam String name) {
        try {
            CollectionReference usersRef = firestore.collection("users");

            // Query based on the correct field: 'childsName'
            Query query = usersRef.whereEqualTo("childsName", name.trim());

            ApiFuture<QuerySnapshot> future = query.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            List<Map<String, Object>> results = new ArrayList<>();

            for (QueryDocumentSnapshot doc : documents) {
                Map<String, Object> data = new HashMap<>();
                data.put("userId", doc.getId());
                data.put("childsName", doc.get("childsName"));
                data.put("childDateOfBirth", doc.get("childDateOfBirth"));
                data.put("email", doc.get("email"));
                data.put("parentName", doc.get("parentName"));
                data.put("parentNumber", doc.get("parentNumber"));
                results.add(data);
            }

            return ResponseEntity.ok(results);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }



}





