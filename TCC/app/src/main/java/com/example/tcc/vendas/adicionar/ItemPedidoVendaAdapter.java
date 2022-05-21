package com.example.tcc.vendas.adicionar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.R;
import com.example.tcc.produtos.Produto;
import com.example.tcc.produtos.ProdutosDAO;
import com.example.tcc.vendas.itemPedido.ItemPedido;

import java.util.ArrayList;

public class ItemPedidoVendaAdapter  extends RecyclerView.Adapter<ItemPedidoVendaAdapter.ItemPedidoVendaViewHolder> {

    Context context;
    ArrayList<Produto> itemPedidos = new ArrayList<>();
    OnClickItemPedidoListener onClickItemPedidoListener;

    public ItemPedidoVendaAdapter(Context context, ArrayList<Produto> itemPedidos, OnClickItemPedidoListener onClickItemPedidoListener){
        this.context=context;
        this.itemPedidos=itemPedidos;
        this.onClickItemPedidoListener=onClickItemPedidoListener;
    }
    @NonNull
    @Override
    public ItemPedidoVendaAdapter.ItemPedidoVendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linha_item_pedido,parent,false);
        return new ItemPedidoVendaViewHolder(view,onClickItemPedidoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPedidoVendaAdapter.ItemPedidoVendaViewHolder holder, int position) {
        holder.nomeItemPedido.setText(itemPedidos.get(position).getNome());
        holder.imagemItemPedido.setImageResource(itemPedidos.get(position).getImg());
        holder.quantidadeItemPedido.setText(String.valueOf(itemPedidos.get(position).getQuantidade()));
        holder.valorItemPedido.setText(String.valueOf(itemPedidos.get(position).getPreco()));
    }

    @Override
    public int getItemCount() {
        return itemPedidos.size();
    }

    public class ItemPedidoVendaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nomeItemPedido, quantidadeItemPedido, valorItemPedido;
        public ImageView imagemItemPedido;
        OnClickItemPedidoListener onClickItemPedidoListener;

        public ItemPedidoVendaViewHolder(@NonNull View itemView, OnClickItemPedidoListener onClickItemPedidoListener) {
            super(itemView);
            this.onClickItemPedidoListener = onClickItemPedidoListener;
            nomeItemPedido = itemView.findViewById(R.id.nome_item_pedido);
            quantidadeItemPedido = itemView.findViewById(R.id.quantidade_item_pedido);
            valorItemPedido = itemView.findViewById(R.id.preco_item_pedido);
            imagemItemPedido = itemView.findViewById(R.id.imagem_item_pedido);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickItemPedidoListener.onClickItemPedido(getAdapterPosition());
        }
    }
    interface OnClickItemPedidoListener{
        void onClickItemPedido(int position);
    }
}
