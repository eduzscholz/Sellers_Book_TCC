package com.example.tcc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tcc.vendas.Venda;
import com.example.tcc.vendas.VendasDAO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragInicio extends Fragment {

    VendasDAO vendasDAO;
    ArrayList<Venda> vendaArrayList;
    TextView lucroDoMes, lucroPrevisto, recebimentoAtrasado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        preencheTV(view);

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