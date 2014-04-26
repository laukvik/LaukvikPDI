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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Vector;

/**
 * The Property class is the central class in the API. VCalendar, VCard VEvent
 * and VTodo extends this class.
 *
 * @version $Revision: 1.10 $
 * @author	Morten Laukvik
 * @link	http://morten.laukvik.no
 * @since	1.5
 */
public class VProperty {

    /**
     * Constant used to indicate the start of a collection of Property objects
     */
    public final static String BEGIN_GROUP = "BEGIN";

    /**
     * Constant used to indicate the end of a collection of Property objects
     */
    public final static String END_GROUP = "END";

    /**
     * Constant used for returning a Property or Parameter based on String
     */
    public final static String MATCH_ALL = "*";

    /**
     * Constant used for seperating rows
     */
    final static byte[] LINE_SEPERATOR = "\r\n".getBytes();

    private String name;
    private String value;
    private Vector<VProperty> properties = new Vector<VProperty>();
    private Vector<VParameter> parameters = new Vector<VParameter>();
    private VProperty parent;
    private boolean isGroup = false;
    private final static int BASE64_CHARS_PR_ROW = 77;

    /**
     * Creates a new <code>Property</code> instance
     *
     */
    public VProperty() {
        setParent(null);
    }

    /**
     * Creates a new <code>Property</code> instance with the specified parent.
     *
     * @param parent The parent Property object
     */
    public VProperty(VProperty parent) {
        setParent(parent);
    }

    /**
     * Creates a new <code>Property</code> instance with the specified parent.
     *
     * @param parent The parent Property object
     * @param isGroup Sets whether the Property is a group or not
     */
    public VProperty(VProperty parent, boolean isGroup) {
        setParent(parent);
        setIsGroup(isGroup);
    }

    /**
     * Creates a new <code>Property</code> instance with the specified parent.
     *
     * @param parent The parent Property object
     * @param name The name to set
     * @param value The value to set
     */
    public VProperty(VProperty parent, String name, String value) {
        setParent(parent);
        setName(name);
        setValue(value);
    }

    /**
     * Creates a new <code>Property</code> instance with the specified name,
     * value and parameter
     *
     * @param name The name to set
     * @param value The value to set
     * @param parameter The parameter to set
     */
    public VProperty(String name, String value, VParameter... parameter) {
        setName(name);
        setValue(value);
        add(parameter);
    }

    /**
     * Creates a new <code>Property</code> instance with the specified parent,
     * name,value and group.
     *
     * @param parent The parent Property object
     * @param name The name to set
     * @param value The value to set
     * @param isGroup Sets whether the Property should be a group or not
     */
    public VProperty(VProperty parent, String name, String value, boolean isGroup) {
        setParent(parent);
        setName(name);
        setValue(value);
        setIsGroup(isGroup);
    }

    /**
     * Returns whether this property has a parent or not.
     *
     * @return <code>true</code> if it has a parent and <code>false</code> if
     * not
     */
    public boolean isRoot() {
        return (parent == null);
    }

    /**
     * Sets whether this Property can contain child properties or not. When a
     * property is a group the name will automatically become BEGIN
     *
     * @param isGroup a boolean whether the Property is a group or not
     */
    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    /**
     * Returns whether the object is a group Property or not.
     *
     * @return <code>true</code> if the Property is a group and
     * <code>false</code> if the Property isn't a group
     */
    public boolean isGroup() {
        return this.isGroup;
    }

    /**
     * Returns whether or not the value is binary
     *
     * @return <code>true</code> if the value is binary and <code>false</code>
     * if its not
     */
    public boolean isBinary() {
        boolean noEncoding = false;
        for (VParameter param : listParameters()) {
            if (param.getName().equalsIgnoreCase("BASE64")) {
                noEncoding = true;
            }
        }
        return noEncoding;
    }

    /**
     * Returns a String representation of this Property
     *
     */
    public String toString() {
        if (isGroup()) {
            return value;
        } else {
            if (isBinary()) {
                return name + ":" + "[BINARY " + value.length() + " bytes]";
            } else {
                return name + ":" + value;
            }
        }
    }

    /**
     * Returns a array of all <code>Property</code> objects in the path from the
     * root <code>Property</code> object to this
     *
     * @return an array of Property objects in the path
     */
    public VProperty[] listPath() {
        Vector<VProperty> items = new Vector<VProperty>();
        VProperty par = this;
        int n = 0;
        while (!par.isRoot() || n > 1000) {
            par = par.getParent();
            items.add(par);
            n++;
        }
        Vector<VProperty> items2 = new Vector<VProperty>();
        n = 0;
        for (int x = items.size() - 1; x > 0; x--) {
            items2.add(items.get(x));
            n++;
        }
        items2.add(this);
        return (VProperty[]) items2.toArray();
    }

    /**
     * Returns the index of this Property in its parent collection of Property
     *
     * @return the index
     */
    public int index() {
        if (isRoot()) {
            return -1;
        } else {
            return parent.properties.indexOf(this);
        }
    }

    /**
     * Returns the amount of parents above this
     *
     * @return the amount of parents
     */
    public int getLevel() {
        if (isRoot()) {
            return 0;
        } else {
            return getParent().getLevel() + 1;
        }
    }

    /**
     * Returns the root Property object
     *
     * @return the root Property object
     */
    public VProperty getRoot() {
        if (isRoot()) {
            return this;
        } else {
            return getParent().getRoot();
        }
    }

    /**
     * Returns the name
     *
     * @return the name of this Property
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
        if (name.equalsIgnoreCase(BEGIN_GROUP)) {
            setIsGroup(true);
        }
    }

    /**
     * Returns the value
     *
     * @return the value of this Property
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns an array of sub values if any
     *
     * @return an array of sub values
     */
    public String[] getValues() {
        return value.split(";");
    }

    /**
     * Sets multiple values
     *
     * @param value the multiple values to set
     */
    public void setValue(String... values) {
        String data = "";
        int n = 0;
        for (String value : values) {
            if (n > 0) {
                data += ",";
            }
            data += value;
            n++;
        }
        this.value = data;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the parent Property
     *
     * @return the parent Property
     */
    public VProperty getParent() {
        return parent;
    }

    /**
     * Sets the parent Property of this Property. If set to null it will be
     * treated as a root Property.
     *
     * @param parent the parent Property
     */
    public void setParent(VProperty parent) {
        this.parent = parent;
    }

    /**
     * Returns a Collection of all Property objects in this Property
     *
     * @return a collection of Property objects
     */
    public Collection<VProperty> list() {
        return properties;
    }

    /**
     * Returns the Property from the list of children with the specified index
     *
     * @param index the index of the Property
     * @return a Property with the specified index
     */
    public VProperty get(int index) {
        return properties.elementAt(index);
    }

    /**
     * Removes the Parameter with the specified index from the list of
     * Parameters
     *
     * @param index the index of the Parameter
     * @return a Parameter with the specified index
     */
    public VParameter getParameter(int index) {
        return parameters.elementAt(index);
    }

    /**
     * Adds a new Parameter
     *
     * @param parameter The Parameter to add
     */
    public void add(VParameter... parameter) {
        for (VParameter par : parameter) {
            parameters.add(par);
        }
    }

    public void add(String name, String value, VParameter... parameter) {
        VProperty prop = new VProperty(name, value, parameter);
        prop.setParent(this);
        properties.add(prop);
    }

    /**
     * Removes the Parameter with the specified index
     *
     * @param index the index of the Paramter to remove
     */
    public void removeParameter(int index) {
        parameters.remove(index);
    }

    /**
     * Returns a Collection of all Parameter associated with this Property
     *
     * @return a Collection of Parameter objects
     */
    public Collection<VParameter> listParameters() {
        return parameters;
    }

    /**
     * Returns the first Property that has the specified
     * <code>propertyName</code>
     *
     * @param propertyName the name of the Property to use
     * @return a Property which matched the <code>propertyName</code> and null
     * if not Property had that name
     */
    public VProperty get(String propertyName) {
        for (VProperty prop : properties) {
            if (prop.getName().equalsIgnoreCase(propertyName)) {
                return prop;
            }
        }
        return null;
    }

    /**
     * Returns a Collection of all Property objects which have the same
     * collection of searchParameters
     *
     * @param searchParameters a collection of Parameter objects to match
     * @return a Collection of Property objects
     */
    public Collection<VProperty> list(VParameter... searchParameters) {
        return list(VProperty.MATCH_ALL, searchParameters);
    }

    /**
     * Returns a Collection of all Property objects which are groups and which
     * match teh specified searce criteria <code>groupValue</code>.
     * <P>
     * If <code>groupValue</code> is set to <code>MATCH_ALL</code>,
     * <code>null</code> or not specified at all, all groups will be returned.
     *
     * @param groupValue The value of the group.
     *
     * @return a collection of Property objects
     */
    public Collection<VProperty> listGroups(String... groupValue) {
        Vector<VProperty> items = new Vector<VProperty>();
        /* Iterate through all child property objects */
        for (VProperty prop : properties) {
            if (prop.isGroup()) {
                String val = prop.getValue();
                /* Iterate through the test collection */
                for (String testGroupValue : groupValue) {
                    if (groupValue == null || testGroupValue.equalsIgnoreCase(VProperty.MATCH_ALL)) {
                        items.add(prop);
                    } else if (val.equalsIgnoreCase(testGroupValue)) {
                        items.add(prop);
                    }
                }
            }
        }
        return items;
    }

    /**
     * Returns a Collection of all Property objects which all has the same
     * propertyName and where they all share the same collection of
     * searchParameters. The name and value in the searchParameters are
     * case-insensitive.
     *
     * The following example returns a collection of all properties with the
     * property name EMAIL and
     *
     * Property.get( "EMAIL", new Parameter[] { new Parameter("type","internet")
     * }
     *
     * The example below returns all EMAIL properties with any parameter with
     * name == type
     *
     * Property.get( "EMAIL", new Parameter[] { new Parameter("type", null) }
     *
     * @param propertyName
     * @param searchParameters
     * @return a Collection of Property objects
     */
    public Collection<VProperty> list(String propertyName, VParameter... searchParameters) {
        Vector<VProperty> items = new Vector<VProperty>();
        if (propertyName == null) {
            return null;
        }
        /* Iterate through all child property objects */
        for (VProperty prop : properties) {
            /* */
            if (propertyName.equalsIgnoreCase(VProperty.MATCH_ALL) || prop.getName().equalsIgnoreCase(propertyName)) {
                if (searchParameters == null) {
                    items.add(prop);
                } else {
                    int matches = 0;
                    /* Iterate through all parameters in search pattern */
                    for (VParameter searchParameter : searchParameters) {
                        log("Searching for: " + searchParameter);
                        for (VParameter parameter : prop.listParameters()) {
                            log("Try match: " + searchParameter.getName() + " with " + parameter.getName());
                            if (searchParameter.getName().equalsIgnoreCase(VProperty.MATCH_ALL)) {
                                /* Using wildcards on name */
                                if (parameter.getValue().equalsIgnoreCase(searchParameter.getValue())) {
                                    matches++;
                                }
                                log("getName==null: " + searchParameter.getValue() + " with " + parameter.getValue());
                            } else if (searchParameter.getValue().equalsIgnoreCase(VProperty.MATCH_ALL)) {
                                /* Using wildcards on value */
                                if (parameter.getName().equalsIgnoreCase(searchParameter.getName())) {
                                    matches++;
                                }
                                log("getValue==null: " + searchParameter.getName() + " with " + parameter.getName());
                            } else {
                                /* No wildcards */
                                if (parameter.getName().equalsIgnoreCase(searchParameter.getName())) {
                                    if (parameter.getValue().equalsIgnoreCase(searchParameter.getValue())) {
                                        matches++;
                                    }
                                }
                                log("nowWildcards: " + searchParameter.getName() + " with " + parameter.getName());
                            }

                        }
                    }
                    log("Result: " + matches);
                    if (matches >= searchParameters.length) {
                        items.add(prop);
                        log("Matches: " + "Yes");
                    } else {
                        log("Matches: " + "No");
                    }
                }
            }
        }
        return items;
    }

    /**
     * Returns the amount Property objects associated with this Property
     *
     * @return the amount of Property object associated with this Property
     */
    public int length() {
        return properties.size();
    }

    /**
     * Adds the specified Property
     *
     * @param property The Property to add
     */
    public void add(VProperty... property) {
        for (VProperty prop : property) {
            prop.setParent(this);
            properties.add(prop);
        }

    }

    /**
     * Removes a Property with the specified index
     *
     * @param index the index of the Property to remove
     */
    public void remove(int index) {
        properties.remove(index);
    }

    /**
     * Removes a Property. Useful when removing a Property when using list
     * features and the index is no longer a valid reference to the Property.
     *
     * @param property
     */
    public boolean remove(VProperty... property) {
        return properties.removeElement(property);
    }

    /**
     * Writes the value of this Property to the specified OutputStream using
     * BASE64 encoding
     *
     * @param out the OutputStream to use
     * @throws IOException throws an IOException when failed or interrupted I/O
     * operations occurs
     */
    private void writeBinaryOut(OutputStream out) throws IOException {
        double max = getValue().length();
        double rows = Math.ceil(max / BASE64_CHARS_PR_ROW);
        for (int x = 0; x < rows; x++) {
            int beginIndex = x * BASE64_CHARS_PR_ROW;
            int endIndex = beginIndex + BASE64_CHARS_PR_ROW;
            String row;
            if (x == 0) {
                // First row
                row = "\n  " + getValue().substring(beginIndex, endIndex);
            } else if (endIndex > max) {
                // Last row
                row = "  " + getValue().substring(beginIndex) + "\n";
            } else {
                // Rows except first and last
                row = "  " + getValue().substring(beginIndex, endIndex);
            }
            out.write(row.getBytes());
        }
    }

    /**
     * Writes the contents of this property and all its children to the
     * specified OutputStrem
     *
     * @param out The OutputStream to write the Object
     * @throws IOException If the object cant be written to the specified
     * OutputStream
     */
    public void writeOut(OutputStream out) throws IOException {
        try {
            if (isGroup()) {
                // Write start of group e.g BEGIN:VCARD\n
                out.write(VProperty.BEGIN_GROUP.getBytes());
                out.write((":").getBytes());
                out.write(getValue().getBytes());
                out.write(LINE_SEPERATOR);
                // Write parameters e.g ;type=pref;type=cell
                for (VParameter param : listParameters()) {
                    out.write((";").getBytes());
                    param.writeOut(out);
                }
                // Write childrens data
                for (VProperty child : list()) {
                    child.writeOut(out);
                    out.write(LINE_SEPERATOR);
                }
                // Write end of group e.g END:VCARD
                out.write(VProperty.END_GROUP.getBytes());
                out.write((":").getBytes());
                out.write(getValue().getBytes());
                out.write(LINE_SEPERATOR);
            } else {
                // Write start of property e.g DTSTART:20061007T131752\n
                out.write(getName().getBytes());
                // Write parameters e.g ;type=pref;type=cell
                for (VParameter param : listParameters()) {
                    out.write((";").getBytes());
                    param.writeOut(out);
                }
                out.write((":").getBytes());
                //
                if (isBinary()) {
                    // Binary value
                    writeBinaryOut(out);
                } else {
                    // Text value
                    out.write(getValue().getBytes());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the specified message to default out
     *
     * @param message the message to write to default out
     */
    public void log(String message) {
//		System.out.println( this.getClass().getName() + ": " + message  );
    }

}
