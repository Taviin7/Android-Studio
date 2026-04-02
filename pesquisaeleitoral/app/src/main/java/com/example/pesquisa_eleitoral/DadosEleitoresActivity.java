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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

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
        setContentView(R.layout.activity_eleitores);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNome    = findViewById(R.id.etNome);
        etCelular = findViewById(R.id.etCelular);
        tvGps     = findViewById(R.id.tvGps);

        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        obterLocalizacao();

        Button btn_salvar = findViewById(R.id.btn_salvar);
        btn_salvar.setOnClickListener(v -> salvar());
    }

    // ---------------------------------------------------------------
    // GPS
    // ---------------------------------------------------------------

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

        //fusedClient.getLastLocation().addOnSuccessListener(location -> {
            //if (location != null) {
             //   latitude  = location.getLatitude();
             //   longitude = location.getLongitude();
            //    tvGps.setText(String.format("GPS: %.5f, %.5f", latitude, longitude));
            //} else {
            //    tvGps.setText("GPS: aguardando sinal...");
            //}
        //});
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            buscarLocalizacao();
        } else {
            tvGps.setText("GPS: permissão negada");
        }
    }

    // ---------------------------------------------------------------
    // Salvar
    // ---------------------------------------------------------------

    private void salvar() {
        String nome    = etNome.getText().toString().trim();
        String celular = etCelular.getText().toString().trim();

        if (nome.isEmpty() || celular.isEmpty()) {
            Toast.makeText(this, "Preencha nome e celular.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Recupera a entrevista vinda da tela anterior
        Entrevista entrevista = (Entrevista) getIntent().getSerializableExtra("entrevista");

        if (entrevista == null) {
            Toast.makeText(this, "Erro: dados da entrevista perdidos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Preenche os campos finais
        entrevista.setNome(nome);
        entrevista.setCelular(celular);
        entrevista.setLatitude(latitude);
        entrevista.setLongitude(longitude);

        // Aqui você salva: lista estática, banco local (Room) ou envia para API
        // Exemplo com lista estática:
        // ListaEntrevistas.getInstance().add(entrevista);

        Toast.makeText(this, "Entrevista salva com sucesso!", Toast.LENGTH_SHORT).show();

        // Volta para MainActivity limpando o backstack
        Intent i = new Intent(DadosEleitoresActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
}