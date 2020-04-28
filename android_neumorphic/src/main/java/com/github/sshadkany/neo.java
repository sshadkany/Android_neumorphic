package com.github.sshadkany;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.florent37.ShapeOfView;

import static android.content.Context.SENSOR_SERVICE;
/**
 * created by sshadkany
 */
public class neo extends ShapeOfView {

    @IntDef({drop_shadow,big_inner_shadow,small_inner_shadow})
    public @interface mystyle{}
    public static final int drop_shadow = 1;
    public static final int big_inner_shadow = 2;
    public static final int small_inner_shadow = 3;
    @mystyle
    public int style = drop_shadow;
    public float shadow_position_x = 25;
    public float shadow_position_y = 25;
    public float shadow_radius = 41;

    public int light_color = Color.WHITE;
    public int dark_color = Color.parseColor("#A3B1C6");
//    public int background_color = Color.parseColor("#E0E5Ec"); // move upper place

    private Paint mPaint;
    private Paint mPaint2;
    private Context mcontext;
    private Paint fillPaint;
    private Paint shadowPaint;
    private Paint lightPaint;
    private PorterDuffXfermode mode;
    private Path bigPath;





    public neo(@NonNull Context context) {
        super(context);
        init(context);
    }

    public neo(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public neo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        mcontext = context;
        setDrawingCacheEnabled(true);
        setWillNotDraw(false);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);

        fillPaint = new Paint();
        fillPaint.setAntiAlias(true);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(background_color);
//        gyroscopeSensor();

        bigPath = new Path();


    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        bigPath.addRect(-1*getWidth(),-1*getHeight(),getWidth(),getHeight(), Path.Direction.CW);

        Bitmap bitmap = Bitmap.createBitmap(getWidth(),getHeight(),Bitmap.Config.ARGB_8888);
        Canvas mc = new Canvas(bitmap);

        Log.i("sajjad", "dispatchDraw: sajjad"+style);
        if (style == drop_shadow) {
            dropShadowOptions(shadow_position_x,shadow_position_y, shadow_radius);
            mc.drawPath(clipPath,shadowPaint);
            mc.drawPath(clipPath,lightPaint);
            canvas.drawBitmap(bitmap,0,0,mPaint);
        }else if (style == big_inner_shadow || style == small_inner_shadow) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
                innerShadowOptions(shadow_position_x, shadow_position_y, shadow_radius);
                Bitmap bitmap2 = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                Canvas mc2 = new Canvas(bitmap2);

                Bitmap rectviewbitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                Canvas rectcanvas = new Canvas(rectviewbitmap);
                Paint p = new Paint();
                p.setColor(Color.BLUE);
                rectcanvas.drawPath(bigPath,p);
                p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                rectcanvas.drawPath(clipPath,p);

//                mc2.drawPath(rectView, shadowPaint);
//                mc2.drawPath(rectView, lightPaint);
                mc2.drawBitmap(rectviewbitmap.extractAlpha(),0,0,shadowPaint);
                mc2.drawBitmap(rectviewbitmap.extractAlpha(),0,0,lightPaint);

                mc.drawPath(clipPath, fillPaint);

                mPaint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                mc.drawBitmap(bitmap2, 0, 0, mPaint2);
                mode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER);
                mPaint.setXfermode(mode);
                canvas.drawBitmap(bitmap, 0, 0, mPaint);

                mode = new PorterDuffXfermode(PorterDuff.Mode.DST_OVER);
                fillPaint.setXfermode(mode);
                fillPaint.setColor(background_color);
                canvas.drawPath(clipPath, fillPaint);
            }else{
                innerShadowOptions(shadow_position_x, shadow_position_y, shadow_radius);
                Bitmap bitmap2 = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                Canvas mc2 = new Canvas(bitmap2);

                mc2.drawPath(rectView, shadowPaint);
                mc2.drawPath(rectView, lightPaint);

                mc.drawPath(clipPath, fillPaint);

                mPaint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                mc.drawBitmap(bitmap2, 0, 0, mPaint2);
                mode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER);
                mPaint.setXfermode(mode);
                canvas.drawBitmap(bitmap, 0, 0, mPaint);

                mode = new PorterDuffXfermode(PorterDuff.Mode.DST_OVER);
                fillPaint.setXfermode(mode);
                fillPaint.setColor(background_color);
                canvas.drawPath(clipPath, fillPaint);
            }
        }




    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
    private void dropShadowOptions(float dx,float dy,float radius){
        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(background_color);
        shadowPaint.setShadowLayer(radius,dx,dy,dark_color);

        lightPaint = new Paint();
        lightPaint.setAntiAlias(true);
        lightPaint.setColor(background_color);
        lightPaint.setShadowLayer(radius,-1*dx,-1*dy,light_color);

        mode = new PorterDuffXfermode(PorterDuff.Mode.DST_OVER);
        mPaint.setXfermode(mode);
    }


    private void innerShadowOptions(float dx,float dy,float radius){
        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setColor(background_color);
        shadowPaint.setShadowLayer(radius,dx,dy,dark_color);

        lightPaint = new Paint();
        lightPaint.setAntiAlias(true);
        lightPaint.setColor(background_color);
        lightPaint.setShadowLayer(radius,-1*dx,-1*dy,light_color);


        mode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
        mPaint.setXfermode(mode);
    }
    private void gyroscopeSensor(){
        SensorManager sensorManager =
                (SensorManager) mcontext.getSystemService(SENSOR_SERVICE);
        Sensor gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(
                        rotationMatrix, sensorEvent.values);
                // Remap coordinate system
                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        remappedRotationMatrix);

                // Convert to orientations
                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);

//                /*orientations[2]  in radian*/
                shadow_position_y = (float) (Math.cos(orientations[2])* shadow_radius) + 5;
                shadow_position_x = (float) (Math.sin(orientations[2])* shadow_radius) + 5;
//                radius = (float) (Math.sin(orientations[1])*50)+10;

                postInvalidate();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sensorManager.registerListener(gyroscopeSensorListener,
                gyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
    }

    //**************** Getter and setter *******************


    public int getStyle() {
        return style;
    }

    /***
     * for set shadow style "small inner shadow" or "big inner shadow" or "drop shadow"
     * "small inner shadow" is same as big inner shadow but smaller and is same size of drop shadow
     * when you want make a button you should use "small inner shadow" for pressing style
     *
     *
     * @param style
     *
     */
    public void setStyle(@mystyle int style) {
        this.style = style;
        requiresShapeUpdate();
    }

    /***
     *
     * same as set style but with a better name !
     *
     */
    public int getShadowStyle() {
        return style;
    }

    /***
     * for set shadow style "small inner shadow" or "big inner shadow" or "drop shadow"
     * "small inner shadow" is same as big inner shadow but smaller and is same size of drop shadow
     * when you want make a button you should use "small inner shadow" for pressing style
     *
     * same as set style but with a better name !
     * @param shadowStyle
     */
    public void setShadowStyle(@mystyle int shadowStyle) {
        this.style = shadowStyle;
        requiresShapeUpdate();
    }

    public float getShadow_position_x() {
        return shadow_position_x;
    }

    public void setShadow_position_x(float shadow_position_x) {
        this.shadow_position_x = shadow_position_x;
        requiresShapeUpdate();
    }

    public float getShadow_position_y() {
        return shadow_position_y;
    }

    public void setShadow_position_y(float shadow_position_y) {
        this.shadow_position_y = shadow_position_y;
        requiresShapeUpdate();
    }

    public float getShadow_radius() {
        return shadow_radius;
    }

    public void setShadow_radius(float shadow_radius) {
        this.shadow_radius = shadow_radius;
        requiresShapeUpdate();
    }

    public int getLight_color() {
        return light_color;
    }

    public void setLight_color(int light_color) {
        this.light_color = light_color;
        requiresShapeUpdate();
    }

    public int getDark_color() {
        return dark_color;
    }

    public void setDark_color(int dark_color) {
        this.dark_color = dark_color;
        requiresShapeUpdate();
    }


    public int getBackground_color(){
        return background_color;
    }

    public void setBackground_color(int background_color) {
        this.background_color = background_color;
        requiresShapeUpdate();
    }

    /**
     *
     *
     * @param x
     * @param y
     * @return true if the point are inside of shape
     */
    public boolean isShapeContainsPoint(float x, float y){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Path tempPath = new Path(); // Create temp Path
            tempPath.moveTo(x, y); // Move cursor to point
            RectF rectangle = new RectF(x - 1, y - 1, x + 1, y + 1); // create rectangle with size 2xp
            tempPath.addRect(rectangle, Path.Direction.CW); // add rect to temp path
            tempPath.op(clipPath, Path.Op.DIFFERENCE); // get difference with our PathToCheck
            if (tempPath.isEmpty()) // if out path cover temp path we get empty path in result
            {
                Log.d("saj", "Path contains this point");
                return true;
            } else {
                return false;
            }
        }else{
            RectF rectF = new RectF();
            clipPath.computeBounds(rectF, false);
            Region r = new Region();
            r.setPath(clipPath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
            if (r.contains((int)x,(int)y)){
                return true;
            }else {
                return false;
            }

        }
    }

    //*****************************************************

}
