package com.example.lddm;

import java.net.URL;

public class Pessoa {
    private String nome;
    private String telefone;
    private String email;
    private URL facebook;
    private URL instagram;

    public Pessoa() {
        this.nome = "";
        this.telefone = "";
        this.email = "";
    }

    public Pessoa(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public URL getFacebook() {
        return facebook;
    }

    public void setFacebook(URL facebook) {
        this.facebook = facebook;
    }

    public URL getInstagram() {
        return instagram;
    }

    public void setInstagram(URL instagram) {
        this.instagram = instagram;
    }
}
