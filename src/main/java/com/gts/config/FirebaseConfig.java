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
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;


@Configuration
public class FirebaseConfig {

    // Environment variable name
    private static final String FIREBASE_JSON_ENV = "FIREBASE_SERVICE_ACCOUNT";

    // Inject the resource from classpath
    @Value("classpath:firebase-service-account.json")
    private Resource serviceAccountResource;

    private InputStream getServiceAccountStream() throws IOException {
        String firebaseJson = System.getenv(FIREBASE_JSON_ENV);
        if (firebaseJson != null && !firebaseJson.isEmpty()) {
            // Use environment variable if set
            return new ByteArrayInputStream(firebaseJson.getBytes(StandardCharsets.UTF_8));
        } else {
            // Otherwise, load from classpath resource
            if (!serviceAccountResource.exists()) {
                throw new RuntimeException("Firebase service account JSON file not found in classpath!");
            }
            return serviceAccountResource.getInputStream();
        }
    }

    @Bean
    public Firestore firestore() throws IOException {
        try (InputStream serviceAccount = getServiceAccountStream()) {
            FirestoreOptions options = FirestoreOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            return options.getService();
        }
    }

   
    
}
