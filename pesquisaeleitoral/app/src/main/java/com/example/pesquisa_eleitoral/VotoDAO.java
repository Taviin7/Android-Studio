package com.example.pesquisa_eleitoral;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VotoDAO {

    @Insert
    void inserir(Voto voto);

    // Contagem de votos por candidato
    @Query("SELECT candidatoId, COUNT(*) as total FROM votos GROUP BY candidatoId")
    List<Voto.ContarVotos> contarVotos();

    // Lista todos os votos
    @Query("SELECT * FROM votos")
    List<Voto> getAll();
}

