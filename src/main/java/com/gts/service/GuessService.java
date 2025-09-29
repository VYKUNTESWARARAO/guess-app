package com.gts.service;

import com.gts.entity.Guess;
import com.gts.entity.Song;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class GuessService {

    private static final String COLLECTION_NAME = "guesses";

    // Submit a guess and calculate points
    public int submitGuess(String userId, Song song, String guessedMovie, long timeTaken) throws ExecutionException, InterruptedException {
        Guess guess = new Guess();
        guess.setUserId(userId);
        guess.setSongId(song.getId());
        guess.setGuessedMovie(guessedMovie);
        guess.setTimeTaken(timeTaken);

        // Calculate points
        int points = calculatePoints(timeTaken, song.getMovieName().equalsIgnoreCase(guessedMovie));
        guess.setPoints(points);
        guess.setCorrect(points > 0);
        guess.setCreatedAt(System.currentTimeMillis());

        // Save guess to Firestore
        DocumentReference docRef = FirestoreClient.getFirestore().collection(COLLECTION_NAME).document();
        guess.setId(docRef.getId());
        ApiFuture<com.google.cloud.firestore.WriteResult> result = docRef.set(guess);
        result.get();

        return points;
    }

    private int calculatePoints(long timeTaken, boolean correct) {
        if (!correct) return 0;
        if (timeTaken <= 10) return 10;
        else if (timeTaken <= 20) return 5;
        else if (timeTaken <= 30) return 3;
        else return 0;
    }
}
