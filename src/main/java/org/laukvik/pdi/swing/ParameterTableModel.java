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

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.laukvik.pdi.Parameter;
import org.laukvik.pdi.Property;

/**
 * 
 * 
 * @version  $Revision: 1.2 $
 * @author	Morten Laukvik
 * @link		http://morten.laukvik.no
 * @since	1.5
 */
public class ParameterTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private Property property;
	
	public ParameterTableModel( Property property ){
		this.property = property;
	}
	
	public void setProperty( Property property ){
		this.property = property;
		fireTableDataChanged();
	}
	
	public Property getProperty(){
		return this.property;
	}
	
	public int getRowCount() {
		if (property == null){ return 0; }
		return property.listParameters().size();
	}

	/**
	 * Always return 3 (icon,name,value)
	 */
	public int getColumnCount() {
		return 3;
	}

	public String getColumnName(int columnIndex) {
		switch (columnIndex){
			case 0 : return "";
			case 1 : return "Parameter";
			case 2 : return "Value";
			default : return "???";
		}
	}

	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0){
			return ParameterCellRenderer.class;
		} else {
			return String.class;
		}
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex > 0){
			return true;
		}
		return false;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (property == null){ return null; }
		Parameter param = property.getParameter( rowIndex );
		switch (columnIndex){
			case 0 : return new String("");
			case 1 : return param.getName();
			case 2 : return param.getValue();
			default : return null;
		}
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (property == null){ return; }
		Parameter param = property.getParameter( rowIndex );
		switch (columnIndex){
			case 0 : break;
			case 1 : param.setName( aValue.toString() ); break;
			case 2 : param.setValue( aValue.toString() ); break;
		}
	}
	
	public void addParameter( Parameter parameter, JTable table ){
		property.add( parameter );
		int size = property.listParameters().size();
		int from = size-2;
		int end = size-1;
		fireTableRowsInserted( from, end );
	}
	
	public void removeRows( int selectedRows [], JTable table ){
		if (property != null){
			for (int n=selectedRows.length; n>0; n--){
				int index = selectedRows[n-1];
				if (index >-1 && index < property.listParameters().size()){
					property.removeParameter( index );
					
				}
			}
			fireTableRowsDeleted( selectedRows[0], selectedRows[ selectedRows.length-1 ] );
		} 
	}

}