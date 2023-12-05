package luishenrique.zipimoveis.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import luishenrique.zipimoveis.R;
import luishenrique.zipimoveis.utils.DialogUtils;
import luishenrique.zipimoveis.utils.StringUtils;

public class CreateLoginEmailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DialogUtils mDialogUtils;
    private MaterialDialog mDialogProgress;
    private EditText mEdtEmail;
    private EditText mEdtSenha;
    private EditText mEdtUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_login_email);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mAuth = FirebaseAuth.getInstance();
        mDialogUtils = new DialogUtils(this);
        mDialogProgress = mDialogUtils.getProgress();

        mEdtEmail = (EditText) findViewById(R.id.edt_email_create);
        mEdtSenha = (EditText) findViewById(R.id.edt_senha_create);
        mEdtUserName = (EditText) findViewById(R.id.edt_user_name_create);

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
                createAccountEmail();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createAccountEmail() {

        mDialogProgress.show();

        String email = mEdtEmail.getText().toString();
        String senha = mEdtSenha.getText().toString();
        String username = mEdtUserName.getText().toString();

        if (TextUtils.isEmpty(email)) {
            dismissProgress();
            mDialogUtils.warning("e-mail é obrigatório");
            return;
        } else {
            if (!StringUtils.isValidEmail(email)) {
                dismissProgress();
                mDialogUtils.warning("e-mail é inválido");
                return;
            }
        }

        if (TextUtils.isEmpty(username)) {
            dismissProgress();
            mDialogUtils.warning("Nome completo é obrigatório");
            return;
        }

        if (TextUtils.isEmpty(senha)) {
            dismissProgress();
            mDialogUtils.warning("Senha é obrigatória");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> completeCreateAccountEmail(task));
    }

    private void completeCreateAccountEmail(Task<AuthResult> task) {
        if (task.isSuccessful()) {
            String username = mEdtUserName.getText().toString();

            FirebaseUser user = mAuth.getCurrentUser();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task1 -> {
                        dismissProgress();
                        if (task1.isSuccessful()) {
                            redirectToMain();
                        }
                    });

        } else {
            dismissProgress();
            mDialogUtils.error("Falha ao criar conta.Tente novamente");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    private void dismissProgress() {
        if (mDialogProgress.isShowing()) {
            mDialogProgress.dismiss();
        }
    }

    private void redirectToMain() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
