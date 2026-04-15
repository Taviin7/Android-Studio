package com.example.pesquisa_eleitoral;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pesquisa_eleitoral.database.AppDatabase;
import com.example.pesquisa_eleitoral.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        EditText ed_usuario = findViewById(R.id.ed_usuario);
        EditText ed_senha = findViewById(R.id.ed_senha);
        Button btn_login = findViewById(R.id.bnt_login);

        // Lógica de autenticação: verifica credenciais no banco e direciona para a tela Admin ou Pesquisador
        btn_login.setOnClickListener(v -> {
            String user = ed_usuario.getText().toString().trim();
            String pass = ed_senha.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Realiza a consulta de login em uma thread separada (exigência do Room)
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(this);
                Usuario usuario = db.usuarioDao().fazerLogin(user, pass);

                runOnUiThread(() -> {
                    if (usuario != null) {
                        Intent i;
                        // Direciona conforme o perfil do usuário
                        if ("ADMIN".equals(usuario.getTipo())) {
                            i = new Intent(this, MainAdminActivity.class);
                        } else {
                            i = new Intent(this, MainActivity.class);
                        }
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(this, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }
}