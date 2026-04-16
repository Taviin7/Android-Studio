package com.example.pesquisa_eleitoral.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "problemas_relatados")
public class ProblemaRelatado {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "prb_id")
    private int id;

    @ColumnInfo(name = "prb_descricao")
    private String descricao;

    public ProblemaRelatado() {
    }

    @Ignore
    public ProblemaRelatado(String descricao) {
        this.descricao = descricao;
    }

    public static class ContarProblemas {
        @ColumnInfo(name = "prb_descricao")
        public String descricao;
        public int total;

        public ContarProblemas() {
        }
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}