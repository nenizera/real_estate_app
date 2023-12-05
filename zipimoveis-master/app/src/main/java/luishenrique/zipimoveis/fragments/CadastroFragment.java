package luishenrique.zipimoveis.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import luishenrique.zipimoveis.R;
import luishenrique.zipimoveis.adapters.CadImagemAdapter;
import luishenrique.zipimoveis.rest.models.ImovelBase;
import luishenrique.zipimoveis.rest.models.PhotosImovel;
import luishenrique.zipimoveis.utils.DialogUtils;
import luishenrique.zipimoveis.utils.PickerImageApp;
import luishenrique.zipimoveis.utils.StringUtils;
import luishenrique.zipimoveis.utils.UtilsApp;
import timber.log.Timber;

public class CadastroFragment extends Fragment {


    private View mInflate;
    private FirebaseDatabase mDatabaseReference;
    private EditText mEdtAreaUtil;
    private EditText mEdtBairro;
    private EditText mEdtCaractImovel;
    private EditText mEdtCaractImovelComum;
    private EditText mEdtCidade;
    private EditText mEdtDescricao;
    private EditText mEdtDormitorios;
    private EditText mEdtSuites;
    private EditText mEdtTipoImovel;
    private EditText mEdtValorVenda;
    private EditText mEdtRua;
    private EditText mEdtVagas;
    private EditText mEdtAnoConstrucao;
    private EditText mEdtValorCondominio;
    private RadioGroup mRgTipoImovel;
    private EditText mEdtCEP;
    private EditText mEdtEstado;
    private DialogUtils mDialogUtil;
    private MaterialDialog mProgressBar;
    private List<PhotosImovel> mPhotosImovelList = new ArrayList<>();
    private PickerImageApp mPickerImageApp;
    private ViewPager mPager;
    private CadImagemAdapter mImagensAdapter;
    private StorageReference mFirebaseStorageRef;
    private List<String> mListPhotoFirebase = new ArrayList<>();
    private int mCountEdt = 0;
    private EditText mEdtValorAluguel;

    public CadastroFragment() {
    }

    //admin@zipimoveis.com
    //123456

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseReference = FirebaseDatabase.getInstance();
        mFirebaseStorageRef = FirebaseStorage.getInstance().getReference();
        setHasOptionsMenu(true);
        getActivity().setTitle("Cadastro");

        configDialog();
    }

    private void configDialog() {
        mDialogUtil = new DialogUtils(getActivity());
        mProgressBar = mDialogUtil.getProgress();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_cadastro, container, false);

        configCampos();
        initAdapterPhotos();
        return mInflate;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_cadastro, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_send) {
            salvar();
        }
        return super.onOptionsItemSelected(item);
    }

    private void configCampos() {
        mEdtAreaUtil = (EditText) mInflate.findViewById(R.id.edt_cad_area_util);
        mEdtBairro = (EditText) mInflate.findViewById(R.id.edt_cad_bairro);
        mEdtCaractImovel = (EditText) mInflate.findViewById(R.id.edt_cad_caract_imovel);
        mEdtCaractImovelComum = (EditText) mInflate.findViewById(R.id.edt_cad_caract_imovel_comum);
        mEdtCidade = (EditText) mInflate.findViewById(R.id.edt_cad_cidade);
        mEdtDescricao = (EditText) mInflate.findViewById(R.id.edt_cad_descricao);
        mEdtDormitorios = (EditText) mInflate.findViewById(R.id.edt_cad_dormitorios);
        mEdtSuites = (EditText) mInflate.findViewById(R.id.edt_cad_suites);
        mEdtTipoImovel = (EditText) mInflate.findViewById(R.id.edt_cad_tipo_imovel);
        mEdtValorVenda = (EditText) mInflate.findViewById(R.id.edt_cad_valor_venda);
        mEdtValorAluguel = (EditText) mInflate.findViewById(R.id.edt_cad_aluguel);
        mEdtRua = (EditText) mInflate.findViewById(R.id.edt_cad_rua);
        mEdtVagas = (EditText) mInflate.findViewById(R.id.edt_cad_vagas);
        mEdtAnoConstrucao = (EditText) mInflate.findViewById(R.id.edt_cad_ano);
        mEdtValorCondominio = (EditText) mInflate.findViewById(R.id.edt_cad_valor_condominio);
        mEdtCEP = (EditText) mInflate.findViewById(R.id.edt_cad_cep);
        mEdtEstado = (EditText) mInflate.findViewById(R.id.edt_cad_estado);
        mRgTipoImovel = (RadioGroup) mInflate.findViewById(R.id.rg_tipo_imovel);


        maskEditText(mEdtValorVenda);
        maskEditText(mEdtValorCondominio);
        maskEditText(mEdtValorAluguel);

        RxTextView.textChangeEvents(mEdtCEP)
                .filter(new Predicate<TextViewTextChangeEvent>() {
                    @Override
                    public boolean test(TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {
                        int count = textViewTextChangeEvent.text().length();
                        if (count == mCountEdt) {
                            return false;
                        }

                        return true;
                    }
                })
                .map(new Function<TextViewTextChangeEvent, String>() {
                    @Override
                    public String apply(TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {

                        CharSequence text = textViewTextChangeEvent.text();

                        if (!TextUtils.isEmpty(text)) {
                            String formatCep = UtilsApp.mask("#####-###", text.toString());

                            mCountEdt = formatCep.length();
                            return formatCep;
                        } else {
                            String s = "";
                            mCountEdt = s.length();
                            return s;
                        }

                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mEdtCEP.setText(s);
                        mEdtCEP.setSelection(s.length());
                    }
                });

    }


    public void maskEditText(EditText editText) {
        RxTextView.textChangeEvents(editText)
                .filter(new Predicate<TextViewTextChangeEvent>() {
                    @Override
                    public boolean test(TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {
                        int count = textViewTextChangeEvent.text().length();
                        if (count == mCountEdt) {
                            return false;
                        }

                        return true;
                    }
                })
                .map(new Function<TextViewTextChangeEvent, String>() {
                    @Override
                    public String apply(TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {

                        CharSequence text = textViewTextChangeEvent.text();

                        if (!TextUtils.isEmpty(text)) {
                            String formatMoeda = StringUtils.formatMoeda(text.toString());
                            mCountEdt = formatMoeda.length();
                            return formatMoeda;
                        } else {
                            String s = "R$ 0,00";
                            mCountEdt = s.length();
                            return s;
                        }

                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                });
    }


    public class UploadFilesTask extends AsyncTask<PhotosImovel, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(PhotosImovel... params) {

            try {
                final PhotosImovel photosImovel = params[0];
                if (photosImovel == null) {
                    return null;
                }

                InputStream stream = getActivity().getContentResolver().openInputStream(photosImovel.getUri());

                StorageReference storageReference = mFirebaseStorageRef.child(photosImovel.getName());
                UploadTask uploadTask = storageReference.putStream(stream);

                uploadTask
                        .addOnFailureListener(e -> mDialogUtil.error("Não foi possível salvar as informações. Tente novamente"))
                        .addOnSuccessListener(taskSnapshot -> mListPhotoFirebase.add(photosImovel.getName()));

            } catch (FileNotFoundException e) {
                mDialogUtil.error("Não foi possível salvar as informações. Tente novamente");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void salvar() {
        try {

            mProgressBar.show();

            DatabaseReference imovelRef = mDatabaseReference.getReference("imoveis");

            ImovelBase imovelBase = new ImovelBase();

            int checkedRadioButtonId = mRgTipoImovel.getCheckedRadioButtonId();

            if (checkedRadioButtonId == R.id.rb_cad_alugar) {
                imovelBase.setTipoImovel("Alugar");
            } else {
                imovelBase.setTipoImovel("Vender");
            }

            if (!TextUtils.isEmpty(mEdtValorVenda.getText().toString())) {
                String valorVenda = mEdtValorVenda.getText().toString();
                imovelBase.setValorVenda(valorVenda);
            } else {

                if (checkedRadioButtonId == R.id.rb_cad_vender) {
                    showMessage("Campo Valor de Venda é obrigatório");
                    return;
                }
            }

            if (!TextUtils.isEmpty(mEdtValorAluguel.getText().toString())) {
                String valorAluguel = mEdtValorAluguel.getText().toString();
                imovelBase.setValorAluguel(valorAluguel);
            } else {

                if (checkedRadioButtonId == R.id.rb_cad_alugar) {
                    showMessage("Campo Valor de Aluguel é obrigatório");
                    return;
                }
            }

            if (!TextUtils.isEmpty(mEdtValorCondominio.getText().toString())) {
                String valorCondominio = mEdtValorCondominio.getText().toString();
                imovelBase.setPrecoCondominio(valorCondominio);
            } else {
                showMessage("Campo Valor do Codominio é obrigatório");
                return;
            }

            if (!TextUtils.isEmpty(mEdtTipoImovel.getText().toString())) {
                String categoria = mEdtTipoImovel.getText().toString();
                imovelBase.setCategoria(categoria);
            } else {
                showMessage("Campo Tipo do Imovel é obrigatório");
                return;
            }

            if (!TextUtils.isEmpty(mEdtRua.getText().toString())) {
                String rua = mEdtRua.getText().toString();
                imovelBase.setRua(rua);
            } else {
                showMessage("Campo Rua é obrigatório");
                return;
            }

            if (!TextUtils.isEmpty(mEdtBairro.getText().toString())) {
                String bairro = mEdtBairro.getText().toString();
                imovelBase.setBairro(bairro);
            } else {
                showMessage("Campo Bairro é obrigatório");
                return;
            }

            if (!TextUtils.isEmpty(mEdtCidade.getText().toString())) {
                String cidade = mEdtCidade.getText().toString();
                imovelBase.setCidade(cidade);
            } else {
                showMessage("Campo Cidade é obrigatório");
                return;
            }

            if (!TextUtils.isEmpty(mEdtCEP.getText().toString())) {
                String cep = mEdtCEP.getText().toString();
                if (cep.length() < 9) {
                    showMessage("Cep inválido");
                    return;
                }
                imovelBase.setCep(cep);
            } else {
                showMessage("Campo Cep é obrigatório");
                return;
            }

            if (!TextUtils.isEmpty(mEdtEstado.getText().toString())) {
                String estado = mEdtEstado.getText().toString();
                imovelBase.setEstado(estado);
            } else {
                showMessage("Campo Estado é obrigatório");
                return;
            }

            if (!TextUtils.isEmpty(mEdtAnoConstrucao.getText().toString())) {
                String ano = mEdtAnoConstrucao.getText().toString();
                if (ano.length() < 4) {
                    showMessage("Campo Ano de construção é inválido");
                    return;
                }

                imovelBase.setAnoConstrucao(ano);

            } else {
                showMessage("Campo Ano de construção é obrigatório");
                return;
            }

            if (!TextUtils.isEmpty(mEdtDormitorios.getText().toString())) {
                String dormitorios = mEdtDormitorios.getText().toString();
                imovelBase.setDormitorios(Integer.valueOf(dormitorios));
            } else {
                showMessage("Campo Dormitório é obrigatório");
                return;
            }

            if (!TextUtils.isEmpty(mEdtSuites.getText().toString())) {
                String suites = mEdtSuites.getText().toString();
                imovelBase.setSuites(Integer.valueOf(suites));
            } else {
                showMessage("Campo Suites é obrigatório");
                return;
            }

            if (!TextUtils.isEmpty(mEdtVagas.getText().toString())) {
                Integer vaga = Integer.valueOf(mEdtVagas.getText().toString());
                imovelBase.setVaga(vaga);
            } else {
                showMessage("Campo Vaga é obrigatório");
                return;
            }

            if (!TextUtils.isEmpty(mEdtAreaUtil.getText().toString())) {
                String areaUtil = mEdtAreaUtil.getText().toString();
                if (Integer.valueOf(areaUtil) == 0) {
                    showMessage("Area util inválida");
                    return;
                }
                imovelBase.setArea(areaUtil);
            } else {
                showMessage("Campo Area util é obrigatório");
                return;
            }

            if (!TextUtils.isEmpty(mEdtCaractImovel.getText().toString())) {
                String caractImovel = mEdtCaractImovel.getText().toString();
                imovelBase.setCaracteristicasImovel(caractImovel);
            } else {
                showMessage("Campo Caracteristicas do Imovel é obrigatório");
                return;
            }

            if (!TextUtils.isEmpty(mEdtCaractImovelComum.getText().toString())) {
                String caractImovelComum = mEdtCaractImovelComum.getText().toString();
                imovelBase.setCaracteristicasAreaComum(caractImovelComum);
            } else {
                showMessage("Campo Caracteristicas Comum é obrigatório");
                return;
            }

            if (!TextUtils.isEmpty(mEdtDescricao.getText().toString())) {
                String descricao = mEdtDescricao.getText().toString();
                imovelBase.setDescricao(descricao);
            } else {
                showMessage("Campo Descrição é obrigatório");
                return;
            }


            List<String> fotos = new ArrayList<>();

            if (mPhotosImovelList.size() > 0) {
                for (PhotosImovel photosImovel : mPhotosImovelList) {
                    if (photosImovel != null) {
                        if (!TextUtils.isEmpty(photosImovel.getName())) {
                            fotos.add(photosImovel.getName());
                        }
                    }
                }

                if (fotos.size() > 0) {
                    imovelBase.setFotos(fotos);
                }
            }

            if (fotos.size() == 0) {
                showMessage("Escolha de uma foto é obrigatório");
                return;
            }

            imovelBase.setAtivado(true);

            imovelRef.push()
                    .setValue(imovelBase)
                    .addOnSuccessListener(aVoid -> gravadoSucesso());

            if (mPhotosImovelList.size() > 0) {
                for (PhotosImovel photosImovel : mPhotosImovelList) {
                    new UploadFilesTask().execute(photosImovel);
                }
            }

        } catch (Exception e) {
            mProgressBar.dismiss();
            mDialogUtil.error("Houve uma falha ao cadastrar o imovel. Tente novamente");
            Timber.e(e);
        }

    }

    private void showMessage(String mensagem) {
        mProgressBar.dismiss();
        mDialogUtil.warning(mensagem);
    }

    private void gravadoSucesso() {
        mProgressBar.dismiss();
        mDialogUtil.success("Sucesso!",
                "Imovel cadastrado com sucesso!",
                () -> clearForm((ViewGroup) mInflate.findViewById(R.id.frameLayoutCad)));
    }

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }

            if (view instanceof RadioGroup) {
                ((RadioGroup) view).check(R.id.rb_cad_alugar);
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearForm((ViewGroup) view);
        }

        mPhotosImovelList.clear();
        mPhotosImovelList.add(null);
        mImagensAdapter.clearAdapter();

    }

    public void initAdapterPhotos() {

        mPickerImageApp = new PickerImageApp(getContext(), this);
        mPager = (ViewPager) mInflate.findViewById(R.id.pager);

        mPhotosImovelList = new ArrayList<>();
        mPhotosImovelList.add(null);

        // Imagens
        mImagensAdapter = new CadImagemAdapter(getContext(), mPhotosImovelList, new CadImagemAdapter.ImagensAdapterListener() {
            @Override
            public void onClickImage(int position) {
                if (position == 0) {
                    mPickerImageApp.pickImage();
                }
            }

            @Override
            public void onRemoveImage(final PhotosImovel photosPet, final int position) {
                new MaterialDialog.Builder(getActivity())
                        .title("Remover Foto")
                        .content("Deseja remover essa foto?")
                        .positiveText("Sim")
                        .negativeText("Cancelar")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                mPhotosImovelList.remove(position);
                                mImagensAdapter.update(mPhotosImovelList);
                            }
                        })
                        .show();

            }
        });

        mPager.setAdapter(mImagensAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mPickerImageApp.onActivityResult(requestCode, resultCode, data, uri -> updateAdapterImages(uri));
    }

    private void updateAdapterImages(Uri uri) {

        PhotosImovel photosImovel = new PhotosImovel();
        photosImovel.setUri(uri);

        final String urlFile = String.format("%s.png", UUID.randomUUID());
        photosImovel.setName(urlFile);

        mPhotosImovelList.add(photosImovel);

        mImagensAdapter.update(mPhotosImovelList);
    }

}
