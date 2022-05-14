package com.example.tcc.vendas.itemPedido;

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

import java.util.ArrayList;

public class ItemPedidoAdapter extends RecyclerView.Adapter<ItemPedidoAdapter.ItemPedidoViewHolder> {

    private ArrayList<ItemPedido> itemPedidoArrayList = new ArrayList<>();
    Context context;

    public ItemPedidoAdapter(Context ct, ArrayList<ItemPedido> itemPedidoArrayList){
        this.context=ct;
        this.itemPedidoArrayList=itemPedidoArrayList;
    }

    @NonNull
    @Override
    public ItemPedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linha_item_pedido,parent,false);
        return new ItemPedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPedidoAdapter.ItemPedidoViewHolder holder, int position) {
        ProdutosDAO produtosDAO = new ProdutosDAO(context);

        Produto p = produtosDAO.readOneProdutoID(itemPedidoArrayList.get(position).getProdutoID());

        holder.valorItemproduto.setText(String.valueOf(itemPedidoArrayList.get(position).getPreco()));
        holder.quantidadeItemProduto.setText(String.valueOf(itemPedidoArrayList.get(position).getQuantidade()));
        holder.nomeItemProduto.setText(p.getNome());
        holder.imagemItemProduto.setImageResource(p.getImg());

    }

    @Override
    public int getItemCount() {return itemPedidoArrayList.size();}

    public static class ItemPedidoViewHolder extends RecyclerView.ViewHolder{

        TextView nomeItemProduto, quantidadeItemProduto, valorItemproduto;
        ImageView imagemItemProduto;

        public ItemPedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeItemProduto = itemView.findViewById(R.id.nome_item_pedido);
            quantidadeItemProduto = itemView.findViewById(R.id.quantidade_item_pedido);
            valorItemproduto = itemView.findViewById(R.id.preco_item_pedido);
            imagemItemProduto = itemView.findViewById(R.id.imagem_item_pedido);
        }
    }
}
