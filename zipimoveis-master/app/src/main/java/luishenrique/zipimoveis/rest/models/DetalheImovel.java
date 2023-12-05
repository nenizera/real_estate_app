package luishenrique.zipimoveis.rest.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import luishenrique.zipimoveis.utils.NumberUtils;

public class DetalheImovel extends Imovel {


    @SerializedName("Fotos")
    @Expose
    private List<String> fotos;

    @SerializedName("Observacao")
    @Expose
    private String observacao;

    @SerializedName("Caracteristicas")
    @Expose
    private List<String> caracteristicas;

    @SerializedName("PrecoCondominio")
    @Expose
    private BigDecimal precoCondominio;

    @SerializedName("CaracteristicasComum")
    @Expose
    private List<String> caracteristicasComum;

    @SerializedName("InformacoesComplementares")
    @Expose
    private String InformacoesComplementares;

    @SerializedName("Categoria")
    @Expose
    private String categoria;

    @SerializedName("AnoConstrucao")
    @Expose
    private Integer anoConstrucao;


    // Implementation Parcelable
    protected DetalheImovel(Parcel in) {
        super(in);

        if (in.readByte() == 0x01) {
            fotos = new ArrayList<>();
            in.readList(fotos, String.class.getClassLoader());
        } else {
            fotos = null;
        }
        observacao = in.readString();
        if (in.readByte() == 0x01) {
            caracteristicas = new ArrayList<>();
            in.readList(caracteristicas, String.class.getClassLoader());
        } else {
            caracteristicas = null;
        }
        precoCondominio = (BigDecimal) in.readValue(BigDecimal.class.getClassLoader());
        if (in.readByte() == 0x01) {
            caracteristicasComum = new ArrayList<>();
            in.readList(caracteristicasComum, String.class.getClassLoader());
        } else {
            caracteristicasComum = null;
        }
        InformacoesComplementares = in.readString();
        categoria = in.readString();
        anoConstrucao = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (fotos == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(fotos);
        }
        dest.writeString(observacao);
        if (caracteristicas == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(caracteristicas);
        }
        dest.writeValue(precoCondominio);
        if (caracteristicasComum == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(caracteristicasComum);
        }
        dest.writeString(InformacoesComplementares);
        dest.writeString(categoria);
        if (anoConstrucao == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(anoConstrucao);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DetalheImovel> CREATOR = new Parcelable.Creator<DetalheImovel>() {
        @Override
        public DetalheImovel createFromParcel(Parcel in) {
            return new DetalheImovel(in);
        }

        @Override
        public DetalheImovel[] newArray(int size) {
            return new DetalheImovel[size];
        }
    };

    // Getters and Setters
    public List<String> getFotos() {
        return fotos;
    }

    public String getObservacao() {
        return observacao;
    }

    public List<String> getCaracteristicas() {
        return caracteristicas;
    }

    public BigDecimal getPrecoCondominio() {
        return precoCondominio;
    }

    public String getPrecoCondominioFormatado() {
        return NumberUtils.currencyFormat(getPrecoCondominio());
    }

    public List<String> getCaracteristicasComum() {
        return caracteristicasComum;
    }

    public String getInformacoesComplementares() {
        return InformacoesComplementares;
    }

    public String getCategoria() {
        return categoria;
    }

    public Integer getAnoConstrucao() {
        return anoConstrucao;
    }

    public String getCaracteristicasCompleta() {
        return convertToString(getCaracteristicas());
    }

    public String getCaracteristicasComumCompleta() {
        return convertToString(getCaracteristicasComum());
    }

    public String getNomeAnunciante() {
        return getCliente().getNomeFantasia();
    }

    private String convertToString(List<String> items) {
        StringBuilder builder = new StringBuilder();
        int size  = items.size();

        if (size > 0) {
            int count = 0;

            for (String item : items) {

                if (count == 0) {
                    builder.append(item);
                } else {
                    if (size-1 == count) {
                        builder.append(item + ".");
                    } else {
                        builder.append(", " + item);
                    }
                }
                count++;
            }
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return "DetalheImovel{" +
                "fotos=" + fotos +
                ", observacao='" + observacao + '\'' +
                ", caracteristicas=" + caracteristicas +
                ", precoCondominio=" + precoCondominio +
                ", caracteristicasComum=" + caracteristicasComum +
                ", InformacoesComplementares='" + InformacoesComplementares + '\'' +
                ", categoria='" + categoria + '\'' +
                ", anoConstrucao='" + anoConstrucao + '\'' +
                '}';
    }
}
