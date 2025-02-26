package project1.helper;

public class MathHelper {
    public static double normalizeRadians(double rad) {
        double mod = rad % Math.PI;

        return rad < -Math.PI ? Math.PI + mod : rad > Math.PI ? -Math.PI + mod : rad;
    }
}
