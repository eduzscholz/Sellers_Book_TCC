package com.example.tcc.produtos;

import androidx.annotation.NonNull;

import java.util.Arrays;

//CLASSE DOS PRODUTOS
public class Produto {
    private byte[] img;    //IMAGEM DO PRODUTO
    private String nome, marca, complemento, medida, tipoDeProduto;
    private double preco;
    private int quantidade;
    private int IDProduto;
    private boolean aberto;

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public Produto(int id, byte[] img, String nome, String marca, String complemento, String medida, double preco, int quantidade, String tipo) {
        this.img = img;
        this.nome = nome;
        this.marca = marca;
        this.complemento = complemento;
        this.medida = medida;
        this.preco = preco;
        this.quantidade = quantidade;
        this.tipoDeProduto = tipo;
        this.IDProduto = id;
        this.aberto = false;
    }

    @NonNull
    @Override
    public String toString() {
        return "Produto{" +
                "img=" + Arrays.toString(img) +
                ", nome='" + nome + '\'' +
                ", marca='" + marca + '\'' +
                ", descricao='" + complemento + '\'' +
                ", unidadeMedida='" + medida + '\'' +
                ", tipoDeProduto='" + tipoDeProduto + '\'' +
                ", preco=" + preco +
                ", quantidade=" + quantidade +
                ", IDProduto=" + IDProduto +
                '}';
    }

    //GETs E SETs

    public String getTipoDeProduto() {
        return tipoDeProduto;
    }

    public void setTipoDeProduto(String tipoDeProduto) {
        this.tipoDeProduto = tipoDeProduto;
    }

    public int getIDProduto() {
        return IDProduto;
    }

    public void setIDProduto(int IDProduto) {
        this.IDProduto = IDProduto;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String unidadeMedida) {
        this.medida = unidadeMedida;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
