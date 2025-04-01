package cnuphys.bCNU.util;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import cnuphys.bCNU.graphics.GraphicsUtilities;

public class DisplayUtilities {

    public static Dimension screenFraction(double fraction) {
        if (fraction <= 0 || fraction > 1) {
            throw new IllegalArgumentException("Fraction must be between 0 and 1 (exclusive).");
        }

        // Get scaled display size
        Dimension fullSize = getScaledDisplaySize();

        // Compute the fractional size
        int width = (int) Math.round(fraction * fullSize.width);
        int height = (int) Math.round(fraction * fullSize.height);

        return new Dimension(width, height);
    }

    private static Dimension getScaledDisplaySize() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        // Get screen size in pixels
        Rectangle screenBounds = gc.getBounds();

        // Get scaling factor
        AffineTransform transform = gc.getDefaultTransform();
        double scaleX = transform.getScaleX();
        double scaleY = transform.getScaleY();

        // Compute scaled screen size
        int scaledWidth = (int) Math.round(screenBounds.width * scaleX);
        int scaledHeight = (int) Math.round(screenBounds.height * scaleY);

        return new Dimension(scaledWidth, scaledHeight);
    }

    public static void main(String[] args) {
        Dimension d = screenFraction(0.5);  // Example: 50% of the display size
        System.out.println("Half-screen size: " + d.width + " x " + d.height);
        
        d = GraphicsUtilities.screenFraction(0.5);  // Example: 40% of the display size
        System.out.println("old version: "+ d.width + " x " + d.height);
    }
}
