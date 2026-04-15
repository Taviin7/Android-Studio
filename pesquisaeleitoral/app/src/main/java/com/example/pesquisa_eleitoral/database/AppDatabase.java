package com.example.pesquisa_eleitoral.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.pesquisa_eleitoral.dao.CandidatoDAO;
import com.example.pesquisa_eleitoral.dao.EntrevistaDAO;
import com.example.pesquisa_eleitoral.dao.ProblemaDAO;
import com.example.pesquisa_eleitoral.dao.UsuarioDAO;
import com.example.pesquisa_eleitoral.dao.VotoDAO;
import com.example.pesquisa_eleitoral.dao.VotoEspontaneoDAO;
import com.example.pesquisa_eleitoral.model.Candidato;
import com.example.pesquisa_eleitoral.model.Entrevista;
import com.example.pesquisa_eleitoral.model.ProblemaRelatado;
import com.example.pesquisa_eleitoral.model.Usuario;
import com.example.pesquisa_eleitoral.model.Voto;
import com.example.pesquisa_eleitoral.model.VotoEspontaneo;

@Database(entities = {
        Entrevista.class,
        Voto.class,
        VotoEspontaneo.class,
        Candidato.class,
        ProblemaRelatado.class,
        Usuario.class
}, version = 8, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract EntrevistaDAO entrevistaDao();
    public abstract VotoDAO votoDAO();
    public abstract VotoEspontaneoDAO votoEspontaneoDAO();
    public abstract CandidatoDAO candidatoDao();
    public abstract ProblemaDAO problemaDao();
    public abstract UsuarioDAO usuarioDao();

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