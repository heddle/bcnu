package cnuphys.bCNU.component;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class EnumComboBox extends JComboBox {

	// reverse hash which takes a string and gives the enum
	protected Hashtable<String, Enum<?>> reverseHash;

	// an extra choce, not an enum name, like "any"
	private String _extraChoice;

	/**
	 * Create a combobox from an enum map.
	 *
	 * @param enumMap    an EnumMap<enum, String>. The enum is the key, and the
	 *                   string, which will become the label, is the value.
	 * @param defaultKey the enum that should default to "on".
	 */
	public EnumComboBox(EnumMap<?, String> enumMap, Enum<?> defaultKey) {
		this(enumMap, defaultKey, null);
	}

	/**
	 * Create a combobox from an enum map.
	 *
	 * @param enumMap     an EnumMap<enum, String>. The enum is the key, and the
	 *                    string, which will become the label, is the value.
	 * @param defaultKey  the enum that should default to "on".
	 * @param extraChoice an optional extra choice, like "None" or "Any"
	 */
	public EnumComboBox(EnumMap<?, String> enumMap, Enum<?> defaultKey, String extraChoice) {
		Set<?> keySet = enumMap.keySet();

		_extraChoice = extraChoice;
		reverseHash = new Hashtable<>(2 * keySet.size() + 1);

		// an extra choice? Like "any"
		if (_extraChoice != null) {
			addItem(_extraChoice);
		}

		// in this enumMap the enum is the key, and a string is the value
		for (Object key : keySet) {
			String niceName = enumMap.get(key);
			addItem(niceName);
			reverseHash.put(niceName, (Enum<?>) key);
		}

		sizeComboBox();

		if (defaultKey != null) {
			setSelectedItem(enumMap.get(defaultKey));
		} else if (_extraChoice != null) {
			setSelectedItem(_extraChoice);
		}
	}

	/**
	 * Get the enum corresponding to the selected string, which is the nice name of
	 * the enum.
	 *
	 * @return the selected enum value. Returns <code>null</code> if "extra choice"
	 *         was used and selected.
	 */
	public Enum<?> getSelectedEnum() {
		String niceName = (String) getSelectedItem();

		// we signify the "extra" choice by returning null
		if ((_extraChoice != null) && (_extraChoice.equals(niceName))) {
			return null;
		}

		return reverseHash.get(niceName);
	}

	// size the combo box to just fit
	private void sizeComboBox() {
		FontMetrics fm = getFontMetrics(getFont());

		int maxSW = 0;
		int count = getItemCount();
		Dimension d = getPreferredSize();
		for (int i = 0; i < count; i++) {
			String s = (String) getItemAt(i);
			int sw = fm.stringWidth(s);
			maxSW = Math.max(maxSW, fm.stringWidth(s));
		}

		d.width = maxSW + 80;
		setPreferredSize(d);
		setMinimumSize(d);
	}

}
