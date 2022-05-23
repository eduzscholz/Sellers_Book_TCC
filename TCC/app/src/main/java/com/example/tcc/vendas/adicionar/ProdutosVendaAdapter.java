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
import java.util.ArrayList;

public class ProdutosVendaAdapter extends RecyclerView.Adapter<ProdutosVendaAdapter.ProdutoVendaViewHolder> {

    ArrayList<Produto> produtos;
    Context context;
    OnClickProdutoListener onClickLinhaListener;

    public ProdutosVendaAdapter(Context context, ArrayList<Produto> produtos, OnClickProdutoListener onClickLinhaListener){
        this.produtos = produtos;
        this.context = context;
        this.onClickLinhaListener = onClickLinhaListener;
    }

    @NonNull
    @Override
    public ProdutosVendaAdapter.ProdutoVendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linha_item_pedido,parent,false);
        return new ProdutoVendaViewHolder(view,onClickLinhaListener);
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutosVendaAdapter.ProdutoVendaViewHolder holder, int position) {
        ByteArrayInputStream imagemStream = new ByteArrayInputStream(produtos.get(position).getImg());
        Bitmap bitmap = BitmapFactory.decodeStream(imagemStream);

        holder.nomeItemPedido.setText(produtos.get(position).getNome());
        holder.imagemItemPedido.setImageBitmap(bitmap);
        holder.quantidadeItemPedido.setText(String.valueOf(produtos.get(position).getQuantidade()));
        holder.valorItemPedido.setText(String.valueOf(produtos.get(position).getPreco()));
    }

    public static class ProdutoVendaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView nomeItemPedido, quantidadeItemPedido, valorItemPedido;
        public ImageView imagemItemPedido;
        OnClickProdutoListener onClickProdutoListener;

        public ProdutoVendaViewHolder(@NonNull View itemView, OnClickProdutoListener onClickProdutoListener) {
            super(itemView);
            this.onClickProdutoListener = onClickProdutoListener;
            nomeItemPedido = itemView.findViewById(R.id.nome_item_pedido);
            quantidadeItemPedido = itemView.findViewById(R.id.quantidade_item_pedido);
            valorItemPedido = itemView.findViewById(R.id.preco_item_pedido);
            imagemItemPedido = itemView.findViewById(R.id.imagem_item_pedido);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickProdutoListener.onClickProduto(getAdapterPosition());
        }
    }

    public interface OnClickProdutoListener {
        void onClickProduto(int position);
    }

}
