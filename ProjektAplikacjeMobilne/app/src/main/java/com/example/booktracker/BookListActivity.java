package com.example.booktracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.google.android.material.button.MaterialButton;
import java.util.List;
import java.util.ArrayList;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class BookListActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecyclerView recyclerView;
    private BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "book-database").allowMainThreadQueries().build();

        // Add static books to DB if empty
        if (db.bookDao().getAllBooks().isEmpty()) {
            db.bookDao().insert(new Book(0, "Wiedźmin", "Andrzej Sapkowski", "Saga o wiedźminie Geralcie z Rivii."));
            db.bookDao().insert(new Book(0, "Lalka", "Bolesław Prus", "Powieść o losach Stanisława Wokulskiego."));
            db.bookDao().insert(new Book(0, "Zbrodnia i kara", "Fiodor Dostojewski", "Klasyka literatury rosyjskiej."));
            db.bookDao().insert(new Book(0, "Hobbit", "J.R.R. Tolkien", "Przygody Bilba Bagginsa w Śródziemiu."));
            db.bookDao().insert(new Book(0, "Mały Książę", "Antoine de Saint-Exupéry", "Opowieść o przyjaźni i dorastaniu."));
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookAdapter(new ArrayList<>(), this::onBookClick);
        recyclerView.setAdapter(adapter);

        MaterialButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        fetchBooks();
    }

    private void fetchBooks() {
        new Thread(() -> {
            List<Book> books = new ArrayList<>();
            Exception error = null;
            try {
                URL url = new URL("http://10.0.2.2/get_book.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                conn.disconnect();

                JSONArray arr = new JSONArray(sb.toString());
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    books.add(new Book(
                            obj.getString("title"),
                            obj.getString("author"),
                            obj.getString("description")
                    ));
                }
            } catch (Exception e) {
                error = e;
            }

            List<Book> finalBooks = books;
            Exception finalError = error;
            runOnUiThread(() -> {
                if (finalError != null) {
                    // Fallback: load from DB
                    List<Book> dbBooks = db.bookDao().getAllBooks();
                    adapter.setBooks(dbBooks);
                    Toast.makeText(this, "Loaded from local database", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.setBooks(finalBooks);
                }
            });
        }).start();
    }

    private void onBookClick(Book book) {
        Intent intent = new Intent(this, BookDetailsActivity.class);
        intent.putExtra("title", book.title);
        intent.putExtra("author", book.author);
        intent.putExtra("description", book.description);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
        return true;
    }
}