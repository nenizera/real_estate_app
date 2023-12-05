package luishenrique.zipimoveis.rest.services;

import luishenrique.zipimoveis.rest.models.CEP;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CEPService {

    @GET("{cep}/json")
    Call<CEP> get(@Path("cep") String cep);
}
