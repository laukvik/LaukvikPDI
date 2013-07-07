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

import java.io.IOException;
import java.io.OutputStream;

/**
 * Parameter's are used to further describe a Property object when the Property name
 * alone is not enough. 
 * <P>
 * Let's say you have to {@link Property} objects which describes an EMAIL and
 * one of them is your WORK EMAIL and the other is your HOME EMAIL.
 * <P>
 * For the work Email you would do like this
 * <BR>
 * new Parameter("type","WORK");
 * <P>
 * For the home Email you would do like this
 * <BR>
 * new Parameter("type","EMAIL");
 * <P>
 * If you want to use several types in one go you would do it like this:
 * <BR>
 * new Parameter("type","EMAIL","WORK","PREF");
 * 
 * @version  $Revision: 1.3 $
 * @author	Morten Laukvik
 * @link		http://morten.laukvik.no
 * @since	1.5
 */
public class Parameter {

	private String name;
	private String [] values = {};
	
	/** A constant to use when separating the name and the value of a Parameter */
	public final static String NAME_AND_VALUE_SEPARATOR = "=";
	
	/** A constant to use when separating multiple values in a Parameter */
	public final static String SUBVALUE_SEPARATOR = ",";
	
	
	/**
	 * Creates a new <code>Parameter</code> instance with the specified name. 
	 * 
	 * @param name The name to set
	 */
	public Parameter( String name ){
		this.name = name;
	}

	/**
	 * Creates a new <code>Parameter</code> instance with the specified name and 
	 * value 
	 * 
	 * @param name The name to set
	 * @param values The values to set
	 */
	public Parameter( String name, String... values ){
		this.name = name;
		if (values == null){
			// Only to avoid nullpointexception in split
			this.values = null;
		} else if ( values.length == 1){
			// When using only one parameter we check if incoming
			// values contains subvalues. If found we split the
			// String into seperate values
			if (values[0].indexOf( SUBVALUE_SEPARATOR ) > -1){
				// We found subvalues
				this.values = values[0].split( SUBVALUE_SEPARATOR );
			} else {
				// No subvalues
				this.values = values;
			}
		} else {
			// Calling with multiple values
			this.values = values;
		}
	}

	/**
	 * Returns the name of this Parameter
	 * 
	 * @return the name of this Parameter
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the value of this Parameter
	 * 
	 * @return the value of this Parameter
	 */
	public String getValue() {
		if (values == null || values.length == 0){
			return null;
		} else {
			return values[ 0 ];
		}
	}
	
	/**
	 * Returns an Array of all values
	 * 
	 * @return
	 */
	public String [] getMultipleValues(){
		return values;
	}
	
	/**
	 * Returns whether this Parameter has multiple values
	 * 
	 * @return <code>true</code> if multiple values are found and <code>false</code>
	 * if not
	 */
	public boolean hasMultipleValues(){
		if (values == null){
			return false;
		} else {
			return values.length > 1;
		}
	}
	
	/**
	 * Sets one or more values
	 * 
	 * @param values the values to set
	 */
	public void setValue( String... values ) {
		this.values = values;
	}
	
	/**
	 * Returns the name and value of this Parameter or only the name if the value
	 * is set to null or the length of the value is zero.
	 * 
	 */
	public String toString(){
		/* Value is optional. If no value is set, only return name */
		if (values == null || values.length == 0){
			return name;
		} else if (values.length == 1){
			return name + NAME_AND_VALUE_SEPARATOR + values[0];
		} else {
			String data = "";
			int n = 0;
			for (String val : values ){
				if (n == 0){
					data = name + NAME_AND_VALUE_SEPARATOR + val;
				} else {
					data += Parameter.SUBVALUE_SEPARATOR + val;
				}
				n++;
			}
			return data;
		}
	}
	
	/**
	 * Writes the contents of this Parameter to the specified {@link OutputStream}
	 *  
	 * @param out the OutputStream to use
	 * @throws IOException If the Parameter can't be written to the OutputStream
	 */
	public void writeOut( OutputStream out ) throws IOException{
		out.write( toString().getBytes() );
	}
	
}