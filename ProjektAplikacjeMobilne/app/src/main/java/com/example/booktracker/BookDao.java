package com.example.booktracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface BookDao {
    @Insert
    void insert(Book book);
    @Query("SELECT * FROM Book")
    List<Book> getAllBooks();
}