// HomeActivity.java
package com.example.booktracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;

public class HomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MaterialButton bookListButton = findViewById(R.id.viewBooksButton);
        MaterialButton addBookButton = findViewById(R.id.addBookButton);
        MaterialButton searchBooksButton = findViewById(R.id.searchBooksButton);
        searchBooksButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchBooksActivity.class));
        });

        bookListButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookListActivity.class);
            startActivity(intent);
        });

        addBookButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddBookActivity.class);
            startActivity(intent);
        });
    }
}