package com.example.booktracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity
public class Book {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title, author, description;
    public String user_referenced;

    public Book(int id, String title, String author, String description, String user_referenced) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.user_referenced = user_referenced;
    }

    @Ignore
    public Book(String title, String author, String description, String user_referenced) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.user_referenced = user_referenced;
    }
}