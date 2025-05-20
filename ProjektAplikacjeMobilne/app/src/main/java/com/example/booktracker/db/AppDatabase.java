package com.example.booktracker.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.booktracker.model.Book;

@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BookDao bookDao();
}
