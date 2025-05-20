// File: 'Mobile_App_Project/ProjektAplikacjeMobilne/app/src/main/java/com/example/booktracker/Book.java'
package com.example.booktracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity
public class Book {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title, author, description;

    public Book(int id, String title, String author, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
    }

    @Ignore
    public Book(String title, String author, String description) {
        this.title = title;
        this.author = author;
        this.description = description;
    }
}