package com.example.pesquisa_eleitoral;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pesquisa_eleitoral.database.AppDatabase;
import com.example.pesquisa_eleitoral.model.Entrevista;
import com.example.pesquisa_eleitoral.model.ProblemaRelatado;
import com.example.pesquisa_eleitoral.model.Voto;
import com.example.pesquisa_eleitoral.model.VotoEspontaneo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class DadosEleitoresActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1001;

    private EditText etNome, etCelular;
    private TextView tvGps;
    private FusedLocationProviderClient fusedClient;

    private double latitude  = 0.0;
    private double longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dados_eleitores);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNome = findViewById(R.id.etNome);
        etCelular = findViewById(R.id.etCelular);
        tvGps = findViewById(R.id.tvGps);

        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        obterLocalizacao();

        Button btn_salvar = findViewById(R.id.btn_salvar);
        btn_salvar.setOnClickListener(v -> salvar());
    }

    private void obterLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            return;
        }
        buscarLocalizacao();
    }

    private void buscarLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;

        fusedClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                latitude  = location.getLatitude();
                longitude = location.getLongitude();
                tvGps.setText(String.format("Localização Aproximada: %.3f, %.3f", latitude, longitude));
            } else {
                tvGps.setText("GPS: aguardando sinal...");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            buscarLocalizacao();
        } else {
            tvGps.setText("GPS: permissão negada");
        }
    }

    private void salvar() {
        String nome = etNome.getText().toString().trim();
        String celular = etCelular.getText().toString().trim();

        if (nome.isEmpty() || celular.isEmpty()) {
            Toast.makeText(this, "Preencha nome e celular.", Toast.LENGTH_SHORT).show();
            return;
        }

        Entrevista entrevista = (Entrevista) getIntent().getSerializableExtra("entrevista");
        if (entrevista == null) return;

        int candidatoId = getIntent().getIntExtra("candidatoId", -1);

        // 1. Configura os dados da Entrevista (Pessoais)
        entrevista.setNome(nome);
        entrevista.setCelular(celular);

        // Sigilo de Localização: 3 casas decimais (precisão de ~100 metros)
        double latReduzida = Math.round(latitude * 1000.0) / 1000.0;
        double lonReduzida = Math.round(longitude * 1000.0) / 1000.0;
        entrevista.setLatitude(latReduzida);
        entrevista.setLongitude(lonReduzida);
        entrevista.setTimestamp(System.currentTimeMillis());

        // 2. SIGILO ABSOLUTO: Extrai dados que não podem ser vinculados ao nome
        
        // Voto Espontâneo
        String textoEspontaneo = entrevista.getVotoEspontaneo();
        entrevista.setVotoEspontaneo(null); 

        // Problemas Relatados: Salvaremos de forma anônima
        List<String> listaProblemas = entrevista.getProblemas();
        entrevista.setProblemas(new ArrayList<>()); // Limpa do objeto principal para não vincular ao nome no card

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            // Salva apenas PERFIL do eleitor (Nome, Celular, GPS, Data)
            db.entrevistaDao().inserir(entrevista);

            // Salva VOTO ESTIMULADO de forma anônima
            if (candidatoId != -1) {
                db.votoDAO().inserir(new Voto(candidatoId));
            }

            // Salva VOTO ESPONTÂNEO de forma anônima
            if (textoEspontaneo != null && !textoEspontaneo.isEmpty()) {
                db.votoEspontaneoDAO().inserir(new VotoEspontaneo(textoEspontaneo));
            }

            // Salva PROBLEMAS de forma anônima
            if (listaProblemas != null) {
                for (String desc : listaProblemas) {
                    db.problemaDao().inserir(new ProblemaRelatado(desc));
                }
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Pesquisa concluída com sigilo garantido!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            });
        }).start();
    }
}