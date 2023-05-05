package cnuphys.advisors;

import java.util.ArrayList;
import java.util.List;

import cnuphys.advisors.io.ITabled;
import cnuphys.bCNU.util.Bits;

public abstract class Person implements ITabled {

	/** bitwise properties */
	public static final int PRELAW         = 01;
	public static final int CCPT           = 02;
	public static final int PREMEDSCHOLAR  = 04;
	public static final int PRESSCHOLAR    = 010;
	public static final int PLP            = 020;
	public static final int MUSICTHEATER   = 0100;
	public static final int BTMG           = 0200;
	public static final int HONOR          = 0400;
	public static final int WIND           = 01000;
	public static final int LOCKED         = 02000;
	public static final int ILC            = 04000;
	
	protected int bits;
	
	/** faculty or student ID */
	public String id;
	
	/**
	 * Convenience method to check whether person is locked
	 * @return true if person is locked
	 */
	public boolean locked() {
		return check(LOCKED);
	}
	
	/**
	 * Convenience method to lock the person
	 */
	public void setLocked() {
		set(LOCKED);
	}
	
	/**
	 * Convenience method to lock or unlock the person
	 */
	public void setLocked(boolean locked) {
		set(LOCKED, locked);
	}
	
	/**
	 * Convenience method to check whether person has ilc status
	 * @return true if person has ilc status
	 */
	public boolean ilc() {
		return check(ILC);
	}
	
	/**
	 * Convenience method to lset the ilc status
	 */
	public void setILC() {
		set(ILC);
	}
	
	/**
	 * Convenience method to set or unset the ilc status
	 */
	public void setILC(boolean ilc) {
		set(ILC, ilc);
	}

	/**
	 * Convenience method to check whether person is honors
	 * @return true if person is honors
	 */
	public boolean honors() {
		return check(HONOR);
	}
	
	/**
	 * Convenience method to set the honors status
	 */
	public void setHonors() {
		set(HONOR);
	}
	
	/**
	 * Convenience method to set or unset the honors status
	 */
	public void setHonors(boolean honors) {
		set(HONOR, honors);
	}

	
	/**
	 * Check if a bit is set
	 * @param bit one of the bits from the class constants
	 * @return true if the bit is set
	 */
	@Override
	public boolean check(int bit) {
		return Bits.checkBit(bits, bit);
	}
	
	/**
	 * Set a bit on or off
	 * @param bit the bit in question
	 * @param on if true, set the bit on, else clear it.
	 */
	public void set(int bit, boolean on) {
		if (on) {
			set(bit);
		}
		else {
			clear(bit);
		}
	}

	/**
	 * Set a bit on
	 * @param bit one of the bits from the class constants
	 */
	public void set(int bit) {
		bits = Bits.setBit(bits, bit);
	}
	
	/**
	 * Clear a bit
	 * @param bit one of the bits from the class constants
	 */
	public void clear(int bit) {
		bits = Bits.clearBit(bits, bit);
	}
	
	/**
	 * Toggle a bit
	 * @param bit one of the bits from the class constants
	 */
	public void toggle(int bit) {
		bits = Bits.toggleBit(bits, bit);
	}
	
	/**
	 * Get a filtered list
	 * @param persons a base set of persons
	 * @param bits the bit pattern to match
	 * @return the filtered list
	 */
	public static List<Person> filteredList(List<Person> persons, int bits) {
		List<Person> filteredList = new ArrayList<>();
		
		for (Person person : persons) {
			if (person.check(bits)) {
				filteredList.add(person);
			}
		}
		
		return filteredList;
	}
	

}
