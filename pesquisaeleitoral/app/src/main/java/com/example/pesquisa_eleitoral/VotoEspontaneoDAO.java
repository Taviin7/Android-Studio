package com.example.pesquisa_eleitoral;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VotoEspontaneoDAO {

    @Insert
    void inserir(VotoEspontaneo voto);

    @Query("SELECT resposta, COUNT(*) as total FROM votos_espontaneos GROUP BY resposta ORDER BY total DESC")
    List<VotoEspontaneo.ContarVotoEspontaneo> contar();
}
