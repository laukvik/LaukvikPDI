package org.laukvik.pdi.project;

import java.awt.event.ActionListener;
import javax.swing.JToolBar;

public class Toolbar extends JToolBar {

	private static final long serialVersionUID = -3426794966813464874L;

	public Toolbar( ActionListener listener ){
		setFloatable( false );
		CoolButton arrangeButton = new CoolButton( "Arrange", Project.getIcon("arrange.png") );
		arrangeButton.setActionCommand( Menu.ARRANGE_EVENTS_ACTION );
		arrangeButton.addActionListener( listener );
		add( arrangeButton );

		CoolButton preferencesButton = new CoolButton( "Preferences", Project.getIcon("preferences.png") );
		preferencesButton.addActionListener( listener );
		preferencesButton.setActionCommand( Menu.PREFERENCES_ACTION );
		add( preferencesButton );
	}

}