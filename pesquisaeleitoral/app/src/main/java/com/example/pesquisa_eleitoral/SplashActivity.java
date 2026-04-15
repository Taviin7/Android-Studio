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

        // Inicialização do Banco em background: cria usuários e candidatos padrão se o banco for novo
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            
            // Cria usuário admin e pesquisador padrão se não houver usuários cadastrados
            if (db.usuarioDao().buscarTotalUsuarios() == 0) {
                Usuario admin = new Usuario();
                admin.setNome("Admin");
                admin.setSenha("admin");
                admin.setTipo("ADMIN");
                db.usuarioDao().criar(admin);

                Usuario pesquisador = new Usuario();
                pesquisador.setNome("Entrevistador");
                pesquisador.setSenha("entrevistador");
                pesquisador.setTipo("USER");
                db.usuarioDao().criar(pesquisador);
            }

            // Cadastro dos candidatos para a pesquisa estimulada caso a tabela esteja vazia
            if (db.candidatoDao().buscarTodos().isEmpty()) {
                db.candidatoDao().inserir(new Candidato("Mickey Mouse", "Partido Disney", "mickey"));
                db.candidatoDao().inserir(new Candidato("Mario", "Partido Nintendo", "mario"));
                db.candidatoDao().inserir(new Candidato("Lara Croft", "Partido Tomb Raider", "lara"));
                db.candidatoDao().inserir(new Candidato("Shrek", "Partido DreamWorks", "shrek"));
                db.candidatoDao().inserir(new Candidato("Batman", "Partido DC", "batman"));
            }
        }).start();

        // Aguarda 3 segundos na tela de Splash antes de redirecionar para o Login
        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }, 3000);

        // Aplica ajustes de margem para respeitar as barras de sistema (status/navegação)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}