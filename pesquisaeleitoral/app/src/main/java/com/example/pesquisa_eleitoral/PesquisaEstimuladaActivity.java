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

import com.example.pesquisa_eleitoral.database.AppDatabase;
import com.example.pesquisa_eleitoral.model.Candidato;
import com.example.pesquisa_eleitoral.model.Entrevista;

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

        // Busca candidatos do banco de dados
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            List<Candidato> candidatosDoBanco = db.candidatoDao().buscarTodos();

            runOnUiThread(() -> {
                // Adiciona candidatos vindos do banco
                for (Candidato c : candidatosDoBanco) {
                    adicionarRadioButton(radioGroup, c.getId(), c.getNome(), c.getPartido());
                }

                // Adiciona opções especiais
                adicionarRadioButton(radioGroup, ID_BRANCO, "Branco", "");
                adicionarRadioButton(radioGroup, ID_NULO, "Nulo", "");
                adicionarRadioButton(radioGroup, ID_NAO_SEI, "Não sei", "");
            });
        }).start();

        Button btn_confirmar = findViewById(R.id.btn_confirmar);
        btn_confirmar.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(this, "Selecione uma opção.", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selected = findViewById(selectedId);
            int candidatoIdReal = (int) selected.getTag();

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

    private void adicionarRadioButton(RadioGroup group, int id, String nome, String partido) {
        RadioButton rb = new RadioButton(this);
        rb.setId(View.generateViewId());
        rb.setTag(id);
        String texto = nome + (partido != null && !partido.isEmpty() ? " - " + partido : "");
        rb.setText(texto);
        group.addView(rb);
    }
}