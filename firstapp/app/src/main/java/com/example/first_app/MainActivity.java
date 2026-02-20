package com.example.first_app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Declarando as variáveis do sistema
        Button btn_calcular = findViewById(R.id.btn_calcular);
        Button btn_limparCampos = findViewById(R.id.btn_limpar);

        EditText edNota1 = findViewById(R.id.ed_nota1);
        EditText edNota2 = findViewById(R.id.ed_nota2);
        EditText edFaltas = findViewById(R.id.ed_faltas);
        TextView tvMedia = findViewById(R.id.tv_media);
        TextView tvSituacao = findViewById(R.id.tv_situacao);
        EditText edDisciplina = findViewById(R.id.ed_disciplina);

        //Realizando a programação
        btn_calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double n1, n2, media;
                int faltas;
                String situacao;

                //Convertendo o texto para número
                n1 = Double.parseDouble(edNota1.getText().toString());
                n2 = Double.parseDouble(edNota2.getText().toString());
                faltas = Integer.parseInt(edFaltas.getText().toString());

                media = (n1 + n2) / 2;

                //Exibindo o resultado (Convertendo double para String)
                tvMedia.setText(String.valueOf(media));

                // tvSituacao.setText("Aprovado".toString());
                if (media >= 6 && faltas <= 5) {
                    tvSituacao.setText("Aprovado");
                    tvSituacao.setTextColor(Color.GREEN);
                }
                else {
                    tvSituacao.setText("Retido");
                    tvSituacao.setTextColor(Color.RED);
                }
            }
        });

        btn_limparCampos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edDisciplina.setText("");
                edNota1.setText("");
                edNota2.setText("");
                edFaltas.setText("");
                tvMedia.setText("0.0");
                tvSituacao.setText("Pendente");
                tvSituacao.setTextColor(Color.GRAY);
            }
        });
    }
}