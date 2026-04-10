package com.example.pesquisa_eleitoral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Variáveis do sistema
        Button btn_login = findViewById(R.id.bnt_login);
        EditText ed_usuario = findViewById(R.id.ed_usuario);
        EditText ed_senha = findViewById(R.id.ed_senha);

        AppDatabase db = AppDatabase.getInstance(this);

        new Thread(() -> {
            if (db.usuarioDAO().buscarTotalUsuarios() == 0) {
                Usuario admin = new Usuario();
                admin.setNome("Admin");
                admin.setSenha("admin");
                admin.setTipo("admin");
                db.usuarioDAO().criar(admin);

                Usuario entrevistador = new Usuario();
                entrevistador.setNome("Entrevistador");
                entrevistador.setSenha("entrevistador");
                entrevistador.setTipo("entrevistador");
                db.usuarioDAO().criar(entrevistador);
            }
        }).start();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    Usuario usuarioLogado = db.usuarioDAO().fazerLogin(ed_usuario.getText().toString(), ed_senha.getText().toString());

                    // Usado quando queremos mostrar imagens na tela
                    runOnUiThread(() -> {
                        Intent i = null;
                        if (usuarioLogado != null) {

                            Toast toast = Toast.makeText(LoginActivity.this, "Bem-vindo!", Toast.LENGTH_SHORT);
                            toast.show();

                            if (usuarioLogado.getTipo().equals("admin")) {
                                i = new Intent(LoginActivity.this, MainAdminActivity.class);
                            } else {
                                i = new Intent(LoginActivity.this, MainActivity.class);
                            }

                            startActivity(i);
                            finish();
                        } else {
                            // Mensagem de aviso
                            String text = "Email ou senha incorretos!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(LoginActivity.this, text, duration);
                            toast.show();
                        }
                    });
                }).start();

            }
        });
    }
}