package luishenrique.zipimoveis.rest.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Endereco implements Parcelable {

    @SerializedName("Logradouro")
    @Expose
    private String logradouro;

    @SerializedName("Numero")
    @Expose
    private String numero;

    @SerializedName("Complemento")
    @Expose
    private String complemento;

    @SerializedName("CEP")
    @Expose
    private String cep;

    @SerializedName("Bairro")
    @Expose
    private String bairro;

    @SerializedName("Cidade")
    @Expose
    private String cidade;

    @SerializedName("Estado")
    @Expose
    private String estado;

    @SerializedName("Zona")
    @Expose
    private String zona;


    // Implementation Parcelable
    protected Endereco(Parcel in) {
        logradouro = in.readString();
        numero = in.readString();
        complemento = in.readString();
        cep = in.readString();
        bairro = in.readString();
        cidade = in.readString();
        estado = in.readString();
        zona = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(logradouro);
        dest.writeString(numero);
        dest.writeString(complemento);
        dest.writeString(cep);
        dest.writeString(bairro);
        dest.writeString(cidade);
        dest.writeString(estado);
        dest.writeString(zona);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Endereco> CREATOR = new Parcelable.Creator<Endereco>() {
        @Override
        public Endereco createFromParcel(Parcel in) {
            return new Endereco(in);
        }

        @Override
        public Endereco[] newArray(int size) {
            return new Endereco[size];
        }
    };


    // Getters and Setters
    public String getLogradouro() {
        return logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getCep() {
        return cep;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getZona() {
        return zona;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "logradouro='" + logradouro + '\'' +
                ", numero='" + numero + '\'' +
                ", complemento='" + complemento + '\'' +
                ", cep='" + cep + '\'' +
                ", bairro='" + bairro + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", zona='" + zona + '\'' +
                '}';
    }
}
