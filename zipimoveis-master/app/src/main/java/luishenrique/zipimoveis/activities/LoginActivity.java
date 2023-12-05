package luishenrique.zipimoveis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.Set;

import luishenrique.zipimoveis.R;
import luishenrique.zipimoveis.utils.DialogUtils;
import luishenrique.zipimoveis.utils.FirebaseAppUtils;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 100;
    private CallbackManager mCallbackManager;

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private DialogUtils mDialogUtils;
    private MaterialDialog mDialogProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // config Firebase
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        mDialogUtils = new DialogUtils(this);
        mDialogProgress = mDialogUtils.getProgress();

        // Config Facebook Auth
        configLoginFacebook();
        initFacebookAuth();
        initGoogleSignin();
        initEmailSignin();

        Button btnCreateAcccount = (Button) findViewById(R.id.btn_create_account);
        btnCreateAcccount.setOnClickListener(v -> redirectToCreateAccount());

    }

    private void redirectToCreateAccount() {
        startActivity(new Intent(this, CreateLoginEmailActivity.class));
    }

    private void initEmailSignin() {
        Button btnEmailSignin = (Button) findViewById(R.id.btn_email_signin);
        btnEmailSignin.setOnClickListener(v -> redirectToEmailLogin());
    }

    private void redirectToEmailLogin() {
        startActivity(new Intent(this, LoginEmailActivity.class));
    }

    private void initGoogleSignin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Button signInButton = (Button) findViewById(R.id.btn_google_signin);
        signInButton.setOnClickListener(v -> googleSignIn());

    }

    private void googleSignIn() {
        mDialogProgress.show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void initFacebookAuth() {
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> sendLoginFacebook());
    }

    private void configLoginFacebook() {

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Set<String> recentlyDeniedPermissions = loginResult.getRecentlyDeniedPermissions();

                dismissProgress();

                boolean isOk = true;
                for (String deniedPermission : recentlyDeniedPermissions) {
                    if (deniedPermission.equals("email")) {
                        isOk = false;
                        break;
                    }
                }

                if (isOk) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                } else {
                    mDialogUtils.warning("Permissão de e-mail é obrigatória");
                }
            }

            @Override
            public void onCancel() {
                dismissProgress();
            }

            @Override
            public void onError(FacebookException error) {
                dismissProgress();
                AccessToken.refreshCurrentAccessTokenAsync();
                mDialogUtils.warning("Não foi possivel autenticar.Tente novamente");
            }
        });
    }

    private void sendLoginFacebook() {
        mDialogProgress.show();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        callAuthFirebase();
    }

    private void callAuthFirebase() {
        if (mAuth.getCurrentUser() instanceof FirebaseUser) {
            redirectToMain();
        }
    }

    private void redirectToMain() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }


    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();

            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
                dismissProgress();
                if (task.isSuccessful()) {
                    redirectToMain();
                } else {
                    mDialogUtils.error("Falha ao logar com Google.Tente novamente ou use outra opção");
                }
            }).addOnFailureListener(e ->
                    mDialogUtils.error("E-mail já utilizado em outra conta ou associada com outra forma de autenticação. Tente usar outra opção")
            );

        } else {
            dismissProgress();
        }
    }

    private void dismissProgress() {
        if (mDialogProgress.isShowing()) {
            mDialogProgress.dismiss();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        mDialogProgress.show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, getOnCompleteListener())
                .addOnFailureListener(e -> validErrorFacebook(e));
    }

    private void validErrorFacebook(Exception e) {

        if (e instanceof FirebaseAuthUserCollisionException) {
            dismissProgress();
            mDialogUtils.warning("Conta já associada com outra forma autenticação. Tente usar outra opção");
        } else {
            dismissProgress();
            mDialogUtils.error("Falha ao autenticar usuário. Tente novamente");
        }

    }

    @NonNull
    private OnCompleteListener<AuthResult> getOnCompleteListener() {
        return task -> {
            if (task.isSuccessful()) {
                redirectToMain();
            }
        };
    }

    // Implementation Google SignIn
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        dismissProgress();

        mDialogUtils.error("Falha ao autenticar com o Google.Tente novamente");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() instanceof FirebaseUser) {
            redirectToMain();
        }
    }

}
