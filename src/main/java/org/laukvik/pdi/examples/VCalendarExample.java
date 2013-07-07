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
package org.laukvik.pdi.examples;

import java.io.File;
import org.laukvik.pdi.PDIManager;
import org.laukvik.pdi.VCalendar;

/**
 * This example shows how to read a VCalendar file and extract som simple 
 * information from it.
 * <P>
 * To have this example work you need a VCalendar file. It typically have the file
 * extension .ics. If you dont have one, this example will not work.
 * 
 * @version  $Revision: 1.2 $
 * @author	Morten Laukvik
 * @link		http://morten.laukvik.no
 * @since	1.5
 */
public class VCalendarExample {

	public static void main(String[] args){
		// Specify file and directory
		String filename = "Home.ics";
		String directory = System.getProperty( "user.home" );
		try {
			// Read the VCalendar file with PDIManager
			VCalendar cal = (VCalendar) PDIManager.read( new File( directory, filename ) );
			
			System.out.println(  cal.get( VCalendar.VERSION ).getRoot() );
			System.out.println(  cal.get( VCalendar.VERSION ).getLevel() );
			
			// Get four properties from the VCalendar object and write out there
			// values to default outputstream
			System.out.println(  cal.get( VCalendar.VERSION ).getValue()  );
			System.out.println(  cal.get( VCalendar.METHOD ).getValue()  );
			System.out.println(  cal.get( VCalendar.CALSCALE ).getValue()  );
			System.out.println(  cal.get( VCalendar.PRODUCT_IDENTIFIER ).getValue()  );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}