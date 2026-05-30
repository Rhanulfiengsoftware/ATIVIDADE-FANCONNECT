package br.com.fanconnect.model;

public enum Categoria {
    PROVAS("Provas"),
    PALESTRAS("Palestras"),
    BUROCRACIA("Burocracia"),
    SOCIAIS("Sociais");

    private final String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}