package com.example.quiz_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class JavaQuestionsActivity extends AppCompatActivity {

    // Views do layout
    private TextView tvPergunta;
    private RadioGroup grupoRadio;
    private RadioButton resposta1, resposta2, resposta3, resposta4;
    private Button btnProxima;

    // Dados do quiz
    private List<Questions> listaQuestoes = new ArrayList<>();
    private int indiceAtual = 0;
    private double pontuacao = 0.0;
    private int acertos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_java);

        // Vincula as views
        tvPergunta  = findViewById(R.id.txt_questao);
        grupoRadio  = findViewById(R.id.radio_group);
        resposta1 = findViewById(R.id.rb_alternativa1);
        resposta2 = findViewById(R.id.rb_alternativa2);
        resposta3 = findViewById(R.id.rb_alternativa3);
        resposta4 = findViewById(R.id.rb_alternativa4);
        btnProxima = findViewById(R.id.btn_next);

        carregarQuestoes();
        mostrarPergunta();

        btnProxima.setOnClickListener(v -> clicarProxima());
    }

    private void carregarQuestoes() {
        listaQuestoes.add(new Questions(
                "Qual palavra-chave cria um objeto em Java?",
                1,
                "create", "new", "build", "make"
        ));

        listaQuestoes.add(new Questions(
                "Qual tipo primitivo é usado para armazenar números decimais?",
                2,
                "int", "char", "double", "boolean"
        ));

        listaQuestoes.add(new Questions(
                "Qual método é o ponto de entrada de um programa Java?",
                0,
                "main", "start", "run", "init"
        ));

        listaQuestoes.add(new Questions(
                "Qual palavra-chave define uma classe?",
                3,
                "def", "struct", "type", "class"
        ));

        listaQuestoes.add(new Questions(
                "Qual desses NÃO é um tipo primitivo?",
                1,
                "int", "String", "boolean", "double"
        ));

        listaQuestoes.add(new Questions(
                "Qual operador é usado para comparação de igualdade?",
                2,
                "=", "!=", "==", "<>"
        ));

        listaQuestoes.add(new Questions(
                "Qual estrutura é usada para repetição?",
                0,
                "for", "if", "switch", "try"
        ));

        listaQuestoes.add(new Questions(
                "Qual palavra-chave é usada para herança?",
                1,
                "implement", "extends", "inherits", "super"
        ));

        listaQuestoes.add(new Questions(
                "Qual modificador permite acesso apenas dentro da classe?",
                2,
                "public", "protected", "private", "default"
        ));

        listaQuestoes.add(new Questions(
                "Qual classe é usada para entrada de dados pelo teclado?",
                3,
                "Input", "DataReader", "Console", "Scanner"
        ));
    }

    private void mostrarPergunta() {
        Questions atual = listaQuestoes.get(indiceAtual);

        grupoRadio.clearCheck();

        tvPergunta.setText((indiceAtual + 1) + ". " + atual.getQuestion());

        List<String> respostas = atual.getAlternativa();
        resposta1.setText(respostas.get(0));
        resposta2.setText(respostas.get(1));
        resposta3.setText(respostas.get(2));
        resposta4.setText(respostas.get(3));

        boolean ultima = (indiceAtual == listaQuestoes.size() - 1);
        btnProxima.setText(ultima ? "Ver resultado" : "Próxima");
    }

    private void clicarProxima() {

        if (grupoRadio.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecione uma alternativa!", Toast.LENGTH_SHORT).show();
            return;
        }

        int indiceSelecionado = obterIndiceSelecionado();

        if (indiceSelecionado == listaQuestoes.get(indiceAtual).getAlternativaCorreta()) {
            pontuacao += 1.0;
            acertos++;
        } else {
            pontuacao -= 0.20;
        }

        pontuacao = Math.max(0, pontuacao);

        indiceAtual++;

        if (indiceAtual < listaQuestoes.size()) {
            mostrarPergunta();
        } else {
            mostrarResultado();
        }
    }

    private int obterIndiceSelecionado() {
        int idSelecionado = grupoRadio.getCheckedRadioButtonId();

        if (idSelecionado == R.id.rb_alternativa1) return 0;
        if (idSelecionado == R.id.rb_alternativa2) return 1;
        if (idSelecionado == R.id.rb_alternativa3) return 2;
        if (idSelecionado == R.id.rb_alternativa4) return 3;

        //Caso nenhum ID corresponda (situação inesperada), nenhuma opção válida encontrada
        return -1;
    }

    private void mostrarResultado() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("SCORE", pontuacao);
        intent.putExtra("TOTAL", listaQuestoes.size());
        intent.putExtra("CORRECT", acertos);
        startActivity(intent);
        finish();
    }
}