package luishenrique.zipimoveis.rest.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contato implements Parcelable {

    public Contato() {}

    @SerializedName("Nome")
    @Expose
    private String nome;

    @SerializedName("Telefone")
    @Expose
    private String telefone;

    @SerializedName("Mensagem")
    @Expose
    private String mensagem;

    @SerializedName("Email")
    @Expose
    private String email;

    @SerializedName("DDD")
    @Expose
    private String ddd;

    @SerializedName("CodCliente")
    @Expose
    private Long codCliente;

    // Implementation Parcelable
    protected Contato(Parcel in) {
        nome        = in.readString();
        telefone    = in.readString();
        mensagem    = in.readString();
        email       = in.readString();
        ddd         = in.readString();
        codCliente  = in.readByte() == 0x00 ? null : in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(telefone);
        dest.writeString(mensagem);
        dest.writeString(email);
        dest.writeString(ddd);
        if (codCliente == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(codCliente);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Contato> CREATOR = new Parcelable.Creator<Contato>() {
        @Override
        public Contato createFromParcel(Parcel in) {
            return new Contato(in);
        }

        @Override
        public Contato[] newArray(int size) {
            return new Contato[size];
        }
    };

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public Long getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Long codCliente) {
        this.codCliente = codCliente;
    }


}
