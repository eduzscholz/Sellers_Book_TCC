package com.example.tcc.vendas.adicionar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.R;
import com.example.tcc.clientes.Cliente;

import java.util.ArrayList;

public class ClienteVendaAdapter extends RecyclerView.Adapter<ClienteVendaAdapter.ClienteVendaViewHolder> {

    ArrayList<Cliente> clientes;
    OnClickClienteListener onClickClienteListener;
    Context context;

    public ClienteVendaAdapter(Context context, ArrayList<Cliente> clientes, OnClickClienteListener onClickClienteListener){
        this.clientes = clientes;
        this.context = context;
        this.onClickClienteListener = onClickClienteListener;
    }

    @NonNull
    @Override
    public ClienteVendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linha_cliente_venda,parent,false);
        return new ClienteVendaAdapter.ClienteVendaViewHolder(view,onClickClienteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteVendaViewHolder holder, int position) {
        holder.nomeCliente.setText(clientes.get(position).getNome());
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public class ClienteVendaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nomeCliente;
        OnClickClienteListener onClickClienteListener;

        public ClienteVendaViewHolder(@NonNull View itemView, OnClickClienteListener onClickClienteListener) {
            super(itemView);
            this.onClickClienteListener = onClickClienteListener;
            nomeCliente = itemView.findViewById(R.id.nome_cliente_add_venda);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {onClickClienteListener.onClickCliente(getAdapterPosition());}
    }

    public interface OnClickClienteListener{
        void onClickCliente(int position);
    }
}
