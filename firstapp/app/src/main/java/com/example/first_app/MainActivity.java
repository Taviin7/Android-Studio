package com.example.first_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        Button btn_voltar = findViewById(R.id.btn_voltar);

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

                // Verificação se os campos estão vazio
                if (!edNota1.getText().toString().isEmpty() &&
                        !edNota2.getText().toString().isEmpty() &&
                        !edFaltas.getText().toString().isEmpty()) {

                    // Convertendo os números
                    double nota1 = Double.parseDouble(edNota1.getText().toString());
                    double nota2 = Double.parseDouble(edNota2.getText().toString());
                    int faltas = Integer.parseInt(edFaltas.getText().toString());
                    double media = (nota1 + nota2) / 2;

                    tvMedia.setText(String.valueOf(media));

                    if (media >= 6 && faltas <= 5) {
                        tvSituacao.setText("Aprovado");
                        tvSituacao.setTextColor(Color.GREEN);
                    } else {
                        tvSituacao.setText("Retido");
                        tvSituacao.setTextColor(Color.RED);
                    }
                } else {
                    //Mensagem de aviso para preencher os campos
                    CharSequence text = "Preencha todos os campos em branco!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(MainActivity.this, text, duration);
                    toast.show();
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

        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}