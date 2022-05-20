package com.example.tcc.clientes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.R;

import java.util.ArrayList;

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ClientesViewHolder> {

    private ArrayList<Cliente> clienteArrayList;
    Context context;

    public ClientesAdapter(Context context, ArrayList<Cliente> clienteArrayList) {
        this.context=context;
        this.clienteArrayList = clienteArrayList;
    }

    @NonNull
    @Override
    public ClientesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linha_cliente,parent,false);
        return new ClientesAdapter.ClientesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientesViewHolder holder, int position) {
        ClientesDAO clientesDAO = new ClientesDAO(context);
        holder.nomeClienteFrente.setText(clienteArrayList.get(position).getNome());
        holder.contatoClienteFrente.setText(clienteArrayList.get(position).getContato());
        holder.enderecoClienteFrente.setText(clienteArrayList.get(position).getEndereço());
        holder.pagementoFrente.setChecked(true);
        holder.cpfClienteFrente.setText(clienteArrayList.get(position).getCPF());
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.cardViewAtras.getVisibility()!=view.VISIBLE) {
                    holder.cardViewAtras.setVisibility(view.VISIBLE);
                    holder.nomeClienteAtras.setText(clienteArrayList.get(holder.getAdapterPosition()).getNome());
                    holder.contatoClienteAtras.setText(clienteArrayList.get(holder.getAdapterPosition()).getContato());
                    holder.enderecoClienteAtras.setText(clienteArrayList.get(holder.getAdapterPosition()).getEndereço());
                    holder.cpfClienteAtras.setText(clienteArrayList.get(holder.getAdapterPosition()).getCPF());                    holder.nomeClienteAtras.setText(clienteArrayList.get(holder.getAdapterPosition()).getNome());
                    holder.pagamentoAtras.setChecked(true);
                }
            }
        });
        holder.btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.cardViewAtras.getVisibility()!=view.GONE) {
                    holder.cardViewAtras.setVisibility(view.GONE);
                }
            }
        });
        holder.btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txtNomeCliente = holder.cardViewAtras.findViewById(R.id.nome_cliente_edicao);
                EditText txtContatoCliente = holder.cardViewAtras.findViewById(R.id.contato_cliente_edicao);
                EditText txtEnderecoCliente = holder.cardViewAtras.findViewById(R.id.endereco_cliente_edicao);
                EditText txtCPFCliente = holder.cardViewAtras.findViewById(R.id.cpf_cliente_edicao);

                String nome = txtNomeCliente.getText().toString();
                String contato = txtContatoCliente.getText().toString();
                String endereco = txtEnderecoCliente.getText().toString();
                String cpf = txtCPFCliente.getText().toString();

                if(clientesDAO.updateCliente(clienteArrayList.get(holder.getAdapterPosition()).getID(),nome,contato,endereco,cpf)){
                    clienteArrayList = clientesDAO.readAllClientes();
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG);
                }
                holder.cardViewAtras.setVisibility(View.GONE);
            }
        });
        holder.btnRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clientesDAO.deleteOneCliente(clienteArrayList.get(holder.getAdapterPosition()).getID())){
                    clienteArrayList = clientesDAO.readAllClientes();
                    notifyDataSetChanged();
                }else{
                    Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG);
                }
                holder.cardViewAtras.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clienteArrayList.size();
    }

    public static class ClientesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CardView cardViewAtras, cardViewFrente;
        private final TextView nomeClienteFrente,contatoClienteFrente, enderecoClienteFrente, cpfClienteFrente, nomeClienteAtras, contatoClienteAtras, enderecoClienteAtras, cpfClienteAtras;
        private final CheckBox pagementoFrente, pagamentoAtras;
        private final Button btnEditar, btnCancelar, btnSalvar, btnRemover;


        public ClientesViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeClienteFrente = itemView.findViewById(R.id.nome_cliente_detalhes);
            contatoClienteFrente = itemView.findViewById(R.id.contato_cliente_detalhes);
            pagementoFrente = itemView.findViewById(R.id.devendo_detalhes);
            enderecoClienteFrente = itemView.findViewById(R.id.endereco_cliente_detalhes);
            cpfClienteFrente = itemView.findViewById(R.id.cpf_cliente_detalhes);
            cardViewFrente = itemView.findViewById(R.id.cardeview_cliente_detalhes);
            cardViewAtras = itemView.findViewById(R.id.cardview_cliente_edicao);
            nomeClienteAtras = itemView.findViewById(R.id.nome_cliente_edicao);
            contatoClienteAtras = itemView.findViewById(R.id.contato_cliente_edicao);
            enderecoClienteAtras = itemView.findViewById(R.id.endereco_cliente_edicao);
            cpfClienteAtras = itemView.findViewById(R.id.cpf_cliente_detalhes);
            pagamentoAtras = itemView.findViewById(R.id.devendo_edicao);
            btnEditar = itemView.findViewById(R.id.editar_cliente);
            btnCancelar = itemView.findViewById(R.id.cancelar_cliente);
            btnSalvar = itemView.findViewById(R.id.salvar_cliente);
            btnRemover = itemView.findViewById(R.id.remover_cliente);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.findViewById(R.id.detalhes_cliente).getVisibility()==view.VISIBLE){
                view.findViewById(R.id.detalhes_cliente).setVisibility(view.GONE);
            }else {
                view.findViewById(R.id.detalhes_cliente).setVisibility(view.VISIBLE);
            }
        }
    }
}
