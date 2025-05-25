package com.example.booktracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;

public class LoginActivity extends BaseActivity {
    private EditText emailEdit, passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_login, findViewById(R.id.content_frame), true);

        emailEdit = findViewById(R.id.editTextEmail);
        passwordEdit = findViewById(R.id.editTextPassword);

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                new LoginTask().execute(email, password);
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        String message = "Login failed";
        String userreferenced = null;

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String email = params[0];
                String password = params[1];
                URL url = new URL("http://10.0.2.2/login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String postData = "email=" + URLEncoder.encode(email, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());
                if (json.getBoolean("success")) {
                    message = "Login successful";
                    // Get user id from response if available
                    if (json.has("user_id")) {
                        userreferenced = json.getString("user_referenced");
                    } else {
                        userreferenced = email; // fallback to email if user_id not provided
                    }
                    return true;
                } else {
                    message = json.getString("message");
                }
            } catch (Exception e) {
                message = "Error: " + e.getMessage();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            if (success) {
                // Set logged_in flag to true and save user_id
                getSharedPreferences("prefs", MODE_PRIVATE)
                        .edit()
                        .putBoolean("logged_in", true)
                        .putString("user_referenced", userreferenced)
                        .apply();
                // Proceed to next activity if needed
                // Example: startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                // finish();
            }
        }
    }
}