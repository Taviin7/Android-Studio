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

    // IDs especiais: longe de -1 (que o Android reserva para "nada selecionado")
    // e longe dos IDs reais de candidatos (positivos: 1, 2, 3…)
    // Senão tiver essa lógica, consigo selecionar mais de 1
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

        List<Candidato> candidatos = new ArrayList<>();
        candidatos.add(new Candidato(1, "Jorge Amado",  "Partido X"));
        candidatos.add(new Candidato(2, "Candidato 2",  "Partido Y"));
        candidatos.add(new Candidato(3, "Candidato 3",  "Partido Z"));

        // Especiais — IDs -100, -200, -300 para não colidir com o -1 do Android
        candidatos.add(new Candidato(ID_BRANCO,  "Branco",  ""));
        candidatos.add(new Candidato(ID_NULO,    "Nulo",    ""));
        candidatos.add(new Candidato(ID_NAO_SEI, "Não sei", ""));

        for (Candidato c : candidatos) {
            RadioButton rb = new RadioButton(this);

            rb.setId(View.generateViewId()); // ID interno do Android
            rb.setTag(c.getId());            // seu ID real

            rb.setText(c.getNome() +
                    (c.getPartido().isEmpty() ? "" : " - " + c.getPartido()));

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
            int candidatoId = (int) selected.getTag();

            Entrevista entrevista = new Entrevista(
                    candidatoId,
                    new ArrayList<>(),
                    "",
                    "",
                    System.currentTimeMillis(),
                    0.0,
                    0.0
            );

            Intent i = new Intent(PesquisaEstimuladaActivity.this, RelatarProblemasActivity.class);
            i.putExtra("entrevista", entrevista);
            startActivity(i);
        });
    }
}