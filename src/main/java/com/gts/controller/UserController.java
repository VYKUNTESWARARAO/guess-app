package com.gts.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gts.entity.Song;
import com.gts.entity.User;
import com.gts.service.GuessService;
import com.gts.service.SongService;
import com.gts.service.UserService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
public class UserController {

    private final SongService songService;
    private final GuessService guessService;
    private final UserService userService;

    public UserController(SongService songService, GuessService guessService, UserService userService) {
        this.songService = songService;
        this.guessService = guessService;
        this.userService = userService;
    }

    // Get a random song for guessing
    @GetMapping("/song")
    public ResponseEntity<Song> getRandomSong() throws ExecutionException, InterruptedException {
        Song song = songService.getRandomSong();
        return ResponseEntity.ok(song);
    }

    // Submit a guess
    @PostMapping("/guess")
    public ResponseEntity<String> submitGuess(
            @AuthenticationPrincipal String userEmail,
            @RequestParam String songId,
            @RequestParam String guessedMovie,
            @RequestParam long timeTaken) throws ExecutionException, InterruptedException {

        Song song = songService.getAllSongs()
                .stream()
                .filter(s -> s.getId().equals(songId))
                .findFirst()
                .orElse(null);

        if (song == null) return ResponseEntity.badRequest().body("Song not found");

        int points = guessService.submitGuess(userEmail, song, guessedMovie, timeTaken);
        return ResponseEntity.ok("You earned " + points + " points!");
    }

    // Get user stats
    @GetMapping("/stats")
    public ResponseEntity<String> getUserStats(@AuthenticationPrincipal String userEmail) throws ExecutionException, InterruptedException {
        int totalScore = userService.getTotalScore(userEmail);
        long completedSongs = userService.getCompletedSongs(userEmail);
        return ResponseEntity.ok("Total Score: " + totalScore + ", Songs Completed: " + completedSongs);
    }
    
    @PostMapping("/post")
    public ResponseEntity<String> createUser(@RequestBody User user) throws ExecutionException, InterruptedException {
        String id = userService.createUser(user);
        return ResponseEntity.ok("User created with ID: " + id);
    }
}
