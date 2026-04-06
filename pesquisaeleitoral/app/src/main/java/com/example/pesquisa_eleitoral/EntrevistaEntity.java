package com.example.pesquisa_eleitoral;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EntrevistaEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nome;
    public String celular;
    public long timestamp;
    public double latitude;
    public double longitude;
}