package com.vilyever.drawingview.brush;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.vilyever.drawingview.VDDrawingPath;
import com.vilyever.drawingview.VDDrawingPoint;

/**
 * VDLineBrush
 * AndroidDrawingView <com.vilyever.drawingview.brush>
 * Created by vilyever on 2015/10/21.
 * Feature:
 */
public class VDLineBrush extends VDShapeBrush {
    final VDLineBrush self = this;


    /* #Constructors */
    public VDLineBrush() {

    }

    public VDLineBrush(float size, int color) {
        this(size, color, Color.TRANSPARENT);
    }

    public VDLineBrush(float size, int color, int solidColor) {
        this(size, color, solidColor, false);
    }

    public VDLineBrush(float size, int color, int solidColor, boolean edgeRounded) {
        super(size, color, solidColor, edgeRounded);
    }

    /* #Overrides */
    @Override
    public RectF drawPath(@NonNull Canvas canvas, @NonNull VDDrawingPath drawingPath, DrawingPointerState state) {
        if (drawingPath.getPoints().size() > 1) {
            VDDrawingPoint beginPoint = drawingPath.getPoints().get(0);
            VDDrawingPoint lastPoint = drawingPath.getPoints().get(drawingPath.getPoints().size() - 1);

            RectF drawingRect = new RectF();
            drawingRect.left = Math.min(beginPoint.x, lastPoint.x);
            drawingRect.top = Math.min(beginPoint.y, lastPoint.y);
            drawingRect.right = Math.max(beginPoint.x, lastPoint.x);
            drawingRect.bottom = Math.max(beginPoint.y, lastPoint.y);

            RectF pathFrame;
            if (!self.isEdgeRounded()) {
                pathFrame = new RectF(drawingRect);

                // 计算画笔延展宽高
                double x = drawingRect.right - drawingRect.left;
                double y = drawingRect.bottom - drawingRect.top;
                double z = Math.sqrt(x * x + y * y);
                double sina = y / z; // 水平线与直线夹角sin值
                double sinb = x / z; // 竖直线与直线夹角sin值
                double ta = Math.asin(sina); // 水平线与直线夹角
                double tb = Math.asin(sinb); // 竖直线与直线夹角
                double tdx = Math.abs(ta - Math.PI / 4);
                double tdy = Math.abs(tb - Math.PI / 4);
                double dx = Math.cos(tdx) * (self.getSize() / 2.0f) * Math.sqrt(2);
                double dy = Math.cos(tdy) * (self.getSize() / 2.0f) * Math.sqrt(2);

                pathFrame.left -= dx;
                pathFrame.top -= dy;
                pathFrame.right += dx;
                pathFrame.bottom += dy;
            }
            else {
                pathFrame = super.drawPath(canvas, drawingPath, state);
            }

            if (state == DrawingPointerState.FetchFrame) {
                return pathFrame;
            }

            Paint paint = self.getPaint();

            Path path = new Path();
            path.moveTo(beginPoint.x, beginPoint.y);
            path.lineTo(lastPoint.x, lastPoint.y);

            if (state == DrawingPointerState.CalibrateToOrigin) {
                path.offset(-pathFrame.left, -pathFrame.top);
            }

            canvas.drawPath(path, paint);

            return pathFrame;
        }

        return null;
    }

    /* #Accessors */     
     
    /* #Delegates */     
     
    /* #Private Methods */    
    
    /* #Public Methods */
    public static VDLineBrush defaultBrush() {
        return new VDLineBrush(5, Color.BLACK);
    }

    /* #Classes */

    /* #Interfaces */     
     
    /* #Annotations @interface */    
    
    /* #Enums */
}