package com.projetox.Model;

public class Categoria {
    private Integer id;
    private String nome;

    public Categoria(){
    }

    public Categoria(Integer id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public Categoria(String nome){
        this.nome = nome;
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
}
