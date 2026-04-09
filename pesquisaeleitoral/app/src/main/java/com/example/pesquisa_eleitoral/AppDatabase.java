package com.example.pesquisa_eleitoral;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Entrevista.class, Voto.class, VotoEspontaneo.class}, version = 4, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract EntrevistaDAO entrevistaDao();
    public abstract VotoDAO votoDAO();
    public abstract VotoEspontaneoDAO votoEspontaneoDAO();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "pesquisa_eleitoral.db"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}