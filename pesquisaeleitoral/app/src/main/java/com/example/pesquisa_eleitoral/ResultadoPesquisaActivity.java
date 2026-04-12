package com.example.pesquisa_eleitoral;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pesquisa_eleitoral.database.AppDatabase;
import com.example.pesquisa_eleitoral.model.Candidato;
import com.example.pesquisa_eleitoral.model.ProblemaRelatado;
import com.example.pesquisa_eleitoral.model.Voto;
import com.example.pesquisa_eleitoral.model.VotoEspontaneo;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
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
        // 1. Habilita o modo EdgeToEdge
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resultado_pesquisa);

        // 2. Configura os Insets para respeitar a barra de status
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        container = findViewById(R.id.containerResultado);
        carregarDados();
    }

    private void carregarDados() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            int totalEntrevistas = db.entrevistaDao().buscarTodas().size();
            List<Voto.ContarVotos> estimulados = db.votoDAO().contarVotos();
            List<VotoEspontaneo.ContarVotoEspontaneo> espontaneos = db.votoEspontaneoDAO().contar();
            List<ProblemaRelatado.ContarProblemas> problemas = db.problemaDao().contar();
            List<Candidato> listaCandidatos = db.candidatoDao().buscarTodos();

            runOnUiThread(() -> {
                exibirResultados(totalEntrevistas, estimulados, espontaneos, problemas, listaCandidatos);
            });
        }).start();
    }

    private void exibirResultados(int total,
                                  List<Voto.ContarVotos> estimulados,
                                  List<VotoEspontaneo.ContarVotoEspontaneo> espontaneos,
                                  List<ProblemaRelatado.ContarProblemas> problemas,
                                  List<Candidato> listaCandidatos) {
        container.removeAllViews();

        adicionarTitulo("RELATÓRIO DE PESQUISA ELEITORAL", 22, Color.BLACK);
        adicionarSubtitulo("Total de entrevistados: " + total);

        // --- SEÇÃO ESTIMULADA ---
        adicionarTitulo("1. PESQUISA ESTIMULADA", 18, Color.BLUE);
        if (estimulados.isEmpty()) {
            adicionarTexto("Sem dados.");
        } else {
            HorizontalBarChart chartEstimulado = criarGraficoEstimulado(total, estimulados, listaCandidatos);
            container.addView(chartEstimulado);
        }

        // --- SEÇÃO ESPONTÂNEA ---
        adicionarTitulo("2. PESQUISA ESPONTÂNEA", 18, Color.parseColor("#388E3C"));
        if (espontaneos.isEmpty()) {
            adicionarTexto("Sem dados.");
        } else {
            for (VotoEspontaneo.ContarVotoEspontaneo item : espontaneos) {
                float pct = (total > 0) ? (item.total * 100f) / total : 0f;
                adicionarTexto(String.format(Locale.getDefault(), "• %s: %d votos (%.1f%%)", item.resposta, item.total, pct));
            }
        }

        // --- SEÇÃO PROBLEMAS ---
        adicionarTitulo("3. MAIORES PROBLEMAS DA CIDADE", 18, Color.RED);
        if (problemas.isEmpty()) {
            adicionarTexto("Sem problemas relatados.");
        } else {
            for (ProblemaRelatado.ContarProblemas p : problemas) {
                float pct = (total > 0) ? (p.total * 100f) / total : 0f;
                adicionarTexto(String.format(Locale.getDefault(), "• %s: %d citações (%.1f%%)", p.descricao, p.total, pct));
            }
        }
    }

    private HorizontalBarChart criarGraficoEstimulado(int total, List<Voto.ContarVotos> itens, List<Candidato> listaCandidatos) {
        HorizontalBarChart barChart = new HorizontalBarChart(this);
        barChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 600));

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < itens.size(); i++) {
            Voto.ContarVotos item = itens.get(i);
            float pct = (total > 0) ? (item.total * 100f) / total : 0f;
            entries.add(new BarEntry(i, pct));
            labels.add(resolverCandidato(item.candidatoId, listaCandidatos));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Intenção (%)");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(11f);
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

    private void adicionarTitulo(String texto, int tamanho, int cor) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setTextSize(tamanho);
        tv.setTextColor(cor);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setPadding(0, 40, 0, 16);
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
        tv.setPadding(40, 4, 40, 4);
        container.addView(tv);
    }

    private String resolverCandidato(int id, List<Candidato> lista) {
        if (id == -100) return "Branco";
        if (id == -200) return "Nulo";
        if (id == -300) return "Não sei";

        for (Candidato c : lista) {
            if (c.getId() == id) return c.getNome();
        }
        return "Candidato " + id;
    }
}
