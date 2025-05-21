// app/src/main/java/com/example/booktracker/model/User.java
package com.example.booktracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String email;
    public String password;
}