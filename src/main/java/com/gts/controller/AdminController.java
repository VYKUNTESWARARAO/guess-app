package com.gts.controller;

import com.gts.entity.Song;
import com.gts.service.SongService;
import com.gts.service.UserValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    private void checkAdmin(Authentication authentication) throws ExecutionException, InterruptedException {
        String email = (String) authentication.getPrincipal();
        if (!userValidationService.isAdmin(email)) {
            throw new RuntimeException("Access denied: Not an admin");
        }
    }

    @PostMapping
    public ResponseEntity<String> addSong(@RequestBody Song song, Authentication authentication) throws Exception {
        checkAdmin(authentication);
        String id = songService.addSong(song);
        return ResponseEntity.ok("Song added with ID: " + id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSong(@PathVariable String id, @RequestBody Song song, Authentication authentication) throws Exception {
        checkAdmin(authentication);
        songService.updateSong(id, song);
        return ResponseEntity.ok("Song updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable String id, Authentication authentication) throws Exception {
        checkAdmin(authentication);
        songService.deleteSong(id);
        return ResponseEntity.ok("Song deleted");
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs(Authentication authentication) throws Exception {
        checkAdmin(authentication);
        return ResponseEntity.ok(songService.getAllSongs());
    }
}
