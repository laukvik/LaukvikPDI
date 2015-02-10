/*
 * Copyright (C) 2015 Laukviks Bedrifter
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.laukvik.pdi.swing;

import java.io.File;
import org.junit.Test;
import org.laukvik.pdi.PDIManager;
import org.laukvik.pdi.VCalendar;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CalendarTest {

    public CalendarTest() {
    }

    @Test
    public void testSomeMethod() throws Exception {

        VCalendar cal = (VCalendar) PDIManager.read(getResource("US-Holidays.ics"));
        Calendar c = new Calendar(cal);
    }

    public static File getResource(String filename) {
        ClassLoader classLoader = CalendarTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

}
