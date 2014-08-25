package com.wimoto.app.widgets.sparkline;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * <p>Base class for sparkline charts.</p>
 * 
 * @author Scott Hyndman
 */
public abstract class SparkView extends View {

  // Default paints (assigned in static constructor)
  
  private static final Paint DEFAULT_LINE_PAINT;
  private static final Paint DEFAULT_FILL_PAINT;
  private static final Paint DEFAULT_BACKGROUND_PAINT;
  
  
  protected List<Float> values = new ArrayList<Float>(); // Usually represent Ys
  protected float chartRangeMin;
  protected float chartRangeMax;
  protected boolean chartRangeSet = false;
  
  protected boolean showFill = false;
  private DrawInfo drawInfo;
  
  protected Paint linePaint = new Paint(DEFAULT_LINE_PAINT);
  protected Paint fillPaint = new Paint(DEFAULT_FILL_PAINT);
  protected Paint backgroundPaint = new Paint(DEFAULT_BACKGROUND_PAINT);
  
  
  public SparkView(Context context) { super(context); }
  public SparkView(Context context, AttributeSet attrs) { super(context, attrs); }
  
  
  /**
   * Gets the list of values rendered by the chart.
   */
  public List<Float> getValues() { return values; }
  
  /**
   * Sets the list of values rendered by the chart.
   */
  public void setValues(List<Float> yVals) { this.values = yVals; }
  
  /**
   * Returns the width, in pixels, of the line used to connect chart values.
   */
  public float getLineWidth() { return linePaint.getStrokeWidth(); }
  
  /**
   * Sets the width, in pixels, of the line used to connect chart values.
   * @param lineWidth
   */
  public void setLineWidth(float lineWidth) { linePaint.setStrokeWidth(lineWidth); }
  
  /**
   * Returns the color of the line used to connect chart values.
   */
  public int getLineColor() { return linePaint.getColor(); }
  
  /**
   * Sets the color of the line used to connect chart values.
   * @param color
   */
  public void setLineColor(int color) { linePaint.setColor(color); }
  
  /**
   * <p>Sets the color of the fill.
   * 
   * <p>The meaning of "fill" is implementation dependent.
   * 
   * @see #setShowFill(boolean)
   */
  public void setFillColor(int color) { fillPaint.setColor(color); }
  
  /**
   * <p>Sets whether the chart's fill is shown.
   * 
   * <p>The meaning of "fill" is implementation dependent.
   * 
   * @see #setFillColor(int)
   */
  public void setShowFill(boolean showFill) { this.showFill = showFill; }
  
  /**
   * Sets the color of the background.
   */
  public void setBackgroundColor(int color) { backgroundPaint.setColor(color); }
  
  /**
   * Sets the minimum and maximum values in the chart's range. Values above or below
   * these values will fall outside the charts extents.
   */
  public void setChartRange(float min, float max) {
    chartRangeMin = min;
    chartRangeMax = max;
    chartRangeSet = true;
  }
  
  
  //
  // PRE-CALCULATION
  //
  
  /**
   * Overridden by subclasses to provide the {@link DrawInfo} (or a subclass) instance used
   * to pre-calculate drawing information.
   */
  protected abstract DrawInfo getDrawInfo();
  
  private final void prepareToDraw() {
    // Create a new draw info if we don't already have one
    if (drawInfo == null)
      drawInfo = getDrawInfo();
    
    updateDrawInfoExtents();
    calculateFeatures();
  }
  
  /**
   * <p>
   * Can be overridden by subclasses to nudge the available canvas inward or outward,
   * depending on some constraint unknown to the base class. 
   * 
   * <p>
   * For example, a spot may be desired to represent a minimum or maximum, which exists 
   * at the edge of the canvas. To draw the spot completely the chart's scale could be
   * reduced. 
   */
  protected void updateDrawInfoExtents() {
    DrawInfo drawInfo = getDrawInfo();
    
    drawInfo.top = getPaddingTop();
    drawInfo.right = getWidth() - getPaddingRight();
    drawInfo.bottom = getHeight() - getPaddingBottom();
    drawInfo.left = getPaddingLeft();
    drawInfo.width = drawInfo.right - drawInfo.left;
    drawInfo.height = drawInfo.bottom - drawInfo.top;
  }
  
  /**
   * Implemented by subclasses to calculate the required graph features prior to drawing.
   */
  protected abstract void calculateFeatures();
  
  /**
   * <p>
   * Converts an X coordinate between the chart's coordinate system, and the canvas'.
   * 
   * <p>
   * Requires that {@link #updateDrawInfoExtents()} that the drawInfo has been refreshed prior 
   * to calling.
   */
  protected float toCanvasX(float xVal) {
    return drawInfo.left + ((xVal - drawInfo.minDrawnX) / drawInfo.drawRangeX) * drawInfo.width;
  }

  /**
   * <p>
   * Converts an Y coordinate between the chart's coordinate system, and the canvas'.
   * 
   * <p>
   * Requires that {@link #updateDrawInfoExtents()} that the drawInfo has been refreshed prior 
   * to calling.
   */
  protected float toCanvasY(float yVal) {
    return drawInfo.top + (1 - (yVal - drawInfo.minDrawnY) / drawInfo.drawRangeY) * drawInfo.height;
  }
  
  
  //
  // DRAWING
  //
  
  /**
   * Marked final to impose some structure in subclasses. Subclasses should implement
   * {@link #internalDraw(Canvas)}.
   */
  @Override
  protected final void onDraw(Canvas canvas) {
    prepareToDraw();
    
    drawBackground(canvas);
    internalDraw(canvas);
  }
  
  /**
   * Draws the background onto the canvas.
   */
  protected void drawBackground(Canvas canvas) {
    canvas.drawPaint(backgroundPaint);
  }

  /**
   * Overridden by subclasses to perform implementation specific drawing. Called after
   * {@link #prepareToDraw()}.
   */
  protected abstract void internalDraw(Canvas canvas);


  /**
   * Stores state for draw methods. 
   */
  protected static class DrawInfo {
    public float left;
    public float top;
    public float right;
    public float bottom;
    public float width;
    public float height;
    
    public float minDrawnX;
    public float maxDrawnX;
    public float drawRangeX;
    public float minDrawnY; // Can include normal range
    public float maxDrawnY; // Can include normal range
    public float drawRangeY;
  }


  static {
    DEFAULT_LINE_PAINT = new Paint();
    DEFAULT_LINE_PAINT.setStyle(Style.STROKE);
    DEFAULT_LINE_PAINT.setColor(0xFF0000FF);
    DEFAULT_LINE_PAINT.setAntiAlias(true);
    
    DEFAULT_FILL_PAINT = new Paint();
    DEFAULT_FILL_PAINT.setStyle(Style.FILL);
    DEFAULT_FILL_PAINT.setColor(0xFFCCDDFF);

    DEFAULT_BACKGROUND_PAINT = new Paint();
    DEFAULT_BACKGROUND_PAINT.setStyle(Style.FILL);
    DEFAULT_BACKGROUND_PAINT.setColor(Color.WHITE);
  }
}