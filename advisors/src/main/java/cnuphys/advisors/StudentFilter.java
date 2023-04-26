package cnuphys.advisors;

import cnuphys.advisors.io.ITabled;
import cnuphys.bCNU.util.Bits;

public class StudentFilter implements IFilter {
	
	public static final int PRELAW      = 01;
	public static final int COMMCAPT    = 02;
	public static final int ANY = 07777777777;
	
	/** filter that accepts  any student */
	public static final StudentFilter anyStudent = new StudentFilter(0);
	
	/** filter that picks out prelaw students */
	public static final StudentFilter prelawStudents = new StudentFilter(PRELAW);

	/** filter that picks out community captain students */
	public static final StudentFilter communityCaptains = new StudentFilter(COMMCAPT);
	
	
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
		else if (Bits.checkBit(_bits, PRELAW) && student.isPrelaw()) {
			return true;
		}
		else if (Bits.checkBit(_bits, COMMCAPT) && student.communityCaptain) {
			return true;
		}
		
		
		return false;
	}

}
