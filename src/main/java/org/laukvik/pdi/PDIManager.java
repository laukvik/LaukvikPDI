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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * <code>PDIManager</code> lets you read and write PDI from the filesystem or from 
 * an Internett address. <code>PDIManager</code> can also import and export binary
 * files and encode/decode them into Property values.
 * <P>
 * Lets say you want to read a VCard from the filesystem with the filename 
 * "Morten.vcf" - then you would do that like this:
 * <P>
 * <code>Property card = PDIManager.parse( new File("Morten.vcf") );</code>
 * <P>
 * If you want to read a VCard from the Internet - then you would do that like this:
 * <P>
 * <code>
 * URL url = new URL("http://www.yourhomepage.com/Morten.vcf");<BR>
 * Property card = PDIManager.parse( url );
 * </code>
 * 
 * 
 * @version  $Revision: 1.6 $
 * @author	Morten Laukvik
 * @link		http://morten.laukvik.no
 * @since	1.5
 */
public class PDIManager{

	/** Constant representing carriage return */
	private final static char CR = (char) 10;
	/** Constant representing linefeeds */
	private final static char LF = (char) 13;
	/** Consant representing carriage return directly followed by linefeed*/
	final static char [] CRLF = new char [] {CR, LF};
	
	  /**
	   * Loads bytes from the given input stream until the end of stream
	   * is reached.  Bytes are read in at the supplied <code>chunkSize</code>
	   * rate.
	   *
	   * @param stream to read the bytes from
	   * @return bytes read from the stream
	   * @exception java.io.IOException reports IO failures
	   */
	  private static byte[] loadBytesFromStream( InputStream in, int chunkSize )
	      throws IOException {
	    if( chunkSize < 1 ){
	      chunkSize = 1024;
	    }
	    int count;
	    ByteArrayOutputStream bo = new ByteArrayOutputStream();
	    byte[] b = new byte[chunkSize];
	    try {
	      while( ( count = in.read( b, 0, chunkSize ) ) > 0 )
	        bo.write( b, 0, count );
	      byte[] thebytes = bo.toByteArray();
	      return thebytes;
	    }
	    finally {
	      bo.close();
	      bo = null;
	    }
	}

	/**
	 * Returns the contents of the specified URL as a String
	 * 
	 * @param url the URL to get the contents of
	 * @return the contents of the URL
	 */
	private static String getString( URL url ){
		try {
			return new String( getBytesFromURL(url) );
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Returns the contents of the specified URL as a byte array
	 * 
	 * @param url the URL the get the bytes from
	 * @return the contents of the URL as a byte array
	 * @throws IOException If the URL cant be read successfully
	 */
	private static byte[] getBytesFromURL( URL url ) throws IOException {
		InputStream is = url.openStream();
		return loadBytesFromStream( is, 1024 );
	}
	
	/**
	 * Returns the contents of the specified file as a byte array
	 * 
	 * @param file
	 * @return a byte array of the contents
	 * @throws IOException If the File cant be read successfully
	 */
	private static byte[] getBytesFromFile( File file ) throws IOException {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        		throw new IOException("Too large file "+file.getName());
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0)
        {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

	/**
	 * Saves the <code>Property</code> to the specified file
	 * 
	 * @param property the Property to save
	 * @param file the file to save the Property as
	 * @throws Exception If the File couldn't be written successfully
	 */
	public static void save( Property property, File file ) throws Exception{
		FileOutputStream out = new FileOutputStream(file);
		property.writeOut( out );
		out.flush();
		out.close();
	}
	
	/**
	 * Returns the contents of the specified file as a String 
	 * 
	 * @param file The file to use
	 * @return the contents of the file
	 * @throws Exception If the file coulnd't be succefully read
	 */
	private static String getString( File file ) throws Exception{
		String tmp = new String( getBytesFromFile( file ) );
		return tmp;
	}
	
	/**
	 * Reads and parses the specified File and returns a Property object
	 * 
	 * @param file the file to be read
	 * @return the a Property object with the data found in the file
	 * @throws Exception If the file couldn't successfully be read and parsed
	 */
	public static Property read( File file ) throws Exception{
		return PDIManager.parse( getString( file ) );
	}
	
	/**
	 * Reads and parses the specified URL and returns a Property object
	 * 
	 * @param url the url to read
	 * @return a Property object with the data found in the URL
	 * @throws Exception If the file couldn't be successfully be read and parsed
	 */
	public static Property read( URL url ) throws Exception{
		return PDIManager.parse( getString( url ) );
	}
	
	/**
	 * Converts the specified String into a parser friendly format
	 * <p>
	 * Our parser is based on the simple principle that each line must
	 * be a complete Property to work. Therefore we must work on the
	 * data string to search and replace certain characters to make
	 * each line contain all data.
	 * 
	 * 
	 * @param data The String to be converted 
	 * @return a parser friendly string
	 */
	private static String toSingleLineProperty( String data ){
		/*
		 * Internal notes
		 * DECIMAL 00 HEX  0    null
		 * DECIMAL 10 HEX  A    New line
		 * DECIMAL 13 HEX  D    Carriage return 
		 */

		/* Trim string to avoid empty rows of data */
		data = data.trim();
		/*
		 * For unknown reasons Addressbook application sometimes
		 * inserts a null character between all characters. Remove
		 * all nulls found.
		 **/
		data = data.replaceAll( "\\x00", "" );

		/* 
		 * When Property value is bigger than approximately 80 
		 * characters with, the value spans across multiple
		 * lines.
		 * 
		 * */
		data = data.replaceAll( "\\n +", "" );

		/* 
		 * Some applications export an extra line between each line
		 * of Property. Here we remove all newline characters
		 * that occurs right after another two times or more.
		 * 
		 * From:
		 * FN:Morten Laukvik
		 * (empty line)
		 * URL:http://morten.laukvik.no
		 * 
		 * Result:
		 * FN:Morten Laukvik
		 * URL:http://morten.laukvik.no
		 * 
		 */
		data = data.replaceAll( "\\n{2,}+", "" );

		

		/* 
		 * Remove all newlines after a semicolon to make each 
		 * line a complete Property. E.g
		 * 
		 * From:
		 * PHOTO;BASE64:
		 * TU0AKgAADvCAP+BQOCQWDQeEQmFQuGQ2HQ+IRGJROKRWLR
		 * 
		 * Result:
		 * PHOTO;BASE64:TU0AKgAADvCAP+BQOCQWDQeEQmFQuGQ2HQ+
		 * 
		 * */
		data = data.replaceAll( ":\\r", ":" );

		/* Return the resulting string */
		return data;
	}
	
	/**
	 * Tests whether the String data is a PDI file
	 * 
	 * @param data the data to test
	 * @return <code>true</code> if the String is PDF formatted; <code>false</code> otherwise
	 */
	public static boolean isPDI( String data ){
		return (data.indexOf("BEGIN:") > -1);
	}

	/**
	 * Parses a String with the specified parent Property as its parent
	 * 
	 * @param parent the parent Property
	 * @param data the string to be parsed
	 * @return a Property object with the data found in the string
	 * @throws PDIException if the string couldn't be parsed successfully
	 */
	private static Property parse( Property parent, String data ) throws PDIException{
		if (data == null || data.length() == 0){
			return null;
		}
		
		// Searcg for the property name/value delimiter
		int indexSplitPoint = data.indexOf(":");
		// Return null if no delimiter was found
		if (indexSplitPoint < 0){
			return null;
		}
		
		/* Parse and get the property name and value */
		String key = data.substring( 0, indexSplitPoint );
		String val = data.substring( indexSplitPoint + 1 );


		// Create an empty property object and set the value
		Property prop;
		if (key.equalsIgnoreCase( Property.BEGIN_GROUP )){
			prop = getObjectByValue( val, parent );
			prop.setIsGroup(true);
			prop.setName( Property.BEGIN_GROUP );
		} else {
			prop = new Property(parent);
			prop.setIsGroup(false);
		}
		prop.setValue( val );
		
		// Check if parameters exists
		int indexFirstParam = key.indexOf(";");
		if (indexFirstParam < 0){
			// We found no parameters. No parameter parsing needed. Use
			// the whole property part as name
			prop.setName( key );
		} else {
			// We found one or more parameters. The property name needs
			// to be parsed
			prop.setName( key.substring( 0, indexFirstParam ) );
			String params [] = key.split( ";" );
			if (params.length > 1){
				for (int n=1; n<params.length; n++){
					String paramNameValue [] = params[ n ].split( "=" );
					if (paramNameValue.length >= 2){
						prop.listParameters().add( new Parameter( paramNameValue[ 0 ], paramNameValue[ 1 ] ) );
						
					} else if (paramNameValue.length == 1){
						prop.listParameters().add( new Parameter( paramNameValue[ 0 ], "" ) );			
					}
				}	
			}
		}
		return prop;
	}
	
	/**
	 * Encodes the specified data with the proper charset to be able to read
	 * non-english charaters
	 * 
	 * @param data
	 * @param encoding
	 * @return
	 */
	private static String encode( String data, String encoding ){
		try {
			return new String( data.getBytes(), encoding );
		} catch (UnsupportedEncodingException e) {
			return data;
		}
	}
	
	/**
	 * Parses the specified String and returns a Property object
	 * 
	 * @param data the data to parse
	 * @return a Property object found in the string
	 * @throws Exception If the string couldn't be parsed successfully
	 * @throws PDIException If the string couldn't be parsed successfully
	 */
	private static Property parse( String data ) throws Exception, PDIException {
		data = encode( data, "UTF-8" );
		// Convert string to a parser friendly string
		data = toSingleLineProperty( data );
		if (data == null){
			throw new NullPointerException();
		} else if (!isPDI(data)){
			throw new PDIException("Not a PDI file");
		}
		// Create an empty root property to hold all data
		Property root = new Property( null );
		// Create a property to use as a working property which
		// is always the last leaf in the property tree
		Property current = root;
		current.setParent( root );
		
		// Split the data string into single rows
		String rows [] = data.split( "\\n" );
		int level = 0;
		Property prop;
		for (int index=0; index<rows.length; index++)
		{
			String row = rows[index];
			if (row.length() == 0){
				prop = null;
			} else if (row.charAt( row.length()-1 ) == PDIManager.LF ){
				prop = PDIManager.parse( current, row.substring( 0, row.length()-1 ) );
			} else {
				prop = PDIManager.parse( current, row );
			}

			if (prop == null){
				// Do nothing. Bug???
			} else if (prop.getName().equalsIgnoreCase( Property.BEGIN_GROUP )){
				
				// A new subentry
				if (level == 0){
					/* Root element here */
					root = getObjectByValue( prop.getValue(), null );
					root.setName( prop.getName() );
					root.setValue( prop.getValue() );
					current = root;
				} else {
					current.add( prop );
					current = prop;
				}
				level++;
			} else if (prop.getName().equalsIgnoreCase( Property.END_GROUP )){
				// The end of a subentry
				current = current.getParent();
				level--;
			} else {
				// Properties in the same entry
				current.add( prop );
			}
		}
		// Set parent to be null or root. For some reason the above
		// code will set this parent to be something else
		root.setParent(null);
		return root;
	}
	
	/**
	 * Returns an object based on the specified Property name 
	 * 
	 * @param propertyName the name of the Property to use
	 * @param parent the parent Property object
	 * @return a sub class of Property or a Property object
	 */
	public static Property getObjectByValue(String propertyName, Property parent ) {
		if (propertyName == null){
			return new Property(parent);
		} else if (propertyName.equalsIgnoreCase( VCalendar.VCALENDAR  )){
			return new VCalendar(parent);
		} else if (propertyName.equalsIgnoreCase( VCard.VCARD )){
			return new VCard(parent);
		} else if (propertyName.equalsIgnoreCase( VEvent.VEVENT )){
			return new VEvent(parent);
		} else if (propertyName.equalsIgnoreCase( VTodo.VTODO )){
			return new VTodo(parent);
		} else if (propertyName.equalsIgnoreCase( VAlarm.VALARM )){
			return new VAlarm(parent);
		} else if (propertyName.equalsIgnoreCase( VJournal.VJOURNAL )){
			return new VTodo(parent);
		} else if (propertyName.equalsIgnoreCase( VTimeZone.VTIMEZONE )){
			return new VTimeZone(parent);
		} else if (propertyName.equalsIgnoreCase( VFreeBusy.VFREEBUSY )){
			return new VTodo(parent);
		} else {
			return new Property(parent);
		}
	}

	/**
	 * Decodes the BASE64 encoded value of the property and saves the results in
	 * the specified <code>file</code>
	 * 
	 * @param property the property to use
	 * @param file the file to use
	 * @throws Exception If the value couldn't be successfully written to the file
	 */
	public static void writeBinaryValueToFile( Property property, File file ) throws Exception{
        // Convert a byte array to base64 string
    		String s = property.getValue();
        byte[] buf = new byte[]{0x12, 0x23};
        // Convert base64 string to a byte array
        buf = new sun.misc.BASE64Decoder().decodeBuffer(s);
        // Open an output stream and write the buffer
        FileOutputStream out = new FileOutputStream( file );
        out.write( buf );
        out.close();
	}
	
	/**
	 * Reads the specified file and encodes the contents using BASE64 encoding
	 * and sets encoded results as the value of the Property
	 * 
	 * @param property The Property to put the encoded results in
	 * @param file The file to read
	 * @throws Exception If the value couldn't be successfully read into the Property
	 */
	public static void readBinaryValueFromFile( Property property, File file ) throws Exception{
        String s = new sun.misc.BASE64Encoder().encode( getBytesFromFile( file ) );
        property.setValue( s );
	}
	
}