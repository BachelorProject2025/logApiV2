package com.premaCare.logApi.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.premaCare.logApi.model.FeedingRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feeding")
public class feedingController {

    private static final String COLLECTION_NAME = "feedingRecords";
    private Firestore firestore;

    // Constructor to inject Firestore instance
    public void FeedingController() {
        this.firestore = FirestoreClient.getFirestore();
    }

    // Add a new feeding record to Firestore
    @PostMapping
    public ResponseEntity<String> addRecord(@RequestBody FeedingRecord record) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference collection = firestore.collection(COLLECTION_NAME);

            // Generate keywords for searching by first name, last name, or full name
            String[] nameParts = record.getBabyName().split(" ");
            List<String> keywords = new ArrayList<>();
            keywords.add(record.getBabyName()); // Full name
            keywords.addAll(Arrays.asList(nameParts)); // Individual name parts

            // Add keywords to the record
            record.setBabyNameKeywords(keywords);

            // Add the record to Firestore, Firestore will auto-generate the ID
            ApiFuture<DocumentReference> future = collection.add(record);
            DocumentReference docRef = future.get();

            // Set the Firestore-generated ID in the FeedingRecord object
            record.setId(docRef.getId());

            return ResponseEntity.ok("Record added successfully");
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


    // Retrieve total amount of milk for a specific date
    @GetMapping("/totalMilk/{date}")
    public ResponseEntity<String> getTotalMilk(@PathVariable String date) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference collection = firestore.collection(COLLECTION_NAME);


            Query query = collection.whereEqualTo("date", date);

            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            // Sum the feedingAmount for the filtered records
            double totalMilk = querySnapshot.get().getDocuments().stream()
                    .mapToDouble(doc -> doc.toObject(FeedingRecord.class).getFeedingAmount())
                    .sum();
            String response = totalMilk + " Ml";

            return ResponseEntity.ok(response);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/totalMilk/{date}/{babyName}")
    public ResponseEntity<String> getTotalMilk(@PathVariable String date, @PathVariable String babyName) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference collection = firestore.collection(COLLECTION_NAME);

            // Query Firestore for records matching the date and baby name
            Query query = collection
                    .whereEqualTo("date", date)
                    .whereArrayContains("babyNameKeywords", babyName); // Matches any part of the name

            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            double totalMilk = querySnapshot.get().getDocuments().stream()
                    .mapToDouble(doc -> doc.toObject(FeedingRecord.class).getFeedingAmount())
                    .sum();

            return ResponseEntity.ok(totalMilk + " ml");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching total milk: " + e.getMessage());
        }
    }



}


