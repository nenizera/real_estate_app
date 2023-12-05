package luishenrique.zipimoveis.rest;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.List;
import java.util.Map;

import luishenrique.zipimoveis.rest.models.Cidades;
import luishenrique.zipimoveis.rest.models.ImovelBase;
import luishenrique.zipimoveis.rest.services.CEPService;
import luishenrique.zipimoveis.rest.services.ContatoService;
import luishenrique.zipimoveis.rest.services.ImoveisService;
import luishenrique.zipimoveis.utils.FirebaseAppUtils;
import luishenrique.zipimoveis.utils.ServerUtils;

public class ApiUtils {

    private final FirebaseAppUtils mFirebaseAppUtils;

    public final GenericTypeIndicator<Map<String, ImovelBase>> formatListImoveis = new GenericTypeIndicator<Map<String, ImovelBase>>() {};
    public final GenericTypeIndicator<Map<String, Cidades>> formatListCidades = new GenericTypeIndicator<Map<String, Cidades>>() {};

    public ApiUtils() {
        mFirebaseAppUtils = new FirebaseAppUtils();
    }

    public DatabaseReference getImoveisService() {
        return mFirebaseAppUtils.getFirebaseDatabase().getReference("imoveis");
    }

    public DatabaseReference getCidadeService() {
        return mFirebaseAppUtils.getFirebaseDatabase().getReference("cidades");
    }

    public static ContatoService getContatoService() {
        return RetrofitClient.getClient(ServerUtils.ZIP_SERVER).create(ContatoService.class);
    }

    public static CEPService getCEPService() {
        return RetrofitClient.getCEP(ServerUtils.CEP_SERVER).create(CEPService.class);
    }

}
