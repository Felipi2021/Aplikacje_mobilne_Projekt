package com.example.booktracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AddBookActivity extends AppCompatActivity {
    private String t, a, d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        MaterialButton homeButton = findViewById(R.id.homeButton);
        MaterialButton addButton = findViewById(R.id.addButton);
        TextInputEditText titleInput = findViewById(R.id.titleInput);
        TextInputEditText authorInput = findViewById(R.id.authorInput);
        TextInputEditText descInput = findViewById(R.id.descInput);

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Inside your AddBookActivity.java

        addButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String author = authorInput.getText().toString().trim();
            String description = descInput.getText().toString().trim();

            boolean valid = true;

            if (title.isEmpty()) {
                titleInput.setError("Title is required");
                valid = false;
            }
            if (author.isEmpty()) {
                authorInput.setError("Author is required");
                valid = false;
            }
            if (description.isEmpty()) {
                descInput.setError("Description is required");
                valid = false;
            }

            if (!valid) return;

            // Proceed to add the book (e.g., send to database or API)
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void sendBook(String title, String author, String description) {
        new Thread(() -> {
            Exception error = null;
            String response = null;
            try {
                URL url = new URL("http://10.0.2.2/add_book.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

                String postData = "title=" + URLEncoder.encode(title, "UTF-8") +
                        "&author=" + URLEncoder.encode(author, "UTF-8") +
                        "&description=" + URLEncoder.encode(description, "UTF-8");

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();
                Log.d("RESPONSE_CODE", String.valueOf(responseCode));
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                conn.disconnect();
                response = sb.toString();
            } catch (Exception e) {
                error = e;
                Log.e("HTTP_ERROR", "Exception: ", e);
            }

            String finalResponse = response;
            Exception finalError = error;
            runOnUiThread(() -> {
                if (finalError != null) {
                    Log.e("AddBookActivity", "Error sending POST", finalError);
                } else {
                    Log.d("AddBookActivity", "Server response: " + finalResponse);
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("title", t);
                resultIntent.putExtra("author", a);
                resultIntent.putExtra("description", d);
                setResult(RESULT_OK, resultIntent);
                finish();
            });
        }).start();
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