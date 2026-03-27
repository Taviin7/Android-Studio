package com.example.pesquisa_eleitoral;

public class Candidato {
    private String nome, partido;

    public Candidato(String nome, String partido) {
        this.nome = nome;
        this.partido = partido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPartido() {
        return partido;
    }

    public void setPartido(String partido) {
        this.partido = partido;
    }
}
