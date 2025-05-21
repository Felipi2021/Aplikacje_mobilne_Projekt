// app/src/main/java/com/example/booktracker/model/AppDatabase.java
package com.example.booktracker.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}