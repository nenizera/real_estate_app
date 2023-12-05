package luishenrique.zipimoveis.rest.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

import luishenrique.zipimoveis.utils.MaskUtils;
import luishenrique.zipimoveis.utils.NumberUtils;

public class Imovel implements Parcelable {

    @SerializedName("CodImovel")
    @Expose
    private String codImovel;

    @SerializedName("TipoImovel")
    @Expose
    private String tipoImovel;

    @SerializedName("Endereco")
    @Expose
    private Endereco endereco;

    @SerializedName("PrecoVenda")
    @Expose
    private BigDecimal precoVenda;

    @SerializedName("Dormitorios")
    @Expose
    private Integer dormitorios;

    @SerializedName("Suites")
    @Expose
    private Integer suites;

    @SerializedName("Vagas")
    @Expose
    private Integer vagas;

    @SerializedName("AreaUtil")
    @Expose
    private Integer areaUtil;

    @SerializedName("AreaTotal")
    @Expose
    private Integer areaTotal;

    @SerializedName("DataAtualizacao")
    @Expose
    private String dataAtualizacao;

    @SerializedName("Cliente")
    @Expose
    private Cliente cliente;

    @SerializedName("SubTipoOferta")
    @Expose
    private String subTipoOferta;

    @SerializedName("SubtipoImovel")
    @Expose
    private String subtipoImovel;

    @SerializedName("EstagioObra")
    @Expose
    private String estagioObra;


    // Implementation Parcelable
    protected Imovel(Parcel in) {
        codImovel = in.readString();
        tipoImovel = in.readString();
        endereco = (Endereco) in.readValue(Endereco.class.getClassLoader());
        precoVenda = (BigDecimal) in.readValue(BigDecimal.class.getClassLoader());
        dormitorios = in.readByte() == 0x00 ? null : in.readInt();
        suites = in.readByte() == 0x00 ? null : in.readInt();
        vagas = in.readByte() == 0x00 ? null : in.readInt();
        areaUtil = in.readByte() == 0x00 ? null : in.readInt();
        areaTotal = in.readByte() == 0x00 ? null : in.readInt();
        dataAtualizacao = in.readString();
        cliente = (Cliente) in.readValue(Cliente.class.getClassLoader());
        subTipoOferta = in.readString();
        subtipoImovel = in.readString();
        estagioObra = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(codImovel);
        dest.writeString(tipoImovel);
        dest.writeValue(endereco);
        dest.writeValue(precoVenda);
        if (dormitorios == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(dormitorios);
        }
        if (suites == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(suites);
        }
        if (vagas == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(vagas);
        }
        if (areaUtil == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(areaUtil);
        }
        if (areaTotal == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(areaTotal);
        }
        dest.writeString(dataAtualizacao);
        dest.writeValue(cliente);
        dest.writeString(subTipoOferta);
        dest.writeString(subtipoImovel);
        dest.writeString(estagioObra);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Imovel> CREATOR = new Parcelable.Creator<Imovel>() {
        @Override
        public Imovel createFromParcel(Parcel in) {
            return new Imovel(in);
        }

        @Override
        public Imovel[] newArray(int size) {
            return new Imovel[size];
        }
    };


    // Getters and Setters
    public String getCodImovel() {
        return codImovel;
    }

    public String getTipoImovel() {
        return tipoImovel;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public String getEnderecoComplemento() {

        if (endereco.getLogradouro() != null) {
            return endereco.getLogradouro() + "-" + endereco.getBairro();
        }

        return MaskUtils.formatCEP(endereco.getCep()) + "-" + endereco.getBairro();
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public String getPrecoVendaFormatado() {
        return NumberUtils.currencyFormat(getPrecoVenda());
    }

    public Integer getDormitorios() {
        return dormitorios;
    }

    public Integer getSuites() {
        return suites;
    }

    public Integer getVagas() {
        return vagas;
    }

    public Integer getAreaUtil() {
        return areaUtil;
    }

    public Integer getAreaTotal() {
        return areaTotal;
    }

    public String getDataAtualizacao() {
        return dataAtualizacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getSubTipoOferta() {
        return subTipoOferta;
    }

    public String getSubtipoImovel() {
        return subtipoImovel;
    }

    public String getEstagioObra() {
        return estagioObra;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getDormsString() {
        return String.valueOf(getDormitorios());
    }

    public String getSuiteString() {
        return String.valueOf(getSuites());
    }

    public String getVagaString() {
        return String.valueOf(getVagas());
    }

    public String getAreaUtilString() {
        return String.valueOf(getAreaUtil());
    }


    @Override
    public String toString() {
        return "Imovel{" +
                "codImovel='" + codImovel + '\'' +
                ", tipoImovel='" + tipoImovel + '\'' +
                ", endereco=" + endereco +
                ", precoVenda=" + precoVenda +
                ", dormitorios=" + dormitorios +
                ", suites=" + suites +
                ", vagas=" + vagas +
                ", areaUtil=" + areaUtil +
                ", areaTotal=" + areaTotal +
                ", dataAtualizacao='" + dataAtualizacao + '\'' +
                ", cliente=" + cliente +
                ", subTipoOferta='" + subTipoOferta + '\'' +
                ", subtipoImovel='" + subtipoImovel + '\'' +
                ", estagioObra='" + estagioObra + '\'' +
                '}';
    }
}
