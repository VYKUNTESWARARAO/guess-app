package com.gts.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    // Read the Firebase service account JSON from environment variable
    private static final String FIREBASE_JSON_ENV = "FIREBASE_SERVICE_ACCOUNT";

    @Bean
    public void init() throws IOException {
        String firebaseJson = System.getenv(FIREBASE_JSON_ENV);
        if (firebaseJson == null || firebaseJson.isEmpty()) {
            throw new RuntimeException(FIREBASE_JSON_ENV + " environment variable is not set!");
        }

        InputStream serviceAccount = new ByteArrayInputStream(firebaseJson.getBytes());

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    @Bean
    public Firestore firestore() throws IOException {
        String firebaseJson = System.getenv(FIREBASE_JSON_ENV);
        if (firebaseJson == null || firebaseJson.isEmpty()) {
            throw new RuntimeException(FIREBASE_JSON_ENV + " environment variable is not set!");
        }

        InputStream serviceAccount = new ByteArrayInputStream(firebaseJson.getBytes());

        FirestoreOptions options = FirestoreOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return options.getService();
    }
}
