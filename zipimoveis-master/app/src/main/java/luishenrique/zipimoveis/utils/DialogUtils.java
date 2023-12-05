package luishenrique.zipimoveis.utils;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import luishenrique.zipimoveis.R;

public class DialogUtils {

    private Activity mActivity;

    public DialogUtils(Activity activity) {
        mActivity = activity;
    }

    public void success(String titleText, String contentText ,OnClickListener listener) {
        new MaterialDialog.Builder(mActivity)
                .title(titleText)
                .content(contentText)
                .positiveText("Ok")
                .positiveColor(mActivity.getResources().getColor(R.color.btnOK))
                .icon(mActivity.getDrawable(R.drawable.ic_check_circle_24dp))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        listener.onClick();
                    }
                })
                .show();
    }

    public void success(String titleText, String contentText) {
        new MaterialDialog.Builder(mActivity)
                .title(titleText)
                .content(contentText)
                .positiveText("Ok")
                .positiveColor(mActivity.getResources().getColor(R.color.btnOK))
                .icon(mActivity.getDrawable(R.drawable.ic_check_circle_24dp))
                .show();
    }

    public void error(String contentText){
        new MaterialDialog.Builder(mActivity)
                .title("Atenção")
                .content(contentText)
                .positiveText("Ok")
                .positiveColor(mActivity.getResources().getColor(R.color.btnOK))
                .icon(mActivity.getDrawable(R.drawable.ic_error_outline_24dp))
                .show();
    }

    public void warning(String contentText){
        new MaterialDialog.Builder(mActivity)
                .title("Atenção")
                .content(contentText)
                .positiveText("Ok")
                .positiveColor(mActivity.getResources().getColor(R.color.btnOK))
                .icon(mActivity.getDrawable(R.drawable.ic_warning_24dp))
                .show();
    }

    public void confirm(String contentText, OnClickListener listener){
        new MaterialDialog.Builder(mActivity)
                .title("Deseja remover?")
                .content(contentText)
                .positiveText("Remover")
                .positiveColor(mActivity.getResources().getColor(R.color.btnOK))
                .icon(mActivity.getDrawable(R.drawable.ic_warning_24dp))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        listener.onClick();
                    }
                })
                .negativeText("Cancelar")
                .show();
    }

    public MaterialDialog getProgress() {
        MaterialDialog show = new MaterialDialog.Builder(mActivity)
                .title("Aguarde")
                .progress(true, 0)
                .build();

        return show;
    }

    public static void settings(Activity activity, String confirmTitle, String confirmText, OnClickDialogUtils listener) {
        new MaterialDialog.Builder(activity)
                .title( confirmTitle )
                .content( confirmText )
                .negativeText( "Cancelar" )
                .positiveText( "Abrir Configurações" )
                .icon(activity.getResources().getDrawable(R.drawable.ic_warning_24dp))
                .onPositive((dialog, which) -> listener.onPositive())
                .show();
    }

    public interface OnClickListener {
        void onClick();
    }

    public interface OnClickDialogUtils {
        void onPositive();
    }
}
