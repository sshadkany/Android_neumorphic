package com.github.sshadkany;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.sshadkany.shapes.RoundRectView;

public class RectButton extends RoundRectView {

    private ImageView imageView;
    private TextView textView;
    private boolean isPressed = false;

    public RectButton(@NonNull Context context) {
        super(context);
    }

    public RectButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void myAddView(View child) {

        if (child instanceof ImageView) {
            imageView = (ImageView) child;
        } else if (child instanceof TextView) {
            textView = (TextView) child;
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        myAddView(child);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        myAddView(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        myAddView(child);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        myAddView(child);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isShapeContainsPoint(event.getX(), event.getY())) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // PRESSED
                    setStyle(neo.small_inner_shadow);
                    if (imageView != null) {
                        imageView.setScaleX(imageView.getScaleX() * 0.9f);
                        imageView.setScaleY(imageView.getScaleY() * 0.9f);
                    }
                    if (textView != null) {
                        textView.setScaleX(textView.getScaleX() * 0.9f);
                        textView.setScaleY(textView.getScaleY() * 0.9f);
                    }
                    isPressed = true;
                    performClick();
                    requiresShapeUpdate();
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // RELEASED
                    setStyle(neo.drop_shadow);
                    if (imageView != null) {
                        imageView.setScaleX(1);
                        imageView.setScaleY(1);
                    }
                    if (textView != null) {
                        textView.setScaleX(1);
                        textView.setScaleY(1);
                    }
                    isPressed = false;
                    requiresShapeUpdate();
                    return true;
            }
        } else {
            // RELEASED
            // handle release
            setStyle(neo.drop_shadow);
            if (imageView != null) {
                imageView.setScaleX(1);
                imageView.setScaleY(1);
            }
            if (textView != null) {
                textView.setScaleX(1);
                textView.setScaleY(1);
            }
            isPressed = false;
            requiresShapeUpdate();
            return true;
        }
        return false;
    }

    @Override
    public boolean performClick() {
        return super.performClick();

    }

}
