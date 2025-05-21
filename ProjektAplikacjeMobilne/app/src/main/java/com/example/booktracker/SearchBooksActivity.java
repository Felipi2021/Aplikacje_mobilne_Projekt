package com.example.booktracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.booktracker.api.GoogleBooksApi;
import com.example.booktracker.model.BookResponse;
import java.util.ArrayList;
import retrofit2.*;
import com.google.android.material.button.MaterialButton;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchBooksActivity extends BaseActivity {
    private ApiBookAdapter adapter;
    private GoogleBooksApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_search_books, findViewById(R.id.content_frame), true);

        SearchView searchView = findViewById(R.id.searchView);
        RecyclerView recyclerView = findViewById(R.id.booksRecyclerView);
        adapter = new ApiBookAdapter(new ArrayList<>(), book -> {
            Intent intent = new Intent(this, BookDetailsActivity.class);
            intent.putExtra("title", book.volumeInfo.title);
            intent.putExtra("author", book.volumeInfo.authors != null && !book.volumeInfo.authors.isEmpty() ? book.volumeInfo.authors.get(0) : "Unknown");
            intent.putExtra("description", book.volumeInfo.description != null ? book.volumeInfo.description : "");
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(GoogleBooksApi.class);

        // Perform a default search (e.g., "android") on open
        searchBooks("android");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBooks(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) { return false; }
        });
    }

    private void searchBooks(String query) {
        api.searchBooks(query).enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateBooks(response.body().items != null ? response.body().items : new ArrayList<>());
                } else {
                    Toast.makeText(SearchBooksActivity.this, "No results", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Toast.makeText(SearchBooksActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}