package luishenrique.zipimoveis.fragments;


import android.os.Bundle;
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
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigDecimal;
import java.util.List;

import luishenrique.zipimoveis.DialogContato;
import luishenrique.zipimoveis.R;
import luishenrique.zipimoveis.adapters.ImagensAdapter;
import luishenrique.zipimoveis.rest.models.ImovelBase;
import luishenrique.zipimoveis.utils.DialogUtils;
import luishenrique.zipimoveis.utils.FirebaseAppUtils;
import luishenrique.zipimoveis.utils.NumberUtils;
import timber.log.Timber;

public class DetalheImovelFragment extends Fragment {

    public static final String REQUEST_DETALHE = "request_detalhe";

    private View mView;
    private DialogContato mDialog;
    private ViewPager mPager;
    private ImovelBase mImovel;
    private TextView mTxtPreco;
    private TextView mTxtEndereco;
    private TextView mTxtSubTipo;
    private TextView mTxtDorms;
    private TextView mTxtSuites;
    private TextView mTxtVagas;
    private TextView mTxtAreaUtil;
    private TextView mTxtDescricao;
    private TextView mTxtCondominio;
    private TextView mTxtCaracteristica;
    private TextView mTxtCaracteristicaComum;
    private DialogUtils mDialogUtil;
    private MaterialDialog mProgressBar;
    private FirebaseDatabase mDatabaseReference;
    private FirebaseAppUtils mFirebaseAppUtils;
    private FirebaseUser mUserAuth;
    private TextView mLblValorAluguel;
    private TextView mTxtValorAluguel;
    private TextView mLblPreco;

    public DetalheImovelFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseReference = FirebaseDatabase.getInstance();
        mFirebaseAppUtils = new FirebaseAppUtils();

        mUserAuth = mFirebaseAppUtils.getUserAuth();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_detalhe_imovel, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initFields();
        initArguments();
        setHasOptionsMenu(true);

        getActivity().setTitle("Imovel");
    }

    private void initArguments() {
        Bundle arguments = getArguments();

        if (arguments != null) {
            mImovel = arguments.getParcelable(REQUEST_DETALHE);
            configDetalhe();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        String email = mFirebaseAppUtils.getUserAuth().getEmail();
        if (!TextUtils.isEmpty(email)) {
            if (email.equalsIgnoreCase("admin@zipimoveis.com")) {
                inflater.inflate(R.menu.menu_detalhes, menu);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_delete) {
            confirmRemocao();
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmRemocao() {
        mDialogUtil.confirm("Essa ação não podera ser desfeita", () -> removerImovel());
    }

    private void removerImovel() {
        mProgressBar.show();
        String idImovel = mImovel.getIdImovel();

        FirebaseDatabase firebaseDatabase = mFirebaseAppUtils.getFirebaseDatabase();

        firebaseDatabase.getReference("imoveis")
                .child(idImovel)
                .removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        removidoSucesso();
                    }
                });
    }

    private void removidoSucesso() {
        mProgressBar.dismiss();
        mDialogUtil.success("Sucesso!",
                "Imovel removido com sucesso!",
                () -> finishFragment());
    }

    private void finishFragment() {
        getFragmentManager().popBackStack();
    }

    private void initFields() {

        mDialogUtil = new DialogUtils(getActivity());

        mProgressBar = mDialogUtil.getProgress();
        mProgressBar.show();

        mPager = (ViewPager) mView.findViewById(R.id.pager);

        mTxtPreco = (TextView) mView.findViewById(R.id.txt_preco_detalhe);
        mLblPreco = (TextView) mView.findViewById(R.id.lbl_valor_venda);
        mTxtEndereco = (TextView) mView.findViewById(R.id.txt_endereco_detalhe);
        mTxtSubTipo = (TextView) mView.findViewById(R.id.txt_sub_tipo_detalhe);

        mTxtDorms = (TextView) mView.findViewById(R.id.txt_dorms_detalhe);
        mTxtSuites = (TextView) mView.findViewById(R.id.txt_suites_detalhe);
        mTxtVagas = (TextView) mView.findViewById(R.id.txt_vagas_detalhe);
        mTxtAreaUtil = (TextView) mView.findViewById(R.id.txt_area_detalhe);

        mLblValorAluguel = (TextView) mView.findViewById(R.id.lbl_valor_aluguel);
        mTxtValorAluguel = (TextView) mView.findViewById(R.id.txt_valor_aluguel);

        mTxtDescricao = (TextView) mView.findViewById(R.id.txt_descricao_detalhe);
        mTxtCondominio = (TextView) mView.findViewById(R.id.txt_condominio_detalhe);
        mTxtCaracteristica = (TextView) mView.findViewById(R.id.txt_caracteristicas_detalhe);
        mTxtCaracteristicaComum = (TextView) mView.findViewById(R.id.txt_caracteristicas_comum_detalhe);
    }

    private void configDetalhe() {

        if (mImovel != null) {

            if (TextUtils.isEmpty(mImovel.getValorAluguel()) || mImovel.getValorAluguel().equals("R$ 0,00") ) {mTxtValorAluguel.setVisibility(View.GONE);
                mLblValorAluguel.setVisibility(View.GONE);
            } else {
                mTxtValorAluguel.setText( mImovel.getValorAluguel() );
            }

            // Imagens
            List<String> fotos = mImovel.getFotos();
            ImagensAdapter imagensAdapter = new ImagensAdapter(getContext(), fotos);
            mPager.setAdapter(imagensAdapter);

            // Preco
            if (TextUtils.isEmpty(mImovel.getValorVenda()) || mImovel.getValorVenda().equals("R$ 0,00") ) {
                mTxtPreco.setVisibility(View.GONE);
                mLblPreco.setVisibility(View.GONE);
            } else {
                mTxtPreco.setText(mImovel.getValorVenda());
            }

            //SubTipo
            mTxtSubTipo.setText(mImovel.getCategoria());

            // Endereco
            mTxtEndereco.setText(mImovel.getEndereco());

            // Items
            mTxtDorms.setText(String.valueOf(mImovel.getDormitorios()));
            mTxtSuites.setText(String.valueOf(mImovel.getSuites()));
            mTxtVagas.setText(String.valueOf(mImovel.getVaga()));

            String areaUtil = String.valueOf(mImovel.getArea());
            mTxtAreaUtil.setText(areaUtil.replace(".", ""));

            //Descricao
            mTxtDescricao.setText(mImovel.getDescricao());

            //Condominio
            mTxtCondominio.setText(mImovel.getPrecoCondominio());

            // Caracteristicas
            if (TextUtils.isEmpty(mImovel.getCaracteristicasImovel())) {
                mTxtCaracteristica.setVisibility(View.GONE);
            } else {
                String caracteristicas = getString(R.string.txt_caracteristicas, mImovel.getCaracteristicasImovel());
                mTxtCaracteristica.setText(caracteristicas);
            }

            if (TextUtils.isEmpty(mImovel.getCaracteristicasAreaComum())) {
                mTxtCaracteristicaComum.setVisibility(View.INVISIBLE);
            } else {
                String caracteristicasComum = getString(R.string.txt_caracteristicas_comum, mImovel.getCaracteristicasAreaComum());
                mTxtCaracteristicaComum.setText(caracteristicasComum);
            }

            mProgressBar.dismiss();
        } else {
            showErrorMessage();
        }
    }

    private void showErrorMessage() {
        mProgressBar.dismiss();
        mDialogUtil.error(getString(R.string.msg_error_generic));
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mDialog == null) {
            mDialog = new DialogContato().getDialog(getActivity());
        }
    }

}
