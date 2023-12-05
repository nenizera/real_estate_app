package luishenrique.zipimoveis.rest.models;

import java.util.List;

public class Cidades {

    private List<String> bairros;
    private String cidade;

    public List<String> getBairros() {
        return bairros;
    }

    public void setBairros(List<String> bairros) {
        this.bairros = bairros;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Override
    public String toString() {
        return "Cidades{" +
                ", bairros=" + bairros +
                ", cidade='" + cidade + '\'' +
                '}';
    }
}
