package com.example.pesquisa_eleitoral;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "votos_espontaneos")
public class VotoEspontaneo {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String resposta; // Texto digitado pelo entrevistado

    public VotoEspontaneo() {}

    @Ignore
    public VotoEspontaneo(String resposta) {
        this.resposta = resposta;
    }

    // Deve ser static para o Room conseguir instanciar
    public static class ContarVotoEspontaneo {
        public String resposta;
        public int total;

        public ContarVotoEspontaneo() {}
    }

    public int getId() { return id; }
    public String getResposta() { return resposta; }

    public void setId(int id) { this.id = id; }
    public void setResposta(String resposta) { this.resposta = resposta; }
}