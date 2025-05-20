package com.example.booktracker;

import android.os.Bundle;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;

public class BookDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String description = getIntent().getStringExtra("description");

        TextView titleView = findViewById(R.id.bookTitle);
        TextView authorView = findViewById(R.id.bookAuthor);
        TextView descView = findViewById(R.id.bookDescription);
        MaterialButton backButton = findViewById(R.id.returnButton);

        titleView.setText(title);
        authorView.setText("Author: " + author);
        descView.setText(description);

        backButton.setOnClickListener(v -> finish());
    }
}