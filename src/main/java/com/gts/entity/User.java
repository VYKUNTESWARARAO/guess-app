// User.java
package com.gts.entity;

import lombok.Data;

@Data
public class User {
    private String id;        
    private String email;
    private String displayName;
    private String role;     // ADMIN / USER
    private int score;       
    private long createdAt;
}
