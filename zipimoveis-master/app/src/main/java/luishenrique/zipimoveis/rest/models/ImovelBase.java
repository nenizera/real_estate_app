package luishenrique.zipimoveis.rest.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class ImovelBase implements Parcelable {

    public ImovelBase() {
    }

    private String IdImovel;
    private String anoConstrucao;
    private String caracteristicasAreaComum;
    private String caracteristicasImovel;
    private String categoria;
    private String bairro;
    private String precoCondominio;
    private String cep;
    private String cidade;
    private String descricao;
    private String estado;
    private String rua;
    private String valorVenda;
    private List<String> fotos;
    private String area;
    private int dormitorios;
    private int suites;
    private int vaga;
    private boolean ativado;
    private String tipoImovel;
    private String valorAluguel;

    public String getValorAluguel() {
        if (TextUtils.isEmpty(valorAluguel)) {
            return "R$ 0,00";
        }
        return valorAluguel;
    }

    public void setValorAluguel(String valorAluguel) {
        this.valorAluguel = valorAluguel;
    }

    public void setIdImovel(String idImovel) {
        IdImovel = idImovel;
    }

    public String getIdImovel() {
        return IdImovel;
    }


    public String getEndereco() {
        return String.format("%s \nCidade: %s - Bairro : %s", rua, cidade, bairro);
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        if (!TextUtils.isEmpty(bairro)) {
            this.bairro = bairro.toLowerCase().trim();
        }
    }

    public String getAnoConstrucao() {
        return anoConstrucao;
    }

    public void setAnoConstrucao(String anoConstrucao) {
        this.anoConstrucao = anoConstrucao;
    }

    public String getCaracteristicasAreaComum() {
        return caracteristicasAreaComum;
    }

    public void setCaracteristicasAreaComum(String caracteristicasAreaComum) {
        this.caracteristicasAreaComum = caracteristicasAreaComum;
    }

    public String getCaracteristicasImovel() {
        return caracteristicasImovel;
    }

    public void setCaracteristicasImovel(String caracteristicasImovel) {
        this.caracteristicasImovel = caracteristicasImovel;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        if (!TextUtils.isEmpty(cidade)) {
            this.cidade = cidade.toLowerCase().trim();
        }
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public String getArea() {
        if (area == null) {
            return "";
        }
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getDormitorios() {
        return dormitorios;
    }

    public void setDormitorios(int dormitorios) {
        this.dormitorios = dormitorios;
    }

    public int getSuites() {
        return suites;
    }

    public void setSuites(int suites) {
        this.suites = suites;
    }

    public int getVaga() {
        return vaga;
    }

    public void setVaga(int vaga) {
        this.vaga = vaga;
    }

    public boolean isAtivado() {
        return ativado;
    }

    public void setAtivado(boolean ativado) {
        this.ativado = ativado;
    }

    public String getPrecoCondominio() {
        if (TextUtils.isEmpty(precoCondominio)) {
            return "R$ 0,00";
        }
        return precoCondominio;
    }

    public void setPrecoCondominio(String precoCondominio) {
        this.precoCondominio = precoCondominio;
    }

    public String getValorVenda() {
        if (TextUtils.isEmpty(valorVenda)) {
            return "R$ 0,00";
        }
        return valorVenda;
    }

    public void setValorVenda(String valorVenda) {
        this.valorVenda = valorVenda;
    }

    public String getTipoImovel() {
        return tipoImovel;
    }

    public void setTipoImovel(String tipoImovel) {
        this.tipoImovel = tipoImovel;
    }

    @Override
    public String toString() {
        return "ImovelBase{" +
                "anoConstrucao=" + anoConstrucao +
                ", caracteristicasAreaComum='" + caracteristicasAreaComum + '\'' +
                ", caracteristicasImovel='" + caracteristicasImovel + '\'' +
                ", categoria='" + categoria + '\'' +
                ", bairro='" + bairro + '\'' +
                ", precoCondominio=" + precoCondominio +
                ", cep='" + cep + '\'' +
                ", cidade='" + cidade + '\'' +
                ", descricao='" + descricao + '\'' +
                ", estado='" + estado + '\'' +
                ", rua='" + rua + '\'' +
                ", valorVenda=" + valorVenda +
                ", fotos=" + fotos +
                ", area=" + area +
                ", dormitorios=" + dormitorios +
                ", suites=" + suites +
                ", vaga=" + vaga +
                ", ativado=" + ativado +
                ", tipoImovel='" + tipoImovel + '\'' +
                '}';
    }

    protected ImovelBase(Parcel in) {
        IdImovel = in.readString();
        anoConstrucao = in.readString();
        caracteristicasAreaComum = in.readString();
        caracteristicasImovel = in.readString();
        categoria = in.readString();
        bairro = in.readString();
        precoCondominio = in.readString();
        cep = in.readString();
        cidade = in.readString();
        descricao = in.readString();
        estado = in.readString();
        rua = in.readString();
        valorVenda = in.readString();
        if (in.readByte() == 0x01) {
            fotos = new ArrayList<>();
            in.readList(fotos, String.class.getClassLoader());
        } else {
            fotos = null;
        }
        area = in.readString();
        dormitorios = in.readInt();
        suites = in.readInt();
        vaga = in.readInt();
        ativado = in.readByte() != 0x00;
        tipoImovel = in.readString();
        valorAluguel = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(IdImovel);
        dest.writeString(anoConstrucao);
        dest.writeString(caracteristicasAreaComum);
        dest.writeString(caracteristicasImovel);
        dest.writeString(categoria);
        dest.writeString(bairro);
        dest.writeString(precoCondominio);
        dest.writeString(cep);
        dest.writeString(cidade);
        dest.writeString(descricao);
        dest.writeString(estado);
        dest.writeString(rua);
        dest.writeString(valorVenda);
        if (fotos == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(fotos);
        }
        dest.writeString(area);
        dest.writeInt(dormitorios);
        dest.writeInt(suites);
        dest.writeInt(vaga);
        dest.writeByte((byte) (ativado ? 0x01 : 0x00));
        dest.writeString(tipoImovel);
        dest.writeString(valorAluguel);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ImovelBase> CREATOR = new Parcelable.Creator<ImovelBase>() {
        @Override
        public ImovelBase createFromParcel(Parcel in) {
            return new ImovelBase(in);
        }

        @Override
        public ImovelBase[] newArray(int size) {
            return new ImovelBase[size];
        }
    };

}
