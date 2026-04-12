package com.example.pesquisa_eleitoral.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.pesquisa_eleitoral.database.Converters;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "entrevistas")
@TypeConverters(Converters.class)
public class Entrevista implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ent_id")
    private int id;

    @Ignore
    private String votoEspontaneo;

    @ColumnInfo(name = "ent_problemas")
    private List<String> problemas;

    @ColumnInfo(name = "ent_nome")
    private String nome;

    @ColumnInfo(name = "ent_celular")
    private String celular;

    @ColumnInfo(name = "ent_timestamp")
    private long timestamp;

    @ColumnInfo(name = "ent_latitude")
    private double latitude;

    @ColumnInfo(name = "ent_longitude")
    private double longitude;

    public Entrevista() {}

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getVotoEspontaneo() { return votoEspontaneo; }
    public void setVotoEspontaneo(String votoEspontaneo) { this.votoEspontaneo = votoEspontaneo; }

    public List<String> getProblemas() { return problemas; }
    public void setProblemas(List<String> problemas) { this.problemas = problemas; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}