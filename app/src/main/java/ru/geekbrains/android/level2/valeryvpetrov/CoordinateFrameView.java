package ru.geekbrains.android.level2.valeryvpetrov;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Android View class for representation of coordinate frame.
 * ! Works only with Z axis rotation.
 */
public class CoordinateFrameView extends View {
    private int axisLength;
    private int axisStrokeWidth;

    private int axisTextSize;
    private int axisTextVOffset = (int) (axisTextSize * 1.5);
    private int axisTextHOffset = axisLength;

    private static final int AXIS_ARROW_ANGLE = 60; // measured in degrees
    private int axisArrowLength = axisLength / 8;

    private Paint paintXAxis;
    private Path pathXAxis;
    private float angleReprXAxis;   // angle with regard to screen. measured in degrees
    private int pinXofXAxis;    // x coordinate of X axis pin
    private int pinYofXAxis;    // y coordinate of X axis pin

    private Paint paintYAxis;
    private Path pathYAxis;
    private float angleReprYAxis;
    private int pinXofYAxis;
    private int pinYofYAxis;

    private Paint paintZAxis;
    private Path pathZAxis;
    private float angleReprZAxis;
    private int pinXofZAxis;
    private int pinYofZAxis;

    private int centerScreenX;
    private int centerScreenY;

    private int viewWidth;
    private int viewHeight;
    private Paint paintBackground;

    public CoordinateFrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints(attrs);
    }

    public CoordinateFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints(attrs);
    }

    private void initPaints(AttributeSet attrs) {
        TypedArray attrsArray = getContext().obtainStyledAttributes(attrs, R.styleable.CoordinateFrameView);
        axisLength = attrsArray.getInteger(R.styleable.CoordinateFrameView_axisLength, 300);
        axisStrokeWidth = attrsArray.getInteger(R.styleable.CoordinateFrameView_axisStrokeWidth, 6);
        axisTextSize = attrsArray.getInteger(R.styleable.CoordinateFrameView_axisTextSize, 50);
        int backgroundAlpha = attrsArray.getInteger(R.styleable.CoordinateFrameView_backgroundAlpha, 50);
        int colorXAxis = attrsArray.getColor(R.styleable.CoordinateFrameView_colorXAxis, Color.GREEN);
        int colorYAxis = attrsArray.getColor(R.styleable.CoordinateFrameView_colorYAxis, Color.BLUE);
        int colorZAxis = attrsArray.getColor(R.styleable.CoordinateFrameView_colorZAxis, Color.RED);
        attrsArray.recycle();

        paintXAxis = new Paint();
        configureAxisPaint(paintXAxis, colorXAxis);

        paintYAxis = new Paint();
        configureAxisPaint(paintYAxis, colorYAxis);

        paintZAxis = new Paint();
        configureAxisPaint(paintZAxis, colorZAxis);

        paintBackground = new Paint();
        paintBackground.setAlpha(backgroundAlpha);
    }

    private void configureAxisPaint(Paint paintAxis, int colorAxis) {
        paintAxis.setColor(colorAxis);
        paintAxis.setStrokeWidth(axisStrokeWidth);
        paintAxis.setStyle(Paint.Style.STROKE);
        paintAxis.setTextSize(axisTextSize);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        viewWidth = right;
        viewHeight = bottom;
        centerScreenX = Math.abs(left - right) / 2;
        centerScreenY = Math.abs(top - bottom) / 2;

        configureAxisMetrics(viewWidth, viewHeight);

        super.onLayout(changed, left, top, right, bottom);
    }

    private void configureAxisMetrics(int viewWidth, int viewHeight) {
        if (viewWidth < viewHeight)
            axisLength = viewWidth / 3;
        else
            axisLength = viewHeight / 3;

        axisTextSize = axisLength / 6;
        axisTextVOffset = (int) (axisTextSize * 1.5);
        axisTextHOffset = axisLength;
        axisArrowLength = axisLength / 8;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawAxis(canvas, pathXAxis, paintXAxis,
                pinXofXAxis, pinYofXAxis,
                angleReprXAxis,
                "X");
        drawAxis(canvas, pathYAxis, paintYAxis,
                pinXofYAxis, pinYofYAxis,
                angleReprYAxis,
                "Y");
        drawAxis(canvas, pathZAxis, paintZAxis,
                pinXofZAxis, pinYofZAxis,
                angleReprZAxis,
                "Z");
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawBitmap(((BitmapDrawable) getContext().getDrawable(R.mipmap.grid)).getBitmap(),
                null,
                new Rect(0, 0, viewWidth, viewHeight),
                paintBackground);
    }

    private void drawAxis(Canvas canvas,
                          Path pathAxis, Paint paintAxis,
                          int pinXofAxis, int pinYofAxis,
                          float angleReprAxis,
                          String axisLabel) {
        canvas.drawPath(pathAxis, paintAxis); // draws axis line
        drawAxisArrow(canvas, paintAxis, pinXofAxis, pinYofAxis, angleReprAxis); // draws axis arrow
        canvas.drawTextOnPath(axisLabel,    // draws axis label
                pathAxis,
                axisTextHOffset, axisTextVOffset,
                paintAxis);
    }

    public void setAngleZAxis(float angleZAxis) {
        rebuildXPath(0, 0, angleZAxis);
        rebuildYPath(0, 0, angleZAxis);
        rebuildZPath(0, 0, angleZAxis);
        invalidate();
    }

    private void rebuildZPath(float angleXAxis, float angleYAxis, float angleZAxis) {
        if (pathZAxis == null)
            pathZAxis = new Path();

        pathZAxis.reset();

        if (angleXAxis == 0 && angleYAxis == 0) {   // rotation about Z axis
            this.angleReprZAxis = 0;
            pinXofZAxis = centerScreenX;
            pinYofZAxis = centerScreenY;

            pathZAxis.moveTo(centerScreenX, centerScreenY);
            pathZAxis.lineTo(pinXofZAxis, pinYofZAxis);
        }

        pathZAxis.close();
    }

    private void rebuildYPath(float angleXAxis, float angleYAxis, float angleZAxis) {
        if (pathYAxis == null)
            pathYAxis = new Path();

        pathYAxis.reset();

        if (angleXAxis == 0 && angleYAxis == 0) {   // rotation about Z axis
            this.angleReprYAxis = angleZAxis;

            pinXofYAxis = centerScreenX
                    + (int) (Math.cos(Math.toRadians(angleZAxis) - Math.PI / 2)
                    * axisLength);
            pinYofYAxis = centerScreenY
                    + (int) (Math.sin(Math.toRadians(angleZAxis) - Math.PI / 2)
                    * axisLength);

            pathYAxis.moveTo(centerScreenX, centerScreenY);
            pathYAxis.lineTo(pinXofYAxis, pinYofYAxis);
        }

        pathYAxis.close();
    }

    private void rebuildXPath(float angleXAxis, float angleYAxis, float angleZAxis) {
        if (pathXAxis == null)
            pathXAxis = new Path();

        pathXAxis.reset();

        if (angleXAxis == 0 && angleYAxis == 0) {   // rotation about Z axis
            this.angleReprXAxis = angleZAxis - 90;

            pinXofXAxis = centerScreenX
                    + (int) (Math.cos(Math.toRadians(angleZAxis))
                    * axisLength);
            pinYofXAxis = centerScreenY
                    + (int) (Math.sin(Math.toRadians(angleZAxis))
                    * axisLength);

            pathXAxis.moveTo(centerScreenX, centerScreenY);
            pathXAxis.lineTo(pinXofXAxis, pinYofXAxis);
        }

        pathXAxis.close();
    }

    private void drawAxisArrow(Canvas canvas,
                               Paint paintAxis,
                               int pinXofAxis, int pinYofAxis,
                               float angleReprAxis) {
        if (pinXofAxis == centerScreenX && pinYofAxis == centerScreenY) {   // directs out of screen upwards
            // draws cross
            int crossLength = axisArrowLength / 2;
            canvas.drawLine(
                    pinXofAxis - crossLength, pinYofAxis - crossLength,
                    pinXofAxis + crossLength, pinYofAxis + crossLength,
                    paintAxis);
            canvas.drawLine(
                    pinXofAxis + crossLength, pinYofAxis - crossLength,
                    pinXofAxis - crossLength, pinYofAxis + crossLength,
                    paintAxis);
        } else {    // lays on plane
            // draws arrow related to axis
        }
    }
}