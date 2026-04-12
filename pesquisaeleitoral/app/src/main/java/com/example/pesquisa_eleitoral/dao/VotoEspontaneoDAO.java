package com.example.pesquisa_eleitoral.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pesquisa_eleitoral.model.VotoEspontaneo;

import java.util.List;

@Dao
public interface VotoEspontaneoDAO {

    @Insert
    void inserir(VotoEspontaneo voto);

    @Query("SELECT vte_resposta, COUNT(*) as total FROM votos_espontaneos GROUP BY vte_resposta ORDER BY total DESC")
    List<VotoEspontaneo.ContarVotoEspontaneo> contar();
}