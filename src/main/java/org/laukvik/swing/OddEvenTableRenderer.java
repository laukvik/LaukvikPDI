package org.laukvik.swing;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A class to render each alternating row in a JTable with different color.
 * 
 * @author Morten Laukvik
 *
 */
public class OddEvenTableRenderer extends DefaultTableCellRenderer{

	private static final long serialVersionUID = -1327274217070392821L;
	private Color odd = new Color( 255, 255, 255 );
	private Color even = new Color( 230, 230, 230 );
	
	/**
	 * Creates a new instance with default set of colors
	 *
	 */
    public OddEvenTableRenderer(){
		super();
    }
	
    /**
     * Creates a new instance with specified colors.
     * 
     * @param odd
     * @param even
     */
    public OddEvenTableRenderer( Color odd, Color even ){
		super();
		this.odd = odd;
		this.even = even;
    }

    /**
     * @see #java.awt.Component.getTableCellRendererComponent
     */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		if (isSelected) {
		} else {
			setBackground(row % 2 == 0 ? odd : even);
		}
		return this;
	}

}