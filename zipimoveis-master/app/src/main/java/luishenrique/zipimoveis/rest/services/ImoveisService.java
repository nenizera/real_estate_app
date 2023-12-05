package luishenrique.zipimoveis.rest.services;

import luishenrique.zipimoveis.rest.responses.DetalheImovelResponse;
import luishenrique.zipimoveis.rest.responses.ImoveisResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImoveisService {

    @GET("imoveis")
    Call<ImoveisResponse> getAll();

    @GET("imoveis/{id}")
    Call<DetalheImovelResponse> getId(@Path("id") String id);
}
