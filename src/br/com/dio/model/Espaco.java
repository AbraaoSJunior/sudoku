package br.com.dio.model;

public class Espaco {

    private Integer atual;
    private final int numEsperado;
    private final boolean fixo;


    public Espaco(final int numEsperado, final boolean fixo) {
        this.numEsperado = numEsperado;
        this.fixo = fixo;
        if (fixo){
            atual = numEsperado;
        }
    }

    public Integer getAtual() {
        return atual;
    }

    public void setAtual(final Integer atual) {
        if (fixo) return;
        this.atual = atual;
    }

    public void limparEspaco(){
        setAtual(null);
    }

    public int getNumEsperado() {
        return numEsperado;
    }

    public boolean isFixo() {
        return fixo;
    }
}
