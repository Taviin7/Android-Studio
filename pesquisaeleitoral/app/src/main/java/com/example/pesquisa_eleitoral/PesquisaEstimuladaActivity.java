package com.example.pesquisa_eleitoral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class PesquisaEstimuladaActivity extends AppCompatActivity {

    public static final int ID_BRANCO  = -100;
    public static final int ID_NULO    = -200;
    public static final int ID_NAO_SEI = -300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pesquisa_estimulada);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RadioGroup radioGroup = findViewById(R.id.radioGroupCandidatos);

        // Lista de candidatos
        List<Candidato> candidatos = new ArrayList<>();
        candidatos.add(new Candidato(1, "Jorge Amado",  "Partido ML"));
        candidatos.add(new Candidato(2, "Caio Cássio",  "Partido AMZ"));
        candidatos.add(new Candidato(3, "Luiza Albergue",  "Partido ALX"));
        candidatos.add(new Candidato(ID_BRANCO,  "Branco",  ""));
        candidatos.add(new Candidato(ID_NULO,    "Nulo",    ""));
        candidatos.add(new Candidato(ID_NAO_SEI, "Não sei", ""));

        for (Candidato c : candidatos) {
            RadioButton rb = new RadioButton(this);
            rb.setId(View.generateViewId());
            rb.setTag(c.getId()); // O ID real do candidato fica na Tag
            rb.setText(c.getNome() + (c.getPartido().isEmpty() ? "" : " - " + c.getPartido()));
            radioGroup.addView(rb);
        }

        Button btn_confirmar = findViewById(R.id.btn_confirmar);
        btn_confirmar.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(this, "Selecione uma opção.", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selected = findViewById(selectedId);
            int candidatoIdReal = (int) selected.getTag(); // Pegamos o ID da Tag

            // Recupera a entrevista que veio da Pesquisa Espontânea
            Entrevista entrevista = (Entrevista) getIntent().getSerializableExtra("entrevista");
            if (entrevista == null) {
                entrevista = new Entrevista();
                entrevista.setProblemas(new ArrayList<>());
            }

            Intent i = new Intent(this, RelatarProblemasActivity.class);
            i.putExtra("candidatoId", candidatoIdReal);
            i.putExtra("entrevista", entrevista);
            startActivity(i);
        });
    }
}