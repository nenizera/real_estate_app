package luishenrique.zipimoveis.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import luishenrique.zipimoveis.R;
import luishenrique.zipimoveis.activities.FiltroImovelActivity;
import luishenrique.zipimoveis.adapters.ImovelAdapter;
import luishenrique.zipimoveis.rest.ApiUtils;
import luishenrique.zipimoveis.rest.models.Imovel;
import luishenrique.zipimoveis.rest.models.ImovelBase;
import luishenrique.zipimoveis.rest.models.ListaImovel;
import luishenrique.zipimoveis.rest.responses.ImoveisResponse;
import luishenrique.zipimoveis.rest.services.ContatoService;
import luishenrique.zipimoveis.rest.services.ImoveisService;
import luishenrique.zipimoveis.utils.Constants;
import luishenrique.zipimoveis.utils.DialogUtils;
import luishenrique.zipimoveis.utils.FragmentUtils;
import luishenrique.zipimoveis.utils.PrefsUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ImoveisFragment extends Fragment {

    private View mView;
    private RecyclerView mRecycleView;
    private ImovelAdapter mImovelAdapter;
    private List<ImovelBase> mImoveis;
    private DialogUtils mDialogUtil;
    private MaterialDialog mProgressBar;

    public ImoveisFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogUtil = new DialogUtils(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_imoveis, container, false);
        }

        getActivity().setTitle("Lista Imoveis");
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);
        initFields();
        initRecycle();

        String cidade = Prefs.getString("cidade", "");
        if (TextUtils.isEmpty(cidade)) {
            startFiltro();
        } else {
            loadImoveis();
        }
    }

    private void startFiltro() {
        Intent intent = new Intent(getActivity(), FiltroImovelActivity.class);
        startActivityForResult(intent, 999);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_filtro_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_filtro_list) {
            startFiltro();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFields() {
        mRecycleView = (RecyclerView) mView.findViewById(R.id.recycle_imoveis);
        mProgressBar = mDialogUtil.getProgress();
    }

    private void initRecycle() {
        mRecycleView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(linearLayoutManager);

        mImovelAdapter = new ImovelAdapter(getActivity(), onClickFavorito());
        mRecycleView.setAdapter(mImovelAdapter);
    }

    private void loadImoveis() {

        mProgressBar.show();

        ApiUtils apiUtils = new ApiUtils();
        mImoveis = new ArrayList<>();

        apiUtils.getImoveisService()
                .orderByChild("bairro")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, ImovelBase> value = dataSnapshot.getValue(apiUtils.formatListImoveis);

                    String cidade = Prefs.getString("cidade", "");
                    String bairro = Prefs.getString("bairro", "");
                    String tipoImovel = Prefs.getString("tipoImovel", "");

                    for (Map.Entry<String, ImovelBase> imovelEntry : value.entrySet()) {

                        String key = imovelEntry.getKey();
                        ImovelBase imovel = imovelEntry.getValue();
                        imovel.setIdImovel(key);

                        if (imovel.getCidade().equalsIgnoreCase(cidade)) {

                            if (TextUtils.isEmpty(bairro) && TextUtils.isEmpty(tipoImovel)) {
                                mImoveis.add(imovel);
                            } else {

                                if (!TextUtils.isEmpty(bairro)) {

                                    if (imovel.getBairro().equalsIgnoreCase(bairro)) {

                                        if (!TextUtils.isEmpty(tipoImovel)) {

                                            if (imovel.getTipoImovel().equalsIgnoreCase(tipoImovel)) {
                                                mImoveis.add(imovel);
                                            }

                                        } else {
                                            mImoveis.add(imovel);
                                        }

                                    }

                                } else {
                                    if (!TextUtils.isEmpty(tipoImovel)) {

                                        if (imovel.getTipoImovel().equalsIgnoreCase(tipoImovel)) {
                                            mImoveis.add(imovel);
                                        }

                                    }
                                }
                            }
                        }
                    }

                    mProgressBar.dismiss();
                    mImovelAdapter.update(mImoveis);
                } else {
                    mProgressBar.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // Generic functions
    @NonNull
    private ImovelAdapter.OnImovelAdapterListener onClickFavorito() {
        return new ImovelAdapter.OnImovelAdapterListener() {
            @Override
            public void onClickItem(ImovelBase listaImovel) {
                detalheImovelFragment(listaImovel);
            }
        };
    }

    // Fragment
    private void detalheImovelFragment(ImovelBase listaImovel) {
        DetalheImovelFragment fragment = new DetalheImovelFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(DetalheImovelFragment.REQUEST_DETALHE, listaImovel);

        fragment.setArguments(bundle);
        FragmentUtils.loadFragment(getFragmentManager(), fragment, R.id.content_frame, Constants.DETALHE_IMOVEL_FRAGMENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 999) {

                Bundle extras = data.getExtras();
                String cidade = extras.getString("cidade");
                String bairro = extras.getString("bairro");
                String tipoImovel = extras.getString("tipoImovel");


                Prefs.putString("cidade", cidade);
                Prefs.putString("bairro", bairro);
                Prefs.putString("tipoImovel", tipoImovel);

                loadImoveis();

            }
        }
    }
}
