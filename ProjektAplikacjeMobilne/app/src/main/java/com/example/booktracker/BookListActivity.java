package com.example.booktracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.net.URLEncoder;
import java.util.concurrent.Executors;

public class BookListActivity extends BaseActivity {

    private static final String TAG = "BookListActivity";
    private AppDatabase db;
    private RecyclerView recyclerView;
    private BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate started");

        try {
            super.onCreate(savedInstanceState);
            Log.d(TAG, "super.onCreate completed");

        } catch (Exception e) {
            Log.e(TAG, "Error in super.onCreate: ", e);
            Toast.makeText(this, "Błąd w BaseActivity", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            // Sprawdź czy content_frame istnieje
            if (findViewById(R.id.content_frame) == null) {
                Log.e(TAG, "content_frame not found in BaseActivity layout!");
                Toast.makeText(this, "Błąd layoutu BaseActivity", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            getLayoutInflater().inflate(R.layout.activity_book_list, findViewById(R.id.content_frame), true);
            Log.d(TAG, "Layout inflated successfully");

        } catch (Exception e) {
            Log.e(TAG, "Error inflating layout: ", e);
            Toast.makeText(this, "Błąd ładowania layoutu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            Log.d(TAG, "Initializing database...");
            // Użyj fallbackToDestructiveMigration() żeby uniknąć problemów z migracją
            db = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, "book-database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
            Log.d(TAG, "Database initialized");

        } catch (Exception e) {
            Log.e(TAG, "Error initializing database: ", e);
            // Nie kończymy aplikacji - możemy działać bez bazy danych
            db = null;
        }

        try {
            Log.d(TAG, "Setting up RecyclerView...");
            recyclerView = findViewById(R.id.recyclerView);
            if (recyclerView == null) {
                Log.e(TAG, "RecyclerView not found!");
                Toast.makeText(this, "Błąd: Brak RecyclerView", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new BookAdapter(new ArrayList<>(), this::onBookClick);
            recyclerView.setAdapter(adapter);
            Log.d(TAG, "RecyclerView setup completed");

        } catch (Exception e) {
            Log.e(TAG, "Error setting up RecyclerView: ", e);
            Toast.makeText(this, "Błąd konfiguracji RecyclerView", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicjalizuj dane w tle
        initializeData();

        Log.d(TAG, "onCreate completed successfully");
    }

    private void initializeData() {
        // Użyj executor do operacji w tle
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Log.d(TAG, "Initializing data...");

                // Sprawdź czy baza danych jest dostępna
                if (db != null) {
                    try {
                        List<Book> existingBooks = db.bookDao().getAllBooks();
                        Log.d(TAG, "Found " + existingBooks.size() + " existing books in database");

                        // Dodaj książki demo jeśli baza jest pusta
                        if (existingBooks.isEmpty()) {
                            Log.d(TAG, "Adding demo books to database...");
                            addDemoBooks();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error accessing database: ", e);
                        db = null; // Wyłącz bazę danych jeśli nie działa
                    }
                }

                // Pobierz książki z serwera
                fetchBooks();

            } catch (Exception e) {
                Log.e(TAG, "Error in initializeData: ", e);

                // Fallback - pokaż przynajmniej puste dane
                runOnUiThread(() -> {
                    if (adapter != null) {
                        adapter.setBooks(createDemoBooksList());
                        Toast.makeText(this, "Używam danych demo", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void addDemoBooks() {
        try {
            if (db != null && db.bookDao() != null) {
                db.bookDao().insert(new Book(0, "Wiedźmin", "Andrzej Sapkowski", "Saga o wiedźminie Geralcie z Rivii.", "demo"));
                db.bookDao().insert(new Book(0, "Lalka", "Bolesław Prus", "Powieść o losach Stanisława Wokulskiego.", "demo"));
                db.bookDao().insert(new Book(0, "Zbrodnia i kara", "Fiodor Dostojewski", "Klasyka literatury rosyjskiej.", "demo"));
                db.bookDao().insert(new Book(0, "Hobbit", "J.R.R. Tolkien", "Przygody Bilba Bagginsa w Śródziemiu.", "demo"));
                db.bookDao().insert(new Book(0, "Mały Książę", "Antoine de Saint-Exupéry", "Opowieść o przyjaźni i dorastaniu.", "demo"));
                Log.d(TAG, "Demo books added to database");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error adding demo books to database: ", e);
        }
    }

    private List<Book> createDemoBooksList() {
        List<Book> demoBooks = new ArrayList<>();
        demoBooks.add(new Book(1, "Wiedźmin", "Andrzej Sapkowski", "Saga o wiedźminie Geralcie z Rivii.", "demo"));
        demoBooks.add(new Book(2, "Lalka", "Bolesław Prus", "Powieść o losach Stanisława Wokulskiego.", "demo"));
        demoBooks.add(new Book(3, "Zbrodnia i kara", "Fiodor Dostojewski", "Klasyka literatury rosyjskiej.", "demo"));
        demoBooks.add(new Book(4, "Hobbit", "J.R.R. Tolkien", "Przygody Bilba Bagginsa w Śródziemiu.", "demo"));
        demoBooks.add(new Book(5, "Mały Książę", "Antoine de Saint-Exupéry", "Opowieść o przyjaźni i dorastaniu.", "demo"));
        return demoBooks;
    }

    private void fetchBooks() {
        try {
            Log.d(TAG, "Fetching books from server...");

            // Get user_referenced from SharedPreferences
            String userReferenced = getSharedPreferences("prefs", MODE_PRIVATE)
                    .getString("user_referenced", "");

            Log.d(TAG, "User referenced: '" + userReferenced + "'");

            if (userReferenced.isEmpty()) {
                Log.w(TAG, "User referenced is empty, using local data");
                loadLocalData();
                return;
            }

            // Pobierz dane z serwera
            fetchBooksFromServer(userReferenced);

        } catch (Exception e) {
            Log.e(TAG, "Error in fetchBooks: ", e);
            loadLocalData();
        }
    }

    private void loadLocalData() {
        runOnUiThread(() -> {
            try {
                List<Book> books;
                if (db != null) {
                    books = db.bookDao().getAllBooks();
                    if (books.isEmpty()) {
                        books = createDemoBooksList();
                    }
                } else {
                    books = createDemoBooksList();
                }

                if (adapter != null) {
                    adapter.setBooks(books);
                }
                Toast.makeText(this, "Załadowano dane lokalne (" + books.size() + " książek)", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e(TAG, "Error loading local data: ", e);
                if (adapter != null) {
                    adapter.setBooks(createDemoBooksList());
                }
                Toast.makeText(this, "Używam danych demo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchBooksFromServer(String userReferenced) {
        try {
            URL url = new URL("http://10.0.2.2/get_book.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            String postData = "user_referenced=" + URLEncoder.encode(userReferenced, "UTF-8");
            conn.getOutputStream().write(postData.getBytes("UTF-8"));

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Server response code: " + responseCode);

            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("Server error: " + responseCode);
            }

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            conn.disconnect();

            String response = sb.toString().trim();
            Log.d(TAG, "Server response: '" + response + "'");

            if (response.isEmpty()) {
                throw new Exception("Empty response from server");
            }

            List<Book> books = new ArrayList<>();
            JSONArray arr = new JSONArray(response);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                books.add(new Book(
                        obj.has("id") ? obj.getInt("id") : 0,
                        obj.optString("title", "No Title"),
                        obj.optString("author", "No Author"),
                        obj.optString("description", ""),
                        obj.optString("user_referenced", userReferenced)
                ));
            }

            Log.d(TAG, "Parsed " + books.size() + " books from server");

            runOnUiThread(() -> {
                if (adapter != null) {
                    adapter.setBooks(books);
                    Toast.makeText(this, "Załadowano " + books.size() + " książek z serwera", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error fetching books from server: ", e);
            runOnUiThread(() -> {
                loadLocalData();
                Toast.makeText(this, "Błąd serwera - użyto danych lokalnych", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void onBookClick(Book book) {
        try {
            Log.d(TAG, "Book clicked: " + book.title);
            Intent intent = new Intent(this, BookDetailsActivity.class);
            intent.putExtra("title", book.title);
            intent.putExtra("author", book.author);
            intent.putExtra("description", book.description);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error starting BookDetailsActivity: ", e);
            Toast.makeText(this, "Błąd otwierania szczegółów książki", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        try {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error navigating back: ", e);
            finish();
            return true;
        }
    }
}