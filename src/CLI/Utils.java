package CLI;

import java.math.BigDecimal;
import java.math.MathContext;

public class Utils {
    /**
     * Outputs easy to read string representing double.
     */
    public static String round(double n) {
        if(n == Double.NEGATIVE_INFINITY || n == Double.POSITIVE_INFINITY) {
            return "Infinity";
        } else if(Double.isNaN(n)) {
            return "NaN";
        }
        BigDecimal bd = new BigDecimal(n);
        bd = bd.round(new MathContext(3));
        return bd.toString();
    }
    public static String formatTime(double milliseconds) {
        if(milliseconds < 1000) {
            return Utils.round(milliseconds) + " ms";
        } else if(milliseconds < 60 * 1000) {
            return Utils.round(milliseconds / 1000) + " s";
        } else if(milliseconds < 60 * 60 * 1000) {
            return Utils.round(milliseconds / (1000 * 60)) + " min";
        } else {
            return Utils.round(milliseconds / (1000 * 60 * 60)) + " h";
        }
    }
}
