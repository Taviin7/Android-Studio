package com.example.pesquisa_eleitoral;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_pesquisa);

        container = findViewById(R.id.containerResultado);
        carregarDados();
    }

    private void carregarDados() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            // 1. Dados Gerais e Estimulados
            int totalEntrevistas = db.entrevistaDao().buscarTodas().size();
            List<Voto.ContarVotos> estimulados = db.votoDAO().contarVotos();

            // 2. Dados Espontâneos
            List<VotoEspontaneo.ContarVotoEspontaneo> espontaneos = db.votoEspontaneoDAO().contar();

            runOnUiThread(() -> {
                exibirResultados(totalEntrevistas, estimulados, espontaneos);
            });
        }).start();
    }

    private void exibirResultados(int total, List<Voto.ContarVotos> estimulados, List<VotoEspontaneo.ContarVotoEspontaneo> espontaneos) {
        container.removeAllViews();

        // Cabeçalho Principal
        adicionarTitulo("RELATÓRIO DE INTENÇÃO DE VOTO", 22, Color.BLACK);
        adicionarSubtitulo("Total de entrevistados: " + total);

        // --- SEÇÃO ESTIMULADA ---
        adicionarTitulo("1. PESQUISA ESTIMULADA", 18, Color.BLUE);
        if (estimulados.isEmpty()) {
            adicionarTexto("Sem dados de pesquisa estimulada.");
        } else {
            HorizontalBarChart chartEstimulado = criarGraficoEstimulado(total, estimulados);
            container.addView(chartEstimulado);
        }

        // Espaçamento
        adicionarTexto("\n");

        // --- SEÇÃO ESPONTÂNEA ---
        adicionarTitulo("2. PESQUISA ESPONTÂNEA", 18, Color.parseColor("#388E3C")); // Verde escuro
        if (espontaneos.isEmpty()) {
            adicionarTexto("Sem dados de pesquisa espontânea.");
        } else {
            // Para a espontânea, vamos listar os textos pois podem ser muitos nomes diferentes
            for (VotoEspontaneo.ContarVotoEspontaneo item : espontaneos) {
                float pct = (total > 0) ? (item.total * 100f) / total : 0f;
                adicionarTexto(String.format(Locale.getDefault(), 
                    "• %s: %d votos (%.1f%%)", item.resposta, item.total, pct));
            }
        }
    }

    private HorizontalBarChart criarGraficoEstimulado(int total, List<Voto.ContarVotos> itens) {
        HorizontalBarChart barChart = new HorizontalBarChart(this);
        barChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 600));

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < itens.size(); i++) {
            Voto.ContarVotos item = itens.get(i);
            float pct = (total > 0) ? (item.total * 100f) / total : 0f;
            entries.add(new BarEntry(i, pct));
            labels.add(resolverCandidato(item.candidatoId));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Intenção (%)");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(), "%.1f%%", value);
            }
        });

        barChart.setData(new BarData(dataSet));
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisLeft().setAxisMaximum(100f);
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.animateY(800);
        
        return barChart;
    }

    // Métodos auxiliares de UI
    private void adicionarTitulo(String texto, int tamanho, int cor) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setTextSize(tamanho);
        tv.setTextColor(cor);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setPadding(0, 32, 0, 16);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        container.addView(tv);
    }

    private void adicionarSubtitulo(String texto) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setTextSize(16);
        tv.setPadding(0, 0, 0, 32);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        container.addView(tv);
    }

    private void adicionarTexto(String texto) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setTextSize(14);
        tv.setPadding(20, 4, 20, 4);
        container.addView(tv);
    }

    private String resolverCandidato(int id) {
        if (id == -100) return "Branco";
        if (id == -200) return "Nulo";
        if (id == -300) return "Não sei";
        switch (id) {
            case 1: return "Jorge Amado";
            case 2: return "Caio Cássio";
            case 3: return "Luiza Albergue";
            default: return "Candidato " + id;
        }
    }
}