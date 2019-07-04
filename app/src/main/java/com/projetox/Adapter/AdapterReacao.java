package com.projetox.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.projetox.Model.Reacao;
import com.projetox.R;
import com.projetox.SQLiteHelper.DatabaseHelper;


import java.util.ArrayList;


public class AdapterReacao extends RecyclerView.Adapter<AdapterReacao.MyViewHolder> {

    private ArrayList<Reacao> listaReacoes;
    private String TAG = "<<< AdapterReacao >>>";
    private DatabaseHelper dbHelper;

    public AdapterReacao(ArrayList<Reacao> listaReacoes) {
        this.listaReacoes = listaReacoes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Este método cria a View para serem exibidos os elementos

        // Converte o layout XML para uma View
        View listaItens = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_post_view, viewGroup, false);

        return new MyViewHolder(listaItens);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        // Este método atualiza a visualização e mostra os elementos
        // i representa cada posição no RecyclerView
        // Como setamos a quantidade de elementos para lista.size()
        // i vai de 0 a lista.size

        myViewHolder.autorComentario.setText(listaReacoes.get(i).getUsuario().getNome());
        myViewHolder.comentario.setText(listaReacoes.get(i).getComentario());
        myViewHolder.qtdEstrelasCad.setProgress(listaReacoes.get(i).getQtdEstrelas());

    }

    @Override
    public int getItemCount() {
        // Retorna a quantidade de itens que serão exibidos
        return listaReacoes.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        //cria elementos gráficos que estarão no modelo
        TextView autorComentario, comentario;
        RatingBar qtdEstrelasCad;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //linka os elementos do layout aos atributos da classe
            autorComentario = itemView.findViewById(R.id.tvAutorComentario);
            comentario = itemView.findViewById(R.id.tvComentarioCadastrado);
            qtdEstrelasCad = itemView.findViewById(R.id.rbQtdEstrelasCad);
        }
    }

}