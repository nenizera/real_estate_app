package luishenrique.zipimoveis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import luishenrique.zipimoveis.R;
import luishenrique.zipimoveis.utils.DialogUtils;
import luishenrique.zipimoveis.utils.StringUtils;

public class LoginEmailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DialogUtils mDialogUtils;
    private MaterialDialog mDialogProgress;
    private EditText mEdtEmail;
    private EditText mEdtSenha;
    private Button mEdRenewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mAuth = FirebaseAuth.getInstance();
        mDialogUtils = new DialogUtils(this);
        mDialogProgress = mDialogUtils.getProgress();

        mEdtEmail = (EditText) findViewById(R.id.edt_email);
        mEdtSenha = (EditText) findViewById(R.id.edt_senha);
        mEdRenewPassword = (Button) findViewById(R.id.btn_renew_password);

        mEdRenewPassword.setOnClickListener(v -> renewPassword());
    }

    private void renewPassword() {

        new MaterialDialog.Builder(this)
                .title("Esqueci minha senha")
                .positiveText("Enviar")
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .input("e-mail", "", (dialog, email) -> {
                    if (TextUtils.isEmpty(email)) {
                        mDialogUtils.warning("e-mail é obrigatório");
                    } else {
                        dialog.dismiss();
                        mDialogProgress.show();
                        sendEmailRenewPassword(email);
                    }

                }).show();
    }

    private void sendEmailRenewPassword(CharSequence email) {
        mAuth.sendPasswordResetEmail(email.toString())
                .addOnCompleteListener(task -> completeRenewPassword(task));
    }

    private void completeRenewPassword(Task<Void> task) {
        dismissProgress();
        if (task.isSuccessful()) {
            mDialogUtils.success("e-mail enviado!","Em breve você receberá um e-mail com o link para redefinir sua senha");
        } else {
            mDialogUtils.warning("Falha ao enviar e-mail. Tente novamente");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home :
                finish();
                return true;
            case R.id.btn_send :
                loginAccountEmail();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    private void loginAccountEmail() {
        mDialogProgress.show();

        String email = mEdtEmail.getText().toString();
        String senha = mEdtSenha.getText().toString();

        if (TextUtils.isEmpty(email)) {
            dismissProgress();
            mDialogUtils.warning("e-mail é obrigatório ");
            return;
        } else {
            if (!StringUtils.isValidEmail(email)) {
                dismissProgress();
                mDialogUtils.warning("e-mail é inválido ");
                return;
            }
        }

        if (TextUtils.isEmpty(senha)) {
            dismissProgress();
            mDialogUtils.warning("Senha é obrigatória ");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> completeLoginEmail(task));
    }

    private void completeLoginEmail(Task<AuthResult> task) {
        dismissProgress();
        if (task.isSuccessful()) {
            redirectToMain();
        } else {
            mDialogUtils.error("e-mail ou senha inválidos");
        }
    }

    private void redirectToMain() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void dismissProgress() {
        if (mDialogProgress.isShowing()) {
            mDialogProgress.dismiss();
        }
    }

}
