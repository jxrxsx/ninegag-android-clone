package com.projetox.Model;

public class Reacao {
    private Integer id;
    private Post post;
    private Usuario usuario;
    private Integer qtdEstrelas;
    private String comentario;

    public Reacao(){

    }

    public Reacao(Post post, Usuario usuario, Integer qtdEstrelas, String comentario) {
        this.post = post;
        this.usuario = usuario;
        this.qtdEstrelas = qtdEstrelas;
        this.comentario = comentario;
    }

    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getQtdEstrelas() {
        return qtdEstrelas;
    }

    public void setQtdEstrelas(Integer qtdEstrelas) {
        this.qtdEstrelas = qtdEstrelas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
