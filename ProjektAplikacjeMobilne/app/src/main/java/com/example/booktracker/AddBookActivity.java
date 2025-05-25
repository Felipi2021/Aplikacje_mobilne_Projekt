package com.example.booktracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AddBookActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_book, findViewById(R.id.content_frame), true);

        MaterialButton addButton = findViewById(R.id.addButton);
        TextInputEditText titleInput = findViewById(R.id.titleInput);
        TextInputEditText authorInput = findViewById(R.id.authorInput);
        TextInputEditText descInput = findViewById(R.id.descInput);

        addButton.setOnClickListener(v -> {
            String title = titleInput.getText() != null ? titleInput.getText().toString().trim() : "";
            String author = authorInput.getText() != null ? authorInput.getText().toString().trim() : "";
            String description = descInput.getText() != null ? descInput.getText().toString().trim() : "";

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

            sendBook(title, author, description);
        });
    }

    private void sendBook(String title, String author, String description) {
        new Thread(() -> {
            final Exception[] error = {null};
            final String[] response = {null};
            try {
                // Get userId from SharedPreferences
                String user_referenced = getSharedPreferences("prefs", MODE_PRIVATE)
                        .getString("user_referenced", "demo");

                URL url = new URL("http://10.0.2.2/add_book.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

                String postData = "title=" + URLEncoder.encode(title, "UTF-8") +
                        "&author=" + URLEncoder.encode(author, "UTF-8") +
                        "&description=" + URLEncoder.encode(description, "UTF-8") +
                        "&user_referenced=" + URLEncoder.encode(user_referenced, "UTF-8");

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
                response[0] = sb.toString();
            } catch (Exception e) {
                error[0] = e;
                Log.e("HTTP_ERROR", "Exception: ", e);
            }

            runOnUiThread(() -> {
                if (error[0] != null) {
                    Toast.makeText(AddBookActivity.this, "Error adding book", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddBookActivity.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("author", author);
                resultIntent.putExtra("description", description);
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