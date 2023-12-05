package luishenrique.zipimoveis.rest.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cliente implements Parcelable {

    @SerializedName("CodCliente")
    @Expose
    private Long codCliente;

    @SerializedName("NomeFantasia")
    @Expose
    private String nomeFantasia;

    // Implementation Parcelable
    protected Cliente(Parcel in) {
        codCliente = in.readByte() == 0x00 ? null : in.readLong();
        nomeFantasia = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (codCliente == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(codCliente);
        }
        dest.writeString(nomeFantasia);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Cliente> CREATOR = new Parcelable.Creator<Cliente>() {
        @Override
        public Cliente createFromParcel(Parcel in) {
            return new Cliente(in);
        }

        @Override
        public Cliente[] newArray(int size) {
            return new Cliente[size];
        }
    };

    // Getters and Setters
    public Long getCodCliente() {
        return codCliente;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "codCliente=" + codCliente +
                ", nomeFantasia='" + nomeFantasia + '\'' +
                '}';
    }
}