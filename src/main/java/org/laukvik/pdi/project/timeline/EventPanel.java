package org.laukvik.pdi.project.timeline;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class EventPanel extends JPanel {

	private static final long serialVersionUID = -2715266690270622433L;
	private TimelinePainter painter;

	public EventPanel(TimelinePainter painter) {
		this.painter = painter;
		setBackground( Color.WHITE );
		setPreferredSize( painter.getGridSize() );
//		setSize( painter.getGridSize() );
//		setMinimumSize( painter.getGridSize() );
//		setMaximumSize( painter.getGridSize() );
	}

	public void paint(Graphics g) {
		super.paint(g);
		int w = getPreferredSize().width;
		int h = getSize().height;
		painter.paintGrid( (Graphics2D) g, w, h );
		painter.paintEvents( (Graphics2D) g, getSize().width, getSize().height );
	}

	public void resize(){
		setPreferredSize( painter.getGridSize() );
	}

}