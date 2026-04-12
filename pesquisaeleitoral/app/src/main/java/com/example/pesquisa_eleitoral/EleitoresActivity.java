package com.example.pesquisa_eleitoral;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pesquisa_eleitoral.database.AppDatabase;
import com.example.pesquisa_eleitoral.model.Entrevista;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

        // Busca todas as entrevistas do banco e resolve as cidades em background
        new Thread(() -> {
            List<Entrevista> entrevistas = AppDatabase.getInstance(this).entrevistaDao().buscarTodas();

            List<String> cidadesResolvidas = new ArrayList<>();
            for (Entrevista e : entrevistas) {
                cidadesResolvidas.add(obterCidade(e.getLatitude(), e.getLongitude()));
            }

            runOnUiThread(() -> {
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
                    container.addView(criarCard(i + 1, entrevistas.get(i), cidadesResolvidas.get(i), sdf));
                }
            });
        }).start();
    }

    private View criarCard(int numero, Entrevista e, String cidade, SimpleDateFormat sdf) {
        // Usando MaterialCardView
        MaterialCardView card = new MaterialCardView(this);
        
        // Configurações visuais do Card
        card.setRadius(dpToPx(12));
        card.setCardElevation(dpToPx(4));
        card.setUseCompatPadding(true); // Garante que a sombra apareça corretamente
        card.setStrokeWidth(1);
        card.setStrokeColor(Color.LTGRAY);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, dpToPx(8));
        card.setLayoutParams(cardParams);

        // Container interno (o conteúdo do card)
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

        content.addView(linha("Entrevistado #" + numero, true));
        content.addView(linha("Nome: " + valorOu(e.getNome(), "—"), false));
        content.addView(linha("Celular: " + valorOu(e.getCelular(), "—"), false));
        content.addView(linha("Cidade: " + cidade, false));
        content.addView(linha("Data/hora: " + sdf.format(new Date(e.getTimestamp())), false));
        content.addView(linha("Localização: " + formatarGps(e.getLatitude(), e.getLongitude()), false));

        card.addView(content);

        return card;
    }

    private String obterCidade(double lat, double lon) {
        if (lat == 0.0 && lon == 0.0) return "Desconhecida";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String localidade = addresses.get(0).getLocality();
                String subAdmin = addresses.get(0).getSubAdminArea();
                String estado = addresses.get(0).getAdminArea();

                String nomeCidade = (localidade != null) ? localidade : subAdmin;

                if (nomeCidade != null) {
                    return nomeCidade + (estado != null ? " - " + estado : "");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "Local não identificado";
    }

    private TextView linha(String texto, boolean negrito) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setTextSize(14f);
        // Usa a cor primária para o título do card
        if (negrito) {
            tv.setTypeface(null, android.graphics.Typeface.BOLD);
            // Ssando a Context corretamente para obter a cor
            int colorPrimary = com.google.android.material.color.MaterialColors.getColor(tv, com.google.android.material.R.attr.colorOnBackground);
            tv.setTextColor(colorPrimary);
            tv.setTextSize(16f);
        } else {
            // Ssando a View para obter a cor do texto padrão
            int textColor = com.google.android.material.color.MaterialColors.getColor(tv, android.R.attr.textColorPrimary);
            tv.setTextColor(textColor);
        }
        tv.setPadding(0, dpToPx(2), 0, dpToPx(2));
        return tv;
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