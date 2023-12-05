package luishenrique.zipimoveis.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import java.util.HashSet;
import java.util.Set;

public class UtilsApp {

    public static void callSettings(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    private static Set<String> initSymbolMask(String mascara){
        Set<String> symbolMask = new HashSet<>();

        for (int i=0; i < mascara.length(); i++){
            char ch = mascara.charAt(i);
            if (ch != '#') {
                symbolMask.add(String.valueOf(ch));
            }
        }

        return symbolMask;
    }

    private static String unmask(String s, String mascara) {

        Set<String> replaceSymbols = initSymbolMask(mascara);

        for (String symbol : replaceSymbols)
            s = s.replaceAll("["+symbol+"]","");

        return s;
    }

    public static String mask(String format, String text){

        String unmask = unmask(text, format);

        String maskedText="";
        int i =0;
        for (char m : format.toCharArray()) {
            if (m != '#') {
                maskedText += m;
                continue;
            }
            try {
                maskedText += unmask.charAt(i);
            } catch (Exception e) {
                break;
            }
            i++;
        }
        return maskedText;
    }
}
