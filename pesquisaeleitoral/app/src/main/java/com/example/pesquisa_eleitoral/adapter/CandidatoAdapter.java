package com.example.pesquisa_eleitoral.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pesquisa_eleitoral.R;
import com.example.pesquisa_eleitoral.model.Candidato;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

/**
 * CandidatoAdapter - Gerencia a exibição da lista de candidatos na RecyclerView.
 *
 * CONCEITO: Funciona como uma ponte entre a lista de objetos 'Candidato' e a interface visual.
 * FUNCIONAMENTO: Ele infla o layout de cada item, preenche os dados (nome, partido, foto)
 * e controla a lógica de 'seleção única', permitindo que o usuário escolha apenas um
 * candidato por vez através de cliques nos cards.
 */
public class CandidatoAdapter extends RecyclerView.Adapter<CandidatoAdapter.CandidatoViewHolder> {

    private List<Candidato> candidatos;
    private int selectedPosition = -1; // Armazena o índice do candidato selecionado atualmente
    private OnCandidatoSelectedListener listener;

    // Interface para comunicar a seleção do candidato para a Activity
    public interface OnCandidatoSelectedListener {
        void onSelected(int candidatoId);
    }

    public CandidatoAdapter(List<Candidato> candidatos, OnCandidatoSelectedListener listener) {
        this.candidatos = candidatos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CandidatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout XML de cada item da lista (card do candidato)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_candidato, parent, false);
        return new CandidatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidatoViewHolder holder, int position) {
        Candidato c = candidatos.get(position);

        // Preenche os textos de Nome e Partido
        holder.txtNome.setText(c.getNome());
        holder.txtPartido.setText(c.getPartido());

        // Lógica de carregamento de fotos: busca o ID da imagem na pasta 'drawable' pelo nome salvo no banco
        if (c.getFoto() != null && !c.getFoto().isEmpty()) {
            Context ctx = holder.itemView.getContext();
            int resId = ctx.getResources().getIdentifier(c.getFoto(), "drawable", ctx.getPackageName());
            if (resId != 0) {
                holder.imgCandidato.setImageResource(resId);
            } else {
                // Caso o nome da foto não seja encontrado nos recursos
                holder.imgCandidato.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            // Ícone padrão para opções especiais (Branco/Nulo) ou candidatos sem foto
            holder.imgCandidato.setImageResource(android.R.drawable.ic_menu_help);
        }

        // Atualiza o estado visual (Radio e Card) conforme a posição selecionada
        holder.radio.setChecked(position == selectedPosition);
        holder.card.setChecked(position == selectedPosition);

        // Listener de seleção: ao clicar em qualquer parte do card, atualiza a posição e notifica a lista
        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            // Atualiza apenas os itens que mudaram de estado (o antigo e o novo selecionado) para performance
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);

            // Notifica a Activity sobre o candidato escolhido
            if (listener != null) {
                listener.onSelected(c.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return candidatos.size();
    }

    // Método auxiliar para retornar o ID do candidato que o usuário marcou
    public int getSelectedCandidatoId() {
        if (selectedPosition != -1) {
            return candidatos.get(selectedPosition).getId();
        }
        return -1; // Retorna -1 se nada foi selecionado
    }

    // ViewHolder: Classe que mapeia os componentes visuais do arquivo XML 'item_candidato'
    public static class CandidatoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtPartido;
        ImageView imgCandidato;
        RadioButton radio;
        MaterialCardView card;

        // Construtor: inicializa os componentes visuais do layout
        public CandidatoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomeCandidato);
            txtPartido = itemView.findViewById(R.id.txtPartidoCandidato);
            imgCandidato = itemView.findViewById(R.id.imgCandidato);
            radio = itemView.findViewById(R.id.radioSelecionado);
            card = itemView.findViewById(R.id.cardCandidato);
        }
    }
}