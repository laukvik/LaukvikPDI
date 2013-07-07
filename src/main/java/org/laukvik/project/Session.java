package org.laukvik.project;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import org.laukvik.pdi.VCalendar;
import org.laukvik.pdi.VEvent;
import org.laukvik.project.timeline.TimelineTableModel;

public class Session {
	
	private VCalendar cal = new VCalendar();
	private GregorianCalendar scopeStart = new GregorianCalendar();
	private GregorianCalendar scopeEnd = new GregorianCalendar();
	private Vector <VEvent> events = new Vector<VEvent>();
	private VCalendar busy = new VCalendar();
	
	public Session( VCalendar calendar ) {
		scopeStart = getDefaultProjectStart();
		scopeEnd = getDefaultProjectEnd();
	}

	public VCalendar getBusy() {
		return busy;
	}

	public void setBusy(VCalendar busy) {
		this.busy = busy;
	}

	/**
	 * Returns the default start date for new projects - which is january 1st
	 * of the current year.
	 * 
	 * @return
	 */
	public GregorianCalendar getDefaultProjectEnd(){
		GregorianCalendar fromDate = new GregorianCalendar();
		fromDate.set( GregorianCalendar.MONTH, GregorianCalendar.JANUARY );
		fromDate.set( GregorianCalendar.DAY_OF_MONTH, 1 );
		fromDate.set( GregorianCalendar.HOUR, 0 );
		fromDate.set( GregorianCalendar.MINUTE, 0 );
		fromDate.set( GregorianCalendar.SECOND, 0 );
		fromDate.set( GregorianCalendar.MILLISECOND, 0 );
		/* Go to next january */
		fromDate.add( GregorianCalendar.YEAR, 1 );
		return fromDate;
	}
	
	/**
	 * Returns the default end date for new projects - which is the january 1st
	 * of next year. 
	 * 
	 * @return
	 */
	public GregorianCalendar getDefaultProjectStart(){
		GregorianCalendar fromDate = new GregorianCalendar();
		fromDate.set( GregorianCalendar.MONTH, GregorianCalendar.JANUARY );
		fromDate.set( GregorianCalendar.DAY_OF_MONTH, 1 );
		fromDate.set( GregorianCalendar.HOUR, 0 );
		fromDate.set( GregorianCalendar.MINUTE, 0 );
		fromDate.set( GregorianCalendar.SECOND, 0 );
		fromDate.set( GregorianCalendar.MILLISECOND, 0 );
		return fromDate;
	}

	public VCalendar getCalendar() {
		return cal;
	}
	
	public void addEvent( VEvent event ){
		cal.addEvent( event );
	}
	
	public void calendarChanged(){
//		this.events = cal.listEvents( scopeStart.getTime(), scopeEnd.getTime() );
		this.events = cal.listEvents( null, null );
	}

	public GregorianCalendar getProjectEnd() {
		return scopeEnd;
	}

	public void setProjectEnd(GregorianCalendar scopeEnd) {
		this.scopeEnd = scopeEnd;
	}

	public GregorianCalendar getScopeStart() {
		return scopeStart;
	}

	public void setScopeStart(GregorianCalendar scopeStart) {
		this.scopeStart = scopeStart;
	}
	
	public int eventLength(){
		return events.size();
	}
	
	public VEvent getEvent( int index ){
//		log( "getEvent: " + index + " " );
		return events.get( index );
	}

	public long firstMillis(){
		return scopeStart.getTimeInMillis();
	}
	
	public long lastMillis(){
		return scopeEnd.getTimeInMillis();
	}

	public long getProjectMillis() {
		return lastMillis() - firstMillis();
	}

	public void removeEvent( int index, TimelineTableModel model) {
//		cal.remove( model.getRow( index ) );
		cal.remove( index );
	}
	
	public void removeEvents( int [] rows, TimelineTableModel model ){
		/* Order them ascending order */
		Arrays.sort( rows );
		log( "Removing: " + rows.toString() );
		/* Reverse sort them */
		for (int n=rows.length-1; n>-1; n--){
			log( "Removing row: " + rows[ n ] );
			removeEvent( rows[ n ], model );
		}
	}

	public void setCalendar( VCalendar calendar ) {
		this.cal = calendar;
	}
	
	public long getNextFreeStart( long start ){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis( start );
		return getNextFreeStart( cal );
	}

	/**
	 * 
	 * @param cal
	 * @return
	 */
	public long getNextFreeStart( GregorianCalendar cal ){
//		GregorianCalendar cal = new GregorianCalendar();
//		cal.setTimeInMillis( calendar.getTimeInMillis() );
		
//		if (cal.get( GregorianCalendar.DAY_OF_WEEK ) == GregorianCalendar.SATURDAY){
//			/* Dont work on saturdays */
//			cal.set( GregorianCalendar.HOUR_OF_DAY, 9 );
//			cal.set( GregorianCalendar.MINUTE, 0 );
//			cal.set( GregorianCalendar.SECOND, 0 );
//			cal.set( GregorianCalendar.MILLISECOND, 0 );
//			cal.add( GregorianCalendar.DAY_OF_MONTH, 2 );
//		} else if (cal.get( GregorianCalendar.DAY_OF_WEEK ) == GregorianCalendar.SUNDAY){
//			/* Dont work on sundays */
//			cal.set( GregorianCalendar.HOUR_OF_DAY, 9 );
//			cal.set( GregorianCalendar.MINUTE, 0 );
//			cal.set( GregorianCalendar.SECOND, 0 );
//			cal.set( GregorianCalendar.MILLISECOND, 0 );
//			cal.add( GregorianCalendar.DAY_OF_MONTH, 1 );
//		} else if (cal.get( GregorianCalendar.HOUR_OF_DAY ) < 9){
//			/* Dont work before 0900*/
//			cal.set( GregorianCalendar.HOUR_OF_DAY, 9 );
//			cal.set( GregorianCalendar.MINUTE, 0 );
//			cal.set( GregorianCalendar.SECOND, 0 );
//			cal.set( GregorianCalendar.MILLISECOND, 0 );
//		} else if (cal.get( GregorianCalendar.HOUR_OF_DAY ) > 17){
//			/* Dont work after 1700*/
//			cal.set( GregorianCalendar.HOUR_OF_DAY, 9 );
//			cal.set( GregorianCalendar.MINUTE, 0 );
//			cal.set( GregorianCalendar.SECOND, 0 );
//			cal.set( GregorianCalendar.MILLISECOND, 0 );
//			cal.add( GregorianCalendar.DAY_OF_MONTH, 1 );
//		}  else if (cal.get( GregorianCalendar.HOUR_OF_DAY ) == 12 && cal.get( GregorianCalendar.MINUTE ) < 30){
//			/* Dont work during lunch from 12:00-12:30 */
//			cal.set( GregorianCalendar.HOUR_OF_DAY, 12 );
//			cal.set( GregorianCalendar.MINUTE, 30 );
//			cal.set( GregorianCalendar.SECOND, 0 );
//			cal.set( GregorianCalendar.MILLISECOND, 0 );
//		}
		log( "getNextFreeStart: Getnext: " + cal.getTime() );
		Date from = cal.getTime();
		Vector <VEvent> events = getBusy().listEvents( null, null );
		int x = 0;
		for (VEvent e : events){
			
			if (from.getTime() < e.getStartDate().getTime()){
				log( "getNextFreeStart: Found: " + e.getEndDate() );

				return e.getStartDate().getTime();

				
			} else {
				log( "getNextFreeStart: Didnt find: " + e.getEndDate() );

			}
			/**/
			x++;
		}

		return cal.getTimeInMillis();
	}
	
	/**
	 * Returns the length of busy time in millis in the given time period
	 * 
	 * @return
	 */
	public long getLockedMillis( long from, long to, long duration ){
		long millis = 0;
		for (VEvent e : listLockedEvents()){
			if (from > e.getStartDate().getTime()  && to < e.getEndDate().getTime()){
				millis += e.getDuration();
			}
		}
		return millis;
	}
	
	public final static long SECOND 	= 1000;
	public final static long MINUTE 	= SECOND * 60;
	public final static long HOUR   	= MINUTE * 60;
	public final static long DAY    	= HOUR * 24;
	public final static long WORKDAY    = HOUR * 8;

	/**
	 * Arranges all events within the specified index range
	 * 
	 * @param start
	 * @param end
	 */
	public void arrangeEvents( int start, int end ) {
		log( "Arranging events: " + start + " - " + end );
		Date date = getEvent( start ).getStartDate();
		date = new Date( getNextFreeStart( date.getTime() ) );
		/* Iterate through only selected events from start-end */
		for (int x=0; x<eventLength(); x++){
			if (x >= start && x <= end){
				VEvent e = getEvent( x );
				log( "arrangeEvent: " + x + " " + e.getStartDate() + "  -  " + date + "  -  " + (e.getDuration()/1000*60*60) );
				/* Set start date to the next normal day and working ours */
				e.setStartDate( date );
				/* Get the amount of non-workable hours*/
				long lockedMillis = getLockedMillis( e.getStartDate().getTime(), e.getEndDate().getTime(), e.getDuration() );
				
				date = new Date( getNextFreeStart( e.getStartDate().getTime() + e.getDuration() + lockedMillis ) );
				
				/* Set end date so all events follow each other */
				e.setEndDate( date );
			}
		}
	}
	
	/**
	 * Lists a collection of events which other events cannot overlap
	 * 
	 * @return
	 */
	public Vector <VEvent> listLockedEvents(){
		return (Vector<VEvent>) busy.listEvents();
	}
	
	
	
	/**
	 * Writes the message to system out for debugging purposes
	 * 
	 * @param message the message to write out
	 */
	public void log( String message ){
		System.out.println( this.getClass().getName() + ": " + message  );
	}

}