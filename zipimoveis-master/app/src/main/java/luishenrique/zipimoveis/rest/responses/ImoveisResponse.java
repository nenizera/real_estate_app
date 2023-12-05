package luishenrique.zipimoveis.rest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import luishenrique.zipimoveis.rest.models.ListaImovel;

public class ImoveisResponse {

    @SerializedName("Imoveis")
    @Expose
    private List<ListaImovel> imoveis;

    public List<ListaImovel> getImoveis() {
        return imoveis;
    }
}
