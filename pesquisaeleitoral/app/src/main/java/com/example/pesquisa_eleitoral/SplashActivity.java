package com.example.pesquisa_eleitoral;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pesquisa_eleitoral.database.AppDatabase;
import com.example.pesquisa_eleitoral.model.Candidato;
import com.example.pesquisa_eleitoral.model.Usuario;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Inicialização do Banco em background na Splash
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            
            // Cria usuário admin padrão se não existir
            if (db.usuarioDao().buscarTotalUsuarios() == 0) {
                Usuario admin = new Usuario();
                admin.setNome("admin");
                admin.setSenha("admin123");
                admin.setTipo("ADMIN");
                db.usuarioDao().criar(admin);

                Usuario pesquisador = new Usuario();
                pesquisador.setNome("user");
                pesquisador.setSenha("123");
                pesquisador.setTipo("USER");
                db.usuarioDao().criar(pesquisador);
            }

            // Cadastra candidatos padrão se o banco for novo (version reset)
            if (db.candidatoDao().buscarTodos().isEmpty()) {
                db.candidatoDao().inserir(new Candidato("Mickey Mouse", "Partido Disney"));
                db.candidatoDao().inserir(new Candidato("Mario ", "Partido Nintendo"));
                db.candidatoDao().inserir(new Candidato("Lara Croft", "Partido Tomb Raider"));
                db.candidatoDao().inserir(new Candidato("Shrek", "Partido DreamWorks"));
                db.candidatoDao().inserir(new Candidato("Batman", "Partido DC"));
            }
        }).start();

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }, 3000);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}