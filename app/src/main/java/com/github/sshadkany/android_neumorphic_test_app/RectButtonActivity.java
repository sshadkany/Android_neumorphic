package com.github.sshadkany.android_neumorphic_test_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.sshadkany.RectButton;

public class RectButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rect_button);

        RectButton recButton = findViewById(R.id.rec_image_button);

        findViewById(R.id.backgroundLayout).setBackgroundColor(recButton.background_color);  // set layout background color from neo object.

        recButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("neo", "onClick: I am rect button !!");
            }
        });
    }
}