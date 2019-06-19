package com.projetox.Model;

public class InteracaoUsuarioPost {
    private Integer id;
    private Integer idPost;
    private Integer idUsuario;
    private Integer qtdEstrelas;
    private String comentario;

    public InteracaoUsuarioPost(){

    }

    public InteracaoUsuarioPost(Integer idPost, Integer idUsuario, Integer qtdEstrelas, String comentario) {
        this.idPost = idPost;
        this.idUsuario = idUsuario;
        this.qtdEstrelas = qtdEstrelas;
        this.comentario = comentario;
    }

    public Integer getId(){
        return this.id;
    }

    public Integer getIdPost() {
        return idPost;
    }

    public void setIdPost(Integer idPost) {
        this.idPost = idPost;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
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
