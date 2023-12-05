package luishenrique.zipimoveis.rest.services;

import luishenrique.zipimoveis.rest.models.Contato;
import luishenrique.zipimoveis.rest.responses.ContatoResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ContatoService {

    @POST("imoveis/contato")
    Call<ContatoResponse> post(@Body Contato contato);

}
