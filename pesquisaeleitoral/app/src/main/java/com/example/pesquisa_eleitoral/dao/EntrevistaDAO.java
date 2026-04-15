package com.example.pesquisa_eleitoral.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pesquisa_eleitoral.model.Entrevista;

import java.util.List;

@Dao
public interface EntrevistaDAO {

    @Insert
    void inserir(Entrevista entrevista);

    @Query("SELECT * FROM entrevistas ORDER BY ent_id")
    List<Entrevista> buscarTodas();

    @Query("SELECT COUNT(*) FROM entrevistas")
    int contarTotal();
}