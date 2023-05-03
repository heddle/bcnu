package cnuphys.advisors;

import cnuphys.advisors.enums.Major;
import cnuphys.advisors.io.ITabled;
import cnuphys.bCNU.util.Bits;

public class AdvisorFilter implements IFilter {

	public static final int HONOR        = 01000;
	public static final int PRESSCHOLAR  = 02000;
	public static final int MUSTHEA      = 04000;
	public static final int CCPT         = 010000;
	public static final int BTMG         = 020000;
	public static final int ILC          = 040000;
	public static final int PRELAW       = 0100000;
	public static final int ANY = 07777777777;

	/** filter that accepts  any student */
	public static final AdvisorFilter anyAdvisor = new AdvisorFilter(0);

	/** filter that picks out honors advisors */
	public static final AdvisorFilter honorsAdvisors = new AdvisorFilter(HONOR);
	
	/** filter that picks out honors advisors */
	public static final AdvisorFilter ilcAdvisors = new AdvisorFilter(ILC);

	/** filter that picks out pres scholar advisors */
	public static final AdvisorFilter presScholarAdvisors = new AdvisorFilter(PRESSCHOLAR);

	/** filter that picks out music and theater advisors */
	public static final AdvisorFilter musTheaAdvisors = new AdvisorFilter(MUSTHEA);

	/** filter that picks out community captains advisors */
	public static final AdvisorFilter ccptAdvisors = new AdvisorFilter(CCPT);

	/** filter that picks out bio tech and management advisors */
	public static final AdvisorFilter btmgAdvisors = new AdvisorFilter(BTMG);

	/** filter that picks out prelaw advisors */
	public static final AdvisorFilter prelawAdvisors = new AdvisorFilter(PRELAW);

	private final int _bits;

	public AdvisorFilter(int bits) {
		_bits = bits;
	}


	@Override
	public boolean pass(ITabled itabled) {
	Advisor advisor = (Advisor)itabled;

		if (_bits == 0) {
			return true; //any student
		}
		else if (Bits.checkBit(_bits, HONOR) && advisor.honors) {
			return true;
		}
		else if (Bits.checkBit(_bits, ILC) && advisor.hasILC) {
			return true;
		}
		else if (Bits.checkBit(_bits, MUSTHEA) && ((advisor.subject == Major.MUSIC) || (advisor.subject == Major.THEA))) {
			return true;
		}
		else if (Bits.checkBit(_bits, PRESSCHOLAR) && advisor.presscholar) {
			return true;
		}
		else if (Bits.checkBit(_bits, CCPT) && advisor.ccpt) {
			return true;
		}
		else if (Bits.checkBit(_bits, BTMG) && advisor.btmg) {
			return true;
		}
		else if (Bits.checkBit(_bits, PRELAW) && advisor.prelaw) {
			return true;
		}


		return false;
	}

}
