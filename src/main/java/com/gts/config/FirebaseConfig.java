package com.gts.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

@Configuration
public class FirebaseConfig {

    // Name of environment variable for credentials
    private static final String FIREBASE_JSON_ENV = "FIREBASE_SERVICE_ACCOUNT";

    // Inject classpath resource for credentials
    @Value("classpath:firebase-service-account.json")
    private Resource serviceAccountResource;

    // Utility to get service account input stream
    private InputStream getServiceAccountStream() throws IOException {
        String firebaseJson = System.getenv(FIREBASE_JSON_ENV);
        if (firebaseJson != null && !firebaseJson.isEmpty()) {
            // Use environment variable if provided
            return new ByteArrayInputStream(firebaseJson.getBytes(StandardCharsets.UTF_8));
        } else {
            // Otherwise, use file from classpath
            if (!serviceAccountResource.exists()) {
                throw new RuntimeException("Firebase service account JSON file not found in classpath!");
            }
            return serviceAccountResource.getInputStream();
        }
    }

    // Initialize FirebaseApp BEFORE anything uses Firestore
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        try (InputStream serviceAccount = getServiceAccountStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
            // Initialize only if not already done
            if (FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.initializeApp(options);
            } else {
                return FirebaseApp.getInstance();
            }
        }
    }

    // Provide Firestore bean, initialized after FirebaseApp
    @Bean
    public Firestore firestore(FirebaseApp firebaseApp) {
        return FirestoreClient.getFirestore();
    }
}
