package com.example.pesquisa_eleitoral.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "candidatos")
public class Candidato {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "can_id")
    private int id;

    @ColumnInfo(name = "can_nome")
    private String nome;

    @ColumnInfo(name = "can_partido")
    private String partido;

    @ColumnInfo(name = "can_foto")
    private String foto; // Nome do arquivo em res/drawable (ex: "mickey")

    public Candidato() {}

    @Ignore
    public Candidato(String nome, String partido, String foto) {
        this.nome = nome;
        this.partido = partido;
        this.foto = foto;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getPartido() { return partido; }
    public void setPartido(String partido) { this.partido = partido; }
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
}