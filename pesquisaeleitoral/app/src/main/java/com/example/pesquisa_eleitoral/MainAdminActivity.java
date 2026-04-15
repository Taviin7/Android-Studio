package com.example.pesquisa_eleitoral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pesquisa_eleitoral.database.AppDatabase;

public class MainAdminActivity extends AppCompatActivity {

    private TextView txtSubtitulo; // Variável para exibir o total de entrevistados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_admin);
        
        // Ajusta margens para as barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Variáveis do Layout
        txtSubtitulo = findViewById(R.id.txt_subtitulo);
        Button btn_eleitores = findViewById(R.id.btn_eleitores);
        Button btn_resultado = findViewById(R.id.btn_resultado);
        Button btn_finalizar = findViewById(R.id.btn_finalizar);

        // Inicializa a exibição do contador de entrevistas
        carregarTotalEntrevistados();

        // Abre a lista detalhada de eleitores cadastrados
        btn_eleitores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainAdminActivity.this, EleitoresActivity.class);
                startActivity(i);
            }
        });

        // Abre a tela de estatísticas e gráficos da pesquisa
        btn_resultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainAdminActivity.this, ResultadoPesquisaActivity.class);
                startActivity(i);
            }
        });

        // Retorna para o login e encerra o acesso administrativo
        btn_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainAdminActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualiza o contador de entrevistas sempre que o administrador volta para esta tela
        carregarTotalEntrevistados();
    }

    // Consulta o banco de dados em background para contar o total de entrevistas realizadas
    private void carregarTotalEntrevistados() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            int total = db.entrevistaDao().contarTotal();
            
            runOnUiThread(() -> {
                txtSubtitulo.setText("Quantidade de Pessoas Entrevistadas: " + total);
            });
        }).start();
    }
}