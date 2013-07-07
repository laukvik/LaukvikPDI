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
package org.laukvik.pdi.swing;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import org.laukvik.swing.utils.RecentItems;

/**
 * 
 * 
 * @version  $Revision: 1.1 $
 * @author	Morten Laukvik
 * @link		http://morten.laukvik.no
 * @since	1.5
 */
public class Helper {

	public static void setCrossPlatformProperties(){
	    	if (System.getProperty( "os.name" ).startsWith("Mac") ){
			System.setProperty( "apple.laf.useScreenMenuBar", "true" );
		}
		try{
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch (Exception e){
		}
	}
	
	public static File getRecentFile(){
		String home = System.getProperty( "user.home" );
		return new File( home, ".orglaukvikpdiviewer");
	}
	
	public static void addToRecentItemsList( String filename ){
		RecentItems recentItems = new RecentItems( Helper.getRecentFile() );
		recentItems.add( filename );
	}
	
	public static Vector <String> getRecentItemsList(){
		try {
			RecentItems recentItems = new RecentItems( Helper.getRecentFile() );
			return recentItems.getItems();
		} catch (Exception e) {
			e.printStackTrace();
			return new Vector <String>();
		}
	}
	
	public static void clearRecentItemsList(){
		try {
			RecentItems recentItems = new RecentItems( Helper.getRecentFile() );
			recentItems.removeAll();
			recentItems.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
    /**
     * Returns a cross-platform keystroke that enables the platform behave natively.
     * This is usually a problem for Mac people who uses the Apple button like
     * Windows people use the Control button.
     * 
     * @param keyevent the keyevent you want the keystroke for
     * @return a cross-platfrom compatible keystroke
     */
    public static KeyStroke getKeystroke( int keyevent ){ 	
		return KeyStroke.getKeyStroke( keyevent , Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    }
    
	/**
	 * Returns an icon object with the given filename in the directory. The method
	 * will only look inside this directory for images
	 * <code>/org/laukvik/pdi/swing/icons/*.gif</code>
	 * 
	 * @param filename The name of the icon file
	 * @return an icon object
	 */
	public static Icon getIcon( String filename ){
		return new javax.swing.ImageIcon( Viewer.class.getResource( "icons/" + filename ));
	}
	
	private static String getPackageName(){
		return Helper.class.getPackage().getName();
	}
	
	public static ResourceBundle getLanguage(){
		Locale locale = Locale.getDefault();
		ResourceBundle messages = ResourceBundle.getBundle( Helper.getPackageName() + ".messages", locale );
		return messages;
	}
	
	public static Image getImage( String filename ){
		Image img = null;
        File file = new File( "icons/" + filename );
        try {
			img = ImageIO.read(file);
			return img;
		} catch (IOException e) {
			return null;
		}
	}
	
}
