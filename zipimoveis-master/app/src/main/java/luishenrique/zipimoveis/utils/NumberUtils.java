package luishenrique.zipimoveis.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class NumberUtils {

    private NumberUtils(){}

    public static String currencyFormat(BigDecimal value) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        return numberFormat.format( value );
    }
}
