package com.gts.controller;

import com.gts.entity.Song;
import com.gts.service.SongService;
import com.gts.service.UserValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/songs")
public class AdminController {

    private final SongService songService;
    private final UserValidationService userValidationService;

    public AdminController(SongService songService, UserValidationService userValidationService) {
        this.songService = songService;
        this.userValidationService = userValidationService;
    }

    /** ✅ Reusable check for admin based on email header */
    private void checkAdmin(String email) throws Exception {
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Missing email header");
        }
        if (!userValidationService.isAdmin(email)) {
            throw new RuntimeException("Access denied: Not an admin");
        }
    }

    /** ➕ Add new song */
    @PostMapping
    public ResponseEntity<String> addSong(
            @RequestBody Song song,
            @RequestHeader("X-User-Email") String email) throws Exception {

        checkAdmin(email);
        String id = songService.addSong(song);
        return ResponseEntity.ok("Song added with ID: " + id);
    }

    /** ✏️ Update existing song */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSong(
            @PathVariable String id,
            @RequestBody Song song,
            @RequestHeader("X-User-Email") String email) throws Exception {

        checkAdmin(email);
        songService.updateSong(id, song);
        return ResponseEntity.ok("Song updated");
    }

    /** ❌ Delete song */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSong(
            @PathVariable String id,
            @RequestHeader("X-User-Email") String email) throws Exception {

        checkAdmin(email);
        songService.deleteSong(id);
        return ResponseEntity.ok("Song deleted");
    }

    /** 📃 Get all songs */
    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs(
            @RequestHeader("X-User-Email") String email) throws Exception {

        checkAdmin(email);
        return ResponseEntity.ok(songService.getAllSongs());
    }
}
