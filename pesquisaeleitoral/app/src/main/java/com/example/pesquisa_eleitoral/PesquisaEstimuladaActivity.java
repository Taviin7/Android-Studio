package com.example.pesquisa_eleitoral;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pesquisa_eleitoral.adapter.CandidatoAdapter;
import com.example.pesquisa_eleitoral.database.AppDatabase;
import com.example.pesquisa_eleitoral.model.Candidato;
import com.example.pesquisa_eleitoral.model.Entrevista;

import java.util.ArrayList;
import java.util.List;

public class PesquisaEstimuladaActivity extends AppCompatActivity {

    public static final int ID_BRANCO  = -100;
    public static final int ID_NULO    = -200;
    public static final int ID_NAO_SEI = -300;

    private CandidatoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pesquisa_estimulada);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configura a RecyclerView
        RecyclerView rvCandidatos = findViewById(R.id.rvCandidatos);
        rvCandidatos.setLayoutManager(new LinearLayoutManager(this));

        // Busca candidatos do banco de dados
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            List<Candidato> candidatosDoBanco = db.candidatoDao().buscarTodos();

            // Cria a lista final incluindo as opções especiais
            List<Candidato> listaCompleta = new ArrayList<>(candidatosDoBanco);
            
            // Adiciona opções especiais como objetos Candidato temporários
            listaCompleta.add(criarCandidatoEspecial(ID_BRANCO, "Branco", ""));
            listaCompleta.add(criarCandidatoEspecial(ID_NULO, "Nulo", ""));
            listaCompleta.add(criarCandidatoEspecial(ID_NAO_SEI, "Não sei", ""));

            // Atualiza a RecyclerView
            runOnUiThread(() -> {
                adapter = new CandidatoAdapter(listaCompleta, null);
                rvCandidatos.setAdapter(adapter);
            });
        }).start();

        // Botão de confirmação para prosseguir para a próxima etapa
        Button btn_confirmar = findViewById(R.id.btn_confirmar);
        btn_confirmar.setOnClickListener(v -> {
            if (adapter == null || adapter.getSelectedCandidatoId() == -1) {
                Toast.makeText(this, "Selecione uma opção.", Toast.LENGTH_SHORT).show();
                return;
            }

            int candidatoIdReal = adapter.getSelectedCandidatoId();

            Entrevista entrevista = (Entrevista) getIntent().getSerializableExtra("entrevista");
            if (entrevista == null) {
                entrevista = new Entrevista();
                entrevista.setProblemas(new ArrayList<>());
            }

            // Inicia a próxima etapa com o candidato selecionado
            Intent i = new Intent(this, RelatarProblemasActivity.class);
            i.putExtra("candidatoId", candidatoIdReal);
            i.putExtra("entrevista", entrevista);
            startActivity(i);
        });
    }

    // Cria um objeto Candidato com características especiais - Nulo, Branco e Não sei
    private Candidato criarCandidatoEspecial(int id, String nome, String foto) {
        Candidato c = new Candidato(nome, "", foto);
        c.setId(id);
        return c;
    }
}