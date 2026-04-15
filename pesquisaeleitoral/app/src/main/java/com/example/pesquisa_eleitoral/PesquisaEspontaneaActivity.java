package com.example.pesquisa_eleitoral;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pesquisa_eleitoral.model.Entrevista;

import java.util.ArrayList;

public class PesquisaEspontaneaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pesquisa_espontanea);

        // Configuração de margens para a barra de status e navegação
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn_confirmar = findViewById(R.id.btn_confirmar);
        EditText ed_nome = findViewById(R.id.ed_nome);

        // Captura o voto espontâneo e inicia o objeto Entrevista para transporte entre telas
        btn_confirmar.setOnClickListener(v -> {
            String resposta = ed_nome.getText().toString().trim();

            if (resposta.isEmpty()) {
                Toast.makeText(this, "Por favor, digite o nome do candidato.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Inicializa a entrevista com o voto espontâneo e timestamp atual
            Entrevista entrevista = new Entrevista();
            entrevista.setVotoEspontaneo(resposta);
            entrevista.setProblemas(new ArrayList<>());
            entrevista.setTimestamp(System.currentTimeMillis());

            // Segue para a próxima etapa: Pesquisa Estimulada
            Intent i = new Intent(this, PesquisaEstimuladaActivity.class);
            i.putExtra("entrevista", entrevista);
            i.putExtra("candidatoId", -1);
            startActivity(i);
        });
    }
}