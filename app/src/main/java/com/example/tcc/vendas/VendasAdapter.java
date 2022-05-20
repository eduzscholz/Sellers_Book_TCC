package com.example.tcc.vendas;

import android.content.Context;
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

import com.example.tcc.R;
import com.example.tcc.clientes.Cliente;
import com.example.tcc.clientes.ClientesDAO;
import com.example.tcc.produtos.Produto;
import com.example.tcc.produtos.ProdutosAdapter;
import com.example.tcc.produtos.ProdutosDAO;
import com.example.tcc.vendas.itemPedido.ItemPedido;
import com.example.tcc.vendas.itemPedido.ItemPedidoAdapter;
import com.example.tcc.vendas.itemPedido.ItemPedidoDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VendasAdapter extends RecyclerView.Adapter<VendasAdapter.VendasViewHolder> {

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    private ArrayList<Venda> vendaArrayList;
    private ArrayList<ItemPedido> itemPedidoArrayList = new ArrayList<>();

    Context context;

    public VendasAdapter(Context context, ArrayList<Venda> vendaArrayList) {
        this.context = context;
        this.vendaArrayList = vendaArrayList;
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
        ClientesDAO clientesDAO = new ClientesDAO(context);
        Cliente c = clientesDAO.readOneClienteID(vendaArrayList.get(position).getClienteID());
        double valorTotal=0;
        VendasDAO vendasDAO = new VendasDAO(context);
        itemPedidoArrayList = vendaArrayList.get(position).getItemPedidoArrayList();


        for (int i=0;i<itemPedidoArrayList.size();i++) {
            valorTotal=valorTotal+(itemPedidoArrayList.get(i).getPreco()*itemPedidoArrayList.get(i).getQuantidade());
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD/MM/yyyy");

        holder.pagamento.setText(String.valueOf(valorTotal));
        holder.dataCompra.setText(simpleDateFormat.format(vendaArrayList.get(position).getDataCompra()));
        holder.dataPrevista.setText(simpleDateFormat.format(vendaArrayList.get(position).getPrevisao()));
        Date date = vendaArrayList.get(position).getDataPagamento();
        if (date==null){
            holder.dataPagamento.setText("Falta Pagamento");
        }else{
            holder.dataPagamento.setText(simpleDateFormat.format(date));
        }
        holder.nomeCliente.setText(c.getNome());
        holder.pagamento.setChecked(date!=null ? true : false);

        ItemPedidoAdapter mAdapter = new ItemPedidoAdapter(context, itemPedidoArrayList);

        holder.vendasRV.setLayoutManager(layoutManager);
        holder.vendasRV.setAdapter(mAdapter);
        holder.vendasRV.setRecycledViewPool(viewPool);

        holder.btnRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = vendaArrayList.get(holder.getAdapterPosition()).getIDVenda();
                if(vendasDAO.deleteOneVenda(id)){
                    holder.cvVenda.findViewById(R.id.detalhes_venda).setVisibility(View.GONE);
                    vendaArrayList = vendasDAO.readAllVenda();
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG);
                }
            }
        });

        holder.btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-DD");
                Date date = new Date();
                if(vendasDAO.updateVenda(vendaArrayList.get(holder.getAdapterPosition()).getIDVenda(),new String[] {VendasDAO.COL_DATA_PAGAMENTO},new String[] {simpleDateFormat.format(date)})){
                    vendaArrayList = vendasDAO.readAllVenda();
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return vendaArrayList.size();
    }

    public static class VendasViewHolder extends RecyclerView.ViewHolder{

        private final TextView nomeCliente,  dataPrevista, dataPagamento, dataCompra;
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
            btnPagar = itemView.findViewById(R.id.pago_venda);
            btnRemover = itemView.findViewById(R.id.remover_venda);
            cvVenda = itemView.findViewById(R.id.cardview_vendas);
            cvVenda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.findViewById(R.id.detalhes_venda).getVisibility()==View.VISIBLE){
                        view.findViewById(R.id.detalhes_venda).setVisibility(View.GONE);
                    }else{
                        view.findViewById(R.id.detalhes_venda).setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}
