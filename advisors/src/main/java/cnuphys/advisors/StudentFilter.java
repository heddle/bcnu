package cnuphys.advisors;

import cnuphys.advisors.io.ITabled;
import cnuphys.bCNU.util.Bits;

public class StudentFilter implements IFilter {
	
	public static final int PRELAW         = 01;
	public static final int COMMCAPT       = 02;
	public static final int PREMEDSCHOLAR  = 04;
	public static final int PRESSCHOLAR    = 010;
	public static final int ASSIGNED       = 020;
	public static final int UNASSIGNED     = 040;

	public static final int ANY = 07777777777;
	
	/** filter that accepts  any student */
	public static final StudentFilter anyStudent = new StudentFilter(0);
	
	/** filter that picks out prelaw students */
	public static final StudentFilter prelawStudents = new StudentFilter(PRELAW);

	/** filter that picks out community captain students */
	public static final StudentFilter communityCaptains = new StudentFilter(COMMCAPT);
	
	/** filter that picks out premed scholars students */
	public static final StudentFilter preMedScholars = new StudentFilter(PREMEDSCHOLAR);
	
	/** filter that picks out pres scholars scholars students */
	public static final StudentFilter presScholars = new StudentFilter(PRESSCHOLAR);
	
	/** filter that picks out assigned students */
	public static final StudentFilter assignedStudents = new StudentFilter(ASSIGNED);

	/** filter that picks out unassigned students */
	public static final StudentFilter unassignedStudents = new StudentFilter(UNASSIGNED);

	
	private final int _bits;
	
	public StudentFilter(int bits) {
		_bits = bits;
	}

	@Override
	public boolean pass(ITabled itabled) {
		Student student = (Student)itabled;
		
		if (_bits == 0) {
			return true; //any student
		}
		else if (Bits.checkBit(_bits, PRELAW) && student.prelaw) {
			return true;
		}
		else if (Bits.checkBit(_bits, COMMCAPT) && student.communityCaptain) {
			return true;
		}
		else if (Bits.checkBit(_bits, PREMEDSCHOLAR) && student.preMedScholar) {
			return true;
		}
		else if (Bits.checkBit(_bits, PRESSCHOLAR) && student.presidentialScholar) {
			return true;
		}
		else if (Bits.checkBit(_bits, ASSIGNED) && student.assigned()) {
			return true;
		}
		else if (Bits.checkBit(_bits, UNASSIGNED) && !student.assigned()) {
			return true;
		}
		
		
		return false;
	}

}
