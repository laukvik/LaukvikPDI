package org.laukvik.pdi.project.timeline;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.laukvik.pdi.VEvent;
import org.laukvik.pdi.project.Session;

/**
 *
 * @author Morten
 *
 */
public class TimelineTableModel implements TableModel{

	private final static long hour = 1000*60*60;
	public final static DateFormat hoursFormat = new SimpleDateFormat( "HH:mm");
	public final static DateFormat dayFormat = new SimpleDateFormat( "d");
	public final static DateFormat monthFormat = new SimpleDateFormat( "MMMMM");
	public final static DateFormat yearFormat = new SimpleDateFormat( "yyyy");

	public final static DateFormat dateFormat = new SimpleDateFormat( "d MMMMM yyyy HH:mm");
	private TimelinePanel timeline;
	private Session session;
	private Vector <TableModelListener> listeners = new Vector <TableModelListener>();

	public TimelineTableModel(Session session) {
		this.session = session;
	}

	public void addTableModelListener(TableModelListener l) {
		listeners.add( l );
	}

	public void tableChanged(){
		for (TableModelListener listener : listeners){
			listener.tableChanged( new TableModelEvent(this) );
		}
	}

	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex){
			case 0 : return Boolean.class;
			case 2 : return String.class;
		}
		return String.class;
	}

	public int getColumnCount() {
		return 7;
	}

	public String getColumnName(int columnIndex) {
		switch (columnIndex){
			case 0 : return "";
			case 1 : return "Task";
			case 2 : return "Duration";
			case 3 : return "Day";
			case 4 : return "Month";
			case 5 : return "Year";
			case 6 : return "Hour";

		}
		return null;
	}

	public int getRowCount() {
		return session.eventLength();
	}

	public VEvent getRow( int rowIndex ){
		return session.getEvent( rowIndex );
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		VEvent event = getRow( rowIndex );
		switch (columnIndex){
			case 0 : return false;
			case 1 : return event.getSummary();
			case 2 : return (long) (event.getDuration() / hour);
			case 3 : return dayFormat.format( event.getStartDate() );
			case 4 : return monthFormat.format( event.getStartDate() );
			case 5 : return yearFormat.format( event.getStartDate() );
			case 6 : return hoursFormat.format( event.getStartDate() );
		}
		return "";
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (columnIndex >= 0);
	}

	public void removeTableModelListener(TableModelListener l) {
		listeners.remove( l );
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		String day = (String) getValueAt( rowIndex, 3 );
		String month = (String) getValueAt( rowIndex, 4 );
		String year = (String) getValueAt( rowIndex, 5 );
		String hour = (String) getValueAt( rowIndex, 6 );
		VEvent event = getRow( rowIndex );
		switch (columnIndex){
			case 1 : event.setSummary( (String) aValue );
				break;
			case 2 : event.setDuration( Long.parseLong( (String) aValue ) * MILLIS_PR_HOUR  );
				break;
			case 3 :
				/* Day */
				day = (String) aValue;
				try {
					Date date = dateFormat.parse( day + " " + month + " " + year + " " + hour );
					event.setStartDate( date  );
					log( date.toString() );
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			case 4 :
				/* Month */
				month = (String) aValue;
				try {
					Date date = dateFormat.parse( day + " " + month + " " + year + " " + hour );
					event.setStartDate( date  );
					log( date.toString() );
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			case 5 :
				/* Year */
				year = (String) aValue;
				try {
					Date date = dateFormat.parse( day + " " + month + " " + year + " " + hour );
					event.setStartDate( date  );
					log( date.toString() );
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			case 6 :
				/* Hour */
				hour = (String) aValue;
				try {
					Date date = dateFormat.parse( day + " " + month + " " + year + " " + hour );
					event.setStartDate( date  );
					log( date.toString() );
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
		}


		session.calendarChanged();

		tableChanged();
		timeline.repaintBoth();
	}

	public final static long MILLIS_PR_HOUR = 1000*60*60;

	public void setTimelinePanel(TimelinePanel panel) {
		this.timeline = panel;
	}

	public void log( String message ){
		System.out.println( this.getClass().getName() + ": " + message  );
	}
}