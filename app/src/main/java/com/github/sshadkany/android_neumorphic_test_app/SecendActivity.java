package com.github.sshadkany.android_neumorphic_test_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.github.sshadkany.neo;
import com.github.sshadkany.shapes.CircleView;
import com.github.sshadkany.shapes.PolygonView;

public class SecendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secend);

//        final PolygonView my_polygon = findViewById(R.id.my_polygon);
//        my_polygon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int side = my_polygon.getSide();
//                if (side > 12){
//                    my_polygon.setSide(3);
//                }else {
//                    my_polygon.setSide(side+1);
//                }
//            }
//        });

        final neo mybtn = findViewById(R.id.my_button);
        mybtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // is shape Contains Point ----> for detect place of Touch is in the shape or not
                if (mybtn.isShapeContainsPoint(event.getX(), event.getY())) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // PRESSED
                            //use only "small inner shadow" because its same size with "drop shadow" style and "big inner shadow" is bigger
                            // "small inner shadow" = "drop shadow"
                            // "big inner shadow"  > "drop shadow"
                            mybtn.setStyle(neo.small_inner_shadow);
                            return true; // if you want to handle the touch event
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            // RELEASED
                            mybtn.setStyle(neo.drop_shadow);
                            return true; // if you want to handle the touch event
                    }
                }
                return false;
            }
        });
    }
}