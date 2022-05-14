package com.example.tcc.clientes;

public class Cliente {
    private int ID;
    private String nome;
    private String endereço;
    private String contato;
    private String CPF;

    public Cliente(int ID, String nome, String CPF, String endereço, String contato) {
        this.ID = ID;
        this.nome = nome;
        this.endereço = endereço;
        this.contato = contato;
        this.CPF = CPF;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "ID=" + ID +
                ", nome='" + nome + '\'' +
                ", endereço='" + endereço + '\'' +
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

    public String getEndereço() {
        return endereço;
    }

    public void setEndereço(String endereço) {
        this.endereço = endereço;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

}
