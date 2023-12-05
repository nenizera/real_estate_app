package luishenrique.zipimoveis.rest.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CEP implements Parcelable {

    @SerializedName("logradouro")
    @Expose
    private String logradouro;

    @SerializedName("bairro")
    @Expose
    private String bairro;


    // Implementation Parcelable
    protected CEP(Parcel in) {
        logradouro = in.readString();
        bairro = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(logradouro);
        dest.writeString(bairro);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CEP> CREATOR = new Parcelable.Creator<CEP>() {
        @Override
        public CEP createFromParcel(Parcel in) {
            return new CEP(in);
        }

        @Override
        public CEP[] newArray(int size) {
            return new CEP[size];
        }
    };


    // Getters and Setters
    public String getLogradouro() {
        return logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    @Override
    public String toString() {
        return "CEP{" +
                "logradouro='" + logradouro + '\'' +
                ", bairro='" + bairro + '\'' +
                '}';
    }
}
