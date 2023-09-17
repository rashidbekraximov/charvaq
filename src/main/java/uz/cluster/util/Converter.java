package uz.cluster.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class Converter {
    public static final int MAX_DIGIT_COUNT = 16;
    private static final BigDecimal BIG_DECIMAL = BigDecimal.valueOf(Math.pow(10.0, MAX_DIGIT_COUNT));


    public static String convertToJson(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();   // configure ObjectMapper to exclude null fields whiel serializing
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String json = mapper.writeValueAsString(obj);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static double add(BigDecimal first,BigDecimal second){
        return Converter.reductionWithRoundOff(first.add(second));
    }

    public static double divide(double first, double second, int dividing) {
        return first / second;
    }

    public static double round(double value, int dividing) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(dividing, RoundingMode.HALF_UP).doubleValue();
    }

    public static BigInteger expandWithRoundOff(BigDecimal x) {
        BigDecimal decimal = x.multiply(BIG_DECIMAL, MathContext.DECIMAL128);
        return decimal.setScale(0, RoundingMode.CEILING).unscaledValue();
    }

    public static double reductionWithRoundOff(BigDecimal x) {
        if (x == null){
            return 0;
        }
        BigDecimal decimal = x.divide(BIG_DECIMAL, MathContext.DECIMAL128);
        BigDecimal b = decimal.setScale(2, RoundingMode.HALF_UP);
        return b.doubleValue();
    }

    public static double getReductionValue(double amount) {
        return Math.round((100.0 * amount)) / 100;
    }

    public static double getReductionToDouble(double amount) {
        return Math.round((100.0 * amount)) / 100.0;
    }


    public static double reductionWithRoundOff(BigInteger x, int newScale) {
        BigDecimal bigDecimal = new BigDecimal(x);
        BigDecimal decimal = bigDecimal.divide(BIG_DECIMAL, MathContext.DECIMAL128);
        BigDecimal b = decimal.setScale(newScale, RoundingMode.HALF_UP);
        return b.doubleValue();
    }
}
