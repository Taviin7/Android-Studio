package com.example.pesquisa_eleitoral;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EntrevistaDAO {

    @Insert
    void inserir(Entrevista entrevista);

    // Retorna todas as entrevistas, da mais recente para a mais antiga
    @Query("SELECT * FROM entrevistas ORDER BY ent_timestamp DESC")
    List<Entrevista> buscarTodas();

    // Retorna o total de entrevistas
    @Query("SELECT COUNT(*) FROM entrevistas")
    int contarTotal();
}