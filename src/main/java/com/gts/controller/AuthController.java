package com.gts.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gts.service.FirebaseService;



@RestController
public class AuthController {

    private final FirebaseService firebaseService;

  
    public AuthController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email) {
        boolean exists = firebaseService.checkUserExists(email);
        if (exists) {
            try {
                String role = firebaseService.getUserRoleByEmail(email);
                Map<String, String> response = new HashMap<>();
                response.put("role", role);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    


}
