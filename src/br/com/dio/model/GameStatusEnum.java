package br.com.dio.model;

public enum GameStatusEnum {

    NAO_INICIADO("não iniciado"),
    INCOMPLETO("incompleto"),
    COMPLETO("completo");

    private String label;

    GameStatusEnum(final String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
