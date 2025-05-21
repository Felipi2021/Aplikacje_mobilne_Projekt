package com.example.booktracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

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
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }
}