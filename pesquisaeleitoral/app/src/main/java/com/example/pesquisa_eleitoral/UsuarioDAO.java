package com.example.pesquisa_eleitoral;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UsuarioDAO {

    @Query("SELECT * FROM Usuario WHERE usu_nome = :nome AND usu_senha = :senha")
    Usuario fazerLogin(String nome, String senha);

    @Insert
    void criar(Usuario usuario);

    @Query("SELECT COUNT(*) FROM Usuario")
    int buscarTotalUsuarios();

}