package org.laukvik.pdi.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class BinaryViewer extends JFrame {

	private static final long serialVersionUID = -2285786811925893168L;
	private JEditorPane editor = new JEditorPane();
	
	public BinaryViewer(){
		editor = new JEditorPane();
		JScrollPane scroll = new JScrollPane( editor );
		add( scroll );
		setVisible(true);
		setSize(500,500);
	}
	
	public void open( File file ) throws IOException{
		setBytes( getBytesFromFile( file ) );
	}
	
	public void setBytes( byte [] bytes){
		String s = new String(bytes);
		s = s.replaceAll( "\n", "[ LINEFEED ]\n" );
		s = s.replaceAll( "\r", "[ RETURN ]" );
		editor.setText( s );
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
	
	public static void main(String[] args) {
		BinaryViewer viewer = new BinaryViewer();
		try {
			viewer.open( new File("/Users/morten/Home.ics") );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}