package com.projetox.Model;

public class Usuario {
    private Integer id;
    private String nome;
    private String user;
    private String email;
    private String senha;
    private Integer ehAdmin;    //0 -> false e 1 -> true

    public Usuario(){

    }

    public Usuario(Integer id, String nome, String user, String email, String senha, Integer ehAdmin){
        this.nome = nome;
        this.user = user;
        this.email = email;
        this.senha = senha;
        this.ehAdmin = ehAdmin;
    }

    public Usuario(String nome, String user, String email, String senha, Integer ehAdmin){
        this.nome = nome;
        this.user = user;
        this.email = email;
        this.senha = senha;
        this.ehAdmin = ehAdmin;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEhAdmin() {
        return ehAdmin;
    }

    public void setEhAdmin(Integer ehAdmin) {
        this.ehAdmin = ehAdmin;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
