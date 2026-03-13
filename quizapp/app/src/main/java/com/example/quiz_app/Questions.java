package com.example.quiz_app;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class Questions {
    private String question;
    private List<String> answers = new ArrayList<>();
    private int rightAnswer;

    public Questions(String questions, Integer rightAnswer, String ... answers) {
        this.question = questions;
        this.rightAnswer = rightAnswer;
        this.answers.add(answers[0]);
        this.answers.add(answers[1]);
        this.answers.add(answers[2]);
        this.answers.add(answers[3]);
    }


    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public Integer getRightAnswer() {
        return rightAnswer;
    }
}
