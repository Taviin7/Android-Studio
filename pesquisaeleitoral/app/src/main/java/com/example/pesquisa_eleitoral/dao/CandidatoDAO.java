package com.example.pesquisa_eleitoral.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pesquisa_eleitoral.model.Candidato;

import java.util.List;

@Dao
public interface CandidatoDAO {

    @Insert
    void inserir(Candidato candidato);

    @Query("SELECT * FROM candidatos")
    List<Candidato> buscarTodos();

    @Query("SELECT * FROM candidatos WHERE can_id = :id LIMIT 1")
    Candidato buscarPorId(int id);
}