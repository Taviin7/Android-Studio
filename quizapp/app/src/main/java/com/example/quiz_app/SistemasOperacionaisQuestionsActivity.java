package com.example.quiz_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class SistemasOperacionaisQuestionsActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_questions_soactivity);

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

        btnProxima.setOnClickListener(v -> ClicarProxima());
    }

    private void carregarQuestoes() {
        listaQuestoes.add(new Questions(
                "O que é um sistema operacional?",
                0,
                "Gerencia hardware e software",
                "Editor de texto",
                "Compilador",
                "Banco de dados"
        ));

        listaQuestoes.add(new Questions(
                "Qual é um exemplo de sistema operacional?",
                2,
                "Chrome", "Word", "Linux", "MySQL"
        ));

        listaQuestoes.add(new Questions(
                "O que é um processo?",
                1,
                "Arquivo em execução",
                "Programa em execução",
                "Memória RAM",
                "Disco rígido"
        ));

        listaQuestoes.add(new Questions(
                "O que é memória RAM?",
                3,
                "Armazenamento permanente",
                "Cache de CPU",
                "HD externo",
                "Memória volátil"
        ));

        listaQuestoes.add(new Questions(
                "Qual função do escalonador (scheduler)?",
                2,
                "Gerenciar arquivos",
                "Controlar rede",
                "Gerenciar processos",
                "Executar programas"
        ));

        listaQuestoes.add(new Questions(
                "O que é multitarefa?",
                1,
                "Executar um programa",
                "Executar vários programas",
                "Gerenciar memória",
                "Controlar hardware"
        ));

        listaQuestoes.add(new Questions(
                "Qual componente gerencia arquivos?",
                0,
                "Sistema de arquivos",
                "Kernel",
                "Driver",
                "Shell"
        ));

        listaQuestoes.add(new Questions(
                "O que é o kernel?",
                3,
                "Interface gráfica",
                "Aplicativo",
                "Editor",
                "Núcleo do sistema"
        ));

        listaQuestoes.add(new Questions(
                "O que é um driver?",
                2,
                "Programa de edição",
                "Sistema operacional",
                "Software que controla hardware",
                "Memória"
        ));

        listaQuestoes.add(new Questions(
                "Qual desses é um sistema operacional móvel?",
                1,
                "Windows", "Android", "Linux Server", "Ubuntu Desktop"
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

    private void ClicarProxima() {

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