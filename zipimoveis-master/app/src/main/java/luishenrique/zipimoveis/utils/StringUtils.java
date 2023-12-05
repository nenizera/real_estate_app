package luishenrique.zipimoveis.utils;

import android.text.TextUtils;

import java.text.NumberFormat;

public class StringUtils {

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final static String formatMoeda(String valor) {
        String cleanString = valor.toString().replaceAll("[R$,.]", "");
        double parsed = Double.parseDouble(cleanString);
        return NumberFormat.getCurrencyInstance().format((parsed/100));
    }

}
