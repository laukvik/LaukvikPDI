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

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * The <code>VCalendar</code> object typically holds several <code>VEvent</code> and 
 * <code>VTodo</code> which together produces the features of a calendar. 
 * 
 * @version  $Revision: 1.6 $
 * @author	Morten Laukvik
 * @param <T>
 * @link		http://morten.laukvik.no
 * @since	1.5
 */
public class VCalendar<T> extends Property{

	/** Constant used to identify a VCalendar object */
	public final static String VCALENDAR			= "VCALENDAR";
	
	/** Constant used to identify which software created this object */
	public final static String PRODUCT_IDENTIFIER	= "PRODID";
	
	public final static String VERSION				= "VERSION";
	public final static String CALSCALE				= "CALSCALE";
	public final static String METHOD				= "METHOD";
	


	/**
	 * Creates a new <code>VCalendar</code> instance
	 *
	 */
	public VCalendar(){
		super(null);
		setIsGroup(true);
		setName( Property.BEGIN_GROUP );
		setValue( VCALENDAR );
	}
	
	/**
	 * Creates a new <code>VCalendar</code> instance with the specfied parent
	 *
	 */
	public VCalendar( Property parent ){
		super(parent);
		setIsGroup(true);
		setName( Property.BEGIN_GROUP );
		setValue( VCALENDAR );
	}
	
	/**
	 * Adds a <code>VEvent</code> object
	 * 
	 * @param event the VEvent to add
	 */
	public void addEvent( VEvent event ){
		add( event );
	}
	
	/**
	 * Adds a <code>VTodo</code> object
	 * 
	 * @param todo the VTodo to add
	 */
	public void addTodo( VTodo todo ){
		add( todo );
	}
	
	/**
	 * Returns a Collection of VEvent objects. This method is a convenience method 
	 * which performs the same as <code>listGroups( VEvent.VEVENT )</code>
	 * 
	 * @return a collection of VEvent
	 */
	public Collection <Property> listEvents(){
		return listGroups( VEvent.VEVENT );
	}
	
	/**
	 * Returns a Collection of VEvent objects which is in between the specified
	 * <code>startDate</code> and <code>endDate</code>.
	 * 
	 * @param startDate
	 * @param endDate
	 * 
	 * @return a collection of VEvent
	 */
	
	@SuppressWarnings("unchecked")
	public Vector <VEvent> listEvents( Date startDate, Date endDate ){
		/* Create an empty vector to contain the results */
		Vector <VEvent> items = new Vector <VEvent>();
		/* List all VEvent objects */
		Collection <Property> events = listEvents();
		/* Sort the result by date */
		Collections.sort( (List<T>) events, VEvent.DATE_ORDER );
		/* Iterate through all events and test the dates */
		for (Property prop : events){
			VEvent event = (VEvent) prop;
			if (startDate == null || event.getStartDate() == null || event.getStartDate().compareTo( startDate ) >= 0){
				if (endDate == null || event.getEndDate() == null || event.getEndDate().compareTo( endDate ) <= 0){
					items.add( event );
				}
			}
		}
		return items;
	}

	/**
	 * Returns a Collection of VTodo objects. This method is a convenience method 
	 * which performs the same as <code>listGroups( VTodo.VTODO )</code>
	 * 
	 * @return
	 */
	public Collection <Property> listTodos(){
		return listGroups( VTodo.VTODO );
	}
	
	/**
	 * Returns a Collection of VAlarm objects. This method is a convenience method 
	 * which performs the same as <code>listGroups( VAlarm.VALARM )</code>
	 * 
	 * @return
	 */
	public Collection <Property> listAlarms(){
		return listGroups( VAlarm.VALARM );
	}
	
	/**
	 * Returns a Collection of VTimeZone objects. This method is a convenience method 
	 * which performs the same as <code>listGroups( VTimeZone.VTIMEZONE )</code>
	 * 
	 * @return
	 */
	public Collection <Property> listTimeZones(){
		return listGroups( VTimeZone.VTIMEZONE );
	}
	
}