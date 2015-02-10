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
import java.awt.GridLayout;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.laukvik.pdi.PDIManager;
import org.laukvik.pdi.VCalendar;

public class Calendar extends JFrame {

    private static final long serialVersionUID = 5981569220290149908L;
    private VCalendar cal;
    private MonthTableModel model;
    private JTable table;

    public Calendar(VCalendar cal) {
        super();
        setLayout(new GridLayout());
        try {
            model = new MonthTableModel(cal);

            table = new JTable();
            table.setSize(7 * 100, 100 * 5 + 20);

            table.setColumnSelectionAllowed(false);
            table.setRowSelectionAllowed(false);
            table.setCellSelectionEnabled(true);
            table.setModel(model);
            table.setRowHeight(100);
            table.setGridColor(Color.GRAY);
            MonthCellRenderer renderer = new MonthCellRenderer();
            table.setDefaultRenderer(renderer.getClass(), renderer);
            JScrollPane scroll = new JScrollPane(table);

            add(scroll);
            setSize(700, 550);
            setVisible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getMyDocuments(String filename) {
        return new File(System.getProperty("user.home"), filename);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
        } else {
            VCalendar cal;
            String filename = args[0];
            try {
                cal = (VCalendar) PDIManager.read(new File(filename));
                Calendar c = new Calendar(cal);
            }
            catch (Exception ex) {
                System.out.println("Could not open file " + filename);
            }

        }
    }

}
