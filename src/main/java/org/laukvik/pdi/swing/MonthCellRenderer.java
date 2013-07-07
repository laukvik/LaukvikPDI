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
import java.awt.Font;
import java.awt.Graphics;
import java.util.GregorianCalendar;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MonthCellRenderer extends JLabel implements TableCellRenderer {
	
	private static final long serialVersionUID = -4790993151427600110L;
	private GregorianCalendar date;
	private boolean isSelected;
	private JTable table;
	private Font label; 
	
	public MonthCellRenderer() {
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
		this.isSelected = isSelected;
		this.table = table;
		if (value instanceof GregorianCalendar){
			this.date = (GregorianCalendar) value;
		}
		System.out.println( date );
		return this;
	}

    // 
    public void paint(Graphics g) {
    		if (isSelected){
    			g.setColor( table.getSelectionBackground() );
    			g.fillRect( 0,0, 100, 50 );
    		}
        g.setColor( table.getForeground() );
        g.setFont( new Font( table.getFont().getFontName(), Font.BOLD, 12 ) );
//        g.drawString( date.get( GregorianCalendar.DATE ), 80, 10 );
        
        g.drawString( "Morten", 80, 10 );
        
        
        
    }

}