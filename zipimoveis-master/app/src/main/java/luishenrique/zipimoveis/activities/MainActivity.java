package luishenrique.zipimoveis.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luishenrique.zipimoveis.R;
import luishenrique.zipimoveis.fragments.CadastroFragment;
import luishenrique.zipimoveis.fragments.FavoritoFragment;
import luishenrique.zipimoveis.fragments.ImoveisFragment;
import luishenrique.zipimoveis.rest.models.Cidades;
import luishenrique.zipimoveis.rest.models.Imovel;
import luishenrique.zipimoveis.rest.models.ImovelBase;
import luishenrique.zipimoveis.rest.models.User;
import luishenrique.zipimoveis.utils.Constants;
import luishenrique.zipimoveis.utils.FirebaseAppUtils;
import luishenrique.zipimoveis.utils.FragmentUtils;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView   mNavigationView;
    private FirebaseAppUtils mFirebaseAppUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initActivity(savedInstanceState);

        // Config Firebase
        mFirebaseAppUtils = new FirebaseAppUtils();

        redirectToLogin();


    }



    @Override
    protected void onResume() {
        super.onResume();

        View headerView = mNavigationView.getHeaderView(0);

        configHeaderNav(headerView);

    }

    private void redirectToLogin() {
        if ( mFirebaseAppUtils == null || !mFirebaseAppUtils.isUserAuth() ) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void initActivity(Bundle savedInstanceState) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer          = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            selectFirstItemMenu();
        }
    }

    private void configHeaderNav(View headerView) {

        if (mFirebaseAppUtils == null || headerView == null) {
            return;
        }

        ImageView imgProfile  = (ImageView) headerView.findViewById(R.id.img_menu_user);
        TextView txtNameUser  = (TextView) headerView.findViewById(R.id.txt_menu_name_user);
        TextView txtEmailUser = (TextView) headerView.findViewById(R.id.txt_menu_email_user);

        FirebaseUser userAuth = mFirebaseAppUtils.getUserAuth();

        if (userAuth instanceof FirebaseUser) {

            String displayName = userAuth.getDisplayName();
            if (!TextUtils.isEmpty(displayName)) {
                txtNameUser.setText( displayName );
            }

            String email = userAuth.getEmail();
            if (!TextUtils.isEmpty(email)) {
                txtEmailUser.setText( email );
            }

            Uri photoUrl = userAuth.getPhotoUrl();
            if (photoUrl != null) {

                RequestOptions myOptions = new RequestOptions()
                        .circleCrop();

                Glide.with(getBaseContext())
                        .load(photoUrl)
                        .apply(myOptions)
                        .into(imgProfile);
            }

        }
    }

    private void selectFirstItemMenu() {
        mNavigationView.getMenu().getItem(0).setChecked(true);
        listaImoveisFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_lista :
                listaImoveisFragment();
                break;
            case R.id.nav_favoritos :
                favoritosFragment();
                break;
            case R.id.nav_logout :
                confirmLogout();
                break;
            case R.id.nav_delete_account :
                confirmDeleteUser();
                break;
            case R.id.nav_cadastro :
                cadastroFragment();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cadastroFragment() {
        selecionaFragment(new CadastroFragment(), Constants.CADASTRO_IMOVEL_FRAGMENT);
    }

    // Fragments
    private void favoritosFragment() {
        selecionaFragment(new FavoritoFragment(), Constants.FAVORITOS_FRAGMENT);
    }

    private void listaImoveisFragment() {
        selecionaFragment(new ImoveisFragment(), Constants.LISTA_IMOVEIS_FRAGMENT);
    }

    private void selecionaFragment(Fragment fragment, String tagFragment) {
        FragmentUtils.loadFragment(getSupportFragmentManager(), fragment, R.id.content_frame, tagFragment);
    }

    private void confirmLogout() {
        new MaterialDialog.Builder(this)
                .title("Sair do aplicativo")
                .content("Deseja realmente sair do aplicativo?")
                .positiveText("Sim")
                .negativeText("Cancelar")
                .onPositive((dialog, which) -> logout())
                .show();
    }

    private void logout() {
        clearPrefs();
        FirebaseAuth.getInstance().signOut();
        redirectToLogin();
    }

    private void confirmDeleteUser() {
        new MaterialDialog.Builder(this)
                .title("Remover conta")
                .content("Sua conta sera removida e não estara mais disponível para acesso. deseja remover?")
                .positiveText("Sim")
                .negativeText("Cancelar")
                .onPositive((dialog, which) -> deleteUser())
                .show();
    }

    private void deleteUser() {
        clearPrefs();
        mFirebaseAppUtils.deleteUser(new FirebaseAppUtils.OnResultListener() {
            @Override
            public void onSuccess() {
                redirectToLogin();
            }

            @Override
            public void onError() {
                Snackbar.make(getCurrentFocus(), "Falha ao remover conta.Tente novamente", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void clearPrefs() {
        Prefs.remove("cidade");
        Prefs.remove("bairro");
        Prefs.remove("tipoImovel");
    }

}
