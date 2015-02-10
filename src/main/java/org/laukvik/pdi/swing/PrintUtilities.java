package org.laukvik.pdi.swing;

import java.awt.*;
import javax.swing.*;
import java.awt.print.*;

/**
 * A simple utility class that lets you very simply print an arbitrary component
 * in JDK 1.2. Just pass the component to PrintUtilities.printComponent. The
 * component you want to print doesn't need a print method and doesn't have to
 * implement any interface or do anything special at all.
 * <P>
 * If you are going to be printing many times, it is marginally more efficient
 * to first do the following:
 * 
 * <PRE>
 * 
 * PrintUtilities printHelper = new PrintUtilities(theComponent);
 * 
 * </PRE>
 * 
 * then later do printHelper.print(). But this is a very tiny difference, so in
 * most cases just do the simpler
 * PrintUtilities.printComponent(componentToBePrinted).
 * 
 * Taken from Core Web Programming from Prentice Hall and Sun Microsystems
 * Press, http://www.corewebprogramming.com/. &copy; 2001 Marty Hall and Larry
 * Brown; may be freely used or adapted.
 */
public class PrintUtilities implements Printable {
	
	protected Component componentToBePrinted;

	public static void printComponent(Component c) {
		new PrintUtilities(c).print();
	}

	public PrintUtilities(Component componentToBePrinted) {
		this.componentToBePrinted = componentToBePrinted;
	}

	public void print() {
		PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(this);
		if (printJob.printDialog())
			try {
				printJob.print();
			} catch (PrinterException pe) {
				System.out.println("Error printing: " + pe);
			}
	}

	// General print routine for JDK 1.2. Use PrintUtilities2
	// for printing in JDK 1.3.
	public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
		if (pageIndex > 0) {
			return (NO_SUCH_PAGE);
		} else {
			Graphics2D g2d = (Graphics2D) g;
			g2d.translate(pageFormat.getImageableX(), pageFormat
					.getImageableY());
			disableDoubleBuffering(componentToBePrinted);
			componentToBePrinted.paint(g2d);
			enableDoubleBuffering(componentToBePrinted);
			return (PAGE_EXISTS);
		}
	}

	/**
	 * The speed and quality of printing suffers dramatically if any of the
	 * containers have double buffering turned on, so this turns it off
	 * globally. This step is only required in JDK 1.2.
	 */

	public static void disableDoubleBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(false);
	}

	/**
	 * Reenables double buffering globally. This step is only required in JDK
	 * 1.2.
	 */

	public static void enableDoubleBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(true);
	}
}