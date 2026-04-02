package com.example.pesquisa_eleitoral;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ResultadoPesquisaActivity extends AppCompatActivity {

    // Cores do gráfico
    private static final int[] CORES = {
            Color.parseColor("#2196F3"), // azul
            Color.parseColor("#F44336"), // vermelho
            Color.parseColor("#4CAF50"), // verde
            Color.parseColor("#FF9800"), // laranja
            Color.parseColor("#9C27B0"), // roxo
            Color.parseColor("#009688"), // teal
            Color.parseColor("#795548"), // marrom  (Nulo)
            Color.parseColor("#9E9E9E"), // cinza   (Branco)
            Color.parseColor("#607D8B"), // azul-cinza (Não sei)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resultado_pesquisa);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout container = findViewById(R.id.containerResultado);

        List<Entrevista> entrevistas = ListaEntrevistas.getInstance().getAll();
        int total = entrevistas.size();

        // Total de entrevistados
        TextView tvTotal = new TextView(this);
        tvTotal.setText("Quant. de pessoas entrevistadas: " + total);
        tvTotal.setTextSize(15f);
        tvTotal.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTotal.setPadding(0, 0, 0, dpToPx(16));
        container.addView(tvTotal);

        if (total == 0) {
            TextView tvVazio = new TextView(this);
            tvVazio.setText("Nenhuma entrevista registrada ainda.");
            tvVazio.setTextSize(14f);
            tvVazio.setGravity(Gravity.CENTER);
            container.addView(tvVazio);
            return;
        }

        // Contagem de votos por candidato
        Map<Integer, Integer> contagem = new HashMap<>();
        for (Entrevista e : entrevistas) {
            int id = e.getCandidatoId();
            contagem.put(id, contagem.getOrDefault(id, 0) + 1);
        }

        // Monta lista ordenada por votos
        List<int[]> itens = new ArrayList<>(); // [candidatoId, votos]
        for (Map.Entry<Integer, Integer> entry : contagem.entrySet()) {
            itens.add(new int[]{entry.getKey(), entry.getValue()});
        }
        itens.sort((a, b) -> b[1] - a[1]); // mais votado primeiro

        // Título votos
        TextView tvTitulo = new TextView(this);
        tvTitulo.setText("Quant. de votos para cada candidato:");
        tvTitulo.setTextSize(14f);
        tvTitulo.setPadding(0, 0, 0, dpToPx(8));
        container.addView(tvTitulo);

        // Linhas de percentual
        List<String> labels = new ArrayList<>();
        List<Float> percentuais = new ArrayList<>();
        List<Integer> cores = new ArrayList<>();
        int corIndex = 0;

        for (int[] item : itens) {
            int id    = item[0];
            int votos = item[1];
            float pct = (votos * 100f) / total;

            String label = resolverCandidato(id);
            labels.add(label);
            percentuais.add(pct);
            cores.add(CORES[corIndex % CORES.length]);

            TextView tvLinha = new TextView(this);
            tvLinha.setText(String.format(Locale.getDefault(),
                    "%s — %d voto(s) — %.1f%%", label, votos, pct));
            tvLinha.setTextSize(13f);
            tvLinha.setPadding(0, dpToPx(2), 0, dpToPx(2));
            container.addView(tvLinha);

            corIndex++;
        }

        // Gráfico de pizza
        TextView tvGrafico = new TextView(this);
        tvGrafico.setText("\nVotos");
        tvGrafico.setTextSize(14f);
        tvGrafico.setTypeface(null, android.graphics.Typeface.BOLD);
        container.addView(tvGrafico);

        PizzaView pizza = new PizzaView(this, labels, percentuais, cores);
        LinearLayout.LayoutParams pizzaParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(280));
        pizzaParams.setMargins(0, dpToPx(8), 0, dpToPx(16));
        pizza.setLayoutParams(pizzaParams);
        container.addView(pizza);

        // Legenda
        for (int i = 0; i < labels.size(); i++) {
            container.addView(criarLegenda(labels.get(i), cores.get(i)));
        }
    }

    static class PizzaView extends View {
        private final List<String> labels;
        private final List<Float> percentuais;
        private final List<Integer> cores;
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        PizzaView(Context ctx, List<String> labels, List<Float> pcts, List<Integer> cores) {
            super(ctx);
            this.labels      = labels;
            this.percentuais = pcts;
            this.cores       = cores;
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(32f);
            textPaint.setTextAlign(Paint.Align.CENTER);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int w = getWidth(), h = getHeight();
            float raio  = Math.min(w, h) * 0.42f;
            float cx    = w * 0.5f;
            float cy    = h * 0.5f;
            RectF oval  = new RectF(cx - raio, cy - raio, cx + raio, cy + raio);

            float angulo = -90f; // começa do topo
            for (int i = 0; i < percentuais.size(); i++) {
                float sweep = percentuais.get(i) * 3.6f; // % → graus
                paint.setColor(cores.get(i));
                canvas.drawArc(oval, angulo, sweep, true, paint);

                // Percentual dentro da fatia (só se fatia >= 5%)
                if (percentuais.get(i) >= 5f) {
                    double mid = Math.toRadians(angulo + sweep / 2f);
                    float tx = cx + (float)(raio * 0.65 * Math.cos(mid));
                    float ty = cy + (float)(raio * 0.65 * Math.sin(mid))
                            + textPaint.getTextSize() / 3f;
                    canvas.drawText(String.format(Locale.getDefault(),
                            "%.0f%%", percentuais.get(i)), tx, ty, textPaint);
                }
                angulo += sweep;
            }
        }
    }

    // Legenda colorida
    private LinearLayout criarLegenda(String label, int cor) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        rp.setMargins(0, dpToPx(4), 0, 0);
        row.setLayoutParams(rp);

        // Quadrado colorido
        View quadrado = new View(this);
        LinearLayout.LayoutParams qp = new LinearLayout.LayoutParams(dpToPx(14), dpToPx(14));
        qp.setMargins(0, 0, dpToPx(6), 0);
        quadrado.setLayoutParams(qp);
        quadrado.setBackgroundColor(cor);
        row.addView(quadrado);

        TextView tv = new TextView(this);
        tv.setText(label);
        tv.setTextSize(12f);
        row.addView(tv);

        return row;
    }

    // Helpers
    private String resolverCandidato(int id) {
        if (id == PesquisaEstimuladaActivity.ID_BRANCO)  return "Branco";
        if (id == PesquisaEstimuladaActivity.ID_NULO)    return "Nulo";
        if (id == PesquisaEstimuladaActivity.ID_NAO_SEI) return "Não sei";
        switch (id) {
            case 1: return "Jorge Amado";
            case 2: return "Caio Cássio";
            case 3: return "Luiza Albergue";
            default: return "ID " + id;
        }
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}