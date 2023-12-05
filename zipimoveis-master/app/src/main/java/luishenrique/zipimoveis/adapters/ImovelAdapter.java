package luishenrique.zipimoveis.adapters;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import luishenrique.zipimoveis.R;
import luishenrique.zipimoveis.rest.models.ImovelBase;
import luishenrique.zipimoveis.utils.FirebaseAppUtils;
import luishenrique.zipimoveis.utils.MaskUtils;
import luishenrique.zipimoveis.utils.NumberUtils;
import timber.log.Timber;

public class ImovelAdapter extends RecyclerView.Adapter<ImovelAdapter.ViewHolder> {

    private final OnImovelAdapterListener mListener;
    private final Activity                mContext;
    private final FirebaseAppUtils mFirebaseAppUtils;
    private List<ImovelBase>             mImoveis;

    public interface OnImovelAdapterListener {
        void onClickItem(ImovelBase listaImovel);
    }

    public ImovelAdapter(Activity context, OnImovelAdapterListener listener) {
        mImoveis  = new ArrayList<>();
        mContext  = context;
        mListener = listener;
        mFirebaseAppUtils = new FirebaseAppUtils();

    }

    public void update(List<ImovelBase> imoveis) {
        this.mImoveis = imoveis;
        notifyDataSetChanged();
    }

    public List<ImovelBase> getImoveis() {
        return mImoveis;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_imovel, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ImovelBase listaImovel = mImoveis.get(position);

        // Preco
        String valorVenda = listaImovel.getValorVenda();
        String valorAluguel = listaImovel.getValorAluguel();

        String valores = "";

        if (!TextUtils.isEmpty(valorVenda) && !valorVenda.equals("R$ 0,00")) {
            valores += "Venda " + valorVenda + " ";
        }

        if (!TextUtils.isEmpty(valorAluguel) && !valorAluguel.equals("R$ 0,00")) {
            valores += " Aluguel " + valorAluguel;
        }

        holder.mTxtPreco.setText(valores);

        // Foto Imovel
        List<String> fotos = listaImovel.getFotos();
        if (fotos != null) {
            if (fotos.size() > 0) {

                mFirebaseAppUtils.getFirebaseStorage().getReference().child( fotos.get(0) )
                        .getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(mContext)
                                        .load(uri)
                                        .into(holder.mImgImovel);
                            }
                        });
            }
        }

        // Sub Tipo
        holder.mTxtTipo.setText(listaImovel.getCategoria());

        // Endereco
        String endereco;
        String bairro           = listaImovel.getBairro();

        if (listaImovel.getCep() == null) {
            String cep = MaskUtils.formatCEP(listaImovel.getCep());
            endereco = mContext.getString(R.string.txt_endereco_cep, cep, bairro);
        } else {
            endereco = mContext.getString(R.string.txt_endereco, listaImovel.getRua(), bairro);
        }

        holder.mTxtEndereco.setText(endereco);

        // Caracteristicas
        String caracteristicas = getDescricao(listaImovel);
        if (!caracteristicas.isEmpty()) {
            holder.mTxtDescricao.setText(caracteristicas);
        }
    }


    private String getDescricao(ImovelBase listaImovel) {
        StringBuilder descricaoBuilder = new StringBuilder();

        Integer qtdDormitorios = listaImovel.getDormitorios();
        Integer qtdSuites      = listaImovel.getSuites();
        Integer qtdVagas       = listaImovel.getVaga();
        String areaUtil        = listaImovel.getArea();

        if (qtdDormitorios > 0) {
            String domitorios = mContext.getResources().getQuantityString(R.plurals.txt_dormitorios, qtdDormitorios, qtdDormitorios);
            descricaoBuilder.append(domitorios);
        }

        if (qtdSuites > 0) {
            String suites = mContext.getResources().getQuantityString(R.plurals.txt_suites, qtdSuites, qtdSuites);
            descricaoBuilder.append(suites);
        }

        if (qtdVagas > 0) {
            String vagas = mContext.getResources().getQuantityString(R.plurals.txt_vagas, qtdVagas, qtdVagas);
            descricaoBuilder.append(vagas);
        }

        if (Integer.valueOf(areaUtil) > 0) {
            String txtAreaUtil = mContext.getString(R.string.txt_area_util, areaUtil);
            descricaoBuilder.append(txtAreaUtil);
        }

        return descricaoBuilder.toString();
    }

    @Override
    public int getItemCount() {
        return mImoveis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView  mTxtDescricao;
        public final TextView  mTxtTipo;
        public final TextView  mTxtEndereco;
        public final TextView  mTxtPreco;
        public final ImageView mImgImovel;
        public final ImageView mImgFavorito;

        public ViewHolder(View itemView) {
            super(itemView);

            mTxtPreco     = (TextView)  itemView.findViewById(R.id.txt_preco_venda);
            mImgImovel    = (ImageView) itemView.findViewById(R.id.img_imovel);
            mTxtTipo      = (TextView)  itemView.findViewById(R.id.txt_sub_tipo_imovel);
            mTxtEndereco  = (TextView)  itemView.findViewById(R.id.txt_endereco);
            mImgFavorito  = (ImageView) itemView.findViewById(R.id.img_favorito);
            mTxtDescricao = (TextView)  itemView.findViewById(R.id.txt_descricao);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            ImovelBase listaImovel = mImoveis.get(getAdapterPosition());
            mListener.onClickItem(listaImovel);
        }
    }
}
