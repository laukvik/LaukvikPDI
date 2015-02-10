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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.laukvik.pdi.VProperty;

/**
 * A class which makes it much easier to work with trees than using the
 * traditional way of using the <code>add</code> methods.
 *
 * @version $Revision: 1.2 $
 * @author	Morten Laukvik
 * @link		http://morten.laukvik.no
 * @since	1.5
 */
public class PropertyTreeModel extends DefaultTreeModel {

    private static final long serialVersionUID = 1L;
    private VProperty root;
    private List<TreeModelListener> treeModelListeners = new ArrayList<>();

    public PropertyTreeModel(TreeNode root) {
        super(root);
    }

    public PropertyTreeModel(VProperty root) {
        super(new DefaultMutableTreeNode(null));
        this.root = root;

    }

    public void setRoot(VProperty root) {
        this.root = root;
    }

    public VProperty getRoot() {
        return root;
    }

    public VProperty getRootProperty() {
        return new VProperty(null);
    }

    public void setRootProperty(VProperty property) {
    }

    public VProperty getChild(Object parent, int index) {
        VProperty prop = (VProperty) parent;
        return prop.get(index);
    }

    public int getChildCount(Object parent) {
        VProperty prop = (VProperty) parent;
        return prop.list().size();
    }

    public boolean isLeaf(Object node) {
        VProperty prop = (VProperty) node;
        return !prop.isGroup();
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        VProperty prop = (VProperty) path.getLastPathComponent();
        try {
            String value = newValue.toString();
            String key = value.substring(0, value.indexOf(":"));
            String val = value.substring(value.indexOf(":") + 1);
            prop.setName(key);
            prop.setValue(val);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getIndexOfChild(Object parent, Object child) {
        VProperty par = (VProperty) parent;
        VProperty chi = (VProperty) child;
        Collection<VProperty> properties = par.list();
        int index = -1;
        for (VProperty prop : properties) {
            index++;
            if (prop == chi) {
                return index;
            }
        }
        return -1;
    }

    public void addProperty(JTree tree, VProperty child) {
        TreePath path = tree.getLeadSelectionPath();
        VProperty parent = (VProperty) path.getLastPathComponent();
        if (parent == null) {
            // Do nothing
        } else if (parent.isGroup()) {
            // Add only when node is a group
            child.setParent(parent);
            parent.add(child);
            TreePath pathNew = path.pathByAddingChild(child);

            Object[] children = new Object[]{getRootProperty()};
            fireTreeStructureChanged(getRootProperty(), children);
            //
            fireTreeStructureChanged(parent, pathNew.getPath());

            tree.setSelectionPath(pathNew);
            tree.scrollPathToVisible(pathNew);
        } else {
            // Cant add to non-groups
        }
    }

    public void addGroup(JTree tree, VProperty child) {
        TreePath path = tree.getLeadSelectionPath();
        VProperty parent = (VProperty) path.getLastPathComponent();
        if (parent.isGroup()) {
            parent.setIsGroup(true);
            child.setParent(parent);
            parent.add(child);

            Object[] children = new Object[]{getRootProperty()};
            fireTreeStructureChanged(getRootProperty(), children);

            TreePath pathNew = path.pathByAddingChild(child);
            fireTreeStructureChanged(parent, pathNew.getPath());

            tree.setSelectionPath(pathNew);
            tree.scrollPathToVisible(pathNew);
        } else {
            // Dont allow to add groups to non-groups
        }
    }

    public void removeProperty(JTree tree) {
        if (tree.getLastSelectedPathComponent() == null) {
            return;
        }
        VProperty prop = (VProperty) tree.getLastSelectedPathComponent();
        VProperty parent = prop.getParent();
        int index = getIndexOfChild(parent, prop);
        if (index > - 1 && index < parent.length()) {
            parent.remove(index);
        }
        tree.updateUI();
    }

    /**
     * Removes a listener previously added with addTreeModelListener().
     */
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.remove(l);
    }

    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     */
    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.add(l);
    }

    /**
     * The only event raised by this model is TreeStructureChanged with the root
     * as path, i.e. the whole tree has changed.
     */
    protected void fireTreeStructureChanged(VProperty oldRoot, Object path[]) {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this, path);
        for (int i = 0; i < len; i++) {
            treeModelListeners.get(i).treeStructureChanged(e);
        }
    }

}
