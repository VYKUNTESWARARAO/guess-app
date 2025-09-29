package com.gts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")

public class PublicController {

    @GetMapping("/rules")
    public ResponseEntity<String> getRules() {
        String rules = """
                1. Guess the movie name of the song.
                2. If guessed within 10 sec → 10 points.
                3. If guessed within 20 sec → 5 points.
                4. If guessed within 30 sec → 3 points.
                5. After 30 sec or wrong guess → 0 points.
                """;
        return ResponseEntity.ok(rules);
    }
    @GetMapping("/")
    public String home() {
        return "🎵 Guess the Song Backend is running!";
    }
}
