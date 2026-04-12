package com.example.pesquisa_eleitoral.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pesquisa_eleitoral.model.Usuario;

@Dao
public interface UsuarioDAO {

    @Query("SELECT * FROM usuarios WHERE usu_nome = :nome AND usu_senha = :senha")
    Usuario fazerLogin(String nome, String senha);

    @Insert
    void criar(Usuario usuario);

    @Query("SELECT COUNT(*) FROM usuarios")
    int buscarTotalUsuarios();
}