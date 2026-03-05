package com.example.loteria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        //Connect com os elementos do layout com as variáveis
        Button btnMega = findViewById(R.id.btnMega);
        Button btnQuina = findViewById(R.id.btnQuina);
        Button btnLotofacil = findViewById(R.id.btnLotofacil);
        Button btnLotomania = findViewById(R.id.btnLotomania);

        btnMega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MegaActivity.class);
                startActivity(i);
                //finish();
            }
        });

        btnQuina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, QuinaActivity.class);
                startActivity(i);
                //finish();
            }
        });

        btnLotomania.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LotomaniaActivity.class);
                startActivity(i);
                //finish();
            }
        });

        btnLotofacil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LotofacilActivity.class);
                startActivity(i);
                //finish();
            }
        });
    }
}