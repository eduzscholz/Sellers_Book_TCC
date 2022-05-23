package com.example.tcc.vendas;

import com.example.tcc.vendas.itemPedido.ItemPedido;

import java.util.ArrayList;
import java.util.Date;

public class Venda {
    private int IDVenda;
    private Date dataCompra, dataPagamento, previsao;
    private String nomeCliente;
    private int clienteID;
    private ArrayList<ItemPedido> itemPedidoArrayList;

    public Venda(int IDVenda, String nomeCliente, Date dataCompra, Date dataPagamento, Date previsao, int clienteID, ArrayList<ItemPedido> itemPedidoArrayList) {
        this.IDVenda = IDVenda;
        this.dataCompra = dataCompra;
        this.dataPagamento = dataPagamento;
        this.previsao = previsao;
        this.clienteID = clienteID;
        this.itemPedidoArrayList = itemPedidoArrayList;
        this.nomeCliente = nomeCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public int getIDVenda() {
        return IDVenda;
    }

    public void setIDVenda(int IDVenda) {
        this.IDVenda = IDVenda;
    }

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Date getPrevisao() {
        return previsao;
    }

    public void setPrevisao(Date previsao) {
        this.previsao = previsao;
    }

    public int getClienteID() {
        return clienteID;
    }

    public void setClienteID(int clienteID) {
        this.clienteID = clienteID;
    }

    public ArrayList<ItemPedido> getItemPedidoArrayList() {
        return itemPedidoArrayList;
    }

    public void setItemPedidoArrayList(ArrayList<ItemPedido> itemPedidoArrayList) {
        this.itemPedidoArrayList = itemPedidoArrayList;
    }
}
