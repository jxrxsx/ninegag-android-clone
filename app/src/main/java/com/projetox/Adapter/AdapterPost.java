package com.projetox.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.projetox.RecyclerItemClickListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyViewHolder> {

    private ArrayList<Post> listaPosts;
    private String TAG = "<<< AdapterPost >>>";
    private ImageView imagem;


    public AdapterPost(ArrayList<Post> listaPosts) {
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

        myViewHolder.imagem.setImageDrawable((loadImageFromStorage(listaPosts.get(i).getCaminhoImagem(), myViewHolder.imagem, listaPosts.get(i).getNomeImagem())));
        myViewHolder.user.setText(listaPosts.get(i).getUsuario().getUser());
        myViewHolder.titulo.setText(listaPosts.get(i).getTitulo());
        myViewHolder.categoria.setText(listaPosts.get(i).getCategoria().getNome());
        myViewHolder.mediaVotos.setText(String.valueOf(listaPosts.get(i).getMediaVotos()));
        myViewHolder.rbEstrelas.setRating(listaPosts.get(i).getMediaVotos().floatValue());

        final int indiceEstrela = i;
        myViewHolder.rbEstrelas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "indice estrela: ");
                return true;
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

    private Drawable loadImageFromStorage(String path, ImageView imagemSalva, String nomeImagem)
    {
        try {
            File f = new File(path, nomeImagem);
            Log.d(TAG, "caminho da pasta: "+ path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            imagemSalva.setImageBitmap(b);

            return imagemSalva.getDrawable();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return imagemSalva.getDrawable();

    }
}

