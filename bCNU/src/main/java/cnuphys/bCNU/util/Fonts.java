package cnuphys.bCNU.util;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Hashtable;

/**
 * This is used so that we can have common fonts across all components.
 *
 * @author DHeddle
 *
 */
public class Fonts {

	// the common font family for uniformity
	private static String commonFamily;

	// the target or desired family
	private static String targetFamily = "Lucida Grande";

	// the bulletproof backup if the target is not found
	private static String backupFamily = "SansSerif";

	private static Hashtable<String, Font> fonts = new Hashtable<>(41);


	//for huge warnings
	public static Font monsterFont = commonFont(Font.BOLD, 44);

	// common medium font used for components
	public static Font hugeFont = commonFont(Font.PLAIN, 18);

	// common medium font used for components
	public static Font largeFont = commonFont(Font.PLAIN, 14);
	
	// slightly bigger font used for components
	public static Font biggerFont = commonFont(Font.PLAIN, 13);

	// common font used for components
	public static Font defaultFont = commonFont(Font.PLAIN, 12);

	// common font used for components
	public static Font defaultLargeFont = commonFont(Font.BOLD, 14);

	// common font used for components
	public static Font defaultBoldFont = commonFont(Font.BOLD, 12);

	// common font used for components
	public static Font defaultItalicFont = commonFont(Font.ITALIC, 12);

	// common medium font used for components
	public static Font mediumFont = commonFont(Font.PLAIN, 11);

	// common medium font used for components
	public static Font mediumBoldFont = commonFont(Font.BOLD, 11);

	// common medium font used for components
	public static Font mediumItalicFont = commonFont(Font.ITALIC, 11);

	// common medium font used for components
	public static Font mediumItalicBoldFont = commonFont(Font.ITALIC + Font.BOLD, 11);


	// common small font used for components
	public static Font tweenFont = commonFont(Font.PLAIN, 10);

	// common small font used for components
	public static Font tweenBoldFont = commonFont(Font.BOLD, 10);

	// common small font used for components
	public static Font tweenItalicFont = commonFont(Font.ITALIC, 10);

	// common small font used for components
	public static Font smallFont = commonFont(Font.PLAIN, 9);

	// common font when a tiny label is needed
	public static Font tinyFont = commonFont(Font.PLAIN, 8);

	// normal monospaced font
	public static Font defaultMono = new Font(Font.MONOSPACED, Font.PLAIN, 12);

	// normal monospaced font
	public static Font mono = new Font(Font.MONOSPACED, Font.PLAIN, 13);

	// small monospaced font
	public static Font smallMono = new Font(Font.MONOSPACED, Font.PLAIN, 10);

	// tiny monospaced font
	public static Font tinyMono = new Font(Font.MONOSPACED, Font.PLAIN, 8);


	/**
	 * Scale a font
	 *
	 * @param font        the font to scale
	 * @param scaleFactor the multiplicative scale factor
	 * @return the derived font
	 */
	public static Font scaleFont(Font font, float scaleFactor) {
		return font.deriveFont(scaleFactor * font.getSize());
	}

	/**
	 * Obtain a font from the common family
	 *
	 * @param style bitwise Font.PLAIN, Font.BOLD, etc
	 * @param size  the size
	 * @return the common font.
	 */
	public static Font commonFont(int style, int size) {
		if (commonFamily == null) {
			String[] fnames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			if (fnames != null) {
				for (String s : fnames) {
					if (targetFamily.equalsIgnoreCase(s)) {
						commonFamily = s;
						break;
					}
				}
			}

			if (commonFamily == null) {
				commonFamily = backupFamily;
			}
		}

		String key = commonFamily + "$" + size + "$" + style;
		Font font = fonts.get(key);
		if (font == null) {
			font = new Font(commonFamily, style, size);
			fonts.put(key, font);
		}
		return font;
	}
}
