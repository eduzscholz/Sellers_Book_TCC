package com.example.tcc.inicio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tcc.Pagamento;
import com.example.tcc.R;
import com.example.tcc.vendas.Venda;
import com.example.tcc.vendas.VendasDAO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragInicio extends Fragment {

    VendasDAO vendasDAO;
    ArrayList<Venda> vendaArrayList, pagamentoEmAberto;
    TextView lucroDoMes, lucroPrevisto, recebimentoAtrasado;

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    Pagamento pagamento;

    public FragInicio(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        pagamentoEmAberto = new ArrayList<>();

        preencheTV(view);

        recyclerView = view.findViewById(R.id.proximos_pagamentos);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new InicioAdapter(this.getContext(), pagamentoEmAberto, pagamento);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    public void preencheTV(View view){
        Date atual = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
        vendasDAO = new VendasDAO(this.getContext());
        vendaArrayList = vendasDAO.readVendaMes(simpleDateFormat.format(atual));

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        double lucro=0, previsto=0, atrasado=0;

        lucroDoMes = view.findViewById(R.id.lucro_mes);
        lucroPrevisto = view.findViewById(R.id.lucro_previsto);
        recebimentoAtrasado = view.findViewById(R.id.recebimento_atrasado);

        for (int i=0;i<vendaArrayList.size();i++) {
            Date date = vendaArrayList.get(i).getDataPagamento();
            double valor = vendaArrayList.get(i).getValorTotal();
            if(date==null){
                atrasado+=valor;
                pagamentoEmAberto.add(vendaArrayList.get(i));
            }else{
                lucro+=valor;
            }
            previsto+=valor;
        }
        lucroDoMes.setText(decimalFormat.format(lucro));
        lucroPrevisto.setText(decimalFormat.format(previsto));
        recebimentoAtrasado.setText(decimalFormat.format(atrasado));
    }
}