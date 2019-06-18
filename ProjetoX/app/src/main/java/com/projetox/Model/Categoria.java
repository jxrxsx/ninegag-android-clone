package com.projetox.Model;

public class Categoria {
    private Integer id;
    private String nome;

    public Categoria(){
    }

    public Categoria(String nome){
        this.nome = nome;
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
