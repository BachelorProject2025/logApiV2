package com.premaCare.logApi.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.premaCare.logApi.model.FeedingRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feeding")
public class feedingController {

    private static final String COLLECTION_NAME = "feedingRecords";

    // Add a new feeding record to Firestore
    @PostMapping
    public ResponseEntity<String> addRecord(@RequestBody FeedingRecord record) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference collection = firestore.collection(COLLECTION_NAME);

            // Add the record to Firestore
            ApiFuture<?> future = collection.add(record);
            future.get(); // Ensure the operation completes

            return ResponseEntity.ok("Record added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to add record: " + e.getMessage());
        }
    }

    // Retrieve all feeding records from Firestore
    @GetMapping
    public ResponseEntity<List<FeedingRecord>> getAllRecords() {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference collection = firestore.collection(COLLECTION_NAME);

            // Fetch all records
            ApiFuture<QuerySnapshot> querySnapshot = collection.get();

            // Convert Firestore documents to FeedingRecord objects
            List<FeedingRecord> records = querySnapshot.get().getDocuments().stream()
                    .map(doc -> {
                        FeedingRecord record = doc.toObject(FeedingRecord.class);
                        record.setId(doc.getId()); // Set Firestore document ID if needed
                        return record;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(records);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}

