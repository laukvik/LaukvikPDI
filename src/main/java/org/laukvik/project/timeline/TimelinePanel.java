package org.laukvik.project.timeline;

import java.awt.Rectangle;
import java.util.GregorianCalendar;

import javax.swing.JScrollPane;

public class TimelinePanel extends JScrollPane {

	private static final long serialVersionUID = 1236456870788156463L;
	private RulerPanel ruler;
	private EventPanel events;
	private TimelinePainter painter;

	public TimelinePanel( TimelinePainter painter ) {
		super();
		/**/
		ruler = new RulerPanel( painter );
		events = new EventPanel( painter );
		this.painter = painter;
		setViewportView( events );
		setColumnHeaderView( ruler );
		setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		setAutoscrolls( true );
		setBorder( null );
	}
	
	public void repaintBoth(){
//		log( "repaintBoth");
		events.resize();
		ruler.resize();
//		log( "resized");
		events.repaint();
		ruler.repaint();
//		log( "repainted");
	}
	
	public void log( String message ){
		System.out.println( this.getClass().getName() + ": " + message  );
	}
	
	public void scrollTo( GregorianCalendar date ){
		int left = painter.millisecondsToPixels( date.getTimeInMillis() )- 30 ;
//		left = painter.toPixels( date.getTimeInMillis() );
		int top = 0;
		Rectangle r = new Rectangle( left, top, events.getWidth(), 10 );
		log( "ScrollTo: " + date.getTime() + " " + r.toString() );
//		scrollRectToVisible( r );
		
		events.scrollRectToVisible( r );
	}
	
}