package org.laukvik.pdi.project;

import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class CoolButton extends JButton {

	private static final long serialVersionUID = 7322514625087660709L;

	public CoolButton(String string, Icon icon) {
		super(string, icon);
		setVerticalTextPosition(  SwingConstants.BOTTOM );
		setVerticalAlignment( SwingConstants.BOTTOM );
		setHorizontalTextPosition( SwingConstants.CENTER );
		setPreferredSize( new Dimension( getPreferredSize().width, 64 ) );
	}

}