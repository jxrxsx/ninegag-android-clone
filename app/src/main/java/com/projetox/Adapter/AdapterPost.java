package com.projetox.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.projetox.R;

import com.projetox.Model.Post;
import com.projetox.Model.Post;
import com.projetox.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyViewHolder> {

    private List<Post> listaPosts;
    private String TAG = "<<< AdapterPost >>>";


    public AdapterPost(List<Post> listaPosts) {
        this.listaPosts = new ArrayList<Post>();
        this.listaPosts = listaPosts;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Este método cria a View para serem exibidos os elementos

        // Converte o layout XML para uma View
        View listaItens = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_post, viewGroup,false);

        return new MyViewHolder(listaItens);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        // Este método atualiza a visualização e mostra os elementos
        // i representa cada posição no RecyclerView
        // Como setamos a quantidade de elementos para lista.size()
        // i vai de 0 a lista.size


        //transformar em bitmap
        //Drawable dr = listaPosts.get(i).getImagem().getDrawable();
        //Bitmap bmp =  ((BitmapDrawable) dr.getCurrent()).getBitmap();
        myViewHolder.imagem.setImageDrawable(listaPosts.get(i).getImagem().getDrawable());
        myViewHolder.user.setText(listaPosts.get(i).getUsuario().getNome());
        myViewHolder.titulo.setText(listaPosts.get(i).getTitulo());
        myViewHolder.categoria.setText(listaPosts.get(i).getCategoria().getNome());
        //myViewHolder.mediaVotos.setText(String.valueOf(listaPosts.get(i).getMediaVotos()));
        //myViewHolder.rbEstrelas.setRating(listaPosts.get(i).getMediaVotos().floatValue());

        int mediaVotos = 4;
        myViewHolder.mediaVotos.setText(String.valueOf(mediaVotos));
        myViewHolder.rbEstrelas.setRating(3);


        myViewHolder.rbEstrelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        myViewHolder.rbEstrelas.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
    }

    @Override
    public int getItemCount() {
        // Retorna a quantidade de itens que serão exibidos
        return listaPosts.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        //cria elementos gráficos que estarão no modelo
        TextView user, titulo, mediaVotos, categoria;
        ImageView imagem;
        RatingBar rbEstrelas;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //linka os elementos do layout aos atributos da classe
            user = itemView.findViewById(R.id.tvUser);
            categoria = itemView.findViewById(R.id.tvCategoria);
            titulo = itemView.findViewById(R.id.tvTitulo);
            imagem = itemView.findViewById(R.id.ivImagem);
            rbEstrelas = itemView.findViewById(R.id.rbEstrelas);
            mediaVotos = itemView.findViewById(R.id.tvMediaVotos);

        }
    }
}

