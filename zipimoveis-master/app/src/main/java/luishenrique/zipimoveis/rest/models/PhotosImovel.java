package luishenrique.zipimoveis.rest.models;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class PhotosImovel implements Parcelable {

    public PhotosImovel() {
    }

    @PropertyName("name")
    private String name;

    @Exclude
    private Uri uri;

    public String getName() {
        if (name != null) {
            if (name.indexOf("_") >= 0) {
                return String.format("%s", name);
            } else {
                return String.format("_%s", name);
            }

        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public Uri getUri() {
        return uri;
    }

    @Exclude
    public void setUri(Uri uri) {
        this.uri = uri;
    }


    @Override
    public String toString() {
        return "PhotosImovel{" +
                "name='" + name + '\'' +
                ", uri=" + uri +
                '}';
    }

    protected PhotosImovel(Parcel in) {
        name = in.readString();
        uri = (Uri) in.readValue(Uri.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeValue(uri);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PhotosImovel> CREATOR = new Parcelable.Creator<PhotosImovel>() {
        @Override
        public PhotosImovel createFromParcel(Parcel in) {
            return new PhotosImovel(in);
        }

        @Override
        public PhotosImovel[] newArray(int size) {
            return new PhotosImovel[size];
        }
    };
}
