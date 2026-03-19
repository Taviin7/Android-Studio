package com.example.quiz_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class SqlQuestionsActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_questions_sql);

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
                "Qual comando é usado para buscar dados?",
                2,
                "INSERT", "UPDATE", "SELECT", "DELETE"
        ));

        listaQuestoes.add(new Questions(
                "Qual comando insere dados em uma tabela?",
                0,
                "INSERT", "ADD", "PUT", "CREATE"
        ));

        listaQuestoes.add(new Questions(
                "Qual comando remove dados de uma tabela?",
                3,
                "DROP", "REMOVE", "CLEAR", "DELETE"
        ));

        listaQuestoes.add(new Questions(
                "Qual comando altera dados existentes?",
                1,
                "MODIFY", "UPDATE", "CHANGE", "EDIT"
        ));

        listaQuestoes.add(new Questions(
                "Qual cláusula filtra resultados?",
                0,
                "WHERE", "GROUP BY", "ORDER BY", "HAVING"
        ));

        listaQuestoes.add(new Questions(
                "Qual cláusula ordena os resultados?",
                2,
                "WHERE", "GROUP BY", "ORDER BY", "SORT"
        ));

        listaQuestoes.add(new Questions(
                "Qual comando cria uma tabela?",
                1,
                "MAKE TABLE", "CREATE TABLE", "NEW TABLE", "BUILD TABLE"
        ));

        listaQuestoes.add(new Questions(
                "Qual comando remove uma tabela?",
                3,
                "DELETE TABLE", "REMOVE TABLE", "CLEAR TABLE", "DROP TABLE"
        ));

        listaQuestoes.add(new Questions(
                "Qual função conta registros?",
                0,
                "COUNT()", "SUM()", "AVG()", "TOTAL()"
        ));

        listaQuestoes.add(new Questions(
                "Qual operador é usado para busca com padrão?",
                2,
                "=", "IN", "LIKE", "MATCH"
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