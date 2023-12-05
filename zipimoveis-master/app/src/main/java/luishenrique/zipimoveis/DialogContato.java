package luishenrique.zipimoveis;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDButton;

import luishenrique.zipimoveis.rest.ApiUtils;
import luishenrique.zipimoveis.rest.models.Cliente;
import luishenrique.zipimoveis.rest.models.Contato;
import luishenrique.zipimoveis.rest.responses.ContatoResponse;
import luishenrique.zipimoveis.rest.services.ContatoService;
import luishenrique.zipimoveis.utils.Constants;
import luishenrique.zipimoveis.utils.DialogUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogContato {

    private Activity        mActivity;
    private MaterialDialog  mDialog;
    private EditText        mEdtNome;
    private EditText        mEdtEmail;
    private EditText        mEdtDDD;
    private EditText        mEdtTelefone;
    private String          mNomeContato;
    private String          mEmailContato;
    private String          mDDDContato;
    private String          mTelefoneContato;
    private EditText        mEdtMessage;
    private String          mMessageContato;
    private Cliente         mCliente;
    private DialogUtils     mDialogUtil;

    public DialogContato getDialog(Activity activity) {
        mActivity = activity;
        if (mDialog == null) {
            createDialogContato();
        }

        return this;
    }

    public void show(Cliente cliente) {
        mCliente = cliente;
        mDialog.show();
    }

    private void createDialogContato() {
        mDialogUtil = new DialogUtils(mActivity);
        mDialog = new MaterialDialog.Builder(mActivity)
                .title( getString(luishenrique.zipimoveis.R.string.title_dialog_contato) )
                .customView( luishenrique.zipimoveis.R.layout.dialog_contato, true )
                .positiveText( getString(luishenrique.zipimoveis.R.string.btn_send) )
                .negativeText( getString(luishenrique.zipimoveis.R.string.btn_cancel) )
                .onPositive( onClickPositiveContato() )
                .onNegative( onClickNegativeContato() )
                .autoDismiss(false)
                .build();

        MDButton btnNegative = mDialog.getActionButton(DialogAction.NEGATIVE);
        btnNegative.setTextColor(ContextCompat.getColor(mActivity, android.R.color.holo_red_light));

        View customView = mDialog.getCustomView();
        if (customView != null) {
            mEdtNome     = (EditText) customView.findViewById(luishenrique.zipimoveis.R.id.edt_nome_dialog_contato);
            mEdtEmail    = (EditText) customView.findViewById(luishenrique.zipimoveis.R.id.edt_email_dialog_contato);
            mEdtDDD      = (EditText) customView.findViewById(luishenrique.zipimoveis.R.id.edt_ddd_dialog_contato);
            mEdtTelefone = (EditText) customView.findViewById(luishenrique.zipimoveis.R.id.edt_telefone_dialog_contato);
            mEdtMessage  = (EditText) customView.findViewById(luishenrique.zipimoveis.R.id.edt_message_dialog_contato);
        }
    }

    @NonNull
    private MaterialDialog.SingleButtonCallback onClickPositiveContato() {
        return new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                String message = messageRequiredFields();
                if (message != null) {
                    mDialogUtil.warning(message);
                } else {
                    dialog.dismiss();
                    sendContato();
                }
            }
        };
    }

    @NonNull
    private MaterialDialog.SingleButtonCallback onClickNegativeContato() {
        return new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        };
    }

    private void sendContato() {
        ContatoService mServiceContato = ApiUtils.getContatoService();
        Contato contato = getContato();
        mServiceContato.post(contato).enqueue( onCallbackContato() );
    }

    @NonNull
    private Callback<ContatoResponse> onCallbackContato() {
        return new Callback<ContatoResponse>() {
            @Override
            public void onResponse(@NonNull Call<ContatoResponse> call, @NonNull Response<ContatoResponse> response) {
                if (response.isSuccessful()) {
                    ContatoResponse body = response.body();

                    if (body != null) {
                        if (body.getMsg().equalsIgnoreCase( Constants.RESPONSE_OK )) {
                            mDialogUtil.success(getString(luishenrique.zipimoveis.R.string.title_success_contato), getString(luishenrique.zipimoveis.R.string.msg_success_contato));
                        }
                    }
                } else {
                    mDialogUtil.error(getString(luishenrique.zipimoveis.R.string.msg_error_generic));
                }
            }

            @SuppressWarnings("NullableProblems")
            @Override
            public void onFailure(Call<ContatoResponse> call, @NonNull Throwable t) {
                mDialogUtil.error(getString(luishenrique.zipimoveis.R.string.msg_error_generic));
            }
        };
    }

    private String getString(int resId) {
        return mActivity.getResources().getString(resId);
    }

    private String getString(int resId, int resIds) {
        return mActivity.getResources().getString(resId, getString(resIds));
    }

    @NonNull
    private Contato getContato() {
        Contato contato = new Contato();
        contato.setNome(mNomeContato);
        contato.setMensagem(mMessageContato);
        contato.setEmail(mEmailContato);
        contato.setTelefone(mTelefoneContato);
        contato.setDdd(mDDDContato);
        contato.setCodCliente(mCliente.getCodCliente());
        return contato;
    }

    private String messageRequiredFields() {

        mNomeContato     = mEdtNome.getText().toString();
        mEmailContato    = mEdtEmail.getText().toString();
        mDDDContato      = mEdtDDD.getText().toString();
        mTelefoneContato = mEdtTelefone.getText().toString();
        mMessageContato  = mEdtMessage.getText().toString();

        if (mNomeContato.isEmpty()) {
            return getString(luishenrique.zipimoveis.R.string.msg_required_field, luishenrique.zipimoveis.R.string.field_nome);
        }

        if (mEmailContato.isEmpty()) {
            return getString(luishenrique.zipimoveis.R.string.msg_required_field, luishenrique.zipimoveis.R.string.field_email);
        }

        if (mDDDContato.isEmpty()) {
            return getString(luishenrique.zipimoveis.R.string.msg_required_field, luishenrique.zipimoveis.R.string.field_ddd);
        }

        if (mTelefoneContato.isEmpty()) {
            return getString(luishenrique.zipimoveis.R.string.msg_required_field, luishenrique.zipimoveis.R.string.field_telefone);
        }

        if (mMessageContato.isEmpty()) {
            return getString(luishenrique.zipimoveis.R.string.msg_required_field, luishenrique.zipimoveis.R.string.field_message);
        }

        return null;
    }
}
