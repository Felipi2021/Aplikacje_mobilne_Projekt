package com.example.booktracker;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_register, findViewById(R.id.content_frame), true);

        MaterialButton registerButton = findViewById(R.id.buttonRegister);
        TextInputEditText emailInput = findViewById(R.id.editTextEmail);
        TextInputEditText passwordInput = findViewById(R.id.editTextPassword);

        registerButton.setOnClickListener(v -> {
            String email = emailInput.getText() != null ? emailInput.getText().toString().trim() : "";
            String password = passwordInput.getText() != null ? passwordInput.getText().toString().trim() : "";

            boolean valid = true;
            if (email.isEmpty()) {
                emailInput.setError("Email is required");
                valid = false;
            }
            if (password.isEmpty()) {
                passwordInput.setError("Password is required");
                valid = false;
            }
            if (!valid) return;

            registerUser(email, password);
        });
    }

    private void registerUser(String email, String password) {
        new Thread(() -> {
            Exception error = null;
            try {
                URL url = new URL("http://10.0.2.2/register.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

                String postData = "email=" + URLEncoder.encode(email, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8");

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode != 200) throw new IOException("HTTP error code: " + responseCode);

                conn.disconnect();
            } catch (Exception e) {
                error = e;
            }

            Exception finalError = error;
            runOnUiThread(() -> {
                if (finalError != null) {
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }).start();
    }
}