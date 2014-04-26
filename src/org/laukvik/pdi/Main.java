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
package org.laukvik.pdi;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 *
 * @author morten
 */
public class Main {

    public Main() {
    }

    public static void main(String[] args) {
        /* Create an empty VCard */
        VCard card = new VCard();
        card.add(VCard.VERSION, "2.0");

        /* Set a few properties  */
        card.add(VCard.FULLNAME, "Sun Microsystems, Inc.");
        card.add(VCard.ORGANISATION, "Sun Microsystems, Inc.");
        card.add(VCard.ADDRESS, "4150 Network Circle;Santa Clara, CA 95054");
        card.add(VCard.TELEPHONE, "1-800-555-9", VCard.DOMESTIC);
        card.add(VCard.TELEPHONE, "1-650-960-1300", VCard.INTERNATIONAL);

        try {
            File file = new File(System.getProperty("user.home"), "Sun_Microsystems.vcf");
            PDIManager.save(card, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            /* Read an existing VCard */
            File file = new File(System.getProperty("user.home"), "Sun_Microsystems.vcf");
            VCard vcard = (VCard) PDIManager.read(file);
            /* Display name */
            System.out.println(vcard.get(VCard.FULLNAME));
            /* List all phone numbers */
            System.out.println(vcard.list(VCard.TELEPHONE));
            /* List all domestic phone numbers */
            System.out.println(vcard.list(VCard.TELEPHONE, VCard.DOMESTIC));
            /* Print out all international numbers */
            System.out.println(vcard.list(VCard.INTERNATIONAL));
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            File file = new File(System.getProperty("user.home"), "mycalendar.ics");
            PDIManager.save(cal, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Iterator it = cal.listEvents().iterator(); it.hasNext();) {
            VEvent e = (VEvent) it.next();
            System.out.println(e.getSummary());
        }

    }

}
