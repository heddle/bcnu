package cnuphys.simanneal;

import java.util.Random;

import javax.management.modelmbean.InvalidTargetObjectTypeException;

import cnuphys.bCNU.attributes.Attribute;
import cnuphys.bCNU.attributes.Attributes;

public class SimulationAttributes extends Attributes {

	// common attribute keys
	public static final String RANDSEED = "randseed";
	public static final String COOLRATE = "coolrate";
	public static final String MINTEMP = "mintemp";
	public static final String THERMALCOUNT = "thermalcount";
	public static final String SUCCESSCOUNT = "successcount";
	public static final String MAXSTEPS = "maxsteps";
	public static final String PLOTTITLE = "plottitle";
	public static final String XAXISLABEL = "xaxislabel";
	public static final String YAXISLABEL = "yaxislabel";
	public static final String USELOGTEMP = "uselogtemp";

	/**
	 * Hold common and custom attributes
	 */
	public SimulationAttributes() {
		setDefaultAttributes();
	}

	private void setDefaultAttributes() {
		add(COOLRATE, 0.03);
		add(RANDSEED, -1L);
		add(THERMALCOUNT, 200);
		add(MAXSTEPS, 2000);
		add(MINTEMP, 1.0e-18);
		add(USELOGTEMP, false, false, false);
		add(PLOTTITLE, "Simulated Annealing", false, false);
		add(XAXISLABEL, "Temperature", false, false);
		add(YAXISLABEL, "Energy", false, false);
		add(SUCCESSCOUNT, 20);

	}

	/**
	 * Create a random number generator based on the seed in the RANDSEED attribute
	 * 
	 * @return a random number generator
	 */
	public Random createRandomGenerator() {

		long seed;
		try {
			seed = getAttribute(RANDSEED).getLong();
			if (seed > 0) {
				return new Random(seed);
			} else {
				return new Random();
			}
		} catch (InvalidTargetObjectTypeException e) {
			e.printStackTrace();
			return new Random();
		}
	}

	/**
	 * Set the main plot title from the PLOTTITLE attribute
	 * 
	 * @param title the new main plot title
	 */
	public void setPlotTitle(String title) {
		setValue(PLOTTITLE, title);
	}

	/**
	 * Get the main plot title from the PLOTTITLE attribute
	 * 
	 * @return the cool rate
	 */
	public String getPlotTitle() {
		try {
			return getAttribute(PLOTTITLE).getString();
		} catch (InvalidTargetObjectTypeException e) {
			e.printStackTrace();
			return "Simulated Annealing";
		}
	}

	/**
	 * Set the x axis from the XAXISLABEL attribute
	 * 
	 * @param label the new x axis label
	 */
	public void setXAxisLabel(String label) {
		setValue(XAXISLABEL, label);
	}

	/**
	 * Get the axis from the XAXISLABEL attribute
	 * 
	 * @return the x axis label
	 */
	public String getXAxisLabel() {
		try {
			return getAttribute(XAXISLABEL).getString();
		} catch (InvalidTargetObjectTypeException e) {
			e.printStackTrace();
			return "Temperature";
		}
	}

	/**
	 * Set the y axis from the YAXISLABEL attribute
	 * 
	 * @param label the new y axis label
	 */
	public void setYAxisLabel(String label) {
		setValue(YAXISLABEL, label);
	}

	/**
	 * Get the y axis from the YAXISLABEL attribute
	 * 
	 * @return the y axis label
	 */
	public String getYAxisLabel() {
		try {
			return getAttribute(YAXISLABEL).getString();
		} catch (InvalidTargetObjectTypeException e) {
			e.printStackTrace();
			return "Energy";
		}
	}

	/**
	 * Set the useLogTemp flag from the USELOGTEMP attribute
	 * 
	 * @param useLog he value of the useLogTemp flag
	 */
	public void setUseLogTemp(boolean useLog) {
		setValue(USELOGTEMP, useLog);
	}

	/**
	 * Get the useLogTemp flag from the USELOGTEMP attribute
	 * 
	 * @return the value of the useLogTemp flag
	 */
	public boolean useLogTemp() {
		
		Attribute useLogAtt = getAttribute(USELOGTEMP);
		if (useLogAtt == null) {
			return false;
		}
		
		try {
			return useLogAtt.getBoolean();
		} catch (InvalidTargetObjectTypeException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Set the cool rate
	 * 
	 * @param coolRate the new cool rate
	 */
	public void setCoolRate(double coolRate) {
		this.setValue(COOLRATE, coolRate);
	}

	/**
	 * Get the coolrate from the COOLRATE attribute
	 * 
	 * @return the cool rate
	 */
	public double getCoolRate() {
		try {
			return getAttribute(COOLRATE).getDouble();
		} catch (InvalidTargetObjectTypeException e) {
			e.printStackTrace();
			return 0.03;
		}
	}

	/**
	 * Set the min temperature
	 * 
	 * @param coolRate the new cool rate
	 */
	public void setMinTemp(double minTemp) {
		this.setValue(MINTEMP, minTemp);
	}

	/**
	 * Get the min temp from the MINTEMP attribute
	 * 
	 * @return the min temp
	 */
	public double getMinTemp() {
		try {
			return getAttribute(MINTEMP).getDouble();
		} catch (InvalidTargetObjectTypeException e) {
			e.printStackTrace();
			return 1.0e-8;
		}
	}

	/**
	 * Set the thermalization count
	 * 
	 * @param thermalizationCount the new thermalization count
	 */
	public void setThermalizationCount(int thermalizationCount) {
		setValue(THERMALCOUNT, thermalizationCount);
	}

	/**
	 * Get the thermalization count from the THERMALCOUNT attribute
	 * 
	 * @return the thermalization count
	 */
	public int getThermalizationCount() {
		try {
			return getAttribute(THERMALCOUNT).getInt();
		} catch (InvalidTargetObjectTypeException e) {
			e.printStackTrace();
			return 200;
		}
	}

	/**
	 * Set the success count
	 * 
	 * @param successCount the new success count
	 */
	public void setSuccessCount(int successCount) {
		setValue(SUCCESSCOUNT, successCount);
	}

	/**
	 * Get the success count from the SUCCESSCOUNT attribute
	 * 
	 * @return the success count
	 */
	public int getSuccessCount() {
		try {
			return getAttribute(SUCCESSCOUNT).getInt();
		} catch (InvalidTargetObjectTypeException e) {
			e.printStackTrace();
			return getThermalizationCount() / 10;
		}
	}

	/**
	 * Set the max number of temp reductions using the MAXSTEPS attribute
	 * 
	 * @param maxSteps the number of max steps (temp reductions)
	 */
	public void setMaxSteps(int maxSteps) {
		setValue(MAXSTEPS, maxSteps);
	}

	/**
	 * Get the max number of temp reductions MAXSTEPS attribute
	 * 
	 * @return max number of temp reductions
	 */
	public int getMaxSteps() {
		try {
			return getAttribute(MAXSTEPS).getInt();
		} catch (InvalidTargetObjectTypeException e) {
			e.printStackTrace();
			return 2000;
		}
	}

}
