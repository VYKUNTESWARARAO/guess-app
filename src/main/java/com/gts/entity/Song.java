// Song.java
package com.gts.entity;

import lombok.Data;

@Data
public class Song {
    private String id;          // Firestore document ID
    private String title;       
    private String movieName;   
    private String genre;       
    private String youtubeUrl;
    private String createdBy;   
    private long createdAt;
}
