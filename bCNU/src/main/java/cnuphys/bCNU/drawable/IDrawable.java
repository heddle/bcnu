package cnuphys.bCNU.drawable;

import java.awt.Graphics;

import cnuphys.bCNU.graphics.container.IContainer;

/**
 * Used to draw things. Typically extra user supplied drawing methods.
 *
 * @author heddle
 *
 */
public interface IDrawable {

	/**
	 * Draw the drawable.
	 *
	 * @param g         the graphics context.
	 * @param container the graphical container being rendered.
	 */
	public void draw(Graphics g, IContainer container);

	/**
	 * This tells the drawable, for example that any thing is has cached, such as a
	 * pixel based polygon, needs to be recomputed.
	 *
	 * @param dirty the value of the dirty flag.
	 */
	public void setDirty(boolean dirty);

	/**
	 * Called when the drawable is about to be removed from a list.
	 */
	public void prepareForRemoval();

	/**
	 * Get the visibility flag.
	 *
	 * @return <code>true</code> if the object is visible.
	 */
	public boolean isVisible();

	/**
	 * Set whether the object is visible.
	 *
	 * @param visible the value of the visibility flag.
	 */
	public void setVisible(boolean visible);

	/**
	 * Get the enabled flag.
	 *
	 * @return <code>true</code> if the object is enabled.
	 */
	public boolean isEnabled();

	/**
	 * Set whether the object is enabled.
	 *
	 * @param enabled the value of the enabled flag.
	 */
	public void setEnabled(boolean enabled);

	/**
	 * Get the name of the object.
	 *
	 * @return the name of the object, as it will appear in a visibility table.
	 */
	public String getName();

}
