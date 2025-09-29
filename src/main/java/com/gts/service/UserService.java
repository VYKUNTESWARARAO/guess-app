package com.gts.service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.gts.entity.Guess;
import com.gts.entity.User;

@Service
public class UserService {

    public int getTotalScore(String userId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection("guesses").whereEqualTo("userId", userId).get();
        List<Guess> guesses = future.get().getDocuments()
                .stream()
                .map(doc -> doc.toObject(Guess.class))
                .collect(Collectors.toList());

        return guesses.stream().mapToInt(Guess::getPoints).sum();
    }

    public long getCompletedSongs(String userId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection("guesses").whereEqualTo("userId", userId).get();
        return future.get().getDocuments().size();
    }
    
    private static final String COLLECTION_NAME = "users";

    public String createUser(User user) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference users = db.collection(COLLECTION_NAME);

        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(users.document().getId()); // auto-generate ID
        }

        user.setCreatedAt(System.currentTimeMillis());
        ApiFuture<DocumentReference> future = users.add(user);
        return user.getId();
    }
}
