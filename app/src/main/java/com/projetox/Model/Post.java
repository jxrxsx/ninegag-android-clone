package com.projetox.Model;

import android.widget.ImageView;

public class Post {
    private Integer id;
    private Usuario usuario;
    private Categoria categoria;
    private String titulo;
    private Double mediaVotos;
    private ImageView imagem;
    private String caminhoImagem;

    public Post(){

    }

    public Post(String titulo, ImageView imagem, String caminhoImagem) {
        this.titulo = titulo;
        this.imagem = imagem;
        this.caminhoImagem = caminhoImagem;
    }

    public Post(Usuario usuario, Categoria categoria, String titulo, ImageView imagem, String caminhoImagem) {
        this.usuario = usuario;
        this.categoria = categoria;
        this.titulo = titulo;
        this.imagem = imagem;
        this.caminhoImagem = caminhoImagem;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getMediaVotos() {
        return mediaVotos;
    }

    public void setMediaVotos(Double mediaVotos) {
        this.mediaVotos = mediaVotos;
    }

    public ImageView getImagem() {
        return imagem;
    }

    public void setImagem(ImageView imagem) {
        this.imagem = imagem;
    }

    public String getCaminhoImagem(){
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem){
        this.caminhoImagem = caminhoImagem;
    }
}

