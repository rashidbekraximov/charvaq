package uz.cluster.util;

public class StringUtil {
    public static boolean isNotEmpty(String text) {
        if (text == null)
            return false;
        return !"".equals(text.trim());
    }

    public static String convertAmountToString(Double amount) {
        if(amount == null)
            return "0";

        int res = (int) ((Math.abs(amount) - Math.round(Math.abs(amount))) * 100);

        if (res == 0) {
            return String.format("%.0f", amount);
        }
        if (res % 10 == 0) {
            return String.format("%.01f", amount);
        }

        return String.format("%.02f", amount);
    }

    public static String convertDoubleToString(double amount, int countOfAfterPoint) throws Exception {
        if (countOfAfterPoint > 9) {
            throw new Exception("this is incorrect");
        }
        String ex = "0.0" + countOfAfterPoint + "f";
        return String.format(ex, amount);
    }
}
