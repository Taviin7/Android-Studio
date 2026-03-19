package com.example.quiz_app;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class Questions {
    private String question;
    private List<String> alternativa = new ArrayList<>();
    private int alternativaCorreta;

    public Questions(String questions, Integer alternativaCorreta, String ... alternativa) {
        this.question = questions;
        this.alternativaCorreta = alternativaCorreta;
        this.alternativa.add(alternativa[0]);
        this.alternativa.add(alternativa[1]);
        this.alternativa.add(alternativa[2]);
        this.alternativa.add(alternativa[3]);
    }


    public String getQuestion() {
        return question;
    }

    public List<String> getAlternativa() {
        return alternativa;
    }

    public Integer getAlternativaCorreta() {
        return alternativaCorreta;
    }
}
