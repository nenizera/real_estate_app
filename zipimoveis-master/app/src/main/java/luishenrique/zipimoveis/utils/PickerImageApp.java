package luishenrique.zipimoveis.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableTransformer;

public class PickerImageApp {

    private Fragment mFragment;
    private Context mContext;
    private Activity mActivity;
    private OnPickerImageApp mListener;
    private List<String> mPermissionOk = new ArrayList<>();
    private RxPermissions mRxPermissions;
    private ObservableTransformer<Object, Permission> mPermissionsUpload;
    private boolean isFromFragment = false;
    private String[] mStringsPermission = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    public PickerImageApp(Activity activity) {
        mActivity = activity;
        init();
    }

    public PickerImageApp(Context context, Fragment fragment) {
        mContext = context;
        mFragment = fragment;
        mActivity = fragment.getActivity();
        isFromFragment = true;
        init();
    }

    private void init() {
        mRxPermissions = new RxPermissions(mActivity);
        mPermissionsUpload = mRxPermissions.ensureEach(mStringsPermission);
    }

    public void pickImage() {
        mRxPermissions.
                requestEach( mStringsPermission )
                .subscribe(permission ->
                        executePermission(permission)
                );
    }

    public void pickImage(View view) {
        RxView.clicks( view )
                .compose( mPermissionsUpload )
                .subscribe(permission ->
                        executePermission(permission)

                );
    }

    private void executePermission(Permission permission) {
        String name = permission.name;
        if (permission.granted) {
            if (!mPermissionOk.contains(name)) {
                mPermissionOk.add(name);
            }

            if (mPermissionOk.size() == 3) {
                mPermissionOk.clear();
                onSelectImageClick();
            }

        } else {
            if (!permission.shouldShowRequestPermissionRationale) {

                if (name.equals( Manifest.permission.READ_EXTERNAL_STORAGE )) {
                    DialogUtils.settings(mActivity, "Permissão de Armazenamento",
                            "Permissão de Armazenamento é obrigatório para você selecionar imagens. Para permitir, vá para Configurações->Aplicativos->Pes de Rua->Permissões ",
                            () -> UtilsApp.callSettings(mActivity));
                } else if (name.equals( Manifest.permission.CAMERA )){
                    DialogUtils.settings(mActivity, "Permissão de Camera",
                            "Permissão de Camera é obrigatório para você tirar fotos. Para permitir, vá para Configurações->Aplicativos->Pes de Rua->Permissões",
                            () -> UtilsApp.callSettings(mActivity));

                }
            }
        }
    }

    public void onSelectImageClick() {
        if (isFromFragment) {
            CropImage.startPickImageActivity(mContext, mFragment);
        } else {
            CropImage.startPickImageActivity(mActivity);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, OnPickerImageApp onPickerImageApp) {
        mListener = onPickerImageApp;

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(mActivity, data);

            if (!CropImage.isReadExternalStoragePermissionsRequired(mActivity, imageUri)) {
                startCropImageActivity(imageUri);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == mActivity.RESULT_OK) {
                Uri resultUri = result.getUri();
                mListener.onSuccess(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.ActivityBuilder activityBuilder = CropImage.activity(imageUri)
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .setRequestedSize(400, 400)
                .setInitialCropWindowPaddingRatio(0)
                .setScaleType(CropImageView.ScaleType.CENTER_INSIDE)
                .setAllowRotation(true)
                .setOutputCompressQuality(60);

        if (isFromFragment) {
            CropImage.activity(imageUri)
                    .start(mContext, mFragment);
        } else {
            activityBuilder
                    .start(mActivity);
        }
    }


    public interface OnPickerImageApp {
        void onSuccess(Uri uri);
    }
}
