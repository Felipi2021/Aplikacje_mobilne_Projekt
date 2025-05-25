package com.example.booktracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {
    protected DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Add this block to set up the hamburger icon
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_book_list) {
                startActivity(new Intent(this, BookListActivity.class));
            } else if (id == R.id.nav_add_book) {
                startActivity(new Intent(this, AddBookActivity.class));
            } else if (id == R.id.nav_search_book) {
                startActivity(new Intent(this, SearchBooksActivity.class));
            } else if (id == R.id.nav_homepage) {
                startActivity(new Intent(this, HomeActivity.class));
            } else if (id == R.id.nav_login) {
                startActivity(new Intent(this, LoginActivity.class));
            } else if (id == R.id.nav_register) {
                startActivity(new Intent(this, RegisterActivity.class));
            } else if (id == R.id.nav_logout) {
                // Clear user session or preferences here if needed
                getSharedPreferences("prefs", MODE_PRIVATE)
                        .edit()
                        .clear()
                        .apply();
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem logoutItem = menu.findItem(R.id.nav_logout); // Use your actual logout item id

        boolean loggedIn = isLoggedIn();
        logoutItem.setVisible(loggedIn);
    }

    // Example login check (customize as needed)
    private boolean isLoggedIn() {
        // For example, check SharedPreferences for a saved token or user id
        return getSharedPreferences("prefs", MODE_PRIVATE).getBoolean("logged_in", false);
    }
}