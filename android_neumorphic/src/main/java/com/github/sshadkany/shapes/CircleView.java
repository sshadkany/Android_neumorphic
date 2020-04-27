package com.github.sshadkany.shapes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.github.florent37.manager.ClipPathManager;

import com.github.sshadkany.android_neumorphic.R;
import com.github.sshadkany.neo;

/**
 * created by com.github.florent37
 * modified by sshadkany
 */
public class CircleView extends neo {
    private float borderWidthPx = 0f;


    @ColorInt
    private int borderColor = Color.WHITE;

    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float adjust_factor = 2.2f;
    public CircleView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public CircleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
            borderWidthPx = attributes.getDimensionPixelSize(R.styleable.CircleView_shape_circle_borderWidth, (int) borderWidthPx);
            borderColor = attributes.getColor(R.styleable.CircleView_shape_circle_borderColor, borderColor);
            //****** neo styles ************
            style = attributes.getInteger(R.styleable.CircleView_shape_circle_shadow_type,drop_shadow);
            shadow_position_x = attributes.getDimensionPixelSize(R.styleable.CircleView_shape_circle_shadow_position_x, (int) shadow_position_x);
            shadow_position_y = attributes.getDimensionPixelSize(R.styleable.CircleView_shape_circle_shadow_position_y, (int) shadow_position_y);
            shadow_radius = attributes.getDimensionPixelSize(R.styleable.CircleView_shape_circle_radius, (int) shadow_radius);
            light_color = attributes.getColor(R.styleable.CircleView_shape_circle_light_color, light_color);
            dark_color = attributes.getColor(R.styleable.CircleView_shape_circle_dark_color, dark_color);
            background_color = attributes.getColor(R.styleable.CircleView_shape_circle_backgroundColor, background_color);
            //******************************
            attributes.recycle();
        }
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        final float abs_shadow_position_x = Math.abs(shadow_position_x);
        final float abs_shadow_position_y = Math.abs(shadow_position_y);
        final float abs_radius = Math.abs(shadow_radius)*adjust_factor;

        super.setClipPathCreator(new ClipPathManager.ClipPathCreator() {
            @Override
            public Path createClipPath(int width, int height) {
                final Path path = new Path();
                if (style == drop_shadow || style == small_inner_shadow) {
                    path.addCircle(width / 2f, height / 2f, Math.min(width - (2 * abs_shadow_position_x) - abs_radius, height - (2 * abs_shadow_position_y) - abs_radius) / 2, Path.Direction.CW);
                }else{
                    path.addCircle(width / 2f, height / 2f, Math.min(width , height) / 2f, Path.Direction.CW);
                }
                return path;
            }

            @Override
            public boolean requiresBitmap() {
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        final float abs_shadow_position_x = Math.abs(shadow_position_x);
        final float abs_shadow_position_y = Math.abs(shadow_position_y);
        final float abs_radius = Math.abs(shadow_radius)*adjust_factor;
        if(borderWidthPx > 0){
            borderPaint.setStrokeWidth(borderWidthPx);
            borderPaint.setColor(borderColor);
            if (style == drop_shadow || style == small_inner_shadow){
                canvas.drawCircle(getWidth()/2f, getHeight()/2f, Math.min(getWidth()-(2*abs_shadow_position_x)-abs_radius, getHeight()-(2*abs_shadow_position_y)-abs_radius)/2, borderPaint);
            }else{
                canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, (Math.min(getWidth() , getHeight()) / 2f)-(borderWidthPx / 2f),borderPaint);
            }
        }
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
}
