package com.example.tcc.inicio;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.Pagamento;
import com.example.tcc.R;
import com.example.tcc.vendas.Venda;
import com.example.tcc.vendas.VendasDAO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InicioAdapter extends RecyclerView.Adapter<InicioAdapter.InicioViewHolder> {
    Context context;
    ArrayList<Venda> vendaArrayList;
    Pagamento pagamento;

    public InicioAdapter(Context context, ArrayList<Venda> vendaArrayList, Pagamento pagamento) {
        this.vendaArrayList = vendaArrayList;
        this.context = context;
        this.pagamento = pagamento;
    }

    @NonNull
    @Override
    public InicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inicio_proximos_pagamentos,parent,false);
        return new InicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InicioViewHolder holder, int position) {
        VendasDAO vendasDAO = new VendasDAO(context);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.dataCompra.setText(simpleDateFormat.format(vendaArrayList.get(position).getDataCompra()));
        holder.dataPrevista.setText(simpleDateFormat.format(vendaArrayList.get(position).getPrevisao()));
        holder.nomeCli.setText(vendaArrayList.get(position).getNomeCliente());
        holder.valorTotal.setText(decimalFormat.format(vendaArrayList.get(position).getValorTotal()));

        holder.button.setOnClickListener(view -> {
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialogo_simples);

            TextView txt = dialog.findViewById(R.id.texto);
            txt.setText("Este cliente pagou?");

            Button buttonNao = dialog.findViewById(R.id.nao);
            Button buttonSim = dialog.findViewById(R.id.sim);
            buttonNao.setOnClickListener(view1 -> dialog.cancel());
            buttonSim.setOnClickListener(view12 -> {
                        SimpleDateFormat simpleDateFormat1 =new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date();
                        if(vendasDAO.updateVenda(vendaArrayList.get(holder.getAdapterPosition()).getIDVenda(),new String[] {VendasDAO.COL_DATA_PAGAMENTO},new String[] {simpleDateFormat1.format(date)})){
                            vendaArrayList = vendasDAO.readAllVenda();
                            holder.button.setEnabled(false);
                            notifyDataSetChanged();
                            pagamento.atualizaViewPager(0);
                        }else{
                            Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }
                    });
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(vendaArrayList.size(), 2);
    }

    public static class InicioViewHolder extends RecyclerView.ViewHolder {

        private final TextView nomeCli, dataPrevista, dataCompra, valorTotal;
        private final Button button;

        public InicioViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeCli = itemView.findViewById(R.id.inicio_nome_cliente);
            dataPrevista = itemView.findViewById(R.id.inicio_data_prevista);
            dataCompra = itemView.findViewById(R.id.inicio_data_compra);
            valorTotal = itemView.findViewById(R.id.inicio_valor_total);
            button = itemView.findViewById(R.id.btn_inicio_pago);
        }
    }
}
