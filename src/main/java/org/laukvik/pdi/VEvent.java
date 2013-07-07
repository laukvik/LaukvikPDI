/*
 * LaukvikPDI is the java implementation of the Internet Mail 
 * Consortium's vCard and vCalendar.
 * 
 * Copyright (C) 2005  Morten Laukvik
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, 
 * Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.laukvik.pdi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The <code>VEvent</code> object stores details about an event.
 * 
 * @version  $Revision: 1.9 $
 * @author	Morten Laukvik
 * @link		http://morten.laukvik.no
 * @since	1.5
 */
public class VEvent extends Property implements Comparable{

	/** Constant used to identify a VEvent object */
	public final static String VEVENT			= "VEVENT";
	/** Constant used to describe the start of an event */
	public final static String START_TIME		= "DTSTART";
	/** Constant used to describe the end of an event */
	public final static String END_TIME			= "DTEND";
	/** Constant used to describe the summary of an event */
	public final static String SUMMARY			= "SUMMARY";
	/** Constant used to have a unique identifier to make sure that two different 
	 * VEvent objects always can be separated */
	public final static String UID				= "UID";
	/** Constant used to describe when the event was created */
	public final static String TIMESTAMP		= "DTSTAMP";
	/** Constant used to describe recurring pattern */
	public final static String RECURRING  		= "RRULE";
	/** Constant used to describe a description */
	public final static String DESCRIPTION		= "DESCRIPTION";
	/** Constant used to describe a URL */
	public final static String URL				= "URL";
	/** Constant used to describe the duration of an event */
	public final static String DURATION			= "DURATION";
	/** Constant used to parse data formats */
	public final static DateFormat formatter = new SimpleDateFormat( "yyyyMMdd'T'HHmmss");
	
	/**
	 * Creates a new <code>VEvent</code> instance with a set of the most commonly
	 * used properties.
	 * 
	 * @param parent the <code>VCalendar</code> which this event belongs to
	 * @param summary the summary to set
	 * @param from the from date to set
	 * @param to the to date to set
	 * @param description the description to set
	 */
	public VEvent( Property parent,String summary, GregorianCalendar from, GregorianCalendar to, String description ){
		super(parent);
		setValue( VEVENT );
		setIsGroup( true );
		add( new Property( this, START_TIME, formatter.format( from.getTime() )) );
		add( new Property( this, END_TIME, formatter.format( to.getTime() ))  );
		add( new Property( this, SUMMARY, summary) );
		add( new Property( this, DESCRIPTION, description) );
		add( new Property( this, DURATION, "PT1H") );
}

	/**
	 * Creates a new <code>VEvent</code> instance
	 * 
	 * @param parent the <code>VCalendar</code> which this event belongs to
	 */
	public VEvent(Property parent) {
		super(parent);
		setIsGroup( true );
		setValue( VEVENT );
	}

	/**
	 * Converts a Date formatted as YYYYMMDDThhmmss to a Date object
	 * 
	 * @param datestring a formatted date
	 * @return a Date
	 */
	private Date getDate( String datestring ){
		Property prop = get( datestring );
		if (prop == null ){
			return new Date();
		}
		String val = prop.getValue();
		try {
			return (Date) formatter.parse( val );
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}
	
	/**
	 * Sets the new date for this event
	 * 
	 * @param date
	 */
	public void setStartDate( Date date ){
		get( START_TIME ).setValue( formatter.format( date ) );
	}

	/**
	 * Returns the start time for this event
	 * 
	 * @return the start time
	 */
	public Date getStartDate(){
		return getDate( START_TIME );
	}
	
	/**
	 * Sets the new date for this event
	 * 
	 * @param date
	 */
	public void setEndDate( Date date ){
		get( END_TIME ).setValue( formatter.format( date ) );
	}
	
	/**
	 * Returns the end time for this event
	 * 
	 * @return the end time
	 */
	public Date getEndDate(){
		return getDate( END_TIME );
	}
	
	/**
	 * Returns the summary for this event
	 * 
	 * @return a summary
	 */
	public String getSummary(){
		return get( SUMMARY ).getValue();
	}
	
	/**
	 * Sets the summary for this event
	 * 
	 * @param summary the summary to set
	 */
	public void setSummary( String summary ){
		get( SUMMARY ).setValue( summary );
	}
	
	/**
	 * Sets the description of the event
	 * 
	 * @param description the description to set
	 */
	public void setDescription( String description ){
		get( DESCRIPTION ).setValue( description );
	}
	
	/**
	 * Returns the duration of the event in millisesconds
	 * 
	 * @return the duration in milliseconds
	 */
	public long getDuration(){
		Property p = get( DURATION );
		if (p == null){
			return 0;
		} else {
			return toMillis( p.getValue() );
		}
	}
	
	/**
	 * Converts a duration format to milliseconds
	 * 
	 * PT1H = 1 hour
	 * PT1D = 1 day 
	 * 
	 * @param duration
	 * @return
	 */
	public long toMillis( String duration ){
		if (duration.startsWith("PT")){
			
			if (duration.endsWith("H")){
				long v = Long.parseLong( duration.substring( 2, duration.length() - 1 ) );
				return v * 1000*60*60;
			} else if (duration.endsWith("D")){
				long v = Long.parseLong( duration.substring( 2, duration.length() - 1 ) );
				return v * 1000*60*60;
			} else {
				return 0;
			}
			
		} else {
			return Long.parseLong( duration );
		}
	}
	
	/**
	 * Sets the duration of the event using a textual format where
	 * 
	 * @param duration
	 */
	public void setDuration( String duration ){
		setDuration( toMillis( duration ) );
	}
	
	/**
	 * Sets the duration of the event in milliseconds
	 * 
	 * @param milliseconds the duration in milliseconds
	 */
	public void setDuration( long milliseconds ){
		Property p = get( DURATION );
		if (p == null){
			add( new Property( this, DURATION, milliseconds + "" ) );
		} else {
			p.setValue( milliseconds + "" );
		}
	}

	/**
	 * Returns the description for this event
	 * @return
	 */
	public String getDescription(){
		return get( DESCRIPTION ).getValue();
	}

	/**
	 * Not implemented yet!
	 * 
	 * TODO - Implement this method
	 * 
	 * @param o
	 * @return
	 */
	public int compareTo(Object o) {
		return 0;
	}

	/**
	 * A Comparator which lets you perform sorting on VEvents.
	 * 
	 */
	public static final Comparator DATE_ORDER  = new Comparator(){
		
		public int compare(Object o1, Object o2) {
			VEvent p1 = (VEvent) o1;
			VEvent p2 = (VEvent) o2;
			return p1.getStartDate().compareTo( p2.getStartDate() );
		}

	};
	
	public void log( String message ){
		System.out.println( this.getClass().getName() + ": " + message  );
	}

}