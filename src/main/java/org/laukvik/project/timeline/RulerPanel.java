package org.laukvik.project.timeline;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class RulerPanel extends JPanel{

	private static final long serialVersionUID = -1032490954981183887L;
	private TimelinePainter painter;

	public RulerPanel(TimelinePainter painter) {
		setLayout( null );
		this.painter = painter;
		setBackground( Color.WHITE );
		setPreferredSize( painter.getRulerSize() );
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		int w = getPreferredSize().width;
		int h = getPreferredSize().height;
		painter.paintRuler( (Graphics2D) g, w, h );
	}
	
	public void resize(){
		setPreferredSize( painter.getRulerSize() );
	}
	
}