package com.example.pesquisa_eleitoral;

import java.util.List;

import java.io.Serializable;
import java.util.List;

public class Entrevista implements Serializable {

    private int candidatoId;
    private List<String> problemas;
    private String nome;
    private String celular;
    private long timestamp;
    private double latitude;
    private double longitude;

    public Entrevista(int candidatoId,
                      List<String> problemas,
                      String nome,
                      String celular,
                      long timestamp,
                      double latitude,
                      double longitude) {

        this.candidatoId = candidatoId;
        this.problemas = problemas;
        this.nome = nome;
        this.celular = celular;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public int getCandidatoId() { return candidatoId; }
    public List<String> getProblemas() { return problemas; }
    public String getNome() { return nome; }
    public String getCelular() { return celular; }
    public long getTimestamp() { return timestamp; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    // Setters
    public void setProblemas(List<String> problemas) { this.problemas = problemas; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCelular(String celular) { this.celular = celular; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}