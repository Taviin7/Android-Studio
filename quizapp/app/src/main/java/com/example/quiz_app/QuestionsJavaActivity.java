package com.example.quiz_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class QuestionsJavaActivity extends AppCompatActivity {

    // Views do layout
    private TextView tvQuestion;
    private RadioGroup radioGroup;
    private RadioButton answer1, answer2, answer3, answer4;
    private Button btnNext;

    // Dados do quiz
    private List<Questions> questionList = new ArrayList<>();
    private int currentIndex = 0;
    private double score = 0.0;
    private int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_java); // nome do seu XML

        // Vincula as views
        tvQuestion  = findViewById(R.id.txt_question);
        radioGroup  = findViewById(R.id.radio_group);
        answer1 = findViewById(R.id.radio_answer1);
        answer2 = findViewById(R.id.radio_answer2);
        answer3 = findViewById(R.id.radio_answer3);
        answer4 = findViewById(R.id.radio_answer4);
        btnNext = findViewById(R.id.btn_next);

        loadQuestions();
        showQuestion();

        btnNext.setOnClickListener(v -> handleNextClick());
    }

    //Montando as perguntas aqui (rightAnswer é índice 0–3)
    private void loadQuestions() {
        questionList.add(new Questions(
                "Qual palavra-chave cria um objeto em Java?",
                1, // "new" — índice 1
                "create", "new", "build", "make"
        ));
        questionList.add(new Questions(
                "Qual é o tipo primitivo para números inteiros?",
                0,
                "int", "integer", "num", "number"
        ));
        questionList.add(new Questions(
                "O que significa POO?",
                2,
                "Programação Orientada a Objetos Ordenados",
                "Projeto Orientado a Objetos",
                "Programação Orientada a Objetos",
                "Processo Orientado a Objetos"
        ));
        questionList.add(new Questions(
                "Qual modificador torna um atributo acessível apenas na própria classe?",
                1,
                "public", "private", "protected", "default"
        ));
        questionList.add(new Questions(
                "Como se chama o método executado ao criar um objeto?",
                3,
                "initiator", "builder", "starter", "construtor"
        ));
    }

    // Exibe a questão atual na tela

    private void showQuestion() {
        Questions current = questionList.get(currentIndex);

        //Limpa seleção anterior
        radioGroup.clearCheck();

        //Preenche a pergunta
        tvQuestion.setText((currentIndex + 1) + ". " + current.getQuestion());

        //Preenche as alternativas
        List<String> answers = current.getAnswers();
        answer1.setText(answers.get(0));
        answer2.setText(answers.get(1));
        answer3.setText(answers.get(2));
        answer4.setText(answers.get(3));

        //Atualiza o texto do botão na última questão
        boolean isLast = (currentIndex == questionList.size() - 1);
        btnNext.setText(isLast ? "Ver resultado" : "Próxima");
    }

    //Chamado ao clicar em "Próxima / Ver resultado"
    private void handleNextClick() {
        // Verifica se o usuário selecionou alguma opção
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecione uma alternativa!", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedIndex = getSelectedIndex();

        //Comparando com a resposta correta e lógica de ponto
        if (selectedIndex == questionList.get(currentIndex).getRightAnswer()) {
            score += 1.0;
            correctAnswers++;
        } else {
            score -= 0.20;
        }

        score = Math.max(0, score); //Evita que o score fique negativo
        currentIndex++;

        if (currentIndex < questionList.size()) {
            showQuestion();
        } else {
            goToResult();
        }
    }

    // Converte o RadioButton marcado em índice 0–3
    private int getSelectedIndex() {
        int checkedId = radioGroup.getCheckedRadioButtonId();

        if (checkedId == R.id.radio_answer1) return 0;
        if (checkedId == R.id.radio_answer2) return 1;
        if (checkedId == R.id.radio_answer3) return 2;
        if (checkedId == R.id.radio_answer4) return 3;

        return -1; // nunca deve chegar aqui, validado função acima
    }

    private void goToResult() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("TOTAL", questionList.size());
        intent.putExtra("CORRECT", correctAnswers);
        startActivity(intent);
        finish();
    }
}