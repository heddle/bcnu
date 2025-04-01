package cnuphys.fastMCed.eventio;

import java.util.Vector;

import org.jlab.clas.physics.Particle;
import org.jlab.clas.physics.PhysicsEvent;

import cnuphys.CLAS12Swim.CLAS12SwimResult;
import cnuphys.CLAS12Swim.CLAS12Swimmer;
import cnuphys.adaptiveSwim.SwimType;
import cnuphys.bCNU.log.Log;
import cnuphys.bCNU.magneticfield.swim.ISwimAll;
import cnuphys.lund.LundId;
import cnuphys.lund.LundSupport;
import cnuphys.lund.TrajectoryRowData;
import cnuphys.magfield.MagneticFieldChangeListener;
import cnuphys.magfield.MagneticFields;
import cnuphys.rk4.RungeKuttaException;
import cnuphys.swim.DefaultSwimStopper;
import cnuphys.swim.SwimTrajectory;
import cnuphys.swim.Swimming;

public class SwimAll implements ISwimAll, MagneticFieldChangeListener {

	// integration cutoff
	private static final double RMAX = 800;
	private static final double PATHMAX = 1000.0;

	private DefaultSwimStopper _stopper;
	
	private CLAS12Swimmer _swimmer;

	public SwimAll() {
		MagneticFields.getInstance().addMagneticFieldChangeListener(this);
	}

	@Override
	public void swimAll() {
		Swimming.clearMCTrajectories(); // clear all existing trajectories
		PhysicsEvent event = PhysicsEventManager.getInstance().getCurrentEvent();
		if ((event == null) || (event.count() < 1)) {
			return;
		}

		for (int index = 0; index < event.count(); index++) {
			Particle particle = event.getParticle(index);
			LundId lid = LundSupport.getInstance().get(particle.pid());
			double pxo = particle.px(); // leave in GeV
			double pyo = particle.py(); // leave in GeV
			double pzo = particle.pz(); // leave in GeV

			// note conversions from mm to cm
			double x = particle.vertex().x();
			double y = particle.vertex().y();
			double z = particle.vertex().z();

			swim(lid, pxo, pyo, pzo, x, y, z);
		}
	}

	@Override
	public Vector<TrajectoryRowData> getRowData() {
		PhysicsEvent event = PhysicsEventManager.getInstance().getCurrentEvent();
		if ((event == null) || (event.count() < 1)) {
			return null;
		}

		Vector<TrajectoryRowData> v = new Vector<TrajectoryRowData>(event.count());

		for (int index = 0; index < event.count(); index++) {
			Particle particle = event.getParticle(index);
			LundId lid = LundSupport.getInstance().get(particle.pid());
			double pxo = particle.px() * 1000.; // convert to MeV
			double pyo = particle.py() * 1000.; // convert to MeV
			double pzo = particle.pz() * 1000.; // convert to MeV

			// note conversions from mm to cm
			double x = particle.vertex().x(); // leave in cm
			double y = particle.vertex().y(); // leave in cm
			double z = particle.vertex().z(); // leave in cm

			double p = Math.sqrt(pxo * pxo + pyo * pyo + pzo * pzo);
			double theta = Math.toDegrees(Math.acos(pzo / p));
			double phi = Math.toDegrees(Math.atan2(pyo, pxo));

			v.add(new TrajectoryRowData(index, lid, x, y, z, p, theta, phi, 0, "FastMC", SwimType.MCSWIM));
		}

		return v;
	}

	// units GeV/c and meters
	private void swim(LundId lid, double px, double py, double pz, double x, double y, double z) {

		double p = Math.sqrt(px * px + py * py + pz * pz);
		double theta = Math.toDegrees(Math.acos(pz / p));
		double phi = Math.toDegrees(Math.atan2(py, px));
		
		CLAS12Swimmer swimmer = new CLAS12Swimmer();


		if (_swimmer == null) {
			_swimmer = new CLAS12Swimmer();
			System.err.println("Created new swimmer");
		}
		double stepSize = 5e-4; // m
		double tolerance = 1.0e-6;

		CLAS12SwimResult result = swimmer.swim(lid.getCharge(), x, y, z, p, theta, phi, PATHMAX, stepSize, tolerance);
		SwimTrajectory traj = result.getTrajectory();
		traj.setLundId(lid);
		Swimming.addMCTrajectory(traj);
	}

	@Override
	public void magneticFieldChanged() {
		_swimmer = null;
	}
}
