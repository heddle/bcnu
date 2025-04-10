package cnuphys.ced.frame;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.jlab.logging.DefaultLogger;

import cnuphys.bCNU.application.BaseMDIApplication;
import cnuphys.bCNU.application.Desktop;
import cnuphys.bCNU.component.MagnifyWindow;
import cnuphys.bCNU.dialog.TextDisplayDialog;
import cnuphys.bCNU.fortune.FortuneManager;
import cnuphys.bCNU.graphics.ImageManager;
import cnuphys.bCNU.menu.MenuManager;
import cnuphys.bCNU.ping.Ping;
import cnuphys.bCNU.util.Environment;
import cnuphys.bCNU.util.FileUtilities;
import cnuphys.bCNU.util.Jar;
import cnuphys.bCNU.util.PropertySupport;
import cnuphys.bCNU.view.PlotView;
import cnuphys.bCNU.view.ViewManager;
import cnuphys.bCNU.view.VirtualView;
import cnuphys.bCNU.wordle.Wordle;
import cnuphys.ced.alldata.DataWarehouse;
import cnuphys.ced.ced3d.view.AlertView3D;
import cnuphys.ced.ced3d.view.CentralView3D;
import cnuphys.ced.ced3d.view.FMTView3D;
import cnuphys.ced.ced3d.view.FTCalView3D;
import cnuphys.ced.ced3d.view.ForwardView3D;
import cnuphys.ced.ced3d.view.SwimmingTestView3D;
import cnuphys.ced.cedview.alert.AlertXYView;
import cnuphys.ced.cedview.alldc.AllDCView;
import cnuphys.ced.cedview.allec.ECView;
import cnuphys.ced.cedview.allpcal.PCALView;
import cnuphys.ced.cedview.central.CentralXYView;
import cnuphys.ced.cedview.central.CentralZView;
import cnuphys.ced.cedview.dchex.DCHexView;
import cnuphys.ced.cedview.dcxy.DCXYView;
import cnuphys.ced.cedview.ft.FTCalXYView;
import cnuphys.ced.cedview.ftof.FTOFView;
import cnuphys.ced.cedview.sectorview.DisplaySectors;
import cnuphys.ced.cedview.sectorview.SectorView;
import cnuphys.ced.cedview.urwell.UrWELLXYView;
import cnuphys.ced.clasio.ClasIoEventManager;
import cnuphys.ced.clasio.ClasIoEventMenu;
import cnuphys.ced.clasio.ClasIoEventView;
import cnuphys.ced.clasio.ClasIoMonteCarloView;
import cnuphys.ced.clasio.ClasIoReconEventView;
import cnuphys.ced.clasio.filter.FilterManager;
import cnuphys.ced.component.DrawingLegendDialog;
import cnuphys.ced.dcnoise.edit.NoiseParameterDialog;
import cnuphys.ced.event.AccumulationManager;
import cnuphys.ced.geometry.GeometryManager;
import cnuphys.ced.magfield.PlotFieldDialog;
import cnuphys.ced.noise.NoiseManager;
import cnuphys.ced.properties.PropertiesManager;
import cnuphys.ced.swim.SwimAllMC;
import cnuphys.ced.swim.SwimAllRecon;
import cnuphys.ced.trigger.TriggerDialog;
import cnuphys.ced.trigger.TriggerManager;
import cnuphys.ced.trigger.TriggerMenuPanel;
import cnuphys.lund.X11Colors;
import cnuphys.magfield.MagneticFieldChangeListener;
import cnuphys.magfield.MagneticFields;
import cnuphys.simanneal.example.ising2D.Ising2DDialog;
import cnuphys.simanneal.example.ts.TSDialog;
import cnuphys.splot.example.MemoryUsageDialog;
import cnuphys.swim.SwimMenu;
import cnuphys.swim.Swimmer;

@SuppressWarnings("serial")
public class Ced extends BaseMDIApplication implements MagneticFieldChangeListener {

	// a shared ping
	private Ping _ping;

	// the singleton
	private static Ced _instance;

	// geometry variation
	private static String _geoVariation = "default";

	// ced release
	public static final String release = "1.9.3t";

	//minimum java major version
	private static final int _minJavaVersion = 17;

	// used for one time inits
	private int _firstTime = 0;

	// for the event count
	private JMenuItem _eventCountLabel;

	// experimental version?
	private static boolean _experimental;

	// the coat java clasdir
	private static File _clasDir;

	// coat java version;
	private static String _coatjavaVersion;

	// event menu
	private ClasIoEventMenu _eventMenu;

	// color menu
	private ColorMenu _colorMenu;

	// weird menu
	private JMenu _weirdMenu;

	// warning label that filtering is active
	private JLabel _filterLabel;

	// event number label on menu bar
	private static JLabel _eventNumberLabel;

	// memory usage dialog
	private MemoryUsageDialog _memoryUsage;

	// Environment display
	private TextDisplayDialog _envDisplay;

	// show which filters are active
	private JMenu _eventFilterMenu;

	// for plotting the field
	private PlotFieldDialog _plotFieldDialog;

	// some views
	private AllDCView _allDCView;
	private VirtualView _virtualView;
	private ClasIoMonteCarloView _monteCarloView;
	private ClasIoReconEventView _reconEventView;
	private ClasIoEventView _eventView;
	private CentralXYView _centralXYView;
	private CentralZView _centralZView;

	private AlertXYView _alertXYView;

//	private RTPCView _rtpcView;
	private FTCalXYView _ftcalXyView;
	private DCXYView _dcXyView;
	private DCHexView _dcHexView;
	private UrWELLXYView _urwellXyView;

	private ECView _ecView;
	private PCALView _pcalView;
	private ForwardView3D _forward3DView;

	private AlertView3D _alert3DView;
	private FMTView3D _fmt3DView;

	private SwimmingTestView3D _swimming3DView;
	private CentralView3D _central3DView;
	private FTCalView3D _ftCal3DView;

	private FTOFView _ftofView;


	// sector views
	private SectorView _sectorView14;
	private SectorView _sectorView25;
	private SectorView _sectorView36;

	// plot view
	private PlotView _plotView;

	// the about string
	private static String _aboutString = "<html><span style=\"font-size:12px\">ced: the cLAS eVENT dISPLAY&nbsp;&nbsp;&nbsp;&nbsp;"
			+ release + "<br><br>Developed by Christopher Newport University"
			+ "<br><br>Download the latest version at <a href=\"https://userweb.jlab.org/~heddle/ced/builds/\">https://userweb.jlab.org/~heddle/ced/builds/</a>"
			+ "<br><br>Email bug reports to david.heddle@cnu.edu";

	// for the traveling salesperson dialog
	private TSDialog _tsDialog;

	// for the ising model 2D dialog
	private Ising2DDialog _i2dDialog;

	// set whether data banks are floating
	private JCheckBoxMenuItem _floatingBankDisplayCB;

	// set whether clusters are connected
	private JCheckBoxMenuItem _connectClusterCB;


	//got Gagik yo color DC hits based on order
	public static boolean useOrderColoring = false;


	/**
	 * Constructor (private--used to create singleton)
	 *
	 * @param keyVals an optional variable length list of attributes in type-value
	 *                pairs. For example, PropertySupport.NAME, "my application",
	 *                PropertySupport.CENTER, true, etc.
	 */
	private Ced(Object... keyVals) {
		super(keyVals);

		// one second maintenance timer
		_ping = new Ping(1000);


		ComponentListener cl = new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent ce) {
			}

			@Override
			public void componentMoved(ComponentEvent ce) {
				placeViewsOnVirtualDesktop();
			}

			@Override
			public void componentResized(ComponentEvent ce) {
				placeViewsOnVirtualDesktop();
			}

			@Override
			public void componentShown(ComponentEvent ce) {
				placeViewsOnVirtualDesktop();
			}

		};

		addComponentListener(cl);
	}

	/**
	 * Get the common Ping object
	 *
	 * @return the ping object
	 */
	public Ping getPing() {
		return _ping;
	}

	// arrange the views on the virtual desktop
	private void placeViewsOnVirtualDesktop() {
		if (_firstTime == 1) {
			// rearrange some views in virtual space
			_virtualView.reconfigure();
			restoreDefaultViewLocations();

			// now load configuration
			Desktop.getInstance().loadConfigurationFile();
			Desktop.getInstance().configureViews();
		}
		_firstTime++;
	}

	/**
	 * Restore the default locations of the default views. Cloned views are
	 * unaffected.
	 */
	private void restoreDefaultViewLocations() {

		_virtualView.moveToStart(_sectorView14, 0, VirtualView.UPPERLEFT);
		_virtualView.moveToStart(_sectorView25, 0, VirtualView.UPPERLEFT);
		_virtualView.moveToStart(_sectorView36, 0, VirtualView.UPPERLEFT);
		_virtualView.moveTo(_plotView, 0, VirtualView.CENTER);  //for bdl plot


		_virtualView.moveTo(_monteCarloView, 1, VirtualView.TOPCENTER);
		_virtualView.moveTo(_reconEventView, 1, VirtualView.BOTTOMCENTER);

		_virtualView.moveTo(_centralXYView, 2, VirtualView.BOTTOMLEFT);
		_virtualView.moveTo(_centralZView, 2, VirtualView.UPPERRIGHT);

		_virtualView.moveTo(_allDCView, 3);
		_virtualView.moveTo(_pcalView, 4, VirtualView.CENTERRIGHT);
		_virtualView.moveTo(_ecView, 4, VirtualView.CENTERLEFT);
		_virtualView.moveTo(_eventView, 5, VirtualView.CENTER);

		// note no constraint means "center"
		_virtualView.moveTo(_dcHexView, 6);

//		_virtualView.moveTo(_rtpcView, 7);
		_virtualView.moveTo(_urwellXyView, 7, VirtualView.BOTTOMLEFT);
		_virtualView.moveTo(_ftofView, 8, VirtualView.UPPERRIGHT);
		_virtualView.moveTo(_ftcalXyView, 9, VirtualView.CENTER);
		_virtualView.moveTo(_dcXyView, 10);

		_virtualView.moveTo(_alertXYView, 11, VirtualView.CENTER);

		_virtualView.moveTo(_alert3DView, 12, VirtualView.CENTER);
		_virtualView.moveTo(_forward3DView, 13, VirtualView.CENTER);
		_virtualView.moveTo(_central3DView, 14, VirtualView.BOTTOMLEFT);
		_virtualView.moveTo(_ftCal3DView, 16, VirtualView.BOTTOMRIGHT);
		_virtualView.moveTo(_fmt3DView, 15, VirtualView.CENTER);

		if (isExperimental()) {
			_virtualView.moveTo(_swimming3DView, 17, VirtualView.CENTER);
		}
	}


	//get a string that tells us what version of coatjava.
	//uses the class path.
	private static String getCoatJavaVersion() {

		if (_coatjavaVersion != null) {
			return _coatjavaVersion;
		}

		String cpat = "coat-libs-";
		String snap = "-SNAP";

		String s = System.getProperty("java.class.path");

		if (s.endsWith("ced.jar")) {

			System.out.println("CP contains ced.jar");

			s = Jar.getManifestAttribute(s, "Class-Path");
//			System.out.println("cp from jar manifest: [" + s + "]");
		}

		int index = s.indexOf(cpat);
		if (index >= 0) {
			_coatjavaVersion = s.substring(index + cpat.length());

			index = _coatjavaVersion.indexOf(snap);

			if (index > 0) {
				_coatjavaVersion = _coatjavaVersion.substring(0, index);
			}

			return _coatjavaVersion;
		}

		return "???";
	}

	/**
	 * Add the initial views to the desktop.
	 */
	private void addInitialViews() {

		// make sure noise listener is instantiated
		NoiseManager.getInstance();

		// add an object that can respond to a "swim all MC" request.

		ClasIoEventManager.getInstance().setAllMCSwimmer(new SwimAllMC());
		ClasIoEventManager.getInstance().setAllReconSwimmer(new SwimAllRecon());

		// make sure accumulation manager is instantiated
		AccumulationManager.getInstance();

		// add a virtual view. Count how many cells are needed

		int numVVCell = 18;

		_virtualView = VirtualView.createVirtualView(numVVCell);
		ViewManager.getInstance().getViewMenu().addSeparator();

		// add event view
		_eventView = ClasIoEventView.createEventView();

		// add three sector views
		ViewManager.getInstance().getViewMenu().addSeparator();
		_sectorView36 = SectorView.createSectorView(DisplaySectors.SECTORS36);
		_sectorView25 = SectorView.createSectorView(DisplaySectors.SECTORS25);
		_sectorView14 = SectorView.createSectorView(DisplaySectors.SECTORS14);
		ViewManager.getInstance().getViewMenu().addSeparator();

		// add monte carlo view
		_monteCarloView = ClasIoMonteCarloView.getInstance();

		// add a reconstructed tracks view
		_reconEventView = ClasIoReconEventView.getInstance();

		ViewManager.getInstance().getViewMenu().addSeparator();

		// add an alldc view
		_allDCView = AllDCView.createAllDCView();

		// add a DC XY View
		_dcXyView = DCXYView.createDCXYView();

		// add a DC Hex View
		_dcHexView = DCHexView.createDCHexView();


		ViewManager.getInstance().getViewMenu().addSeparator();

		// add a bstXYView
		_centralXYView = CentralXYView.createCentralXYView();

		// add a bstZView
		_centralZView = CentralZView.createCentralZView();

		ViewManager.getInstance().getViewMenu().addSeparator();

		// add an ec view
		_ecView = ECView.createECView();

		// add an pcal view
		_pcalView = PCALView.createPCALView();

		ViewManager.getInstance().getViewMenu().addSeparator();

		//add and ALERT XY view
		_alertXYView = AlertXYView.createAlertXYView();

		// add a ftcalxyYView
		_ftcalXyView = FTCalXYView.createFTCalXYView();

		//add a urwell xy view
		_urwellXyView = UrWELLXYView.createUrWELLView();


		// add an RTPC vie
		//_rtpcView = RTPCView.createRTPCView();

        //FTOF
		_ftofView = FTOFView.createFTOFView();

		ViewManager.getInstance().getViewMenu().addSeparator();
		_alert3DView = new AlertView3D();
		_central3DView = new CentralView3D();
		_fmt3DView = new FMTView3D();
		_forward3DView = new ForwardView3D();
		_ftCal3DView = new FTCalView3D();


		if (isExperimental()) {
			_swimming3DView = new SwimmingTestView3D();
		}

		// add logview
		ViewManager.getInstance().getViewMenu().addSeparator();

		_plotView = new PlotView();

		// the trigger bit "view"
		ActionListener al3 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TriggerDialog.showDialog();
			}
		};

		JMenuItem menuItem = new JMenuItem("Trigger Bits");
		menuItem.addActionListener(al3);
		ViewManager.getInstance().getViewMenu().add(menuItem, 1);


		// use config file info
		// Desktop.getInstance().configureViews();

		_virtualView.toFront();
	}


	/**
	 * Accessor for the event menu
	 *
	 * @return the event menu
	 */
	public ClasIoEventMenu getEventMenu() {
		return _eventMenu;
	}

	/**
	 * Add items to existing menus and/or create new menus NOTE: Swim menu is
	 * created by the SwimManager
	 */
	private void createMenus() {
		MenuManager mmgr = MenuManager.getInstance();

		_eventMenu = new ClasIoEventMenu(true, false);
		mmgr.addMenu(_eventMenu);

		// the options menu
		addToOptionMenu(mmgr.getOptionMenu());

		//the color menu
		_colorMenu = new ColorMenu();
		getJMenuBar().add(_colorMenu);

		// ET menu
		// mmgr.addMenu(ETSupport.getETMenu());

		// create the mag field menu
		MagneticFields.getInstance().setActiveField(MagneticFields.FieldType.TORUS);
		addToMagneticFieldMenu();

		// the swimmer menu
		mmgr.addMenu(SwimMenu.getInstance());

		// remove the option menu until I need it
		// mmgr.removeMenu(mmgr.getOptionMenu());

		// add to swim menu
		addToSwimMenu();

		// add to the file menu
		addToFileMenu();

		// add to the event menu
		addToEventMenu();

	}


	// add items to the basic mag field menu
	private void addToMagneticFieldMenu() {
		JMenu magMenu = MagneticFields.getInstance().getMagneticFieldMenu();
		final JMenuItem plotItem = new JMenuItem("Plot the Field...");
//		final JMenuItem reconfigItem = new JMenuItem("Remove Solenoid and Torus Overlap");
//		final JMenuItem samenessItem = new JMenuItem("Sameness Test with/without Overlap Removal");
		magMenu.addSeparator();

		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (e.getSource() == plotItem) {
					if (_plotFieldDialog == null) {
						_plotFieldDialog = new PlotFieldDialog(getCed(), false);
					}

					_plotFieldDialog.setVisible(true);

				}
//				else if (e.getSource() == reconfigItem) {
//					MagneticFields.getInstance().removeMapOverlap();
//				}
//				else if (e.getSource() == samenessItem) {
//					MagTests.samenessTest();
//				}
			}
		};

//		reconfigItem.addActionListener(al);
//		samenessItem.addActionListener(al);
		plotItem.addActionListener(al);
//		magMenu.add(reconfigItem);
//		magMenu.add(samenessItem);
		magMenu.add(plotItem);

		MenuManager.getInstance().addMenu(magMenu);
	}

	// add some fun stuff

	private void addWeirdMenu(JMenu menu) {
		String weirdTitle = "w" + "\u018e" + "i" + "\u1d19" + "d";
		_weirdMenu = new JMenu(weirdTitle);

		final JMenuItem wordleItem = new JMenuItem("Wordle...");
		final JMenuItem fortuneItem = new JMenuItem("Fortune...");
		final JMenuItem tsItem = new JMenuItem("Traveling Salesperson ...");
		final JMenuItem i2dItem = new JMenuItem("2D Ising Model ...");

		ActionListener al1 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();

				if (source == wordleItem) {
					Wordle.getInstance().setVisible(true);
				} else if (source == fortuneItem) {
					FortuneManager.getInstance().showDialog();
				} else if (source == tsItem) {
					if (_tsDialog == null) {
						_tsDialog = new TSDialog();
					}
					_tsDialog.setVisible(true);
				} else if (source == i2dItem) {
					if (_i2dDialog == null) {
						_i2dDialog = new Ising2DDialog();
					}
					_i2dDialog.setVisible(true);
				}
			}
		};

		wordleItem.addActionListener(al1);
		fortuneItem.addActionListener(al1);
		tsItem.addActionListener(al1);
		i2dItem.addActionListener(al1);
		_weirdMenu.add(wordleItem);
		_weirdMenu.add(fortuneItem);
		_weirdMenu.add(tsItem);
		_weirdMenu.add(i2dItem);

		menu.add(_weirdMenu, 0);

	}

	// add to the file menu
	private void addToSwimMenu() {
	}

	// private void run some swim test

	// add to the file menu
	private void addToFileMenu() {
		MenuManager mmgr = MenuManager.getInstance();
		JMenu fmenu = mmgr.getFileMenu();

		fmenu.insertSeparator(0);

		// restore default config
		final JMenuItem defConItem = new JMenuItem("Restore Default Configuration");

		ActionListener al1 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();

				if (source == defConItem) {
					restoreDefaultViewLocations();
					refresh();
				}
			}
		};

		defConItem.addActionListener(al1);
//		fmenu.add(defConItem, 6);
		fmenu.add(defConItem, 0);

		addWeirdMenu(fmenu);

		JMenuItem aboutItem = new JMenuItem("About ced...");
		ActionListener al0 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(Ced.getInstance(), _aboutString, "About ced",
						JOptionPane.INFORMATION_MESSAGE, ImageManager.cnuIcon);

			}
		};
		aboutItem.addActionListener(al0);
		fmenu.add(aboutItem, 0);

		// some event file menus

		fmenu.insertSeparator(0);
		fmenu.add(ClasIoEventMenu.getConnectETItem(), 0);
//		fmenu.add(ClasIoEventMenu.getConnectAnyRingItem(), 0);
		fmenu.insertSeparator(0);

		fmenu.add(ClasIoEventMenu.getRecentEventFileMenu(), 0);
		fmenu.add(ClasIoEventMenu.getOpenEventFileItem(), 0);
	}

	/**
	 * Does the user want the data bank displays to float?
	 * @return <code>true</code> if the user wants the data bank displays to float.
	 */
	public boolean isFloatingBankDisplay() {
		return _floatingBankDisplayCB.isSelected();
	}

	/**
	 * Does the user want the cluster endpoints to be connected?
	 *
	 * @return <code>true</code> if the user wants the cluster endpoints to be
	 *         connected.
	 */
	public boolean isConnectCluster() {
		return _connectClusterCB.isSelected();
	}

	// create the options menu
	private void addToOptionMenu(JMenu omenu) {

		//read default for floating bank displays
		boolean defFloat = true;
		String floatStr = PropertiesManager.getInstance().get("FLOATBANK");
		if (floatStr != null) {
			defFloat = Boolean.parseBoolean(floatStr);
		}

		_floatingBankDisplayCB = new JCheckBoxMenuItem("Bank Views are Free Floating", defFloat);


		_floatingBankDisplayCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// save the new state
				PropertiesManager.getInstance().put("FLOATBANK", "" + _floatingBankDisplayCB.isSelected());
				PropertiesManager.getInstance().writeProperties();
			}
		});

		//read default for connecting clusters
		boolean defCluster = false;
		String clusterStr = PropertiesManager.getInstance().get("CONNECTCLUSTER");
		if (clusterStr != null) {
			defCluster = Boolean.parseBoolean(clusterStr);
		}
		_connectClusterCB = new JCheckBoxMenuItem("Connect Cluster Endpoints", defCluster);
		_connectClusterCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// save the new state
				PropertiesManager.getInstance().put("CONNECTCLUSTER", "" + _connectClusterCB.isSelected());
				PropertiesManager.getInstance().writeProperties();
				Ced.refresh();
			}
		});

		omenu.add(_floatingBankDisplayCB);
		omenu.add(_connectClusterCB);
		omenu.addSeparator();

		omenu.add(MagnifyWindow.magificationMenu());
		omenu.addSeparator();

		final JMenuItem memPlot = new JMenuItem("Memory Usage...");
		final JMenuItem environ = new JMenuItem("Environment...");
		final JMenuItem drawLeg = new JMenuItem("Drawing Symbology...");
		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Object source = e.getSource();

				if (source == memPlot) {
					if (_memoryUsage == null) {
						_memoryUsage = new MemoryUsageDialog(Ced.getFrame());
					}

					_memoryUsage.setVisible(true);
				}

				else if (source == environ) {
					if (_envDisplay == null) {
						_envDisplay = new TextDisplayDialog("Environment Information");
					}
					_envDisplay.setText(Environment.getInstance().toString());
					_envDisplay.setVisible(true);
				}

				else if (source == drawLeg) {
					DrawingLegendDialog.showDialog();
				}

			}

		};


		environ.addActionListener(al);
		memPlot.addActionListener(al);
		drawLeg.addActionListener(al);
		omenu.add(environ);
		omenu.add(memPlot);
		omenu.add(drawLeg);

	}

	/**
	 * Refresh all views (with containers)
	 */
	public static void refresh() {

		if (SwingUtilities.isEventDispatchThread()) {
			refreshAllViews();
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						refreshAllViews();
					}
				});
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void refreshAllViews() {
	    for (JInternalFrame frame : Desktop.getInstance().getAllFrames()) {
	        frame.repaint();
	    }
	}

	/**
	 * Change the label to reflect whether or not we are filtering events
	 *
	 * @param filtering if <code>true</code> we are filtering
	 */
	public void setEventFilteringLabel(boolean filtering) {
		_filterLabel.setVisible(filtering);
	}

	/**
	 * Change the label to reflect whether or not we are filtering events
	 *
	 * @param filtering if <code>true</code> we are filtering
	 */
	public void fixEventFilteringLabel() {
		_filterLabel.setVisible(FilterManager.getInstance().isFilteringOn());
	}

	/**
	 * Set the event number label
	 *
	 * @param num the event number
	 */
	public static void setEventNumberLabel(int seqnum, int truenum) {
	    if (ClasIoEventManager.getInstance().isAccumulating()) {
	        return;
	    }

	    String newText;
	    if (seqnum < 0) {
	        newText = "  No Event           ";
	    } else {
	        newText = String.format("  Event Seq: %d  True: %d", seqnum, truenum);
	    }

	    // Update label only if the text has changed
	    if (!_eventNumberLabel.getText().equals(newText)) {
	        SwingUtilities.invokeLater(() -> _eventNumberLabel.setText(newText));
	    }
	}

	/**
	 * Fix the event count label
	 */
	public void fixEventCount() {
		int count = ClasIoEventManager.getInstance().getEventCount();
		if (count < Integer.MAX_VALUE) {
			_eventCountLabel.setText("Event Count: " + count);
		} else {
			_eventCountLabel.setText("Event Count: N/A");
		}
	}

	/**
	 * Get the event filter menu
	 *
	 * @return the event filter menu
	 */
	public JMenu getEventFilterMenu() {
		return _eventFilterMenu;
	}

	// add to the event menu
	private void addToEventMenu() {

		_eventCountLabel = new JMenuItem("Event Count: N/A");
		_eventCountLabel.setOpaque(true);
		_eventCountLabel.setBackground(Color.white);
		_eventCountLabel.setForeground(X11Colors.getX11Color("Dark Blue"));
		_eventMenu.add(_eventCountLabel);

		// add the event filter menu
		_eventFilterMenu = new JMenu("Event Filters");
		_eventMenu.add(_eventFilterMenu);

		// add the noise parameter menu item
		ActionListener al2 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NoiseParameterDialog dialog = new NoiseParameterDialog();
				dialog.setVisible(true);
			}
		};

		_eventMenu.addSeparator();
		MenuManager.addMenuItem("SNR Parameters...", _eventMenu, al2);

	}

	/**
	 * Get the virtual view
	 *
	 * @return the virtual view
	 */
	public VirtualView getVirtualView() {
		return _virtualView;
	}

	/**
	 * private access to the Ced singleton.
	 *
	 * @return the singleton Ced (the main application frame.)
	 */
	private static Ced getInstance() {
		if (_instance == null) {
			_instance = new Ced(PropertySupport.TITLE, "ced " + versionString(), PropertySupport.BACKGROUNDIMAGE,
					"images/cnu.png", PropertySupport.FRACTION, 0.9);

			_instance.addInitialViews();
			_instance.createMenus();
			_instance.placeViewsOnVirtualDesktop();

//			_instance.createBusyPanel();
			_instance.createFilterLabel();

			_instance.createTriggerPanel();

			_instance.createEventNumberLabel();
			MagneticFields.getInstance().addMagneticFieldChangeListener(_instance);

		}
		return _instance;
	}

	/**
	 * public access to the singleton
	 *
	 * @return the singleton Ced (the main application frame.)
	 */
	public static Ced getCed() {
		return _instance;
	}

	/**
	 * Generate the version string
	 *
	 * @return the version string
	 */
	public static String versionString() {
		return release + (_experimental ? " (Experimental)" : "");
	}


	private void createTriggerPanel() {
		getJMenuBar().add(Box.createHorizontalStrut(20));
		getJMenuBar().add(Box.createHorizontalGlue());
		getJMenuBar().add(new TriggerMenuPanel());
	}


	// create the event number label
	private void createFilterLabel() {
		_filterLabel = new JLabel(" Event Filtering On ");
		_filterLabel.setOpaque(true);
		_filterLabel.setBackground(Color.white);
		_filterLabel.setForeground(Color.red);
		_filterLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		_filterLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));

		getJMenuBar().add(Box.createHorizontalGlue());
		getJMenuBar().add(_filterLabel);
		getJMenuBar().add(Box.createHorizontalStrut(5));

		setEventFilteringLabel(false);
	}

	// create the event number label
	private void createEventNumberLabel() {
		_eventNumberLabel = new JLabel("  Event:     ");
		_eventNumberLabel.setOpaque(true);
		_eventNumberLabel.setBackground(Color.black);
		_eventNumberLabel.setForeground(Color.yellow);
		_eventNumberLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		_eventNumberLabel.setBorder(BorderFactory.createLineBorder(Color.cyan, 1));
		setEventNumberLabel(-1, -1);

		getJMenuBar().add(Box.createHorizontalGlue());
		getJMenuBar().add(_eventNumberLabel);
		getJMenuBar().add(Box.createHorizontalStrut(5));
	}

	/**
	 * Get the plot view
	 *
	 * @return the plot voew;
	 */
	public PlotView getPlotView() {
		return _plotView;
	}

	/**
	 * Fix the title of the main frame
	 */
	public void fixTitle() {
		String title = getTitle();
		String prefix = "  [";
		int index = title.indexOf(prefix);
		if (index > 0) {
			title = title.substring(0, index);
		}

		title += prefix;

		if (MagneticFields.getInstance().hasActiveTorus()) {
			String path = MagneticFields.getInstance().getTorusBaseName();
			title += " (" + path + ")";
		}
		if (MagneticFields.getInstance().hasActiveSolenoid()) {
			String path = MagneticFields.getInstance().getSolenoidBaseName();
			title += " (" + path + ")";
		}
		

		title += "] [Swimmer " + Swimmer.getVersion() + "]";

		title += " [Coatjava " + getCoatJavaVersion() + "] [JAVA runtime " + Environment.getJavaMajorVersion() + "]";

		setTitle(title);
	}

	@Override
	public void magneticFieldChanged() {
//		Swimming.clearAllTrajectories();
		fixTitle();
		ClasIoEventManager.getInstance().reloadCurrentEvent();
	}
	/**
	 * Is this an experimental version?
	 * @return <code>true</code> if this version has experimental features
	 */
	public static boolean isExperimental() {
		return _experimental;
	}

	/**
	 * Get the parent frame
	 *
	 * @return the parent frame
	 */
	public static JFrame getFrame() {
		return _instance;
	}

	/**
	 * Get the COAT Java clasdir
	 *
	 * @return the COAT Java clasdir
	 */
	public static File getCLASDir() {
		return _clasDir;
	}

	// this is so we can find json files
	private static void initClas12Dir() throws IOException {

		// for running from runnable jar (for coatjava)
		String clas12dir = System.getProperty("CLAS12DIR");

		if (clas12dir == null) {
			clas12dir = "coatjava";
		}

		_clasDir = new File(clas12dir);

		if (_clasDir.exists() && _clasDir.isDirectory()) {
			System.out.println("**** Found CLAS12DIR [" + _clasDir.getCanonicalPath() + "]");
			System.setProperty("CLAS12DIR", clas12dir);
			return;
		} else {
			System.out.println("**** Did not find CLAS12DIR [" + _clasDir.getCanonicalPath() + "]");
		}

		String cwd = Environment.getInstance().getCurrentWorkingDirectory();
		clas12dir = cwd + "/../../../../../cnuphys/coatjava";
		_clasDir = new File(clas12dir);

		if (_clasDir.exists() && _clasDir.isDirectory()) {
			System.out.println("**** Found CLAS12DIR [" + _clasDir.getCanonicalPath() + "]");
			System.setProperty("CLAS12DIR", clas12dir);
			return;
		} else {
			System.out.println("**** Did not find CLAS12DIR [" + _clasDir.getCanonicalPath() + "]");
		}

		clas12dir = cwd + "/../../../../../bCNU/coatjava";
		_clasDir = new File(clas12dir);

		if (_clasDir.exists() && _clasDir.isDirectory()) {
			System.out.println("**** Found CLAS12DIR [" + _clasDir.getCanonicalPath() + "]");
			System.setProperty("CLAS12DIR", clas12dir);
			return;
		} else {
			System.out.println("**** Did not find CLAS12DIR [" + _clasDir.getCanonicalPath() + "]");
		}

		//one last try
		clas12dir = System.getenv("CLAS12DIR");
		if (clas12dir != null) {
			System.out.println("Trying with environment variable CLAS12DIR = "  + clas12dir);
			_clasDir = new File(clas12dir);
			if (_clasDir.exists() && _clasDir.isDirectory()) {
				System.out.println("**** Found CLAS12DIR [" + _clasDir.getCanonicalPath() + "]");
				System.setProperty("CLAS12DIR", clas12dir);
				return;
			}
			else {
				System.out.println("**** Did not find CLAS12DIR [" + _clasDir.getCanonicalPath() + "]");
			}
		}


		throw (new IOException("Could not locate the coatjava directory."));
	}

	/**
	 * Get the geometry variation
	 *
	 * @return the geometry variation
	 */
	public static String getGeometryVariation() {
		return _geoVariation;
	}

	// data collectors need to be initialized before
	// any events come through
	private static void initDataCollectors() {
		DataWarehouse.getInstance();
	}

	/**
	 * Main program launches the ced gui.
	 * <p>
	 * Command line arguments:</br>
	 * -p [dir] dir is the default directory
	 *
	 * @param arg the command line arguments.
	 */
	public static void main(String[] arg) {

		//get the java version
		int javaVersion = Environment.getJavaMajorVersion();
		if (javaVersion < _minJavaVersion) {
			System.err.println("Java version must be at least " + _minJavaVersion + ". You have " + javaVersion);
			System.exit(1);
		}
		System.out.println("Java version: " + javaVersion);
		
		System.out.println("Screen scale factor: " + Environment.getDisplayScaleFactor());

		Environment.setLookAndFeel();
		System.setProperty("sun.java2d.opengl", "true");


		//this is supposed to create less pounding of ccdb
		DefaultLogger.initialize();

		String variation = System.getProperty("GEOVARIATION");
		if (variation != null) {
			_geoVariation = new String(variation);
		}

		// read in userprefs
		PropertiesManager.getInstance();

		// initialize the trigger manager
		TriggerManager.getInstance();

		// initialize the filter manager
		FilterManager.getInstance();


		// init the clas 12 dir wherev the json files are
		try {
			initClas12Dir();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		FileUtilities.setDefaultDir("data");

		// create a console log listener
		// Log.getInstance().addLogListener(new ConsoleLogListener());

		// splash frame
		final SplashWindowCED splashWindow = new SplashWindowCED("ced", null, 920, release);

		// now make the frame visible, in the AWT thread
		try {
			EventQueue.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					splashWindow.setVisible(true);
				}

			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

//process command args
		if ((arg != null) && (arg.length > 0)) {
			int len = arg.length;
			int lm1 = len - 1;
			boolean done = false;
			int i = 0;
			while (!done) {
				if (arg[i].equalsIgnoreCase("-p")) {
					if (i < lm1) {
						i++;
						FileUtilities.setDefaultDir(arg[i]);
					}
				} else if (arg[i].contains("EXP")) {
					_experimental = true;
					System.out.println("Note: This is an experimental version");
				}

				i++;
				done = (i >= len);
			} // !done
		} // end command arg processing

		// initialize magnetic fields
		MagneticFields.getInstance().initializeMagneticFields();

		// initialize geometry
		GeometryManager.getInstance();

		// Initialize data collectors
		initDataCollectors();

//	    getInstance();  //creates ced frame

		// now make the frame visible, in the AWT thread
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				getInstance();
				splashWindow.setVisible(false);
				getCed().setVisible(true);
				getCed().fixTitle();

				FilterManager.getInstance().setUpFilterMenu();
				System.out.println(String.format("ced %s is ready. COATJAVA: %s Geometry variation: %s", versionString(), getCoatJavaVersion(), _geoVariation));
			}

		});

	} // end main

}
