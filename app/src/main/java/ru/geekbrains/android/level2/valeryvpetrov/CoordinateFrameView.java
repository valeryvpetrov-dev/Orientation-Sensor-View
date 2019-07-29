package ru.geekbrains.android.level2.valeryvpetrov;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

public class CoordinateFrameView extends View {
    /**
     * Android View class for representation of coordinate frame.
     * ! Works only with X axis rotation.
     */
    private int axisLength = 300;
    private int axisStrokeWidth = 6;

    private int axisTextSize = 50;
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

    public CoordinateFrameView(Context context) {
        super(context);
        initPaints();
    }

    public CoordinateFrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    public CoordinateFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    private void initPaints() {
        paintXAxis = new Paint();
        configureAxisPaint(paintXAxis, Color.RED);

        paintYAxis = new Paint();
        configureAxisPaint(paintYAxis, Color.GREEN);

        paintZAxis = new Paint();
        configureAxisPaint(paintZAxis, Color.BLUE);

        paintBackground = new Paint();
        paintBackground.setAlpha(50);
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

    public void setAngleXAxis(float angleXAxis) {
        rebuildXPath(angleXAxis, 0, 0);
        rebuildYPath(angleXAxis, 0, 0);
        rebuildZPath(angleXAxis, 0, 0);
        invalidate();
    }

    private void rebuildXPath(float angleXAxis, float angleYAxis, float angleZAxis) {
        if (pathXAxis == null)
            pathXAxis = new Path();

        pathXAxis.reset();

        if (angleYAxis == 0 && angleZAxis == 0) {   // rotation about X axis
            this.angleReprXAxis = 0;
            pinXofXAxis = centerScreenX;
            pinYofXAxis = centerScreenY;

            pathXAxis.moveTo(centerScreenX, centerScreenY);
            pathXAxis.lineTo(pinXofXAxis, pinYofXAxis);
        }

        pathXAxis.close();
    }

    private void rebuildYPath(float angleXAxis, float angleYAxis, float angleZAxis) {
        if (pathYAxis == null)
            pathYAxis = new Path();

        pathYAxis.reset();

        if (angleYAxis == 0 && angleZAxis == 0) {   // rotation about X axis
            this.angleReprYAxis = angleXAxis;

            pinXofYAxis = centerScreenX
                    + (int) (Math.cos(Math.toRadians(angleXAxis))
                    * axisLength);
            pinYofYAxis = centerScreenY
                    + (int) (Math.sin(Math.toRadians(angleXAxis))
                    * axisLength);

            pathYAxis.moveTo(centerScreenX, centerScreenY);
            pathYAxis.lineTo(pinXofYAxis, pinYofYAxis);
        }

        pathYAxis.close();
    }

    private void rebuildZPath(float angleXAxis, float angleYAxis, float angleZAxis) {
        if (pathZAxis == null)
            pathZAxis = new Path();

        pathZAxis.reset();

        if (angleYAxis == 0 && angleZAxis == 0) {   // rotation about X axis
            this.angleReprZAxis = angleXAxis + 90;

            pinXofZAxis = centerScreenX
                    + (int) (Math.cos(Math.toRadians(angleXAxis) + Math.PI / 2)
                    * axisLength);
            pinYofZAxis = centerScreenY
                    + (int) (Math.sin(Math.toRadians(angleXAxis) + Math.PI / 2)
                    * axisLength);

            pathZAxis.moveTo(centerScreenX, centerScreenY);
            pathZAxis.lineTo(pinXofZAxis, pinYofZAxis);
        }

        pathZAxis.close();
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
            canvas.drawLine(
                    pinXofAxis, pinYofAxis,
                    pinXofAxis + (int) (Math.cos(Math.toRadians(angleReprAxis + 90 + AXIS_ARROW_ANGLE)) * axisArrowLength),
                    pinYofAxis + (int) (Math.sin(Math.toRadians(angleReprAxis + 90 + AXIS_ARROW_ANGLE)) * axisArrowLength),
                    paintAxis);
            canvas.drawLine(
                    pinXofAxis, pinYofAxis,
                    pinXofAxis + (int) (Math.cos(Math.toRadians(angleReprAxis - 90 - AXIS_ARROW_ANGLE)) * axisArrowLength),
                    pinYofAxis + (int) (Math.sin(Math.toRadians(angleReprAxis - 90 - AXIS_ARROW_ANGLE)) * axisArrowLength),
                    paintAxis);
        }
    }
}