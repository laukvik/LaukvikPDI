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

import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import org.laukvik.pdi.PDIManager;
import org.laukvik.pdi.VParameter;
import org.laukvik.pdi.VProperty;

/**
 * A Swing application that enables you to view VCalendar, VCard and other
 * files that are formatted in the same manner.
 *
 * @version  $Revision: 1.3 $
 * @author	Morten Laukvik
 * @link		http://morten.laukvik.no
 * @since	1.5
 */
public class Viewer extends JFrame implements FocusListener, TreeSelectionListener, DropTargetListener{

	public static final String applicationName      = "Viewer";
	private static final String aboutTitle   = applicationName;
	private static final String aboutMessage =
								"Written by Morten Laukvik 2005\n" +
								"using the LaukvikPDI. Please visit\n" +
								"my homepage at http://morten.laukvik.no\n" +
								"for more information about me or LaukvikPDI.   \n\n" +
								"Copyright (C) 2004 Morten Laukvik.";
	private static final String authorURL = "http://morten.laukvik.no";
	private static final long serialVersionUID = 1L;

    private PropertyTreeModel treeModel;
	private javax.swing.JTree tree;
	private ParameterTableModel tableModel;
	private javax.swing.JTable table;
    private DefaultTreeCellRenderer renderer;
	private JMenu openRecentMenu;
	private String focus;
	private boolean saved = true;
	private JLabel status;
	private JButton addGroupButton;
	private JButton addPropertyButton;
	private JButton addParameterButton;
	private JButton removeButton;
	private JButton importBinaryButton;
	private JButton exportBinaryButton;
	private JButton gotoURLButton;
	private ResourceBundle language;

	public Viewer(){
            super();
            status = new JLabel();
            renderer = new DefaultTreeCellRenderer();
            tableModel = new ParameterTableModel(null);
            tree = new javax.swing.JTree(treeModel);
            treeModel = new PropertyTreeModel(new VProperty(null));
		language = Helper.getLanguage();
		initComponents();
		status.setEnabled(false);
		status.setPreferredSize( new Dimension(32000,24) );
		setIconImage( Helper.getImage("new.gif") );
		tree.addFocusListener( this );
		tree.setAutoscrolls(true);


		// Create the DropTarget and register it
		new DropTarget(tree, DnDConstants.ACTION_COPY_OR_MOVE, this, true, null);


		table.addFocusListener( this );
		updateRecentItems();
		newDocument();
	}



//    public void actionPerformed(java.awt.event.ActionEvent evt) {
//    		String command = evt.getActionCommand();
//    		if (command.equalsIgnoreCase("newdocument")){
//    			newDocument();
//    		} else if (command.equalsIgnoreCase("recent")){
//    		}
//    }

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	/**
	 * Reloads the list of recent items and updates the menu with
	 * the results
	 *
	 */
    public void updateRecentItems(){
    		openRecentMenu.removeAll();
		List <String> filenames = Helper.getRecentItemsList();
		for (Object filename : filenames){
			JMenuItem item = new JMenuItem( (String) filename );
			item.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            		JMenuItem openItem = (JMenuItem)evt.getSource();
	            		openFileOrURL( openItem.getText()  );
	            }

	        });
			openRecentMenu.add( item );
		}
		openRecentMenu.addSeparator();
		JMenuItem item = new JMenuItem( language.getString("file.openrecent.empty") );
		item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            		clearRecentItems();
            }
        });
		openRecentMenu.add( item );
    }

    /**
     * Empties the list of recent items and empties the menu as well
     *
     */
    public void clearRecentItems(){
    		Helper.clearRecentItemsList();
    		updateRecentItems();
    }

	/**
	 * Creates the user interface components and initializes them
	 *
	 */
	public void initComponents(){
		renderer.setLeafIcon( Helper.getIcon("property.gif") );
		renderer.setOpenIcon( Helper.getIcon("groupopen.gif") );
		renderer.setClosedIcon( Helper.getIcon("groupclosed.gif") );
		tree.setCellRenderer( renderer );

		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);


		addGroupButton = new JButton( language.getString("toolbar.addgroup") );
		addGroupButton.setIcon( renderer.getClosedIcon() );
		addGroupButton.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            if (tree.getLastSelectedPathComponent() == null){
	            		return;
	            }
	            handleAddGroup();
	        }
	    });


		addPropertyButton = new JButton(language.getString("toolbar.addproperty"));
		addPropertyButton.setIcon( renderer.getLeafIcon() );
	    	addPropertyButton.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            if (tree.getLastSelectedPathComponent() == null){
	            		return;
	            }
	            handleAddProperty();
	        }
	    });

		addParameterButton = new JButton(language.getString("toolbar.addparameter"));
		addParameterButton.setIcon( Helper.getIcon("parameter.gif") );
		addParameterButton.addActionListener(new java.awt.event.ActionListener() {
		        public void actionPerformed(java.awt.event.ActionEvent evt) {
		        		handleAddParameter();
		        }
		    });


		removeButton = new JButton(language.getString("toolbar.remove"));
		removeButton.setIcon( Helper.getIcon("delete.gif") );
		removeButton.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        		handleRemove();
	        }
	    });

		importBinaryButton = new JButton(language.getString("toolbar.import"));
		importBinaryButton.setIcon( Helper.getIcon("importbinary.gif") );
		importBinaryButton.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        		handleImportBinary();
	        }
	    });

		exportBinaryButton = new JButton(language.getString("toolbar.export"));
		exportBinaryButton.setIcon( Helper.getIcon("exportbinary.gif") );
		exportBinaryButton.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        		handleExportBinary();
	        }
	    });

		gotoURLButton = new JButton(language.getString("toolbar.browse"));
		gotoURLButton.setIcon( Helper.getIcon("gotourl.gif") );
		gotoURLButton.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	        		handleGotoURL();
	        }
	    });

		toolbar.add( addGroupButton );
		toolbar.add( addPropertyButton );
		toolbar.add( addParameterButton );
		toolbar.add( new JToolBar.Separator() );
		toolbar.add( removeButton );
		toolbar.add( new JToolBar.Separator() );
		toolbar.add( importBinaryButton );
		toolbar.add( exportBinaryButton );
		toolbar.add( new JToolBar.Separator() );
		toolbar.add( gotoURLButton );
		//
		javax.swing.JSplitPane jSplitPane = new javax.swing.JSplitPane();
		javax.swing.JScrollPane scrollTree = new javax.swing.JScrollPane();

		javax.swing.JScrollPane scrollTable = new javax.swing.JScrollPane();
		table = new javax.swing.JTable( tableModel );


		tree.addTreeSelectionListener( this );
		tree.setLargeModel( true );

        setLayout(new java.awt.BorderLayout());

        add( toolbar, java.awt.BorderLayout.NORTH );

        add( status, java.awt.BorderLayout.SOUTH );

        jSplitPane.setDividerLocation(400);
        jSplitPane.setResizeWeight( 1 );
        scrollTree.setViewportView(tree);

        jSplitPane.setLeftComponent(scrollTree);
        scrollTable.setViewportView(table);
        jSplitPane.setRightComponent(scrollTable);
        add(jSplitPane, java.awt.BorderLayout.CENTER);



		// Create
		JMenuBar bar = new JMenuBar();
		JMenu fileMenu = new JMenu( language.getString("file") );
		JMenuItem newMenuItem = new JMenuItem(language.getString( "file.new" ));
		newMenuItem.setIcon( Helper.getIcon("new.gif") );
		newMenuItem.setAccelerator( Helper.getKeystroke( KeyEvent.VK_N ) );
        	newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newDocument();
            }
        });
		JMenuItem openMenuItem = new JMenuItem(language.getString("file.open"));
		openMenuItem.setIcon( Helper.getIcon("open.gif") );
		openMenuItem.setAccelerator( Helper.getKeystroke( KeyEvent.VK_O ) );
        	openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleOpen();
            }
        });
    		JMenuItem openUrlMenuItem = new JMenuItem(language.getString("file.openurl"));
    		openUrlMenuItem.setIcon( Helper.getIcon("openurl.gif") );
    		openUrlMenuItem.setAccelerator( Helper.getKeystroke( KeyEvent.VK_U ) );
            	openUrlMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    handleOpenURL();
                }
            });

         openRecentMenu = new JMenu(language.getString("file.openrecent"));
         openRecentMenu.setIcon( Helper.getIcon("open.gif") );

    		JMenuItem saveMenuItem = new JMenuItem(language.getString("file.save"));
    		saveMenuItem.setIcon( Helper.getIcon("save.gif") );
    		saveMenuItem.setAccelerator( Helper.getKeystroke( KeyEvent.VK_S ) );
            saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                		handleSave();
                }
            });

    		JMenuItem saveAsMenuItem = new JMenuItem(language.getString("file.saveas"));
    		saveAsMenuItem.setIcon( Helper.getIcon("saveas.gif") );
            	saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    handleSaveAs();
                }
            });

    		JMenuItem printMenuItem = new JMenuItem(language.getString("file.print"));
    		printMenuItem.setIcon( Helper.getIcon("print.gif") );
    		printMenuItem.setAccelerator( Helper.getKeystroke( KeyEvent.VK_P ) );
    		printMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                		handlePrint();
                }
            });

		JMenuItem exitMenuItem = new JMenuItem(language.getString("file.exit"));
		exitMenuItem.setAccelerator( Helper.getKeystroke( KeyEvent.VK_X ) );
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            		handleQuit();
            }
        });



        fileMenu.add( newMenuItem );
        fileMenu.addSeparator();
		fileMenu.add( openMenuItem );
		fileMenu.add( openRecentMenu );
		fileMenu.add( openUrlMenuItem );
		fileMenu.addSeparator();
		fileMenu.add( saveMenuItem );
		fileMenu.add( saveAsMenuItem );
		fileMenu.addSeparator();
		fileMenu.add( printMenuItem );
		fileMenu.addSeparator();
		fileMenu.add( exitMenuItem );

		JMenuItem aboutMenuItem = new JMenuItem(language.getString("help.about"));
		aboutMenuItem.setIcon( Helper.getIcon("help.gif") );
		aboutMenuItem.setAccelerator( Helper.getKeystroke( KeyEvent.VK_HELP ) );
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleAbout();
            }
        });
		JMenuItem authorMenuItem = new JMenuItem(language.getString("help.homepage"));
		authorMenuItem.setIcon( Helper.getIcon("homepage.gif") );
        authorMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleOpenHomepage();
            }
        });
        // Close program when window closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
            		handleQuit();
            }
        });
        JMenu helpMenu = new JMenu( language.getString("help") );

        helpMenu.add( authorMenuItem );
        helpMenu.add( new JSeparator() );
        helpMenu.add( aboutMenuItem );

		bar.add( fileMenu );
		bar.add( helpMenu );
		//
		setJMenuBar( bar );

		tree.setVisible(false);

		// Mac stuff
    		if (System.getProperty( "os.name" ).startsWith("Mac") ){
	    		scrollTree.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    		scrollTree.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		}

		//
		setVisible(true);
		setSize( 640 , 450 );
	}


	/**
	 * Return true if new documents can be created
	 * @return
	 */
	public boolean confirmNotSaved(){
		if (isSaved()){
			return true;
		}
		String title = "";
		String message = language.getString( "dialog.savebeforeclose" );
		String save = language.getString( "dialog.savebeforeclose.save" );
		String dont = language.getString( "dialog.savebeforeclose.dont" );
		String cancel = language.getString( "dialog.savebeforeclose.cancel" );

		Object[] options = { save, dont, cancel };
		int n = JOptionPane.showOptionDialog( this,
		    message,
		    title,
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    options,
		    options[2]
		);
		switch (n){
			case JOptionPane.YES_OPTION:
				handleSave();
				return isSaved();
			case JOptionPane.NO_OPTION:
				return true;
			case JOptionPane.CANCEL_OPTION:
				return false;
			default:
				return true;
		}
	}

	public void newDocument(){
		setStatus("Ready");
		if (confirmNotSaved()){
                    VProperty root = new VProperty(null);
			root.setName("BEGIN");
			root.setValue("VCARD");
			treeModel.setRoot( root);
			tableModel.setProperty( root );
			table.updateUI();
			table.getColumnModel().getColumn(0).setMinWidth( 24 );
			table.getColumnModel().getColumn(0).setMaxWidth( 24 );
			table.getColumnModel().getColumn(0).setPreferredWidth( 24 );
			table.setDefaultRenderer( ParameterCellRenderer.class, new ParameterCellRenderer() );
			tree.updateUI();

			setOpenedFile(null);
			tree.setVisible(true);
		} else {
			// Do nothing
		}
	}

	public void handleOpen(){
		java.awt.FileDialog fd = new java.awt.FileDialog( this, language.getString("dialog.file.open"), java.awt.FileDialog.LOAD );
		fd.setDirectory( System.getProperty("user.home") );
		fd.setVisible(true);
		if (fd.getFile() != null){
			java.io.File file = new java.io.File( fd.getDirectory(), fd.getFile()  );
			open( file );
		}
	}

	public void handleAddGroup(){
            VProperty child = new VProperty(null);
            child.setName(VProperty.BEGIN_GROUP);
        child.setValue( "Group" );
        treeModel.addGroup( tree, child );
        setSaved(false);
	}

	public void handleAddProperty(){
            VProperty child = new VProperty(null);
        child.setName("Forfatter");
        child.setValue("Morten");
        treeModel.addProperty( tree, child );
        setSaved(false);
	}

	public void handleRemove(){
		if (focus == null){

		} else if (focus.equalsIgnoreCase("tree")){
			treeModel.removeProperty( tree );
			tableModel.setProperty(null);
			setSaved(false);
		} else if (focus.equalsIgnoreCase("table")){
			tableModel.removeRows( table.getSelectedRows(), table );
			setSaved(false);
		}
	}

	public void handleAddParameter(){
            tableModel.addParameter(new VParameter("name", "value"), table);
        setSaved(false);
	}

	public void setStatus( Object status ){
		this.status.setText( "  Status: " + status.toString() );
	}

	public void setStatus( String status ){
		this.status.setText( "  Status: " + status );
	}

    public void handleOpenURL(){
    		if (confirmNotSaved()){
    			String surl = getInput();
    			if (surl != null){
    				openFileOrURL( surl );
    			}
    		} else {
    			// Do nothing
    		}
    }



	public void handleSave(){
		if (getOpenedFile() == null || getOpenedFile().length() == 0){
			handleSaveAs();
		} else if (getOpenedFile().startsWith("http:")){
			handleSaveAs();
		} else {
			File file = new File( getOpenedFile() );
			saveFile( file );
		}
	}

	/**
	 * Reloads the same file
	 *
	 */
	public void handleReload(){
		if (getOpenedFile() == null || getOpenedFile().length() == 0){
			open( getOpenedFile() );
		}
	}

	public void handlePrint(){
		if (focus == null){

		} else if (focus.equalsIgnoreCase("tree")){
			PrintUtilities.printComponent(tree);
		} else if (focus.equalsIgnoreCase("table")){
			PrintUtilities.printComponent(table);
		}
	}

	public void handleSaveAs(){
		java.awt.FileDialog fd = new java.awt.FileDialog( this, language.getString("dialog.file.saveas"), java.awt.FileDialog.SAVE );
		fd.setDirectory( System.getProperty("user.home") );
		fd.setVisible(true);
		if (fd.getFile() != null){
			java.io.File file = new java.io.File( fd.getDirectory(), fd.getFile()  );
			saveFile( file );
		}
	}

	private void saveFile( File file){
		try {
			Date from = new Date();
			PDIManager.save( treeModel.getRoot(), file );
			setOpenedFile( file.getAbsolutePath() );
			setSaved(true);
			Date to = new Date();
			long duration = to.getTime() -  from.getTime();
			setStatus("Saved file in " + duration + " milliseconds.");
		} catch (Exception e) {
			showError( e );
		}
	}

	public void handleImportBinary(){
		Object obj = tree.getLastSelectedPathComponent();
            if (obj instanceof VProperty) {
                VProperty prop = (VProperty) obj;
			java.awt.FileDialog fd = new java.awt.FileDialog( this, language.getString("dialog.import.binary"), java.awt.FileDialog.LOAD );
			fd.setDirectory( System.getProperty("user.home") );
			fd.setVisible(true);
			if (fd.getFile() != null){
				java.io.File file = new java.io.File( fd.getDirectory(), fd.getFile()  );
				try {
					PDIManager.readBinaryValueFromFile( prop, file );
					showInfo( "File successfully imported" );
				} catch (Exception e) {
					showError( e );
				}
			}
		} else {
			// Do nothing
		}
	}

	public void handleExportBinary(){
		Object obj = tree.getLastSelectedPathComponent();
            if (obj instanceof VProperty) {
                VProperty prop = (VProperty) obj;
			java.awt.FileDialog fd = new java.awt.FileDialog( this, language.getString("dialog.export.binary"), java.awt.FileDialog.SAVE );
			fd.setDirectory( System.getProperty("user.home") );
			fd.setVisible(true);
			if (fd.getFile() != null){
				java.io.File file = new java.io.File( fd.getDirectory(), fd.getFile()  );
				try {
					PDIManager.writeBinaryValueToFile( prop, file );
					showInfo( "File successfully exported." );
				} catch (Exception e) {
					showError( e );
				}
			}
		} else {
			// Do nothing
		}
	}

	/**
	 * Opens and parses the given URL and shows the result
	 *
	 * @param url the URL to open
	 */
	public void open( URL url ){
		try {
			tree.setEditable(false);
                    VProperty prop = PDIManager.read(url);
			treeModel.setRoot( prop );
			setOpenedFile( url.toExternalForm() );
			tree.updateUI();
			tree.setEditable(true);
			tree.setVisible(true);
		} catch (Exception e) {
			setOpenedFile(null);
			showError( e );
		}
	}

	public void open( String file ){
		open( new File(file) );
	}

	private void openFileOrURL( String file ) {
		if (file == null || file.length() == 0){
			// Do nothing
		} else if ( file.startsWith("http://") || file.startsWith("ftp://") || 	file.startsWith("file://")  ){
			try {
				open( new URL(file) );
			} catch (MalformedURLException e) {
				showError(e);
			}
		} else if (file.startsWith("webcal://")){
			String filename = file.replaceFirst("webcal:","http:") + "";
			try {
				open( new URL( filename ) );
			} catch (MalformedURLException e) {
				showError(e);
			}
		} else {
			open( file );
		}
	}

	/**
	 * Parses and shows a file
	 *
	 * @param file the file to parse and show
	 */
	public void open( File file ){
		try {
			Date from = new Date();
			tree.setEditable(false);
			setOpenedFile( null );
                    VProperty prop = PDIManager.read(file);
			treeModel.setRoot( prop );
			setOpenedFile( file.getAbsolutePath() );
			tree.updateUI();
			tree.setEditable(true);
			tree.setVisible(true);
			Date to = new Date();
			long duration = to.getTime() -  from.getTime();
			setStatus("Opened file in " + duration + " milliseconds.");
		} catch (Exception e) {
			showError( e );
		}
	}

	/**
	 * Sets the currently opened file
	 *
	 * @param filename the name of the currently opened file
	 */
	public void setOpenedFile( String filename ){
		TreePath path = new TreePath( treeModel.getRoot() );
		tree.setSelectionPath( path );
		if (filename == null){
			setTitle( "" );
		} else {
			setTitle( filename );
			Helper.addToRecentItemsList( filename );
			updateRecentItems();
		}
	}

	/**
	 * Returns the currently opened file
	 *
	 * @return the currently opened file
	 */
	public String getOpenedFile(){
		return getTitle();
	}

	/**
	 * Shows a dialog box and displays the message found in the specified
	 * exception
	 *
	 * @param e The exception to show
	 */
	private void showError( Exception e ){
		String message = e.getMessage();
		JOptionPane.showMessageDialog(this, message, applicationName, JOptionPane.ERROR_MESSAGE );
	}

	private void showInfo( String message ){
		JOptionPane.showMessageDialog(this, message, applicationName, JOptionPane.INFORMATION_MESSAGE );
	}

	/**
	 * Shows a dialog box with the about information
	 *
	 */
    public void handleAbout(){
		JOptionPane.showMessageDialog(this, aboutMessage, aboutTitle, JOptionPane.INFORMATION_MESSAGE, Helper.getIcon("logo.png") );
    }

    /**
     * Fires when the <code>Exit</code> menu item is performed
     *
     */
    public void handleQuit(){
    		if (confirmNotSaved()){
    			System.exit(0);
    		}
    }

    /**
     * Fires when the <code>Preferences</code> menu item is performed
     *
     */
    public void handlePrefs(){

    }

    /**
     * Opens a dialog box and prompts the user for a URL to open. After the user
     * has closed the dialog box, the method returns the String entered. If nothing
     * is entered the method returns <code>null</code>
     *
     * @return the user entered url to open
     */
    public String getInput(){
    		String defaultAnswer = "http://www.?.com";
    		String title ="Opening a file from the web";
    		String message = "Please enter the full URL of the file";
	    	String s = (String)JOptionPane.showInputDialog( this,message,title, JOptionPane.QUESTION_MESSAGE, null, null, defaultAnswer);
	    	return s;
    }

    public void handleGotoURL(){
        VProperty prop = (VProperty) tree.getLastSelectedPathComponent();
    		openInBrowser( prop.getValue() );
    }

    public void openInBrowser( String url ){
		try {
			Runtime.getRuntime().exec( "open " + url );
		} catch (IOException e) {
			showError( e );
		}
    }


	/**
	 * Opens the homepage of the author of this software
	 *
	 */
	public void handleOpenHomepage(){
		openInBrowser( authorURL );
	}

	public void focusGained(FocusEvent e) {
		String classname = e.getSource().getClass().toString();
		if (classname.indexOf("JTree") > -1){
			this.focus = "tree";
		} else if (classname.indexOf("JTable") > -1){
			this.focus = "table";
		} else {
			this.focus = null;
		}
	}

	public void focusLost(FocusEvent e) {
	}

	public void valueChanged(TreeSelectionEvent e) {
		setStatus( "" );
		if (e.getNewLeadSelectionPath() != null) {
			Object last = e.getNewLeadSelectionPath().getLastPathComponent();

			if (last != null) {
                            VProperty prop = (VProperty) last;
				tableModel.setProperty(prop);
				table.updateUI();

				if (prop.isRoot()){
					// Root node
					addGroupButton.setEnabled( true );
					addPropertyButton.setEnabled( true );
					addParameterButton.setEnabled( false );
					removeButton.setEnabled( false );
					importBinaryButton.setEnabled(false);
					exportBinaryButton.setEnabled(false);
					gotoURLButton.setEnabled(false);
				} else if (prop.isGroup()){
					// Group nodes
					addGroupButton.setEnabled( true );
					addPropertyButton.setEnabled( true );
					addParameterButton.setEnabled( false );
					removeButton.setEnabled( true );
					importBinaryButton.setEnabled(false);
					exportBinaryButton.setEnabled(false);
					gotoURLButton.setEnabled(false);
				} else {
					// Properties
					addGroupButton.setEnabled( false );
					addPropertyButton.setEnabled( false );
					addParameterButton.setEnabled( true );
					removeButton.setEnabled( true );
					if (prop.isBinary()){
						importBinaryButton.setEnabled(true);
						exportBinaryButton.setEnabled(true);
					} else {
						importBinaryButton.setEnabled(true);
						exportBinaryButton.setEnabled(false);
					}
					if (prop.getName().toLowerCase().endsWith("url")){
						gotoURLButton.setEnabled(true);
					} else {
						gotoURLButton.setEnabled(false);
					}
				}

				String stat = "";
                            for (VProperty prop1 : prop.listPath()) {
					stat += prop1.getValue() + "/";
				}

				setStatus( stat + "  Index: " + prop.index() );
			} else {
				// Do nothing
			}

		}
		table.updateUI();
	}

	public static void main(String[] args){
		Viewer view = new Viewer();
		if (args != null && args.length > 0){
			view.open( new File( args[0] ) );
		}
	}


	public void dragEnter(DropTargetDragEvent dtde) {
		acceptOrRejectDrag(dtde);
	}

	public void dragExit(DropTargetEvent dte) {
	}

	public void dragOver(DropTargetDragEvent dtde) {
		acceptOrRejectDrag(dtde);
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		acceptOrRejectDrag(dtde);
	}

	public void drop(DropTargetDropEvent dtde) {
		if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0){
			dtde.acceptDrop( dtde.getDropAction() );
			Transferable transferable = dtde.getTransferable();
			try{
				boolean result = dropFile(transferable);
				dtde.dropComplete(result);
			} catch (Exception e){
				dtde.dropComplete(false);
			}
		} else {
			dtde.rejectDrop();
		}
	}

	protected boolean acceptOrRejectDrag( DropTargetDragEvent dtde ){
		int dropAction = dtde.getDropAction();
		int sourceActions = dtde.getSourceActions();
		boolean acceptDrag = false;
		boolean acceptableType = dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
		if (!acceptableType || (sourceActions & DnDConstants.ACTION_COPY_OR_MOVE) == 0){
			dtde.rejectDrag();
		} else if ( (dropAction & DnDConstants.ACTION_COPY_OR_MOVE) == 0 ){
			dtde.acceptDrag( DnDConstants.ACTION_COPY);
			acceptDrag = true;
		} else {
			dtde.acceptDrag(dropAction);
			acceptDrag = true;
		}
		return acceptDrag;
	}


	protected boolean dropFile( Transferable transferable ) throws IOException, UnsupportedFlavorException, MalformedURLException{
		List fileList = (List) transferable.getTransferData( DataFlavor.javaFileListFlavor );
		File transferFile = (File) fileList.get(0);
		if (fileList.size() > 1){
			return false;
		} else if (transferFile.isDirectory()){
			return false;
		} else {
			open( transferFile );
			return true;
		}
	}

}