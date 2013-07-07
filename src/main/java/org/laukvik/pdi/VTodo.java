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

/**
 * You can use the <code>VTodo</code> class to describe an action you want to do.
 * The action can have the following properties:
 * 
 * <ul> 
 * <li>Summary
 * <li>Start date 
 * <li>Due date
 * <li>Comments
 * <li>Priority
 * <li>Status
 * <li>Completed
 * <li>Unique id
 * <li>Reoccurrence
 * <li>Alarm
 * </ul>
 * 
 * @version  $Revision: 1.3 $
 * @author	Morten Laukvik
 * @link		http://morten.laukvik.no
 * @since	1.5
 */
public class VTodo extends Property{

	/** Constant used to indicate a priority which is not set */
	public final static int PRIORITY_NONE			= 0;
	/** Constant used to indicate the highest possible priority */
	public final static int PRIORITY_1			= 1;
	/** Constant used to indicate the level of priority. The most important priority
	 * is PRIORITY_1 and the least is PRIORITY_9. */
	public final static int PRIORITY_2			= 2;
	/** Constant used to indicate the level of priority. The most important priority
	 * is PRIORITY_1 and the least is PRIORITY_9. */
	public final static int PRIORITY_3			= 3;
	/** Constant used to indicate the level of priority. The most important priority
	 * is PRIORITY_1 and the least is PRIORITY_9. */
	public final static int PRIORITY_4			= 4;
	/** Constant used to indicate the level of priority. The most important priority
	 * is PRIORITY_1 and the least is PRIORITY_9. */
	public final static int PRIORITY_5			= 5;
	/** Constant used to indicate the level of priority. The most important priority
	 * is PRIORITY_1 and the least is PRIORITY_9. */
	public final static int PRIORITY_6			= 6;
	/** Constant used to indicate the level of priority. The most important priority
	 * is PRIORITY_1 and the least is PRIORITY_9. */
	public final static int PRIORITY_7			= 7;
	/** Constant used to indicate the level of priority. The most important priority
	 * is PRIORITY_1 and the least is PRIORITY_9. */
	public final static int PRIORITY_8			= 8;
	/** Constant used to indicate the least possible priority */
	public final static int PRIORITY_9			= 9;
	/** Constant used to identify VTodo objects */
	public final static String VTODO		= "VTODO";
	/** Constant used to describe a brief description */
	public final static String SUMMARY	= "SUMMARY";
	/** Constant used to have a unique identifier to make sure that two different 
	 * VTodo objects always can be separated */
	public final static String UID		= "UID";
	/** Constant used to describe the start time */
	public final static String START		= "DTSTART";
	/** Constant used to describe the time this todo was created */
	public final static String TIMESTAMP	= "DTSTAMP";
	/** Constant used to describe the due date */
	public final static String DUE		= "DUE";
	/** Constant used to describe the importance  */
	public final static String PRIORITY	= "PRIORITY";
	/** Constant used to describe the VTodo with a longer description */
	public final static String COMMENTS	= "COMMENTS";
	/** Constant used to describe recurring pattern */
	public final static String RECURRING  = "RRULE";
	/** Constant used to describe the time when this action was completed */
	public final static String COMPLETED  = "COMPLETED";
	/** Constant used to describe the status */
	public final static String STATUS		= "STATUS";
	
	/**
	 * Creates a new <code>VTodo</code> instance with the specfied parent
	 * 
	 * @param parent The parent VCalendar object
	 */
	public VTodo(Property parent) {
		super(parent);
		setName( Property.BEGIN_GROUP );
		setValue( VTODO );
		setIsGroup(true);
	}
	
	/**
	 * Creates a new <code>VTodo</code> instance with the specfied parent
	 * 
	 * @param parent The parent VCalendar object
	 * @param summary the summary to set
	 * @param priority the priority to set
	 * @param due the due date to set
	 * @param comments the comments to set
	 */
	public VTodo( Property parent, String summary, int priority, String due, String comments ){
		super(parent);
		setName( Property.BEGIN_GROUP );
		setValue( VTODO );
		setIsGroup(true);
		add( new Property( this, SUMMARY, summary) );
		add( new Property( this, PRIORITY, priority + "" ) );
		add( new Property( this, DUE, due) );
		add( new Property( this, COMMENTS, comments) );
	}

}