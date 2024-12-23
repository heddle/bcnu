package cnuphys.kinetic.molecule;

import java.util.Random;

public class MaxwellSpeedDistribution {
    private static final Random rand = new Random();

    public static double generateSpeed(double normalizedTemperature) {
        double m = 1.0;  // Mass of the particle (arbitrary units)
        double kB = 1.0; // Boltzmann constant in those units
        double T = normalizedTemperature * 300;  // Scale temperature to a typical range (e.g., 0 to 300 K)
        double vMax = Math.sqrt(3 * kB * T / m); // Maximum speed (upper normalization bound)

        while (true) {
            // Sample speed from a uniform distribution [0, vMax]
            double v = vMax * rand.nextDouble();
            // Calculate the probability density of this speed under Maxwell distribution
            double f_v = (4 * Math.PI * Math.pow(v, 2) * Math.exp(-m * Math.pow(v, 2) / (2 * kB * T))) /
                         Math.pow((2 * Math.PI * kB * T / m), 1.5);
            // Acceptance probability (simplified)
            if (rand.nextDouble() < f_v * 10) { // Adjust the scaling factor as needed
                return v / vMax; // Return normalized speed
            }
        }
    }

    public static double mostProbableNormalizedSpeed(double normalizedTemperature) {
        return Math.sqrt(2.0 / 3.0 * normalizedTemperature);
    }


    public static void main(String[] args) {
        double normalizedTemp = 1000; // Example: Normalized temperature
        double speed = generateSpeed(normalizedTemp);
        System.out.println("Normalized Speed: " + speed);
    }
}
