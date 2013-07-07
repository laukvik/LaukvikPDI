package org.laukvik.project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import org.laukvik.pdi.PDIManager;
import org.laukvik.pdi.VCalendar;
import org.laukvik.pdi.VEvent;
import org.laukvik.pdi.swing.Helper;
import org.laukvik.pdi.swing.Viewer;
import org.laukvik.project.timeline.TimelinePainter;
import org.laukvik.project.timeline.TimelinePanel;
import org.laukvik.project.timeline.TimelineTableModel;
import org.laukvik.project.timeline.views.WeekPainter;
import org.laukvik.swing.OddEvenTableRenderer;
import org.laukvik.swing.utils.PrintUtilities;

public class Project extends JFrame implements ActionListener {

	private static final long serialVersionUID = 4526149253820934373L;
	public static final Color EVEN = new Color(232,242,254);
	public static final Color ODD = new Color(255,255,255);
	public static final Color HILITE = new Color(61,128,223);
	private Toolbar toolbar;
	private Menu menu = new Menu( this );
	private TimelinePainter painter;
	private VCalendar cal = new VCalendar();
	private Session session = new Session( cal );
	private TimelineTableModel model = new TimelineTableModel( session );
	private JTable table = new JTable( model );	
	private TimelinePanel timeline;
	private JLabel status = new JLabel();
	private File file;
	
	public Project() {
		/* Initialize GUI */
		initComponents();
		newCalendar();
	}
	
	public void setStatus( String message ){
		status.setText( message );
	}
	
	public void newCalendar(){
		file = null;
		cal = new VCalendar();
		setCalendar( cal );
	}
	
	public void setCalendar( VCalendar cal ){
		session.setCalendar( cal );
		session.setBusy( getBusyCalendar()  );
		calendarChangedEvent();
		timeline.scrollTo( new GregorianCalendar() );
	}
	
	/**
	 * Reloads the table and repaints the timeline
	 *
	 */
	public void calendarChangedEvent(){
		log( "repaintCalendar" );
		session.calendarChanged();
		model.tableChanged();
		timeline.repaintBoth();
	}
	
	public int getHeaderWidth( String string ){
		return SwingUtilities.computeStringWidth( table.getFontMetrics( table.getFont() ) , string ) + 20;
	}
	
	public void initComponents(){
		addWindowListener( new WindowAdapter(){
            public void windowClosing( WindowEvent e ){
                System.exit( 0 );
            }
        });                     
		setJMenuBar( menu );
		setLayout( new BorderLayout() );
		
		status.setEnabled(false);
		status.setPreferredSize( new Dimension(32000,24) );
		
		toolbar = new Toolbar( this );
//		add( toolbar, BorderLayout.NORTH );
		add( status, BorderLayout.SOUTH );
		
		model = new TimelineTableModel( session );
		table = new JTable( model );		
		
		table.setDefaultRenderer( Object.class, new OddEvenTableRenderer(ODD,EVEN) );
		table.setRowHeight(table.getRowHeight() + 5);
		table.setGridColor(new Color(210, 210, 210));
		table.setAutoResizeMode( JTable.AUTO_RESIZE_NEXT_COLUMN );
		table.getColumnModel().getColumn(0).setWidth( 24 );
		table.getColumnModel().getColumn(0).setMaxWidth( 24 );
		table.getColumnModel().getColumn(0).setPreferredWidth( 24 );
		
		table.getColumnModel().getColumn(1).setPreferredWidth( 250 );
		
		table.getColumnModel().getColumn(2).setPreferredWidth( getHeaderWidth("121") );
		table.getColumnModel().getColumn(3).setPreferredWidth( getHeaderWidth("31") );
		table.getColumnModel().getColumn(2).setPreferredWidth( getHeaderWidth("December") );
		table.getColumnModel().getColumn(5).setPreferredWidth( getHeaderWidth("2006") );
		table.getColumnModel().getColumn(6).setPreferredWidth( getHeaderWidth("00:00") );


		JScrollPane scrollL = new JScrollPane( table );
		scrollL.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
		scrollL.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		
		painter = new WeekPainter( session, table );
		timeline = new TimelinePanel( painter );
		
		 
		model.setTimelinePanel( timeline );
		
		int h = painter.getRulerSize().height - table.getTableHeader().getPreferredSize().height;
		scrollL.setBorder( BorderFactory.createMatteBorder( h, 0, 0, 0, getBackground() ) );
		
		timeline.setVerticalScrollBar( scrollL.getVerticalScrollBar() );
		
		JSplitPane split = new JSplitPane();
		split.setLeftComponent( scrollL );
		split.setRightComponent( timeline );
		split.setDividerLocation( 500 );
		
		add( split );

		pack();
		setVisible(true);
//		setSize( Toolkit.getDefaultToolkit().getScreenSize() );
		setSize( 1000, 500 );
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Helper.setCrossPlatformProperties();
//		Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		new Project();
            }
        });

	}
	
	public void openFile( File calendarFile ) {
		try {
			this.cal = (VCalendar) PDIManager.read( calendarFile );
			setCalendar( this.cal );
			file = calendarFile;
		} catch (Exception e) {
			alert( e );
		}
	}
	
	public void saveFile( File calendarFile ) {
		try {
			PDIManager.save( session.getCalendar(), calendarFile );
		} catch (Exception e) {
			alert( e );
		}
	}

	public static File getMyDocuments( String filename ){
		return new File( System.getProperty("user.home"), filename);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase( Menu.OPENFILE_ACTION )){
			handleOpen();
		} else 	if (e.getActionCommand().equalsIgnoreCase( Menu.NEWFILE_ACTION )){
			handleNew();
		} else 	if (e.getActionCommand().equalsIgnoreCase( Menu.NEW_EVENT_ACTION )){
			handleNewEvent();
		} else 	if (e.getActionCommand().equalsIgnoreCase( Menu.ARRANGE_EVENTS_ACTION )){
			handleArrangeEvents();
		} else 	if (e.getActionCommand().equalsIgnoreCase( Menu.SAVEAS_ACTION )){
			handleSaveAs();
		} else 	if (e.getActionCommand().equalsIgnoreCase( Menu.SAVE_ACTION )){
			handleSave();
		} else 	if (e.getActionCommand().equalsIgnoreCase( Menu.ABOUT_ACTION )){
			handleAbout();
		} else 	if (e.getActionCommand().equalsIgnoreCase( Menu.PRINT_ACTION )){
			handlePrint();
		} else 	if (e.getActionCommand().equalsIgnoreCase( Menu.DELETE_ACTION )){
			handleDelete();
		}		
	}
	
	public void handleDelete() {
		/* Get selected events*/
		int rows [] = table.getSelectedRows();
		session.removeEvents( rows, model );
		calendarChangedEvent();
	}
	
	public void handlePrint() {
		PrintUtilities.printComponent( timeline );
	}

	public void handleArrangeEvents(){
		int rows [] = table.getSelectedRows();
		if (rows.length == 1){
			/* Arrange one */
			int start = rows[ 0 ];
			int end = rows[ 0 ];
			session.arrangeEvents( start, end );
			calendarChangedEvent();
			
		} else if (rows.length > 1){
			/* Arrange more than one */
			int start = rows[ 0 ];
			int end = rows[ rows.length - 1 ];
			session.arrangeEvents( start, end );
			calendarChangedEvent();
			
		} else {
			/* Arrange all */
			session.arrangeEvents( 0, table.getRowCount() );
			calendarChangedEvent();
		}
	}
	
    public void handleAbout(){
		JOptionPane.showMessageDialog(this, "Project v0.1 written by Morten Laukvik", "About", JOptionPane.INFORMATION_MESSAGE, Helper.getIcon("logo.png") );	
    }

	public void handleNew(){
		newCalendar();
		calendarChangedEvent();
	}
	
	public void handleOpen(){
		java.awt.FileDialog fd = new java.awt.FileDialog( this, "Open Calendar", java.awt.FileDialog.LOAD );
		fd.setDirectory( System.getProperty("user.home") );
		fd.setVisible(true);
		if (fd.getFile() != null){
			java.io.File file = new java.io.File( fd.getDirectory(), fd.getFile()  );
			openFile( file );
		}
	}
	
	public void handleSave(){
		if (file == null){
			handleSaveAs();
		} else {
			saveFile( file );
		}
	}
	
	public void handleSaveAs(){
		java.awt.FileDialog fd = new java.awt.FileDialog( this, "Save Calendar", java.awt.FileDialog.SAVE );
		fd.setDirectory( System.getProperty("user.home") );
		fd.setVisible(true);
		if (fd.getFile() != null){
			java.io.File file = new java.io.File( fd.getDirectory(), fd.getFile()  );
			saveFile( file );
		}
	}
	
	public void handleNewEvent(){
		GregorianCalendar from = new GregorianCalendar();
		GregorianCalendar to = new GregorianCalendar();
		to.roll( GregorianCalendar.HOUR_OF_DAY, 1 );
		VEvent e = new VEvent( session.getCalendar(), "Totally new event", from, to, "" );
		session.addEvent( e );
		calendarChangedEvent();
	}
	
	public void log( String message ){
		System.out.println( this.getClass().getName() + ": " + message  );
	}
	
	public void alert( Exception e ){
		e.printStackTrace();
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
		return new javax.swing.ImageIcon( Project.class.getResource( "icons/" + filename ));
	}
	
	public VCalendar getBusyCalendar(){
		VCalendar busy = new VCalendar();
		
		GregorianCalendar end = session.getDefaultProjectEnd();
		GregorianCalendar current = session.getDefaultProjectStart();

		while(current.getTimeInMillis() < end.getTimeInMillis()){
			/* Add hours before work */
			GregorianCalendar morningFrom = new GregorianCalendar();
			morningFrom.setTimeInMillis( current.getTimeInMillis() );
			morningFrom.set( GregorianCalendar.HOUR_OF_DAY, 0 );
			
			GregorianCalendar morningTo = new GregorianCalendar();
			morningTo.setTimeInMillis( current.getTimeInMillis() );
			morningTo.set( GregorianCalendar.HOUR_OF_DAY, 9 );
			
			VEvent morning = new VEvent(null,"Locked",morningFrom,morningTo,"");
			morning.setDuration( morningTo.getTimeInMillis() - morningFrom.getTimeInMillis() );
			
			busy.addEvent( morning );
			
			/* Add hours after work */
			GregorianCalendar eveningFrom = new GregorianCalendar();
			eveningFrom.setTimeInMillis( current.getTimeInMillis() );
			eveningFrom.set( GregorianCalendar.HOUR_OF_DAY, 17 );
			
			GregorianCalendar eveningTo = new GregorianCalendar();
			eveningTo.setTimeInMillis( current.getTimeInMillis() );
			eveningTo.set( GregorianCalendar.HOUR_OF_DAY, 23 );
			eveningTo.add( GregorianCalendar.HOUR_OF_DAY , 1 );
			
			VEvent evening = new VEvent(null,"Locked",eveningFrom,eveningTo,"");
			evening.setDuration( eveningTo.getTimeInMillis() - eveningFrom.getTimeInMillis() );
			
			busy.addEvent( evening );
			
			/* Go to next day */
			current.add( GregorianCalendar.DAY_OF_MONTH, 1 );
			
			
		}
		return busy;
	}

}