package com.example.booktracker.api;

import com.example.booktracker.model.BookResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleBooksApi {
    @GET("volumes")
    Call<BookResponse> searchBooks(@Query("q") String query);
}