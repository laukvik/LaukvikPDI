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
import org.laukvik.pdi.VCard;

/**
 * This example shows how to read a VCard file and extract som simple properties 
 * from it.
 * <P>
 * 
 * @version  $Revision: 1.3 $
 * @author	Morten Laukvik
 * @link		http://morten.laukvik.no
 * @since	1.5
 */
public class VCardExample {
	
	public static void readCard(){
		// Specify file and directory
		String filename = "Sun_Microsystems.vcf";
		String directory = System.getProperty("user.home");
		try {
			File file = new File(directory, filename);
			// Get a VCard object from file
			VCard card = (VCard) PDIManager.read( file );

			// Print out first email address found
			System.out.println( card.get( VCard.FULLNAME ) );
			
			// List all phone numbers
			System.out.println( card.list( VCard.TELEPHONE ) );

			// Print out all domestic phone numbers
			System.out.println( card.list( VCard.TELEPHONE, VCard.DOMESTIC ) );
			
			// Print out all international properties
			System.out.println( card.list( VCard.INTERNATIONAL ) );

		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	public static void writeCard(){
		// Create an empty VCard
		VCard card = new VCard();
		card.add( VCard.VERSION, "2.0" );
		// Set the name
		card.add( VCard.FULLNAME , "Sun Microsystems, Inc." );
		card.add( VCard.ORGANISATION , "Sun Microsystems, Inc." );
		card.add( VCard.ADDRESS , "4150 Network Circle;Santa Clara, CA 95054" );
		card.add( VCard.TELEPHONE , "1-800-555-9", VCard.DOMESTIC );
		card.add( VCard.TELEPHONE , "1-650-960-1300", VCard.INTERNATIONAL );

		// Specify file and directory
		String filename = "Sun_Microsystems.vcf";
		String directory = System.getProperty("user.home");
		File file = new File( directory, filename );
		try {
			PDIManager.save( card, file );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		VCardExample.writeCard();
		VCardExample.readCard();
	}
	
}