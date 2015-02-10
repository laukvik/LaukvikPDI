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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 *
 * @version $Revision: 1.1 $
 * @author	Morten Laukvik
 * @link		http://morten.laukvik.no
 * @since	1.5
 */
public class RecentItems {

    private File file;
    private Properties prop = new Properties();
    private int limit = 10;
    private List<String> items = new ArrayList<>();

    public RecentItems(File file) {
        this.file = file;
        // Read properties file.
        try {
            this.prop.load(new FileInputStream(file));
            int index = 0;
            for (Object item : prop.values()) {
                if (index < limit) {
                    items.add(item.toString());
                }
            }
        }
        catch (FileNotFoundException e) {
            try {
                file.createNewFile();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            // Clear properties
            prop.clear();
            // Set properties from List
            for (int x = 0; x < items.size(); x++) {
                prop.put(x + 1 + "", items.get(x));
            }
            prop.store(new FileOutputStream(file), null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(String filename) {
        if (items.contains(filename)) {
            // Dont add if already exist
        } else {
            items.add(filename);
            save();
        }
    }

    public void removeAll() {
        items = new ArrayList<String>();
    }

    public List<String> getItems() {
        return items;
    }

}
