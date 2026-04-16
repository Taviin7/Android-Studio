package com.example.pesquisa_eleitoral.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "votos_espontaneos")
public class VotoEspontaneo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "vte_id")
    private int id;

    @ColumnInfo(name = "vte_resposta")
    private String resposta;

    public VotoEspontaneo() {
    }

    @Ignore
    public VotoEspontaneo(String resposta) {
        this.resposta = resposta;
    }

    public static class ContarVotoEspontaneo {
        @ColumnInfo(name = "vte_resposta")
        public String resposta;
        public int total;

        public ContarVotoEspontaneo() {
        }
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
}