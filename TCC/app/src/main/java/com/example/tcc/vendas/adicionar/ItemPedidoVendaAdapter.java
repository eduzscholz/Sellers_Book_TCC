package com.example.tcc.vendas.adicionar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.R;
import com.example.tcc.produtos.Produto;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ItemPedidoVendaAdapter  extends RecyclerView.Adapter<ItemPedidoVendaAdapter.ItemPedidoVendaViewHolder> {

    Context context;
    ArrayList<Produto> carrinho = new ArrayList<>();
    OnClickItemPedidoListener onClickItemPedidoListener;

    public ItemPedidoVendaAdapter(Context context, ArrayList<Produto> carrinho, OnClickItemPedidoListener onClickItemPedidoListener){
        this.context=context;
        this.carrinho = carrinho;
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
        try {
            ByteArrayInputStream imagemStream = new ByteArrayInputStream(carrinho.get(position).getImg());
            Bitmap bitmap = BitmapFactory.decodeStream(imagemStream);
            holder.imagemItemPedido.setImageBitmap(bitmap);
        }catch(Exception e){
            holder.imagemItemPedido.setImageResource(R.drawable.ic_baseline_image_24);
        }
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        holder.nomeItemPedido.setText(carrinho.get(position).getNome());
        holder.quantidadeItemPedido.setText(String.valueOf(carrinho.get(position).getQuantidade()));
        holder.valorItemPedido.setText(decimalFormat.format(carrinho.get(position).getPreco()));
    }

    @Override
    public int getItemCount() {
        return carrinho.size();
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
