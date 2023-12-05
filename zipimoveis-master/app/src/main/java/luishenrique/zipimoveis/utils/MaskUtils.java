package luishenrique.zipimoveis.utils;

import android.text.TextUtils;

public class MaskUtils {

    private MaskUtils() {}

    public static String formatCEP(String cep) {
        if (TextUtils.isEmpty(cep)) {
            return "";
        }
        return cep.substring(0, 5) + "-" + cep.substring(5);
    }

}
