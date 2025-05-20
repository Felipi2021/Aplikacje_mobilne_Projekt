package com.example.booktracker.db;

import androidx.room.*;
import com.example.booktracker.model.Book;
import java.util.List;

@Dao
public interface BookDao {
    @Query("SELECT * FROM books")
    List<Book> getAll();

    @Insert
    void insert(Book book);

    @Delete
    void delete(Book book);

    @Update
    void update(Book book);
}
