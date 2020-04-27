package com.github.sshadkany.android_neumorphic_test_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.github.sshadkany.shapes.PolygonView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup myview1 = findViewById(R.id.btn2);
        final PolygonView childAt = (PolygonView)myview1.getChildAt(0);
        final PolygonView myview = (PolygonView) myview1;
        myview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int side = myview.getSide();
                if (side > 10){
                    myview.setSide(5);
                    childAt.setSide(5);
                }else {
                    myview.setSide(side+1);
                    childAt.setSide(side+1);
                }
            }
        });
    }
}