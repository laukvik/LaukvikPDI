package org.laukvik.pdi.project.timeline.views;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.util.GregorianCalendar;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import org.laukvik.pdi.VEvent;
import org.laukvik.pdi.project.Session;
import org.laukvik.pdi.project.timeline.TimelinePainter;


public class WeekPainter implements TimelinePainter{

	public JTable table;
	private Session session;
	private int MINUTES_PR_PIXELS = 60;
	public static Color gradientDark = new Color(45,104,221);
	public static Color gradientLite = new Color(98,149,241);
	public static Color gradientBorder = new Color( 28, 83, 210 );
	public static Color WEEKEND_COLOR = Color.GRAY;
	private final static AlphaComposite transparent = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.1F );
	private final static AlphaComposite solid = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1F );
	private final static long MILLIS_PR_MINUTE = 1000*60;
	private final static long DAY = 1000*60*60*24;


	public WeekPainter( Session session, JTable table ){
		this.session = session;
		this.table = table;
	}

	public Dimension getRulerSize(){
		return new Dimension( getTimelineWidth(), 60 );
	}

	public Dimension getGridSize(){
		log( "Rows: " + session.eventLength()  + " rowheight=" + getRowHeight() );
		return new Dimension( getTimelineWidth(), session.eventLength() * getRowHeight() );
	}

	public int getTimelineWidth(){
		return toPixels( session.getProjectMillis()  );
	}

	public int getFontHeight(){
		return 12;
	}

	public int getRowHeight(){
		return table.getRowHeight();
	}

	/**
	 * Paints a ruler with name of months, week numbers and week days
	 *
	 * @param g
	 * @param width
	 * @param height
	 */
	public void paintRuler( Graphics2D g, int width, int height) {

		/* NB! Create a new copy of calendar otherwise the original object will
		 * be changed
		 * */
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis( session.getScopeStart().getTimeInMillis() );
		cal.setFirstDayOfWeek( GregorianCalendar.MONDAY );

		GregorianCalendar calLast = new GregorianCalendar();
		calLast.setTimeInMillis( session.getProjectEnd().getTimeInMillis() );
		calLast.setFirstDayOfWeek( GregorianCalendar.MONDAY );

		Font monthFont = new Font( table.getTableHeader().getFont().getName(), Font.BOLD,  table.getTableHeader().getFont().getSize()+4 );
		Font weekFont = table.getTableHeader().getFont();
		Font dayFont = table.getTableHeader().getFont();
		Color dayColor = table.getTableHeader().getForeground();
		dayColor = Color.GRAY;
		Color dayBg = table.getTableHeader().getBackground();
		Color monthColor = table.getTableHeader().getForeground();
		Color weekColor = table.getTableHeader().getForeground();

		int dayFromHeight = height - table.getTableHeader().getHeight();
		int dayToHeight = height;
		int dayStart = dayToHeight - ( (table.getTableHeader().getHeight() - dayFont.getSize())   );

		g.setColor( dayBg );
		g.fillRect( 0, dayFromHeight, width, dayToHeight - dayFromHeight );

		while(cal.getTimeInMillis() < calLast.getTimeInMillis()){
			int weekday = millisecondsToPixels( cal.getTimeInMillis() );

			/* Draw days */
			g.setColor( dayColor );
			g.setFont( dayFont );
			g.drawString( cal.get( GregorianCalendar.DAY_OF_MONTH ) + "", weekday+9, dayStart );
			g.setColor( Color.BLACK );

			/* Draw week numbers */
			if (cal.get( GregorianCalendar.DAY_OF_WEEK ) == cal.getFirstDayOfWeek()){
				String weekNumberText = getWeekText( cal );
				int weekNumberStringWidth = getStringWidth( getWeekText( cal ), weekFont );
				g.setFont( weekFont );
				g.setColor( weekColor );
				g.drawString( weekNumberText, weekday + (weekPixels()/2)-(weekNumberStringWidth/2), 35 );
			}

			/* Draw month names */
			if (cal.get( GregorianCalendar.DAY_OF_MONTH ) == 1){
				String month = getMonthName( cal.get( GregorianCalendar.MONTH ) );
				int monthStringWidth = getStringWidth( month, monthFont );
				int days = cal.getActualMaximum( GregorianCalendar.DAY_OF_MONTH );
				int monthPixels = toPixels( days * DAY );
				g.setColor( monthColor );
				g.setFont( monthFont );
				g.drawString( month, weekday + ((monthPixels)/2) - (monthStringWidth/2), getFontHeight() + 4 );
			}

			cal.add( GregorianCalendar.DAY_OF_MONTH, 1 );

		}
	}

	public String getWeekText( GregorianCalendar cal ){
		return "Uke " + cal.get( GregorianCalendar.WEEK_OF_YEAR );
	}



	public void paintGrid( Graphics2D g, int w, int h ){

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis( session.getScopeStart().getTimeInMillis() );
		cal.setFirstDayOfWeek( GregorianCalendar.MONDAY );

		GregorianCalendar calLast = new GregorianCalendar();
		calLast.setTimeInMillis( session.getProjectEnd().getTimeInMillis() );
		calLast.setFirstDayOfWeek( GregorianCalendar.MONDAY );

		Composite savesComposite = g.getComposite();

		while(cal.getTimeInMillis() < calLast.getTimeInMillis()){

			cal.add( GregorianCalendar.DAY_OF_MONTH, 1 );
			g.setComposite( transparent );
			switch (cal.get( GregorianCalendar.DAY_OF_WEEK )){
				case GregorianCalendar.SATURDAY :
					g.setColor( WEEKEND_COLOR );
					int saturday = millisecondsToPixels( cal.getTimeInMillis() );
					int saturdayNight = millisecondsToPixels( cal.getTimeInMillis() + DAY );
					g.fillRect( saturday, 0, saturdayNight-saturday, h );
					break;

				case GregorianCalendar.SUNDAY :
					g.setColor( WEEKEND_COLOR );
					int sunday = millisecondsToPixels( cal.getTimeInMillis() );
					int sundayNight = millisecondsToPixels( cal.getTimeInMillis() + DAY );
					g.fillRect( sunday, 0, sundayNight-sunday, h );
					break;

			}

			g.setColor( Color.BLACK );

			int weekday = millisecondsToPixels( cal.getTimeInMillis() );
			g.drawLine( weekday, 0, weekday, h );
			g.setComposite( solid );
			if (cal.get( GregorianCalendar.DAY_OF_MONTH ) == 1){
				g.drawLine( weekday, 0, weekday, h );

			}
		}
		g.setComposite( savesComposite );
	}

	public void paintLocked( Graphics2D g, int panelWidth, int panelHeight ){
		int row = 0;
		g.setComposite( transparent );
		g.setColor( Color.GREEN );
		for (VEvent event : session.listLockedEvents()){

			/* Get drawing coordinates for event */
			int x = millisecondsToPixels( event.getStartDate().getTime() );
			int y = row * getRowHeight();
			int endX = millisecondsToPixels( event.getEndDate().getTime()  );
			int width = endX - x;
			int height = getRowHeight();

			/* Create a gradient paint */
//			GradientPaint gradient = new GradientPaint( x, y, gradientDark, x, y+height-5, gradientLite, true );
//	        g.setPaint( gradient );
	        g.fillRect( x, 0, width, panelHeight );
	        row++;
		}
		g.setComposite( solid );
	}

	/**
	 * Paints the events on the panel
	 *
	 *
	 * @param g
	 * @param panelWidth
	 */
	public void paintEvents( Graphics2D g, int panelWidth, int panelHeight ){
		paintLocked( g, panelWidth, panelHeight );

		/* Draw very first line */
		g.setColor( table.getGridColor() );
		g.drawLine( 0, 0, panelWidth, 0 );

		for (int row = 0; row<session.eventLength(); row++){
			VEvent event = session.getEvent( row );
			/* Get drawing coordinates for event */
			int x = millisecondsToPixels( event.getStartDate().getTime() );
			int y = row * getRowHeight();
			int endX = millisecondsToPixels( event.getEndDate().getTime()  );
			int width = endX - x;
			int height = getRowHeight();

			/* Create a gradient paint */
			GradientPaint gradient = new GradientPaint( x, y, gradientDark, x, y+height-5, gradientLite, true );
	        g.setPaint( gradient );

	        g.fillRect( x, y+2, width, height-3 );
	        g.setColor( gradientBorder );
	        g.drawRect( x, y+3, width, height-4 );
	        g.setPaint( Color.BLUE );

	        /* Draw the grid itself */
			g.setColor( table.getGridColor() );
			g.drawLine( 0, y+getRowHeight(), panelWidth, y+getRowHeight() );
		}

//		g.setComposite( transparent );
//		paintLocked( g, panelWidth, panelHeight );
//		g.setComposite( solid );
	}

	public int getStringWidth( String string, Font font ){
		return SwingUtilities.computeStringWidth( table.getFontMetrics( font ) , string );
	}

	public int weekPixels(){
		return toPixels( DAY*7 );
	}

	public String getDayName( int day ){
		switch (day){
			case GregorianCalendar.MONDAY : return "M";
			case GregorianCalendar.TUESDAY : return "T";
			case GregorianCalendar.WEDNESDAY : return "O";
			case GregorianCalendar.THURSDAY : return "T";
			case GregorianCalendar.FRIDAY : return "F";
			case GregorianCalendar.SATURDAY : return "L";
			case GregorianCalendar.SUNDAY : return "S";
		}
		return "";
	}

	public String getMonthName( int month ){
		switch (month){
			case GregorianCalendar.JANUARY :	return "Januar";
			case GregorianCalendar.FEBRUARY :	return "Februar";
			case GregorianCalendar.MARCH : 		return "Mars";
			case GregorianCalendar.APRIL : 		return "April";
			case GregorianCalendar.MAY : 		return "Mai";
			case GregorianCalendar.JUNE : 		return "Juni";
			case GregorianCalendar.JULY : 		return "Juli";
			case GregorianCalendar.AUGUST : 	return "August";
			case GregorianCalendar.SEPTEMBER : 	return "September";
			case GregorianCalendar.OCTOBER : 	return "Oktober";
			case GregorianCalendar.NOVEMBER : 	return "November";
			case GregorianCalendar.DECEMBER : 	return "Desember";
		}
		return "";
	}





	public int millisecondsToPixels( long millis ){
		return toPixels( millis - session.firstMillis() );
	}

	public int toPixels( long millis ){
		return (int) (  (millis) / MILLIS_PR_MINUTE) / MINUTES_PR_PIXELS;
	}

	public void setMinutesPrPixel( int minutes ){
		this.MINUTES_PR_PIXELS = minutes;
	}

	public int getMinutesPrPixel() {
		return MINUTES_PR_PIXELS;
	}

	public void log( String message ){
		System.out.println( this.getClass().getName() + ": " + message  );
	}

}