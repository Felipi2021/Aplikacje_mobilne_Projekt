package com.example.booktracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Book {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String author;
    public String description;
    public String coverUrl;
    public boolean isRead;
    public int rating;
}
