package com.github.sshadkany;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.provider.CalendarContract;
import android.text.Layout;
import android.text.LoginFilter;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.github.florent37.manager.ClipPathManager;

import com.github.sshadkany.android_neumorphic.R;
import com.github.sshadkany.neo;

/**
 * created by sshadkany
 */
public class neoText extends neo {
    private float borderWidthPx = 0f;
    private TextView textView;

    String mText = "";
    TextPaint mTextPaint;

    @ColorInt
    private int borderColor = Color.WHITE;

    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public neoText(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public neoText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public neoText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void myAddView(View child) {
        if (child instanceof TextView) {
            textView = (TextView) child;
            textView.setTextColor(Color.TRANSPARENT);
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


    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.neoText);
            borderWidthPx = attributes.getDimensionPixelSize(R.styleable.neoText_neo_borderWidth, (int) borderWidthPx);
            borderColor = attributes.getColor(R.styleable.neoText_neo_borderColor, borderColor);
            //****** neo text default styles ************
            shadow_position_x = 4;
            shadow_position_y = 4;
            shadow_radius = 4;
            //******************************
            style = attributes.getInteger(R.styleable.neoText_neo_shadow_type, drop_shadow);
            shadow_position_x = attributes.getDimensionPixelSize(R.styleable.neoText_neo_shadow_position_x, (int) shadow_position_x);
            shadow_position_y = attributes.getDimensionPixelSize(R.styleable.neoText_neo_shadow_position_y, (int) shadow_position_y);
            shadow_radius = attributes.getDimensionPixelSize(R.styleable.neoText_neo_radius, (int) shadow_radius);
            light_color = attributes.getColor(R.styleable.neoText_neo_light_color, light_color);
            dark_color = attributes.getColor(R.styleable.neoText_neo_dark_color, dark_color);
            background_color = attributes.getColor(R.styleable.neoText_neo_backgroundColor, background_color);
            //******************************

            //*****************************
            attributes.recycle();
        }
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);

        super.setClipPathCreator(new ClipPathManager.ClipPathCreator() {
            @Override
            public Path createClipPath(int width, int height) {
                final Path path = new Path();
                if (textView != null) {
                    mText = textView.getText().toString();
                    mTextPaint = textView.getPaint();
//                        initLabelView(mText,mTextPaint);
//                        TextPaint textViewPaint = mStaticLayout.getPaint();
                    float text_position_x = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        text_position_x = findTextBounds(textView).left;

                    }
//                        String s = textView.getText().toString();
                    boolean flag = true;
                    int line = 0;
                    int startPointer = 0;
                    int endPointer = mText.length();

                    while (flag) {
                        Path p = new Path();
                        int breakText = mTextPaint.breakText(mText.substring(startPointer), true, width, null);
                        mTextPaint.getTextPath(mText, startPointer, startPointer + breakText, text_position_x,
                                textView.getBaseline() + mTextPaint.getFontSpacing() * line, p);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            path.op(p, Path.Op.UNION);
                        }
                        endPointer -= breakText;
                        startPointer += breakText;
                        line++;
                        if (endPointer == 0) {
                            flag = false;
                        }
                    }

                }
                return path;
            }

            @Override
            public boolean requiresBitmap() {
                return false;
            }
        });
    }


    public void setBorderWidth(float borderWidth) {
        this.borderWidthPx = borderWidth;
        requiresShapeUpdate();
    }

    public void setBorderColor(@ColorInt int borderColor) {
        this.borderColor = borderColor;
        requiresShapeUpdate();
    }

    public void setBorderWidthDp(float borderWidth) {
        setBorderWidth(dpToPx(borderWidth));
    }

    public float getBorderWidth() {
        return borderWidthPx;
    }

    public float getBorderWidthDp() {
        return pxToDp(getBorderWidth());
    }

    public int getBorderColor() {
        return borderColor;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private Rect findTextBounds(TextView textView) {
        // Force measure of text pre-layout.
        textView.measure(0, 0);
        String s = (String) textView.getText();

        // bounds will store the rectangle that will circumscribe the text.
        Rect bounds = new Rect();
        Paint textPaint = textView.getPaint();

        // Get the bounds for the text. Top and bottom are measured from the baseline. Left
        // and right are measured from 0.
        textPaint.getTextBounds(s, 0, s.length(), bounds);
        int baseline = textView.getBaseline();
        bounds.top = baseline + bounds.top;
        bounds.bottom = baseline + bounds.bottom;
        int startPadding = textView.getPaddingStart();
        bounds.left += startPadding;

        // textPaint.getTextBounds() has already computed a value for the width of the text,
        // however, Paint#measureText() gives a more accurate value.
        bounds.right = (int) textPaint.measureText(s, 0, s.length()) + startPadding;
        return bounds;
    }


}

