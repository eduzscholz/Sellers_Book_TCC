package com.example.tcc.vendas;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.Pagamento;
import com.example.tcc.R;
import com.example.tcc.vendas.itemPedido.ItemPedido;
import com.example.tcc.vendas.itemPedido.ItemPedidoAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VendasAdapter extends RecyclerView.Adapter<VendasAdapter.VendasViewHolder> {

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    private ArrayList<Venda> vendaArrayList;
    private ArrayList<ItemPedido> itemPedidoArrayList = new ArrayList<>();
    private Pagamento pagamento;

    Context context;

    public VendasAdapter(Context context, ArrayList<Venda> vendaArrayList, Pagamento pagamento) {
        this.context = context;
        this.vendaArrayList = vendaArrayList;
        this.pagamento = pagamento;
    }

    @NonNull
    @Override
    public VendasAdapter.VendasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linha_vendas,parent,false);
        return new VendasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendasAdapter.VendasViewHolder holder, int position) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.vendasRV.getContext());
        VendasDAO vendasDAO = new VendasDAO(context);
        itemPedidoArrayList = vendaArrayList.get(position).getItemPedidoArrayList();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        holder.idVenda.setText(String.valueOf(vendaArrayList.get(position).getIDVenda()));
        holder.pagamento.setText("R$ "+decimalFormat.format(vendaArrayList.get(position).getValorTotal()));
        holder.dataCompra.setText(simpleDateFormat.format(vendaArrayList.get(position).getDataCompra()));
        holder.dataPrevista.setText(simpleDateFormat.format(vendaArrayList.get(position).getPrevisao()));
        Date date = vendaArrayList.get(position).getDataPagamento();
        if (date==null){
            holder.dataPagamento.setText("Falta Pagamento");
            holder.pagamento.setChecked(false);
        }else{
            holder.dataPagamento.setText(simpleDateFormat.format(date));
            holder.pagamento.setChecked(true);
            holder.btnPagar.setEnabled(false);
        }
        holder.nomeCliente.setText(vendaArrayList.get(position).getNomeCliente());

        ItemPedidoAdapter mAdapter = new ItemPedidoAdapter(context, itemPedidoArrayList);

        holder.vendasRV.setLayoutManager(layoutManager);
        holder.vendasRV.setAdapter(mAdapter);
        holder.vendasRV.setRecycledViewPool(viewPool);

        holder.btnRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
                dialog .setTitle("Deseja apagar essa venda?")
                        .setNegativeButton("Não",null)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int id = vendaArrayList.get(holder.getAdapterPosition()).getIDVenda();
                                if(vendasDAO.deleteOneVenda(id)){
                                    holder.cvVenda.findViewById(R.id.detalhes_venda).setVisibility(View.GONE);
                                    vendaArrayList.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
                                }else{
                                    Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG);
                                }
                            }
                        });
                dialog.show();
            }
        });

        holder.btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
                dialog .setTitle("Este cliente pagou?")
                        .setNegativeButton("Não",null)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                                Date date = new Date();
                                if(vendasDAO.updateVenda(vendaArrayList.get(holder.getAdapterPosition()).getIDVenda(),new String[] {VendasDAO.COL_DATA_PAGAMENTO},new String[] {simpleDateFormat.format(date)})){
                                    vendaArrayList = vendasDAO.readAllVenda();
                                    holder.btnPagar.setEnabled(false);
                                    notifyDataSetChanged();
                                    pagamento.atualizaViewPager(2);
                                }else{
                                    Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG);
                                }
                            }
                        });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return vendaArrayList.size();
    }

    public static class VendasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView nomeCliente,  dataPrevista, dataPagamento, dataCompra, idVenda;
        private final CheckBox pagamento;
        private final RecyclerView vendasRV;
        private Button btnRemover, btnPagar;
        private CardView cvVenda;

        public VendasViewHolder(@NonNull View itemView) {
            super(itemView);
            pagamento = itemView.findViewById(R.id.valor_esta_pago_venda);
            nomeCliente = itemView.findViewById(R.id.nome_cliente_vendas);
            dataCompra = itemView.findViewById(R.id.data_compra_venda);
            dataPrevista = itemView.findViewById(R.id.data_previsao_venda);
            dataPagamento = itemView.findViewById(R.id.data_pagamento_venda);
            vendasRV = itemView.findViewById(R.id.produtos_venda);
            vendasRV.setHasFixedSize(true);
            idVenda = itemView.findViewById(R.id.id_venda);
            btnPagar = itemView.findViewById(R.id.pago_venda);
            btnRemover = itemView.findViewById(R.id.remover_venda);
            cvVenda = itemView.findViewById(R.id.cv_venda_detalhes);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.findViewById(R.id.detalhes_venda).getVisibility()==View.VISIBLE){
                view.findViewById(R.id.detalhes_venda).setVisibility(View.GONE);
            }else{
                view.findViewById(R.id.detalhes_venda).setVisibility(View.VISIBLE);
            }
        }
    }
}
