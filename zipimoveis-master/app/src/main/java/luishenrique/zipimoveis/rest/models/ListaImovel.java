package luishenrique.zipimoveis.rest.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListaImovel extends Imovel {

    @SerializedName("StatusQualidadeTotal")
    @Expose
    private String statusQualidadeTotal;

    @SerializedName("UrlImagem")
    @Expose
    private String urlImagem;

    private boolean isFavorito;


    // Implementation Parcelable
    protected ListaImovel(Parcel in) {
        super(in);
        statusQualidadeTotal = in.readString();
        urlImagem = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(statusQualidadeTotal);
        dest.writeString(urlImagem);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ListaImovel> CREATOR = new Parcelable.Creator<ListaImovel>() {
        @Override
        public ListaImovel createFromParcel(Parcel in) {
            return new ListaImovel(in);
        }

        @Override
        public ListaImovel[] newArray(int size) {
            return new ListaImovel[size];
        }
    };


    // Getters and Setters
    public String getStatusQualidadeTotal() {
        return statusQualidadeTotal;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public boolean isFavorito() {
        return isFavorito;
    }

    public void setFavorito(boolean favorito) {
        isFavorito = favorito;
    }

    @Override
    public String toString() {
        return "ListaImovel{" +
                "statusQualidadeTotal='" + statusQualidadeTotal + '\'' +
                ", urlImagem='" + urlImagem + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListaImovel)) return false;

        ListaImovel that = (ListaImovel) o;

        if (isFavorito() != that.isFavorito()) return false;
        return getCodImovel().equals(that.getCodImovel());

    }

    @Override
    public int hashCode() {
        int result = getCodImovel().hashCode();
        result = 31 * result + (isFavorito() ? 1 : 0);
        return result;
    }
}
