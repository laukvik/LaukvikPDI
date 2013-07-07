package org.laukvik.project.timeline;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

public interface TimelinePainter{

	public Dimension getRulerSize();
	
	public Dimension getGridSize();
	
	public int getTimelineWidth();
	
	public int getFontHeight();
	
	public int getRowHeight();
	
	/**
	 * Paints a ruler with name of months, week numbers and week days
	 * 
	 * @param g
	 * @param width
	 * @param height
	 */
	public void paintRuler( Graphics2D g, int width, int height);
	
	public void paintGrid( Graphics2D g, int w, int h );
	
	/**
	 * Paints the events on the panel
	 * 
	 * 
	 * @param g
	 * @param panelWidth
	 */
	public void paintEvents( Graphics2D g, int panelWidth, int panelHeight );
	
	public int getStringWidth( String string, Font font );
	
	public int weekPixels();
	
	public String getDayName( int day );
	
	public String getMonthName( int month );

	public int millisecondsToPixels( long millis );
	
	public int toPixels( long millis );
	
	public void setMinutesPrPixel( int minutes );
	
	public int getMinutesPrPixel();

}