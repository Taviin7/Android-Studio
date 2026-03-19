package com.example.quiz_app;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        txtResult = findViewById(R.id.txt_result);

        double score = getIntent().getDoubleExtra("SCORE", 0);
        int total = getIntent().getIntExtra("TOTAL", 0);
        int correct = getIntent().getIntExtra("CORRECT", 0);

        txtResult.setText(
                "Pontuação: " + String.format("%.2f", score) +
                        "\nAcertos: " + correct + " de " + total
        );
    }
}