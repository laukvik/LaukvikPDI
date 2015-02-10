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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.List;

/**
 * The <code>VCalendar</code> object typically holds several <code>VEvent</code>
 * and <code>VTodo</code> which together produces the features of a calendar.
 *
 * @version $Revision: 1.6 $
 * @author	Morten Laukvik
 * @param <T>
 * @link		http://morten.laukvik.no
 * @since	1.5
 */
public class VCalendar<T> extends VProperty {

    /**
     * Constant used to identify a VCalendar object
     */
    public final static String VCALENDAR = "VCALENDAR";

    /**
     * Constant used to identify which software created this object
     */
    public final static String PRODUCT_IDENTIFIER = "PRODID";

    public final static String VERSION = "VERSION";
    public final static String CALSCALE = "CALSCALE";
    public final static String METHOD = "METHOD";

    /**
     * Creates a new <code>VCalendar</code> instance
     *
     */
    public VCalendar() {
        super(null);
        setIsGroup(true);
        setName(VProperty.BEGIN_GROUP);
        setValue(VCALENDAR);
    }

    /**
     * Creates a new <code>VCalendar</code> instance with the specfied parent
     *
     */
    public VCalendar(VProperty parent) {
        super(parent);
        setIsGroup(true);
        setName(VProperty.BEGIN_GROUP);
        setValue(VCALENDAR);
    }

    /**
     * Adds a <code>VEvent</code> object
     *
     * @param event the VEvent to add
     */
    public void addEvent(VEvent event) {
        add(event);
    }

    /**
     * Adds a <code>VTodo</code> object
     *
     * @param todo the VTodo to add
     */
    public void addTodo(VTodo todo) {
        add(todo);
    }

    public void addTodo(String summary, int priority, Date due, String comments) {
        VTodo todo = new VTodo(this, summary, priority, due, comments);
        todo.setParent(this);
        add(todo);
    }

    /**
     * Returns a Collection of VEvent objects. This method is a convenience
     * method which performs the same as
     * <code>listGroups( VEvent.VEVENT )</code>
     *
     * @return a collection of VEvent
     */
    public Collection<VProperty> listEvents() {
        return listGroups(VEvent.VEVENT);
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
    public List<VEvent> listEvents(Date startDate, Date endDate) {
        /* Create an empty List to contain the results */
        List<VEvent> items = new ArrayList<VEvent>();
        /* List all VEvent objects */
        Collection<VProperty> events = listEvents();
        /* Sort the result by date */
        Collections.sort((List<T>) events, VEvent.DATE_ORDER);
        /* Iterate through all events and test the dates */
        for (VProperty prop : events) {
            VEvent event = (VEvent) prop;
            if (startDate == null || event.getStartDate() == null || event.getStartDate().compareTo(startDate) >= 0) {
                if (endDate == null || event.getEndDate() == null || event.getEndDate().compareTo(endDate) <= 0) {
                    items.add(event);
                }
            }
        }
        return items;
    }

    /**
     * Returns a Collection of VTodo objects. This method is a convenience
     * method which performs the same as <code>listGroups( VTodo.VTODO )</code>
     *
     * @return
     */
    public Collection<VProperty> listTodos() {
        return listGroups(VTodo.VTODO);
    }

    /**
     * Returns a Collection of VAlarm objects. This method is a convenience
     * method which performs the same as
     * <code>listGroups( VAlarm.VALARM )</code>
     *
     * @return
     */
    public Collection<VProperty> listAlarms() {
        return listGroups(VAlarm.VALARM);
    }

    /**
     * Returns a Collection of VTimeZone objects. This method is a convenience
     * method which performs the same as
     * <code>listGroups( VTimeZone.VTIMEZONE )</code>
     *
     * @return
     */
    public Collection<VProperty> listTimeZones() {
        return listGroups(VTimeZone.VTIMEZONE);
    }

    public void addEvent(String summary, Date from, Date to, String details) {
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(from);

        GregorianCalendar gcal2 = new GregorianCalendar();
        gcal2.setTime(to);
        VEvent evt = new VEvent(this, summary, gcal, gcal2, details);
        addEvent(evt);
    }

}
