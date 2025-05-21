
package com.example.booktracker;
import android.os.Bundle;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.LinearLayout;

public class HomeActivity extends BaseActivity {
    // In HomeActivity.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_home, findViewById(R.id.content_frame), true);
        fetchNews();
    }

    private void fetchNews() {
        new Thread(() -> {
            JSONArray arr = null;
            try {
                URL url = new URL("http://10.0.2.2/get_news.php");
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

                arr = new JSONArray(sb.toString());
            } catch (Exception e) {
                // handle error
            }

            JSONArray finalArr = arr;
            runOnUiThread(() -> {
                LinearLayout newsContainer = findViewById(R.id.news_container);
                newsContainer.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(this);
                if (finalArr != null) {
                    for (int i = 0; i < finalArr.length(); i++) {
                        try {
                            JSONObject obj = finalArr.getJSONObject(i);
                            View newsPanel = inflater.inflate(R.layout.item_news_panel, newsContainer, false);
                            TextView title = newsPanel.findViewById(R.id.news_title);
                            TextView content = newsPanel.findViewById(R.id.news_content);
                            title.setText(obj.getString("title"));
                            content.setText(obj.getString("content"));
                            newsContainer.addView(newsPanel);
                        } catch (Exception ignored) {
                        }
                    }
                } else {
                    TextView tv = new TextView(this);
                    tv.setText("Failed to load news.");
                    newsContainer.addView(tv);
                }
            });
        }).start();
    }
}