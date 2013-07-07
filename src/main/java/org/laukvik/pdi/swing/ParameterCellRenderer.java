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

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * 
 * @version  $Revision: 1.1 $
 * @author	Morten Laukvik
 * @link		http://morten.laukvik.no
 * @since	1.5
 */
public class ParameterCellRenderer extends JLabel implements TableCellRenderer{

	private static final long serialVersionUID = 1L;	
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//		boolean even = ( row & 2 ) == 0;
		if (column == 0){
			setIcon( Helper.getIcon("parameter.gif") );
		} else {
			setText( value.toString() + row );
		}
		setBackground( new Color(100,100,200) );
		return this;
	}

}