package com.github.sshadkany.android_neumorphic_test_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.github.sshadkany.neoText;

public class NeoTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neo_text);
        neoText neotext = findViewById(R.id.neotext);
        findViewById(R.id.backgroundLayout).setBackgroundColor(neotext.background_color);
    }
}