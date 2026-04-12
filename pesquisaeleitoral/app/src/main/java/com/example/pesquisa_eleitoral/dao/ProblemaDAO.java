package com.example.pesquisa_eleitoral.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pesquisa_eleitoral.model.ProblemaRelatado;

import java.util.List;

@Dao
public interface ProblemaDAO {

    @Insert
    void inserir(ProblemaRelatado problema);

    @Query("SELECT prb_descricao, COUNT(*) as total FROM problemas_relatados GROUP BY prb_descricao ORDER BY total DESC")
    List<ProblemaRelatado.ContarProblemas> contar();
}