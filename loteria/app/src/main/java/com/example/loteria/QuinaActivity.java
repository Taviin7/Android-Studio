package com.example.loteria;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class QuinaActivity extends AppCompatActivity {

    //Campos da interface do usuário
    EditText edtQtdNumeros, edtQtdJogos;
    TextView txtResultado;
    Button btnGerar;
    GridLayout gridResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quina);

        //Connect com os elementos do layout com as variáveis
        edtQtdNumeros = findViewById(R.id.edtQtdNumeros);
        edtQtdJogos = findViewById(R.id.edtQtdJogos);
        txtResultado = findViewById(R.id.txtResultado);
        btnGerar = findViewById(R.id.btnGerar);
        gridResultado = findViewById(R.id.gridResultado);

        btnGerar.setOnClickListener(v -> gerar());
    }

    //Metodo responsável por gerar os jogos e mostrar na tela
    private void gerar() {

        //Assim, evita que o app crashe caso seja executa sem quantidade de números/jogs
        String strQtdNumeros = edtQtdNumeros.getText().toString();
        String strQtdJogos = edtQtdJogos.getText().toString();

        if(strQtdNumeros.isEmpty()){
            Toast.makeText(this,"Digite a quantidade de números",Toast.LENGTH_SHORT).show();
            return;
        }

        if(strQtdJogos.isEmpty()){
            Toast.makeText(this,"Digite a quantidade de jogos",Toast.LENGTH_SHORT).show();
            return;
        }

        //Convertendo para int
        int qtdNumeros = Integer.parseInt(strQtdNumeros);
        int qtdJogos = Integer.parseInt(strQtdJogos);

        if(qtdNumeros < 5 || qtdNumeros > 15){
            Toast.makeText(this,"Escolha entre 5 e 15 números",Toast.LENGTH_LONG).show();
            return;
        }

        gridResultado.removeAllViews(); // limpa antes de gerar todos os jogos

        //Loop para gerar os jogos conforme solicitado
        for(int i = 1; i <= qtdJogos; i++){

            //Quina os números vão de 1 a 80
            List<Integer> jogo = GeradorLoteria.gerar(1,80,qtdNumeros);
            mostrarJogo(i, jogo);
        }

        txtResultado.setVisibility(View.VISIBLE);
        //edtQtdJogos.setText("");
        //edtQtdNumeros.setText("");
    }

    //Metodo responsável por exibir um jogo gerado no GridLayout da interface.
    private void mostrarJogo(int numeroDoJogo, List<Integer> jogo){

        // Texto "Jogo 1", "Jogo 2", etc.
        TextView titulo = new TextView(this);
        titulo.setText("Jogo " + numeroDoJogo);
        titulo.setTextSize(16);
        titulo.setPadding(0,30,0,15);

        // Faz o título ocupar todas as colunas do GridLayout
        GridLayout.LayoutParams tituloParams = new GridLayout.LayoutParams();
        tituloParams.columnSpec = GridLayout.spec(0, 6); // ocupa 6 colunas

        titulo.setLayoutParams(tituloParams);

        // Adiciona o título ao GridLayout
        gridResultado.addView(titulo);

        // Percorre todos os números gerados no jogo
        for(Integer numero : jogo){

            // Cria um TextView que representará a "bola" do número
            TextView bola = new TextView(this);

            // Formata o número com dois dígitos (ex: 01, 02, 10)
            bola.setText(String.format("%02d", numero));

            bola.setTextSize(18);
            bola.setTextColor(Color.WHITE);
            bola.setGravity(Gravity.CENTER);

            // Define o fundo personalizado da bola (drawable) criado antes
            bola.setBackgroundResource(R.drawable.bola_quina);

            // Define margens para espaçamento entre as bolas
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(8,8,8,8);

            bola.setLayoutParams(params);

            // Adiciona a bola com o número no GridLayout
            gridResultado.addView(bola);
        }
    }
}