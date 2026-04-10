package com.example.pesquisa_eleitoral;

import java.util.ArrayList;
import java.util.List;

/**
 * Armazena todas as entrevistas salvas em memória enquanto o app está aberto.
 * Substituir futuramente por Room Database sem mudar nada nas Activities.
 */
public class ListaEntrevistas {

    private static ListaEntrevistas instance;
    private final List<Entrevista> lista = new ArrayList<>();

    private ListaEntrevistas() {
    }

    public static ListaEntrevistas getInstance() {
        if (instance == null) instance = new ListaEntrevistas();
        return instance;
    }

    public void add(Entrevista e) {
        lista.add(e);
    }

    public List<Entrevista> getAll() {
        return lista;
    }

    public int total() {
        return lista.size();
    }
}