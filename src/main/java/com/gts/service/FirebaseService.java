package com.gts.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

@Service
public class FirebaseService {

    private final Firestore firestore;

    
    public FirebaseService(Firestore firestore) {
        this.firestore = firestore;
    }

    public boolean checkUserExists(String email) {
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("users")
                .whereEqualTo("email", email)
                .get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            return !documents.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getUserRoleByEmail(String email) throws Exception {
        ApiFuture<QuerySnapshot> future = firestore.collection("users")
            .whereEqualTo("email", email)
            .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if (!documents.isEmpty()) {
            String role = documents.get(0).getString("role");
            return role != null ? role : "user"; // default role if none set
        }
        return "user"; // default if no user found
    }

}
