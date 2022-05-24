package com.example.tcc.vendas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.tcc.R;
import com.example.tcc.vendas.adicionar.AdicionarVendaCliente;

import java.util.ArrayList;

public class VendasFragment extends Fragment {

    //VARIAVEIS NECESSARIAS PARA O FUNCIONAMENTO DO RECYCLERVIEW
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton adcVenda;
    private VendasAdapter.Pagamento pagamento;
    private ArrayList<Venda>  vendaArrayList = new ArrayList<>();

    public VendasFragment(VendasAdapter.Pagamento pagamento){
        super();
        this.pagamento = pagamento;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendas, container, false);
        VendasDAO vendasDAO = new VendasDAO(this.getContext());
        vendaArrayList = vendasDAO.readAllVenda();

        recyclerView = view.findViewById(R.id.lista_vendas);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new VendasAdapter(this.getContext(),vendaArrayList, pagamento);
        recyclerView.setAdapter(mAdapter);

        adcVenda = view.findViewById(R.id.adcionar_venda);
        adcVenda.setOnClickListener(view1 -> {
            Intent intent = new Intent(view1.getContext(), AdicionarVendaCliente.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}