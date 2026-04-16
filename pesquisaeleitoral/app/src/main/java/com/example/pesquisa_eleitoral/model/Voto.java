package com.example.pesquisa_eleitoral.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "votos")
public class Voto {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "voto_id")
    private int id;

    @ColumnInfo(name = "voto_candidato_id")
    private int candidatoId;

    public Voto() {
    }

    @Ignore
    public Voto(int candidatoId) {
        this.candidatoId = candidatoId;
    }

    public static class ContarVotos {
        @ColumnInfo(name = "voto_candidato_id")
        public int candidatoId;
        public int total;

        public ContarVotos() {
        }
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCandidatoId() {
        return candidatoId;
    }

    public void setCandidatoId(int candidatoId) {
        this.candidatoId = candidatoId;
    }
}