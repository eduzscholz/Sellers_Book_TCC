package com.example.tcc.vendas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tcc.R;
import com.example.tcc.vendas.itemPedido.ItemPedido;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VendasFragment extends Fragment {

    //VARIAVEIS NECESSARIAS PARA O FUNCIONAMENTO DO RECYCLERVIEW
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Venda>  vendaArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //COMPACTA E PEGA O FRAGMENT_VENDAS.XML
        View view = inflater.inflate(R.layout.fragment_vendas, container, false);

        //ACHA O RECYCLER VIEW
        recyclerView = view.findViewById(R.id.lista_vendas);
        //RECOMENDADO PELO GOOGLE
        recyclerView.setHasFixedSize(true);
        //INICIALIZA E SETA O LAYOUT MANAGER
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //CRIA UM PRODUTO E ADICIONA NA LISTA PARA TESTE
        Date dataPagamento = new Date();
        Date dataCompra = new Date();
        Date previsao = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-DD");
        try {
            dataPagamento = simpleDateFormat.parse("2022-04-29");
            dataCompra = simpleDateFormat.parse("2022-04-29");
            previsao = simpleDateFormat.parse("2022-04-29");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<ItemPedido> pedidos = new ArrayList<>();
        pedidos.add(new ItemPedido(1,1,200,5,1));
        Venda venda = new Venda(1,dataCompra,dataPagamento,previsao,1,pedidos);
        vendaArrayList.add(venda);

        //CRIA E SETA O ADAPTER
        mAdapter = new VendasAdapter(this.getContext(),vendaArrayList);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}