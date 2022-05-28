package com.example.tcc.clientes;

import androidx.annotation.NonNull;

public class Cliente {
    private int ID;
    private String nome;
    private String endereco;
    private String contato;
    private String CPF;
    private boolean aberto;

    public Cliente(int ID, String nome, String CPF, String endereco, String contato) {
        this.ID = ID;
        this.nome = nome;
        this.endereco = endereco;
        this.contato = contato;
        this.CPF = CPF;
        this.aberto = false;
    }

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    @NonNull
    @Override
    public String toString() {
        return "Cliente{" +
                "ID=" + ID +
                ", nome='" + nome + '\'' +
                ", endereço='" + endereco + '\'' +
                ", contato='" + contato + '\'' +
                ", CPF='" + CPF + '\'' +
                '}';
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

}
