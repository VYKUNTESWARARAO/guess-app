package com.gts.service;



import com.gts.entity.Song;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class SongService {

    private static final String COLLECTION_NAME = "songs";

    // Add a song
    public String addSong(Song song) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document();
        song.setId(docRef.getId());
        song.setCreatedAt(System.currentTimeMillis());
        ApiFuture<WriteResult> result = docRef.set(song);
        result.get(); // wait for completion
        return docRef.getId();
    }

    // Update a song
    public void updateSong(String id, Song song) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        ApiFuture<WriteResult> result = docRef.set(song);
        result.get();
    }

    // Delete a song
    public void deleteSong(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> result = db.collection(COLLECTION_NAME).document(id).delete();
        result.get();
    }

    // Get all songs
    public List<Song> getAllSongs() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return documents.stream().map(doc -> doc.toObject(Song.class)).collect(Collectors.toList());
    }

    // Get random song (for user guessing)
    public Song getRandomSong() throws ExecutionException, InterruptedException {
        List<Song> songs = getAllSongs();
        if (songs.isEmpty()) return null;
        int randomIndex = (int) (Math.random() * songs.size());
        return songs.get(randomIndex);
    }
}

