package com.example.pesquisa_eleitoral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class RelatarProblemasActivity extends AppCompatActivity {

    private Entrevista entrevista;
    private LinearLayout layoutProblemas;
    private CheckBox cbOutro;
    private EditText edtOutro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_relatar_problemas);

        // Referências
        layoutProblemas = findViewById(R.id.layoutProblemas);
        cbOutro = findViewById(R.id.cb_outro);
        edtOutro = findViewById(R.id.edt_outro);
        Button btn = findViewById(R.id.btn_proximo);

        // Recupera a entrevista
        entrevista = (Entrevista) getIntent().getSerializableExtra("entrevista");

        // Lista de problemas
        String[] problemas = {
                "Saúde",
                "Educação",
                "Segurança",
                "Transporte",
                "Corrupção",
                "Desemprego"
        };

        // Criar CheckBoxes dinamicamente
        for (String p : problemas) {
            CheckBox cb = new CheckBox(this);
            cb.setText(p);
            layoutProblemas.addView(cb);
        }

        // Lógica do "Outro"
        cbOutro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                edtOutro.setVisibility(View.VISIBLE);
            } else {
                edtOutro.setVisibility(View.GONE);
                edtOutro.setText("");
            }
        });

        // Botão próximo
        btn.setOnClickListener(v -> {

            List<String> selecionados = new ArrayList<>();

            // Percorre os checkboxes criados dinamicamente
            for (int i = 0; i < layoutProblemas.getChildCount(); i++) {
                View view = layoutProblemas.getChildAt(i);

                if (view instanceof CheckBox) {
                    CheckBox cb = (CheckBox) view;

                    if (cb.isChecked()) {
                        selecionados.add(cb.getText().toString());
                    }
                }
            }

            // Trata "Outro", validando se há texto
            if (cbOutro.isChecked()) {
                String outroTexto = edtOutro.getText().toString().trim();

                if (outroTexto.isEmpty()) {
                    edtOutro.setError("Digite o problema");
                    return;
                }

                selecionados.add(outroTexto);
            }

            // Validação geral
            if (selecionados.isEmpty()) {
                Toast.makeText(this, "Selecione ao menos um problema", Toast.LENGTH_SHORT).show();
                return;
            }

            // Atualiza a entrevista
            entrevista.setProblemas(selecionados);

            // Próxima tela
            int candidatoId = getIntent().getIntExtra("candidatoId", -1);

            Intent i = new Intent(this, DadosEleitoresActivity.class);
            i.putExtra("candidatoId", candidatoId);
            i.putExtra("entrevista", entrevista);
            startActivity(i);
        });
    }
}