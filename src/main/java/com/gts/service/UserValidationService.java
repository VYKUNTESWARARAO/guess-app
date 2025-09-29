package com.gts.service;

import com.gts.entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class UserValidationService {

    private final Firestore firestore;

    public UserValidationService(Firestore firestore) {
        this.firestore = firestore;
    }

    public boolean isAdmin(String email) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection("users")
                .whereEqualTo("email", email)
                .get();

        for (DocumentSnapshot doc : future.get().getDocuments()) {
            User user = doc.toObject(User.class);
            if (user != null && "ADMIN".equalsIgnoreCase(user.getRole())) {
                return true;
            }
        }
        return false;
    }
}
