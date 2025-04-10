package com.example.tcc.vendas.itemPedido;

import androidx.annotation.NonNull;

public class ItemPedido {
    private int itemPedidoID;
    private int quantidade;
    private double preco;
    private int produtoID;
    private int vendaID;
    private String nomeProduto;

    public ItemPedido(int itemPedidoID, int quantidade, double preco, int produtoID, int vendaID, String nomeProduto) {
        this.itemPedidoID = itemPedidoID;
        this.quantidade = quantidade;
        this.preco = preco;
        this.produtoID = produtoID;
        this.vendaID = vendaID;
        this.nomeProduto = nomeProduto;
    }

    @NonNull
    @Override
    public String toString() {
        return "ItemPedido{" +
                "itemPedidoID=" + itemPedidoID +
                ", quantidade=" + quantidade +
                ", preco=" + preco +
                ", produtoID=" + produtoID +
                ", vendaID=" + vendaID +
                '}';
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getItemPedidoID() {
        return itemPedidoID;
    }

    public void setItemPedidoID(int itemPedidoID) {
        this.itemPedidoID = itemPedidoID;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getProdutoID() {
        return produtoID;
    }

    public void setProdutoID(int produtoID) {
        this.produtoID = produtoID;
    }

    public int getVendaID() {
        return vendaID;
    }

    public void setVendaID(int vendaID) {
        this.vendaID = vendaID;
    }
}