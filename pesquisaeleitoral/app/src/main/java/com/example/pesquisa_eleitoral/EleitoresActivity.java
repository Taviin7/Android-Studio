package com.example.pesquisa_eleitoral;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EleitoresActivity extends AppCompatActivity {

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

        LinearLayout container = findViewById(R.id.containerEleitores);

        // Busca todas as entrevistas do banco
        List<Entrevista> entrevistas = AppDatabase.getInstance(this)
                .entrevistaDao()
                .buscarTodas();

        if (entrevistas.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("Nenhuma entrevista registrada ainda.");
            tv.setTextSize(14f);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(0, dpToPx(80), 0, 0);
            container.addView(tv);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        for (int i = 0; i < entrevistas.size(); i++) {
            container.addView(criarCard(i + 1, entrevistas.get(i), sdf));
        }
    }

    private LinearLayout criarCard(int numero, Entrevista e, SimpleDateFormat sdf) {
        // Card externo
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, dpToPx(12));
        card.setLayoutParams(cardParams);
        card.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

        // Número do entrevistado
        card.addView(linha("Entrevistado #" + numero, true));

        // Campos
        card.addView(linha("Nome: " + valorOu(e.getNome(), "—"), false));
        card.addView(linha("Celular: " + valorOu(e.getCelular(), "—"), false));
        card.addView(linha("Data/hora: " + sdf.format(new Date(e.getTimestamp())), false));
        card.addView(linha("Localização: " + formatarGps(e.getLatitude(), e.getLongitude()), false));

        // Problemas
        String problemas = e.getProblemas() != null && !e.getProblemas().isEmpty()
                ? String.join(", ", e.getProblemas())
                : "—";
        card.addView(linha("Problemas: " + problemas, false));

        return card;
    }

    private TextView linha(String texto, boolean negrito) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setTextSize(13f);
        tv.setTextColor(getResources().getColor(R.color.black));
        if (negrito) tv.setTypeface(null, android.graphics.Typeface.BOLD);
        tv.setPadding(0, dpToPx(2), 0, dpToPx(2));
        return tv;
    }

    private String resolverCandidato(int id) {
        if (id == PesquisaEstimuladaActivity.ID_BRANCO)  return "Branco";
        if (id == PesquisaEstimuladaActivity.ID_NULO)    return "Nulo";
        if (id == PesquisaEstimuladaActivity.ID_NAO_SEI) return "Não sei";

        // Busca na lista de candidatos que definido na PesquisaEstimuladaActivity
        // Se tiver um Repositorio/lista global, usaria aqui. Caso contrário:
        switch (id) {
            case 1: return "Jorge Amado";
            case 2: return "Candidato 2";
            case 3: return "Candidato 3";
            default: return "ID " + id;
        }
    }

    private String formatarGps(double lat, double lon) {
        if (lat == 0.0 && lon == 0.0) return "não capturado";
        return String.format(Locale.getDefault(), "%.4f, %.4f", lat, lon);
    }

    private String valorOu(String valor, String fallback) {
        return (valor == null || valor.isEmpty()) ? fallback : valor;
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}