/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.laukvik.pdi;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import org.junit.Test;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class VCalendarTest {

    public VCalendarTest() {
    }

    @Test
    public void shouldReadUSHolidays() throws Exception {
        VCalendar vcal = (VCalendar) PDIManager.read(getResource("US-Holidays.ics"));

    }

    @Test
    public void shouldWriteCalendar() {
        /* Create empty calendar */
        VCalendar cal = new VCalendar();
        /* Adding a simple to do item*/
        cal.addTodo("Remember to create JavaDoc", VTodo.PRIORITY_1, new Date(), "No comments");
        /* Creating a single event in schedule */
        GregorianCalendar from = new GregorianCalendar();
        GregorianCalendar to = new GregorianCalendar();
        to.add(GregorianCalendar.HOUR_OF_DAY, 3);
        cal.addEvent("Visit JavaZone", from.getTime(), to.getTime(), "");
        try {
            /* Exporting calendar to standard ics file */
            File file = File.createTempFile("VCalendarTest", ".ics");
            PDIManager.save(cal, file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        for (Iterator it = cal.listEvents().iterator(); it.hasNext();) {
            VEvent e = (VEvent) it.next();
            System.out.println(e.getSummary());
        }
    }

    public static File getResource(String filename) {
        ClassLoader classLoader = VCardTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

}
