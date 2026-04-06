package com.example.pesquisa_eleitoral;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Ponto de entrada do banco de dados.
 * Sempre acesse via AppDatabase.getInstance(context).entrevistaDao()
 *
 * version: incremente sempre que mudar a estrutura da tabela (adicionar campo, etc.)
 */
@Database(entities = {Entrevista.class, Voto.class}, version = 2, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    // Retorna o DAO para fazer operações
    public abstract EntrevistaDAO entrevistaDao();
    public abstract VotoDAO votoDAO();

    // Singleton — garante um único banco aberto no app
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "pesquisa_eleitoral.db"       // nome do arquivo .db gerado
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()     // recria o banco se mudar a versão, evitando crash
                    .build();
        }
        return instance;
    }
}