package com.example.pesquisa_eleitoral;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//Classe de Entidade (Room) para salvar voto, sem dados pessoais
@Entity(tableName = "votos")
public class Voto {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "vto_candidato")
    private int candidatoId;

    // Construtor vazio (Room)
    public Voto() {
    }

    // Construtor prático
    @Ignore
    public Voto(int candidatoId) {
        this.candidatoId = candidatoId;
    }

    public static class ContarVotos {
        @ColumnInfo(name = "vto_candidato")
        public int candidatoId;
        public int total;

        // O Room precisa de um construtor vazio ou que os campos sejam públicos
        public ContarVotos() {
        }
    }


    // Getters
    public int getId() {
        return id;
    }

    public int getCandidatoId() {
        return candidatoId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCandidatoId(int candidatoId) {
        this.candidatoId = candidatoId;
    }
}