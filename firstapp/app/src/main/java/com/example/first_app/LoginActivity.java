package com.example.first_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Variáveis do sistema
        Button btn_login = findViewById(R.id.btn_login);
        EditText ed_email = findViewById(R.id.ed_email);
        EditText ed_senha = findViewById(R.id.ed_senha);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "otavio@gmail.com", senha = "otavio1234";

                if(ed_email.getText().toString().equals(email) && ed_senha.getText().toString().equals(senha)){
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                    //Mensagem de aviso
                    CharSequence text = "Bem-vindo!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(LoginActivity.this, text, duration);
                    toast.show();
                }
                else{
                    //Mensagem de aviso
                    CharSequence text = "Email ou senha incorretos!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(LoginActivity.this, text, duration);
                    toast.show();
                }
            }
        });
    }
}