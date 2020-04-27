package com.github.sshadkany.shapes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.florent37.manager.ClipPathManager;

import com.github.sshadkany.android_neumorphic.R;
import com.github.sshadkany.neo;

/**
 * created by sshadkany@gmail.com
 */
public class PolygonView extends neo {
    private float borderWidthPx = 0f;
    private int side = 3;
    private float corner_radius = 55;
    private float adjust_factor = 2.2f;
    @ColorInt
    private int borderColor = Color.WHITE;

    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public PolygonView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public PolygonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PolygonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PolygonView);
            borderWidthPx = attributes.getDimensionPixelSize(R.styleable.PolygonView_shape_polygon_borderWidth, (int) borderWidthPx);
            borderColor = attributes.getColor(R.styleable.PolygonView_shape_polygon_borderColor, borderColor);
            //****** neo styles ************
            style = attributes.getInteger(R.styleable.PolygonView_shape_polygon_shadow_type,drop_shadow);
            shadow_position_x = attributes.getDimensionPixelSize(R.styleable.PolygonView_shape_polygon_shadow_position_x, (int) shadow_position_x);
            shadow_position_y = attributes.getDimensionPixelSize(R.styleable.PolygonView_shape_polygon_shadow_position_y, (int) shadow_position_y);
            shadow_radius = attributes.getDimensionPixelSize(R.styleable.PolygonView_shape_polygon_radius, (int) shadow_radius);
            light_color = attributes.getColor(R.styleable.PolygonView_shape_polygon_light_color, light_color);
            dark_color = attributes.getColor(R.styleable.PolygonView_shape_polygon_dark_color, dark_color);
            background_color = attributes.getColor(R.styleable.PolygonView_shape_polygon_backgroundColor, background_color);
            side = attributes.getInteger(R.styleable.PolygonView_shape_polygon_side, (int) side);
            corner_radius = attributes.getDimensionPixelSize(R.styleable.PolygonView_shape_polygon_corner_radius, (int) corner_radius);
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
                final Path path;
                if (style == drop_shadow || style == small_inner_shadow) {
                    path = createPath(side,Math.min(width - (2 * abs_shadow_position_x) - abs_radius, height - (2 * abs_shadow_position_y) - abs_radius) / 2,width / 2f, height / 2f);
//                    path.addCircle(width / 2f, height / 2f, Math.min(width - (2 * abs_shadow_position_x) - abs_radius, height - (2 * abs_shadow_position_y) - abs_radius) / 2, Path.Direction.CW);
                }else{
                    path=createPath(side,Math.min(width , height) / 2f,width / 2f, height / 2f);
//                    path.addCircle(width / 2f, height / 2f, Math.min(width , height) / 2f, Path.Direction.CW);
                }
                return path;
            }

            @Override
            public boolean requiresBitmap() {
                return false;
            }
        });
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        final float abs_shadow_position_x = Math.abs(shadow_position_x);
        final float abs_shadow_position_y = Math.abs(shadow_position_y);
        final float abs_radius = Math.abs(shadow_radius)*adjust_factor;
        if(borderWidthPx > 0){
            borderPaint.setStrokeWidth(borderWidthPx);
            borderPaint.setColor(borderColor);

            final Path path;
            float width = getWidth();
            float height = getHeight();
            if (style == drop_shadow || style == small_inner_shadow) {
                path = createPath(side,Math.min(width - (2 * abs_shadow_position_x) - abs_radius, height - (2 * abs_shadow_position_y) - abs_radius) / 2,width / 2f, height / 2f);
            }else{
                path=createPath(side,Math.min(width , height) / 2f,width / 2f, height / 2f);
            }

            canvas.drawPath(path,borderPaint);
        }
    }

//    public Path createPath(int sides,float radius,float cx,float cy){
//        Path path = new Path();
//        float angle = (float) (2.0 * Math.PI / sides);
//        path.moveTo(
//                (float)(cx + (radius * Math.cos(0.0))),
//                (float)(cy + (radius * Math.sin(0.0))));
//        for (int i = 1; i < sides; i++) {
//            path.lineTo(
//                    ((float) (cx + (radius * Math.cos(angle * i)))),
//                    ((float) (cy + (radius * Math.sin(angle * i)))));
//        }
//        path.close();
//        return path;
//    }

    public Path createPath(int sides,float radius,float cx,float cy){
        Log.i("sajjad", "createPath: corner"+corner_radius);
        Path path = new Path();
        float angle = (float) (2.0 * Math.PI / sides);
        float corner = Math.abs(corner_radius)/radius;
        corner = Math.min(0.5f,corner); //should be between 0 and 0.5
        float currentAngle = 0;
        float currentpointY=0,y1=0,x1=0,currentpointX=0,nextpointY=0,nextpointX=0;
        float lessX =0;
        float lessY =0;
        float moreX =0;
        float moreY =0;
        x1=  (float) (cx + (radius * Math.cos(angle*(sides))));
        y1 = (float) (cy + (radius * Math.sin(angle*(sides))));

        currentpointX=  (float) (cx + (radius * Math.cos(currentAngle)));
        currentpointY = (float) (cy + (radius * Math.sin(currentAngle)));
        nextpointX=  (float) (cx + (radius * Math.cos(angle)));
        nextpointY = (float) (cy + (radius * Math.sin(angle)));

        moreX = lerp(currentpointX,nextpointX,corner);
        moreY = lerp(currentpointY,nextpointY, corner);

        path.moveTo(moreX,moreY);

        for (int i = 1; i <= sides; i++) {
            currentAngle = angle * i;
            currentpointX=  (float) (cx + (radius * Math.cos(currentAngle)));
            currentpointY = (float) (cy + (radius * Math.sin(currentAngle)));
            nextpointX=  (float) (cx + (radius * Math.cos(angle * (i+1))));
            nextpointY = (float) (cy + (radius * Math.sin(angle * (i+1))));

            lessX = lerp(x1,currentpointX,1-corner);
            lessY = lerp(y1, currentpointY, 1-corner);
            moreX = lerp(currentpointX,nextpointX,corner);
            moreY = lerp(currentpointY,nextpointY, corner);

            path.lineTo(lessX,lessY);
            path.quadTo(
                    currentpointX,
                    currentpointY,
                    moreX,
                    moreY);

            x1 = currentpointX;
            y1 = currentpointY;
        }
        path.close();
        return path;
    }
    float lerp(float a, float b, float f)
    {
        return a + f * (b - a);
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

    public float getBorderWidthPx() {
        return borderWidthPx;
    }

    public void setBorderWidthPx(float borderWidthPx) {
        this.borderWidthPx = borderWidthPx;
        requiresShapeUpdate();
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
        requiresShapeUpdate();
    }

    public float getCorner_radius() {
        return corner_radius;
    }

    public void setCorner_radius(float corner_radius) {
        this.corner_radius = corner_radius;
        requiresShapeUpdate();
    }
}
