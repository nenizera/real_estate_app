package luishenrique.zipimoveis.fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import luishenrique.zipimoveis.R;
import luishenrique.zipimoveis.activities.MainActivity;
import luishenrique.zipimoveis.adapters.ImovelAdapter;
import luishenrique.zipimoveis.rest.models.ListaImovel;
import luishenrique.zipimoveis.utils.Constants;
import luishenrique.zipimoveis.utils.FragmentUtils;

public class FavoritoFragment extends Fragment {

    private View              mView;
    private RecyclerView      mRecycleView;
    private ImovelAdapter     mImovelAdapter;
    private TextView          mTxtEmptyMessage;
    private List<ListaImovel> mFavorites;

    public FavoritoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);
        mView = inflater.inflate(R.layout.fragment_favorito, container, false);
        initFragment();
        return mView;
    }

    private void initFragment() {
        ActionBar supportActionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(R.string.title_favoritos);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mFavorites = savedInstanceState.getParcelableArrayList(Constants.LIST_IMOVEIS_FAVORITOS);
        }

        initFields();
        initRecycle();

    }

    private void initFields() {
        mTxtEmptyMessage = (TextView) mView.findViewById(R.id.txt_empty_favorite);
        mRecycleView     = (RecyclerView) mView.findViewById(R.id.recycle_favoritos);
    }

    private void initRecycle() {
        mRecycleView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(linearLayoutManager);

//        mImovelAdapter = new ImovelAdapter(getActivity(), onClickFavorito());
//        mRecycleView.setAdapter(mImovelAdapter);
//
//        mFavorites = PrefsUtils.getImoveis(Constants.LIST_IMOVEIS_FAVORITOS);

//        if (mFavorites != null && mFavorites.size() > 0) {
//            showRecycle();
//            mImovelAdapter.update(mFavorites);
//        } else {
//            showEmptyMessage();
//        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.LIST_IMOVEIS_FAVORITOS, (ArrayList<? extends Parcelable>) mFavorites);
    }

//    @NonNull
//    private ImovelAdapter.OnImovelAdapterListener onClickFavorito() {
//        return new ImovelAdapter.OnImovelAdapterListener() {
//            @Override
//            public void onClickFavorito(ListaImovel listaImovel, int position) {
//                mFavorites.remove(position);
//                PrefsUtils.removeFavoritos(mFavorites, listaImovel);
//                mImovelAdapter.update(mFavorites);
//
//                if (mFavorites.size() == 0) {
//                    showEmptyMessage();
//                }
//
//                successRemoved(getView());
//            }
//
//            @Override
//            public void onClickItem(ListaImovel listaImovel) {
//                detalheImovelFragment(listaImovel);
//            }
//        };
//    }

    // Fragment
    private void detalheImovelFragment(ListaImovel listaImovel) {
        DetalheImovelFragment fragment = new DetalheImovelFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(DetalheImovelFragment.REQUEST_DETALHE, listaImovel);

        fragment.setArguments(bundle);
        FragmentUtils.loadFragment(getFragmentManager(), fragment, R.id.content_frame, Constants.DETALHE_IMOVEL_FRAGMENT);
    }

    private void showEmptyMessage() {
        mRecycleView.setVisibility(View.GONE);
        mTxtEmptyMessage.setVisibility(View.VISIBLE);
    }

    private void showRecycle() {
        mTxtEmptyMessage.setVisibility(View.GONE);
        mRecycleView.setVisibility(View.VISIBLE);
    }

    private void successRemoved(View view) {
        Snackbar.make(view, R.string.msg_favorite_success_removed, Snackbar.LENGTH_LONG).show();
    }

}
