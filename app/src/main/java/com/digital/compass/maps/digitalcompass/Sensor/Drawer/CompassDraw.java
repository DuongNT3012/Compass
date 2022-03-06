package com.digital.compass.maps.digitalcompass.Sensor.Drawer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.exifinterface.media.ExifInterface;

import com.digital.compass.maps.digitalcompass.R;
import com.digital.compass.maps.digitalcompass.Sensor.model.SensorValue;
import com.digital.compass.maps.digitalcompass.Utils.Utility;

import java.util.Locale;

public class CompassDraw {
    private static final String TAG = "CanvasHelper";
    private int mAccentColor;
    private int mBackgroundColor;
    private final Paint mBackgroundPaint = new Paint(1);
    private Point mCenter;
    private Path mClockPathPrimary = null;
    private Path mClockPathSecondary = null;
    private Context mContext;
    private final Paint mDirectionTextPaint = new Paint(1);
    private int mForegroundColor;
    private boolean mIsPaintCreated = false;
    private final Paint mMagneticPaint = new Paint(1);
    private final float mMaxRadius = 430.0f;
    private final Paint mNumberTextPaint = new Paint(1);
    private final Path mPath = new Path();
    private final Paint mPathPaint = new Paint(1);
    private float mPixelScale;
    private int mPrimaryTextColor;
    private final Paint mPrimaryTextPaint = new Paint(1);
    private int mSecondaryTextColor;
    private final Paint mSecondaryTextPaint = new Paint(1);
    private Typeface mTypeface;
    private float mUnitPadding;
    private final SensorValue sensorValue = new SensorValue();

    public CompassDraw(Context context) {
        this.mContext = context;
    }

    public SensorValue getSensorValue() {
        return this.sensorValue;
    }

    public void draw(Canvas canvas) {
        this.mPixelScale = ((float) Math.min(canvas.getWidth(), canvas.getHeight())) / 1000.0f;
        this.mCenter = new Point(canvas.getWidth() / 2, canvas.getHeight() / 2);
        this.mUnitPadding = realPx(5.0f);
        initPaint();
        drawClock(canvas);
        drawAzimuthValue(canvas);
    }

    private void initPaint() {
        this.mTypeface = ResourcesCompat.getFont(this.mContext, R.font.exo);
        this.mNumberTextPaint.setTextSize(realPx(30.0f));
        this.mNumberTextPaint.setColor(this.mPrimaryTextColor);
        this.mNumberTextPaint.setTypeface(this.mTypeface);
        if (!this.mIsPaintCreated) {
            this.mDirectionTextPaint.setTextSize(realPx(60.0f));
            this.mDirectionTextPaint.setTypeface(this.mTypeface);
            this.mMagneticPaint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, realPx(500.0f), new int[]{-16711936, -16711936, SupportMenu.CATEGORY_MASK, SupportMenu.CATEGORY_MASK}, (float[]) null, Shader.TileMode.MIRROR));
            this.mMagneticPaint.setStrokeWidth(realPx(25.0f));
            this.mMagneticPaint.setStyle(Paint.Style.STROKE);
            this.mMagneticPaint.setStrokeCap(Paint.Cap.ROUND);
            this.mForegroundColor = ContextCompat.getColor(this.mContext, R.color.compass_foreground_color);
            this.mBackgroundColor = ContextCompat.getColor(this.mContext, R.color.compass_background_color);
            this.mPrimaryTextColor = ContextCompat.getColor(this.mContext, R.color.compass_text_primary_color);
            this.mSecondaryTextColor = ContextCompat.getColor(this.mContext, R.color.compass_text_secondary_color);
            this.mAccentColor = ContextCompat.getColor(this.mContext, R.color.compass_accent_color);
            this.mBackgroundPaint.setColor(this.mBackgroundColor);
            this.mBackgroundPaint.setStyle(Paint.Style.FILL);
            this.mPathPaint.setStrokeCap(Paint.Cap.ROUND);
            this.mSecondaryTextPaint.setColor(this.mSecondaryTextColor);
            this.mSecondaryTextPaint.setTypeface(this.mTypeface);
            this.mPrimaryTextPaint.setColor(this.mPrimaryTextColor);
            this.mPrimaryTextPaint.setTypeface(this.mTypeface);
            this.mIsPaintCreated = true;
        }
    }

    public String getMagnetic() {
        return String.format(Locale.US, "%dμT", Integer.valueOf((int) this.sensorValue.getMagneticField()));
    }

    private void drawAzimuthValue(Canvas canvas) {
        this.mPath.reset();
        double radians = (double) ((float) Math.toRadians(270.0d));
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        this.mPath.moveTo(((float) this.mCenter.x) + (realPx(370.0f) * cos), ((float) this.mCenter.y) + (realPx(370.0f) * sin));
        this.mPath.lineTo((realPx(450.0f) * cos) + ((float) this.mCenter.x), (realPx(450.0f) * sin) + ((float) this.mCenter.y));
        this.mPathPaint.setStrokeCap(Paint.Cap.SQUARE);
        this.mPathPaint.setColor(-1);
        this.mPathPaint.setStrokeWidth(realPx(12.0f));
        canvas.drawPath(this.mPath, this.mPathPaint);
    }

    public String getNumberSW() {
        return ((int) this.sensorValue.getAzimuth()) + "° ";
    }

    public String getTextSW() {
        return Utility.getDirectionText(this.sensorValue.getAzimuth());
    }

    private void drawClock(Canvas canvas) {
        canvas.save();
        canvas.rotate(-this.sensorValue.getAzimuth(), (float) this.mCenter.x, (float) this.mCenter.y);
        drawClock(canvas, this.mCenter);
        drawClockBig(canvas, this.mCenter);
        drawNumber(canvas);
        drawDirectionText(canvas);
        canvas.restore();
    }

    private void drawClock(Canvas canvas, Point point) {
        this.mPathPaint.setColor(-1);
        this.mPathPaint.setStyle(Paint.Style.STROKE);
        this.mPathPaint.setStrokeWidth(realPx(3.0f));
        if (this.mClockPathSecondary == null) {
            this.mClockPathSecondary = new Path();
            float f = 0.0f;
            while (true) {
                double d = (double) f;
                if (d >= 6.283185307179586d) {
                    break;
                }
                float cos = (float) Math.cos(d);
                float sin = (float) Math.sin(d);
                this.mClockPathSecondary.moveTo((realPx(330.0f) * cos) + ((float) point.x), (realPx(330.0f) * sin) + ((float) point.y));
                this.mClockPathSecondary.lineTo((realPx(360.0f) * cos) + ((float) point.x), (realPx(360.0f) * sin) + ((float) point.y));
                double radians = Math.toRadians((double) 2.5f);
                Double.isNaN(d);
                f = (float) (d + radians);
            }
        }
        canvas.drawPath(this.mClockPathSecondary, this.mPathPaint);
    }

    private float realPx(float f) {
        return f * this.mPixelScale;
    }

    private void drawClockBig(Canvas canvas, Point point) {
        this.mPathPaint.setStrokeWidth(realPx(7.0f));
        if (this.mClockPathPrimary == null) {
            this.mClockPathPrimary = new Path();
            float f = 0.0f;
            while (true) {
                double d = (double) f;
                if (d >= 6.283185307179586d) {
                    break;
                }
                float cos = (float) Math.cos(d);
                float sin = (float) Math.sin(d);
                this.mClockPathPrimary.moveTo((realPx(330.0f) * cos) + ((float) point.x), (realPx(330.0f) * sin) + ((float) point.y));
                this.mClockPathPrimary.lineTo((cos * realPx(380.0f)) + ((float) point.x), (sin * realPx(380.0f)) + ((float) point.y));
                double radians = Math.toRadians((double) 30.0f);
                Double.isNaN(d);
                f = (float) (d + radians);
            }
        }
        this.mPathPaint.setColor(-1);
        canvas.drawPath(this.mClockPathPrimary, this.mPathPaint);
        this.mPath.reset();
        double radians2 = (double) ((float) Math.toRadians(270.0d));
        float cos2 = (float) Math.cos(radians2);
        float sin2 = (float) Math.sin(radians2);
        this.mPath.moveTo(((float) this.mCenter.x) + (realPx(320.0f) * cos2), ((float) this.mCenter.y) + (realPx(320.0f) * sin2));
        this.mPath.lineTo((realPx(400.0f) * cos2) + ((float) this.mCenter.x), (realPx(400.0f) * sin2) + ((float) this.mCenter.y));
        this.mPathPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.mPathPaint.setStrokeWidth(realPx(9.0f));
        canvas.drawPath(this.mPath, this.mPathPaint);
    }

    private void drawNumber(Canvas canvas) {
        drawNumber(canvas, 300.0f, "30", 450.0f);
        drawNumber(canvas, 330.0f, "60", 450.0f);
        drawNumber(canvas, 360.0f, "90", 450.0f);
        drawNumber(canvas, 30.0f, "120", 450.0f);
        drawNumber(canvas, 60.0f, "150", 450.0f);
        drawNumber(canvas, 90.0f, "180", 450.0f);
        drawNumber(canvas, 120.0f, "210", 450.0f);
        drawNumber(canvas, 150.0f, "240", 450.0f);
        drawNumber(canvas, 180.0f, "270", 450.0f);
        drawNumber(canvas, 210.0f, "300", 450.0f);
        drawNumber(canvas, 240.0f, "330", 450.0f);
    }

    private void drawNumber(Canvas canvas, float f, String str, float f2) {
        Paint.FontMetrics fontMetrics = this.mNumberTextPaint.getFontMetrics();
        float f3 = (fontMetrics.bottom - fontMetrics.top) + fontMetrics.leading;
        double d = (double) f;
        canvas.save();
        canvas.translate((((float) Math.cos(Math.toRadians(d))) * realPx(f2)) + ((float) this.mCenter.x), (((float) Math.sin(Math.toRadians(d))) * realPx(f2)) + ((float) this.mCenter.y));
        canvas.rotate(f + 90.0f);
        canvas.drawText(str, (-this.mNumberTextPaint.measureText(str)) / 2.0f, f3, this.mNumberTextPaint);
        canvas.restore();
    }
    private void drawDirectionText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = this.mNumberTextPaint.getFontMetrics();
        float realPx = (realPx(380.0f) - ((fontMetrics.bottom - fontMetrics.top) + fontMetrics.leading)) - realPx(this.mUnitPadding);
        this.mDirectionTextPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.mDirectionTextPaint.setTextSize(realPx(60.0f));
        drawDirectionText(canvas, 270.0f, "N", realPx, this.mDirectionTextPaint);
        this.mDirectionTextPaint.setColor(this.mPrimaryTextColor);
        drawDirectionText(canvas, 0.0f, ExifInterface.LONGITUDE_EAST, realPx, this.mDirectionTextPaint);
        drawDirectionText(canvas, 90.0f, ExifInterface.LATITUDE_SOUTH, realPx, this.mDirectionTextPaint);
        drawDirectionText(canvas, 180.0f, ExifInterface.LONGITUDE_WEST, realPx, this.mDirectionTextPaint);
        this.mDirectionTextPaint.setTextSize(realPx(40.0f));
        this.mDirectionTextPaint.setColor(this.mSecondaryTextColor);
        drawDirectionText(canvas, 315.0f, "NE", realPx, this.mDirectionTextPaint);
        drawDirectionText(canvas, 45.0f, "SE", realPx, this.mDirectionTextPaint);
        drawDirectionText(canvas, 135.0f, "SW", realPx, this.mDirectionTextPaint);
        drawDirectionText(canvas, 225.0f, "NW", realPx, this.mDirectionTextPaint);
    }

    private void drawDirectionText(Canvas canvas, float f, String str, float f2, Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float f3 = (fontMetrics.bottom - fontMetrics.top) + fontMetrics.leading;
        double d = (double) f;
        canvas.save();
        canvas.translate((((float) Math.cos(Math.toRadians(d))) * f2) + ((float) this.mCenter.x), (((float) Math.sin(Math.toRadians(d))) * f2) + ((float) this.mCenter.y));
        canvas.rotate(f + 90.0f);
        canvas.drawText(str, (-paint.measureText(str)) / 2.0f, f3, paint);
        canvas.restore();
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        this.mIsPaintCreated = false;
    }
}
