package luishenrique.zipimoveis.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixplicity.easyprefs.library.Prefs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import luishenrique.zipimoveis.rest.models.ListaImovel;

public class PrefsUtils {

    private PrefsUtils(){}

    private static Gson mGson;

    public static void getInstance() {
        if (mGson == null) {
            mGson        = new Gson();
        }
    }

    public static List<ListaImovel> getImoveis(String tag) {
        getInstance();

        String json = Prefs.getString(tag, "");

        Type type   = new TypeToken<List<ListaImovel>>() {}.getType();
        List<ListaImovel> imoveis = mGson.fromJson(json, type);

        if (imoveis == null) {
            return new ArrayList<>();
        }
        return imoveis;
    }

    public static void addImoveis(List<ListaImovel> imoveis, String tag) {
        getInstance();
        String json = mGson.toJson(imoveis);
        Prefs.putString(tag, json);
    }

    public static void addFavoritos(List<ListaImovel> favoritos) {
        getInstance();

        String json = mGson.toJson(favoritos);
        Prefs.putString(Constants.LIST_IMOVEIS_FAVORITOS, json);
    }

    public static void removeFavoritos(List<ListaImovel> favoritos, ListaImovel favorito) {

        try {

            // Remove Favorito
            int count = 0;
            for (ListaImovel imovel : favoritos) {
                if (imovel.getCodImovel().equals( favorito.getCodImovel() )) {
                    favoritos.remove(count);
                }
                count++;
            }

            addFavoritos(favoritos);

            // Remove Favoritos dos imoveis
            List<ListaImovel> imoveis = getImoveis(Constants.LIST_IMOVEIS);
            List<ListaImovel> novos = new ArrayList<>();

            for (ListaImovel imovel : imoveis) {
                if (imovel.getCodImovel().equals( favorito.getCodImovel() )) {
                    imovel.setFavorito(false);
                }

                novos.add(imovel);
            }

            addImoveis(novos, Constants.LIST_IMOVEIS);

        } catch (Exception ex) {}

    }

}
