package org.laukvik.pdi.swing;

import org.laukvik.pdi.VCalendar;

public class PDIMonthTableModel extends MonthTableModel {

    private static final long serialVersionUID = 7440704726741615505L;
    private final VCalendar cal;

    public PDIMonthTableModel(VCalendar cal) {
        super(cal);
        this.cal = cal;
    }

}
