package com.example.pesquisa_eleitoral;

public class Candidato {

    private int id;
    private String nome;
    private String partido;

    public Candidato(int id, String nome, String partido) {
        this.id = id;
        this.nome = nome;
        this.partido = partido;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getPartido() { return partido; }
}