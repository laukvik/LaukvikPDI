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

public class VJournal extends VProperty {

    /**
     * Constant used to specify that a Property is a VJournal object
     */
    public static final String VJOURNAL = "VJOURNAL";

    /**
     * Creates a new <code>VJournal</code> instance with the specified parent
     * Property
     *
     * @param parent the parent property
     */
    public VJournal(VProperty parent) {
        super(parent);
        setIsGroup(true);
        setValue(VJOURNAL);
    }

}
