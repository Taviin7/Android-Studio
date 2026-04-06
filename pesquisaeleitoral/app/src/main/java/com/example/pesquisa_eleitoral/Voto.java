package com.example.pesquisa_eleitoral;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
//Classe de Entidade (Room) para salvar voto, sem dados pessoais
@Entity(tableName = "votos")
public class Voto {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int candidatoId;

    // Construtor vazio (Room)
    public Voto() {}

    // Construtor prático
    public Voto(int candidatoId) {
        this.candidatoId = candidatoId;
    }

    // Dentro de Voto.java
    public static class ContarVotos {
        public int candidatoId;
        public int total;

        // O Room precisa de um construtor vazio ou que os campos sejam públicos
        public ContarVotos() {}
    }


    // Getters
    public int getId() { return id; }
    public int getCandidatoId() { return candidatoId; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setCandidatoId(int candidatoId) { this.candidatoId = candidatoId; }
}