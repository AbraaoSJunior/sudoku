package br.com.dio.model;

import java.util.Collection;
import java.util.List;

import static br.com.dio.model.GameStatusEnum.COMPLETO;
import static br.com.dio.model.GameStatusEnum.INCOMPLETO;
import static br.com.dio.model.GameStatusEnum.NAO_INICIADO;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private final List<List<Espaco>> espacos;

    public Board(final List<List<Espaco>> espacos) {
        this.espacos = espacos;
    }

    public List<List<Espaco>> getEspacos() {
        return espacos;
    }

    public GameStatusEnum getStatus(){
        if (espacos.stream().flatMap(Collection::stream).noneMatch(s -> !s.isFixo() && nonNull(s.getAtual()))){
            return NAO_INICIADO;
        }

        return espacos.stream().flatMap(Collection::stream).anyMatch(s -> isNull(s.getAtual())) ? INCOMPLETO : COMPLETO;
    }

    public boolean temErros(){
        if(getStatus() == NAO_INICIADO){
            return false;
        }

        return espacos.stream().flatMap(Collection::stream)
                .anyMatch(s -> nonNull(s.getAtual()) && !s.getAtual().equals(s.getNumEsperado()));
    }

    public boolean mudarValor(final int coluna, final int linha, final int valor){
        var espaco = espacos.get(coluna).get(linha);
        if (espaco.isFixo()){
            return false;
        }

        espaco.setAtual(valor);
        return true;
    }

    public boolean excluirValor(final int coluna, final int linha){
        var espaco = espacos.get(coluna).get(linha);
        if (espaco.isFixo()){
            return false;
        }

        espaco.limparEspaco();
        return true;
    }

    public void resetar(){
        espacos.forEach(c -> c.forEach(Espaco::limparEspaco));
    }

    public boolean jogoFinalizado(){
        return !temErros() && getStatus().equals(COMPLETO);
    }

}
