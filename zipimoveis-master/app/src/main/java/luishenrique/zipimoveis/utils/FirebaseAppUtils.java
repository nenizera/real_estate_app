package luishenrique.zipimoveis.utils;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseAppUtils {

    private final FirebaseFirestore mFirestore;
    private final FirebaseAuth mFirebaseAuth;
    private final FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFirebaseDatabase;

    public FirebaseAppUtils() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
    }

    public FirebaseUser getUserAuth() {
        return mFirebaseAuth.getCurrentUser();
    }

    public boolean isUserAuth() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            return true;
        }
        return false;
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return mFirebaseDatabase;
    }

    public FirebaseStorage getFirebaseStorage() {
        return mFirebaseStorage;
    }

    public StorageReference getFirebaseStorageRef() {
        return mFirebaseStorage.getReference();
    }

    public FirebaseFirestore getFirestore() {
        return mFirestore;
    }

    public CollectionReference getFirestoreUser() {
        return mFirestore.collection("users");
    }

    public void deleteUser(final OnResultListener listener) {
        FirebaseUser userAuth = getUserAuth();
        if (userAuth instanceof FirebaseUser) {

            userAuth.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            listener.onSuccess();
                        } else {
                            listener.onError();
                        }
                    }
                });
        }
    }

    public interface OnResultListener {
        void onSuccess();
        void onError();
    }
}
