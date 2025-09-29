// Guess.java
package com.gts.entity;

import lombok.Data;

@Data
public class Guess {
    private String id;          
    private String songId;      
    private String userId;      
    private String guessedMovie;
    private long timeTaken;     
    private int points;         
    private boolean correct;    
    private long createdAt;
}
