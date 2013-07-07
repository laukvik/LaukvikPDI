package org.laukvik.project;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import org.laukvik.pdi.swing.Helper;

public class Menu extends JMenuBar {

	private static final long serialVersionUID = 6258216220891675970L;
	public final static String OPENFILE_ACTION = "file_open";
	public final static String NEWFILE_ACTION = "file_new";
	public final static String QUIT_ACTION = "quit";
	public final static String NEW_EVENT_ACTION = "new_event";
	public final static String SAVEAS_ACTION = "saveas";
	public static final String SAVE_ACTION = "save";
	public final static String PRINT_ACTION = "print";
	public final static String CUT_ACTION = "cut";
	public final static String COPY_ACTION = "copy";
	public final static String PASTE_ACTION = "paste";
	public final static String DELETE_ACTION = "delete";
	public final static String ARRANGE_EVENTS_ACTION = "arrange_events";
	public final static String PREFERENCES_ACTION = "preferences";
	public final static String ABOUT_ACTION = "about";
	
	

	public Menu( ActionListener listener ) {
		JMenu fileMenu = new JMenu( "File" );
		JMenuItem newItem = new JMenuItem( "New" );
		newItem.setIcon( Helper.getIcon("new.gif") );
		newItem.setAccelerator( Helper.getKeystroke( KeyEvent.VK_N ) );
        newItem.addActionListener( listener );
        newItem.setActionCommand( NEWFILE_ACTION );
		JMenuItem openItem = new JMenuItem( "Open" );
		openItem.setActionCommand( OPENFILE_ACTION );
		openItem.setIcon( Helper.getIcon("open.gif") );
		openItem.setAccelerator( Helper.getKeystroke( KeyEvent.VK_O ) );
		openItem.addActionListener( listener );
		JMenuItem saveItem = new JMenuItem( "Save" );
		saveItem.setIcon( Helper.getIcon("save.gif") );
		saveItem.setAccelerator( Helper.getKeystroke( KeyEvent.VK_S ) );
		saveItem.addActionListener( listener );
		JMenuItem saveAsItem = new JMenuItem( "Save as" );
		saveAsItem.setIcon( Helper.getIcon("save.gif") );
		saveAsItem.addActionListener( listener );
		saveAsItem.setActionCommand( SAVEAS_ACTION );
		
		JMenuItem printItem = new JMenuItem( "Print" );
		printItem.setIcon( Helper.getIcon("print.gif") );
		printItem.addActionListener( listener );
		printItem.setActionCommand( PRINT_ACTION );
		printItem.setAccelerator( Helper.getKeystroke( KeyEvent.VK_P ) );
		
		JMenuItem quitItem = new JMenuItem( "Quit" );
		quitItem.setActionCommand( QUIT_ACTION );
		quitItem.setAccelerator( Helper.getKeystroke( KeyEvent.VK_Q ) );
		quitItem.addActionListener( listener );
		fileMenu.add( newItem );
		fileMenu.add( openItem );
		fileMenu.add( saveItem );
		fileMenu.add( saveAsItem );
		fileMenu.addSeparator();
		fileMenu.add( printItem );
		fileMenu.addSeparator();
		fileMenu.add( quitItem );
		
		JMenu editMenu = new JMenu( "Edit" );
		JMenuItem cut = new JMenuItem( "Cut", Helper.getIcon("cut.gif") );
		cut.setActionCommand( CUT_ACTION );
		cut.addActionListener( listener );
		cut.setAccelerator( Helper.getKeystroke( KeyEvent.VK_X ) );
		JMenuItem copy = new JMenuItem( "Copy", Helper.getIcon("copy.gif") );
		copy.addActionListener( listener );
		copy.setActionCommand( COPY_ACTION );
		copy.setAccelerator( Helper.getKeystroke( KeyEvent.VK_C ) );
		JMenuItem paste = new JMenuItem( "Paste", Helper.getIcon("paste.gif") );
		paste.addActionListener( listener );
		paste.setActionCommand( PASTE_ACTION );
		paste.setAccelerator( Helper.getKeystroke( KeyEvent.VK_V ) );
		JMenuItem delete = new JMenuItem( "Delete" );
		delete.addActionListener( listener );
		delete.setActionCommand( DELETE_ACTION );
		delete.setAccelerator( Helper.getKeystroke( KeyEvent.VK_BACK_SPACE ) );
		JMenuItem preferences = new JMenuItem( "Preferences" );
		preferences.addActionListener( listener );
		preferences.setActionCommand( PREFERENCES_ACTION );
		preferences.setAccelerator( Helper.getKeystroke( KeyEvent.VK_COMMA ) );
		editMenu.add( cut );
		editMenu.add( copy );
		editMenu.add( paste );
		editMenu.addSeparator();
		editMenu.add( delete );
		editMenu.addSeparator();
		editMenu.add( preferences );
		
		JMenu eventMenu = new JMenu( "Event" );
		
		JMenuItem newEvent = new JMenuItem( "New Event"  );
		newEvent.setActionCommand( NEW_EVENT_ACTION );
		newEvent.addActionListener( listener );
		newEvent.setAccelerator( Helper.getKeystroke( KeyEvent.VK_E ) );
		eventMenu.add( newEvent );
		eventMenu.addSeparator();
		JMenuItem arrangeEvents = new JMenuItem( "Arrange" );
		arrangeEvents.setActionCommand( ARRANGE_EVENTS_ACTION );
		arrangeEvents.addActionListener( listener );
		arrangeEvents.setAccelerator( Helper.getKeystroke( KeyEvent.VK_R ) );
		eventMenu.add( arrangeEvents );
		
		JMenu viewMenu = new JMenu( "View" );
		JMenuItem weeks = new JRadioButtonMenuItem( "Weeks" );
		weeks.setEnabled( false );
		weeks.addActionListener( listener );
		weeks.setAccelerator( Helper.getKeystroke( KeyEvent.VK_1 ) );
		JMenuItem month = new JRadioButtonMenuItem( "Months" );
		month.setEnabled( true );
		month.setSelected( true );
		month.addActionListener( listener );
		month.setAccelerator( Helper.getKeystroke( KeyEvent.VK_2 ) );
		JMenuItem year = new JRadioButtonMenuItem( "Days" );
		year.addActionListener( listener );
		year.setEnabled( false );
		year.setAccelerator( Helper.getKeystroke( KeyEvent.VK_3 ) );
		viewMenu.add( weeks );
		viewMenu.add( month );
		viewMenu.add( year );
		
		JMenu helpMenu = new JMenu( "Help" );
		JMenuItem about = new JMenuItem( "About" );
		about.addActionListener( listener );
		about.setActionCommand( ABOUT_ACTION );
		helpMenu.add( about );
		
		add( fileMenu );
		add( editMenu );
		add( eventMenu );
		add( viewMenu );
		add( helpMenu );
	}
	
}