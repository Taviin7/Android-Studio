package com.example.pesquisa_eleitoral;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResultadoPesquisaActivity extends AppCompatActivity {

    private LinearLayout container;
    private HorizontalBarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_pesquisa);

        container = findViewById(R.id.containerResultado);
        barChart = new HorizontalBarChart(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 800); // 800px de altura
        barChart.setLayoutParams(lp);

        carregarDados();
    }

    private void carregarDados() {
        // RODAR EM BACKGROUND (Obrigatório para o Room)
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            // 1. Busca total de entrevistas (eleitores)
            int totalEntrevistas = db.entrevistaDao().buscarTodas().size();

            // 2. Busca contagem de votos (sigilosos)
            List<Voto.ContarVotos> itens = db.votoDAO().contarVotos();

            // Volta para a UI Thread para desenhar
            runOnUiThread(() -> {
                exibirResultados(totalEntrevistas, itens);
            });
        }).start();
    }

    private void exibirResultados(int totalEntrevistas, List<Voto.ContarVotos> itens) {
        container.removeAllViews();

        // Texto do Total
        TextView tvTotal = new TextView(this);
        tvTotal.setText("Total de entrevistados: " + totalEntrevistas);
        tvTotal.setTextSize(18f);
        container.addView(tvTotal);

        if (itens.isEmpty()) {
            TextView tvVazio = new TextView(this);
            tvVazio.setText("\nNenhum voto registrado.");
            container.addView(tvVazio);
            return;
        }

        // Preparar dados para o gráfico de barras
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < itens.size(); i++) {
            Voto.ContarVotos item = itens.get(i);
            String nomeCandidato = resolverCandidato(item.candidatoId);
            float pct = (totalEntrevistas > 0) ? (item.total * 100f) / totalEntrevistas : 0f;

            // Invertemos a ordem para que o primeiro da lista apareça no topo
            entries.add(new BarEntry(i, pct));
            labels.add(nomeCandidato);

            // Adiciona legenda em texto abaixo
            TextView tvLinha = new TextView(this);
            tvLinha.setText(String.format(Locale.getDefault(),
                    "%s: %d votos (%.1f%%)", nomeCandidato, item.total, pct));
            container.addView(tvLinha);
        }

        // Configurar o DataSet
        BarDataSet dataSet = new BarDataSet(entries, "Intenção de Voto (%)");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        // Formata o valor dentro da barra com %
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(), "%.1f%%", value);
            }
        });

        BarData data = new BarData(dataSet);
        barChart.setData(data);

        // Configuração do eixo X (que é o eixo vertical no HorizontalBarChart)
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.size());
        xAxis.setTextSize(12f);

        // Configuração do eixo Y (que é o eixo horizontal no HorizontalBarChart)
        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setAxisMaximum(100f); // Máximo 100%
        yAxisLeft.setDrawGridLines(true);

        barChart.getAxisRight().setEnabled(false); // Desativa o eixo da direita
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false); // Legenda já está no eixo X
        barChart.setFitBars(true); // Ajusta as barras para não ficarem cortadas
        barChart.animateY(1000); // Animação de entrada
        barChart.invalidate(); // Atualiza o gráfico

        container.addView(barChart); // Adiciona o gráfico ao container (layout)
    }

    private String resolverCandidato(int id) {
        // IDs constantes da PesquisaEstimuladaActivity
        if (id == -100) return "Branco"; // ID_BRANCO
        if (id == -200) return "Nulo";   // ID_NULO
        if (id == -300) return "Não sei";// ID_NAO_SEI
        
        switch (id) {
            case 1:
                return "Jorge Amado";
            case 2:
                return "Caio Cássio";
            case 3:
                return "Luiza Albergue";
            default:
                return "Candidato " + id;
        }
    }
}
