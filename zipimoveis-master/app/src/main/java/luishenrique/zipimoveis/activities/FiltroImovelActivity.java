package luishenrique.zipimoveis.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import luishenrique.zipimoveis.R;
import luishenrique.zipimoveis.rest.ApiUtils;
import luishenrique.zipimoveis.rest.models.Cidades;
import luishenrique.zipimoveis.utils.DialogUtils;
import timber.log.Timber;

public class FiltroImovelActivity extends AppCompatActivity {

    private Spinner mSpinnerCidade;
    private List<Cidades> mCidades = new ArrayList<>();
    private Spinner mSpinnerBairro;
    private MaterialDialog mProgressBar;
    private DialogUtils mDialogUtil;
    private Spinner mSpinnerTipoImovel;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filtro_imovel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_filtro) {
            configFiltro();
            return true;

        } else {
            if (id == android.R.id.home) {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void configFiltro() {

        if (mSpinnerCidade.getSelectedItemPosition() == 0) {
            showMessage("Selecione uma Cidade");
            return;
        }

        String cidade = String.valueOf(mSpinnerCidade.getSelectedItem());

        String bairro = "";
        String tipoImovel = "";
        if (mSpinnerBairro.getSelectedItemPosition() > 0) {
            bairro = String.valueOf(mSpinnerBairro.getSelectedItem());
        }

        if (mSpinnerTipoImovel.getSelectedItemPosition() > 0) {
            tipoImovel = String.valueOf(mSpinnerTipoImovel.getSelectedItem());
        }

        returnFitros(cidade, bairro, tipoImovel);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro_imovel);

        ApiUtils apiUtils = new ApiUtils();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("Selecione um filtro");
        mCidades = new ArrayList<>();
        mDialogUtil = new DialogUtils(this);
        mProgressBar = mDialogUtil.getProgress();

        mSpinnerCidade = (Spinner) findViewById(R.id.spinnerCidade);
        mSpinnerBairro = (Spinner) findViewById(R.id.spinnerBairro);
        mSpinnerTipoImovel = (Spinner) findViewById(R.id.spinnerTipoImovel);

        configTipoImovel();
        mSpinnerCidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                List<String> bairros = new ArrayList<>();
                bairros.add("Selecionar");

                if (position > 0) {
                    Cidades cidades = mCidades.get(position-1);
                    bairros.addAll(cidades.getBairros());
                    configBairro(bairros);
                }  else {

                    configBairro( bairros );
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mProgressBar.show();
        apiUtils.getCidadeService().orderByChild("cidade")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            List<String> cidades = new ArrayList<>();
                            cidades.add("Selecionar");


                            Map<String, Cidades> value = dataSnapshot.getValue(apiUtils.formatListCidades);

                            for (Map.Entry<String, Cidades> cidadeEntry : value.entrySet()) {
                                Cidades cidade = cidadeEntry.getValue();
                                if (cidade != null ) {
                                    String nome = cidade.getCidade();
                                    cidades.add(nome);
                                    mCidades.add(cidade);
                                }


                            }

                            mProgressBar.dismiss();
                            configCidade(cidades);

                        } else {
                            mProgressBar.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mProgressBar.dismiss();
                    }
                });


    }

    private void configCidade(List<String> cidades) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cidades);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCidade.setAdapter(dataAdapter);

    }

    private void configBairro(List<String> list) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerBairro.setAdapter(dataAdapter);
    }

    private void configTipoImovel() {
        List<String> tipoImovel = new ArrayList<>();
        tipoImovel.add("Todos");
        tipoImovel.add("Alugar");
        tipoImovel.add("Vender");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tipoImovel);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTipoImovel.setAdapter(dataAdapter);
    }


    private void returnFitros(String cidade, String bairro, String tipoImovel) {
        Intent intent = new Intent();

        intent.putExtra("cidade", cidade);
        if (!TextUtils.isEmpty(bairro)) {
            intent.putExtra("bairro", bairro);
        }

        intent.putExtra("tipoImovel", tipoImovel);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void showMessage(String mensagem) {
        mDialogUtil.warning(mensagem);
    }

}
