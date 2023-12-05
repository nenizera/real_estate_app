package luishenrique.zipimoveis.rest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import luishenrique.zipimoveis.rest.models.DetalheImovel;

public class DetalheImovelResponse {

    @SerializedName("Imovel")
    @Expose
    private DetalheImovel detalheImovel;

    public DetalheImovel getDetalheImovel() {
        return detalheImovel;
    }
}
