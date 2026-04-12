package com.example.pesquisa_eleitoral.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pesquisa_eleitoral.model.Voto;

import java.util.List;

@Dao
public interface VotoDAO {

    @Insert
    void inserir(Voto voto);

    @Query("SELECT voto_candidato_id, COUNT(*) as total FROM votos GROUP BY voto_candidato_id")
    List<Voto.ContarVotos> contarVotos();

    @Query("SELECT * FROM votos")
    List<Voto> getAll();
}