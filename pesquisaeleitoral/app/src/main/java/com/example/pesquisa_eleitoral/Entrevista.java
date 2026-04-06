package com.example.pesquisa_eleitoral;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "entrevistas")
@TypeConverters(Converters.class) //Para salvar List<String> já que o Room não sabe como salvar
public class Entrevista implements Serializable { //Serializable para passar entre telas

    @PrimaryKey(autoGenerate = true)
    private int id;

    private List<String> problemas;

    private String nome;
    private String celular;
    private long timestamp;
    private double latitude;
    private double longitude;

    //Construtor vazio obrigatório para Room
    public Entrevista() {}

    //Construtor usado no app (fluxo das telas)
    public Entrevista(List<String> problemas,
                      String nome,
                      String celular,
                      long timestamp,
                      double latitude,
                      double longitude) {

        this.problemas = problemas;
        this.nome = nome;
        this.celular = celular;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public int getId() { return id; }
    public List<String> getProblemas() { return problemas; }
    public String getNome() { return nome; }
    public String getCelular() { return celular; }
    public long getTimestamp() { return timestamp; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setProblemas(List<String> problemas) { this.problemas = problemas; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCelular(String celular) { this.celular = celular; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}