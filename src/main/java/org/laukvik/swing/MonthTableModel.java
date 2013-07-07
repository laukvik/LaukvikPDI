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
package org.laukvik.swing;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.table.AbstractTableModel;

import org.laukvik.pdi.VCalendar;

public class MonthTableModel extends AbstractTableModel{

	private static final long serialVersionUID = -6166510656948057809L;
	private Locale locale = new Locale("no","no");
	private TimeZone zone = TimeZone.getTimeZone( locale.getCountry() );
	private DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
	private GregorianCalendar month = new GregorianCalendar(zone,locale);
	private Date selectedDate;
	private GregorianCalendar now = new GregorianCalendar(zone,locale);
	
	public MonthTableModel( VCalendar cal ){
		setMonth( now.get( Calendar.YEAR ), now.get( Calendar.MONTH ) );
	}
	
	public Class<?> getColumnClass(int columnIndex) {
		return GregorianCalendar.class;
	}
	
	public void setMonth( int selectedYear, int selectedMonth ){
		this.month = new GregorianCalendar( selectedYear, selectedMonth, 0 );
		fireTableDataChanged();
	}
	
	public String getSelectedMonthName(){
		return getMonths()[ month.get(Calendar.MONTH)+1 ];
	}
	
	public String [] getMonths(){
		return dateFormatSymbols.getMonths();
	}
	
	public String [] getDays(){
		return dateFormatSymbols.getWeekdays();
	}
	
	public String getFirstLetterUpperCase( String name ){
		return name.substring(0,1).toUpperCase() + name.substring(1);
	}
	
	public String getThreeFirstLetters( String name ){
		return name.substring(0,3).toUpperCase();
	}
	
	public String getColumnName(int column) {
		String name;
		if (month.getFirstDayOfWeek() == Calendar.SUNDAY){
			name =  getDays()[ column+1 ];
		} else {
			name = getDays()[ column ];
		}
		return getThreeFirstLetters( name );
	}
	
	public int getRowCount() {
		return 5;
	}

	public int getColumnCount() {
		return 7;
	}
	
	public void previousMonth(){
		month.add( Calendar.MONTH, -1 );
		fireTableDataChanged();
	}
	public void nextMonth(){
		month.add( Calendar.MONTH, 1 );
		fireTableDataChanged();
	}
	
	public GregorianCalendar getDateByIndex( int index ){
		/* Figure out what day the month starts with */
		int startWeekday = month.get( Calendar.DAY_OF_WEEK );
		
		int selectedYear  = month.get( Calendar.YEAR );
		int selectedMonth = month.get( Calendar.MONTH );
		int selectedDay   = index-startWeekday+1;
		
		/* Create */
		GregorianCalendar day = new GregorianCalendar( selectedYear, selectedMonth, selectedDay );
		return day;
	}
	
	public GregorianCalendar getValue( int index ){
		GregorianCalendar day = getDateByIndex( index );
		return day;
	}

	public GregorianCalendar getValueAt(int rowIndex, int columnIndex) {
		int index = columnIndex + rowIndex*7;
		return getValue( index );
	}

	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	public int getSelectedYear() {
		return month.get( Calendar.YEAR );
	}
	
}