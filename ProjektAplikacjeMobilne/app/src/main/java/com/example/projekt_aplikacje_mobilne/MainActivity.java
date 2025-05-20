package com.example.projekt_aplikacje_mobilne;

import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout listLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Root layout
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.parseColor("#F5F5F5"));
        root.setPadding(32, 80, 32, 32);

        // Title
        TextView title = new TextView(this);
        title.setText("Stylish List (Static Data)");
        title.setTextSize(28);
        title.setTextColor(Color.parseColor("#3F51B5"));
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 32);
        root.addView(title);

        // List container
        listLayout = new LinearLayout(this);
        listLayout.setOrientation(LinearLayout.VERTICAL);
        root.addView(listLayout);

        setContentView(root);

        // Add static data cards
        addCard("Pierwszy element", "To jest opis pierwszego elementu.");
        addCard("Drugi element", "To jest opis drugiego elementu.");
        addCard("Trzeci element", "To jest opis trzeciego elementu.");
        addCard("Czwarty element", "To jest opis czwartego elementu.");
        addCard("Piąty element", "To jest opis piątego elementu.");
    }

    private void addCard(String title, String desc) {
        MaterialCardView card = new MaterialCardView(this);
        card.setCardBackgroundColor(Color.WHITE);
        card.setRadius(32);
        card.setCardElevation(12);
        card.setUseCompatPadding(true);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 32);
        card.setLayoutParams(cardParams);

        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setPadding(48, 32, 48, 32);

        TextView cardTitle = new TextView(this);
        cardTitle.setText(title);
        cardTitle.setTextSize(22);
        cardTitle.setTextColor(Color.parseColor("#3F51B5"));
        cardContent.addView(cardTitle);

        TextView cardDesc = new TextView(this);
        cardDesc.setText(desc);
        cardDesc.setTextSize(16);
        cardDesc.setTextColor(Color.parseColor("#444444"));
        cardDesc.setPadding(0, 12, 0, 0);
        cardContent.addView(cardDesc);

        card.addView(cardContent);
        listLayout.addView(card);
    }
}
