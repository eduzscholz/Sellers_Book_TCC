package com.example.tcc.clientes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.tcc.sumirBotaoAdc;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ClientesViewHolder> {

    private ArrayList<Cliente> clienteArrayList;
    Context context;
    sumirBotaoAdc sumirBotaoAdc;

    public ClientesAdapter(Context context, ArrayList<Cliente> clienteArrayList, sumirBotaoAdc sumirBotaoAdc) {
        this.context=context;
        this.clienteArrayList = clienteArrayList;
        this.sumirBotaoAdc = sumirBotaoAdc;
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
        boolean pagamento = clientesDAO.verificarPagamentoCliente(clienteArrayList.get(position).getID());

        holder.nomeClienteFrente.setText(clienteArrayList.get(position).getNome());
        holder.contatoClienteFrente.setText(clienteArrayList.get(position).getContato());
        holder.enderecoClienteFrente.setText(clienteArrayList.get(position).getEndereço());
        holder.pagamentoFrente.setText(pagamento ? "" : "Atrasado" );
        holder.cpfClienteFrente.setText(clienteArrayList.get(position).getCPF());
        holder.btnEditar.setOnClickListener(view -> {
            if(holder.cardViewAtras.getVisibility()!=view.VISIBLE) {
                holder.cardViewAtras.setVisibility(view.VISIBLE);
                holder.nomeClienteAtras.setText(clienteArrayList.get(holder.getAdapterPosition()).getNome());
                holder.contatoClienteAtras.setText(clienteArrayList.get(holder.getAdapterPosition()).getContato());
                holder.enderecoClienteAtras.setText(clienteArrayList.get(holder.getAdapterPosition()).getEndereço());
                holder.cpfClienteAtras.setText(clienteArrayList.get(holder.getAdapterPosition()).getCPF());
                sumirBotaoAdc.sumir();
            }
        });
        holder.btnCancelar.setOnClickListener(view -> {
            if(holder.cardViewAtras.getVisibility()!=view.GONE) {
                holder.cardViewAtras.setVisibility(view.GONE);
                sumirBotaoAdc.sumir();
            }
        });
        holder.btnSalvarEdicao.setOnClickListener(view -> {
            if(holder.nomeClienteAtras.getText().toString().equals("") || holder.nomeClienteAtras.getText().toString().equals(null)){
                holder.nomeClienteAtras.setError("O cliente precisa de um nome");
                return;
            }
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
            dialog .setTitle("Deseja editar esse cliente?")
                    .setNegativeButton("Não",null)
                    .setPositiveButton("Sim", (dialogInterface, i) -> {
                        String nome = holder.nomeClienteAtras.getText().toString();
                        String contato = holder.contatoClienteAtras.getText().toString();
                        String endereco = holder.enderecoClienteAtras.getText().toString();
                        String cpf = holder.cpfClienteAtras.getText().toString();

                        if(clientesDAO.updateCliente(clienteArrayList.get(holder.getAdapterPosition()).getID(),nome,contato,endereco,cpf)){
                            clienteArrayList = clientesDAO.readAllClientes();
                            notifyDataSetChanged();
                        }else{
                            Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG);
                        }
                        holder.cardViewAtras.setVisibility(View.GONE);
                    });
            dialog.show();
        });
        holder.btnRemover.setOnClickListener(view -> {
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
            dialog .setTitle("Deseja remover esse cliente?")
                    .setNegativeButton("Não",null)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(clientesDAO.deleteOneCliente(clienteArrayList.get(holder.getAdapterPosition()).getID())){
                                holder.cardViewFrente.findViewById(R.id.detalhes_cliente).setVisibility(View.GONE);
                                clienteArrayList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                            }else{
                                Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG);
                            }
                            holder.cardViewAtras.setVisibility(View.GONE);
                        }
                    });
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return clienteArrayList.size();
    }

    public static class ClientesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CardView cardViewAtras, cardViewFrente;
        private final TextView nomeClienteFrente, pagamentoFrente, contatoClienteFrente, enderecoClienteFrente, cpfClienteFrente;
        private final TextInputEditText nomeClienteAtras, contatoClienteAtras, enderecoClienteAtras, cpfClienteAtras;
        private final Button btnEditar, btnCancelar, btnSalvarEdicao, btnRemover;

        public ClientesViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewFrente = itemView.findViewById(R.id.cardview_cliente_detalhes);
            nomeClienteFrente = itemView.findViewById(R.id.nome_cliente_detalhes);
            contatoClienteFrente = itemView.findViewById(R.id.contato_cliente_detalhes);
            pagamentoFrente = itemView.findViewById(R.id.devendo_detalhes);
            enderecoClienteFrente = itemView.findViewById(R.id.endereco_cliente_detalhes);
            cpfClienteFrente = itemView.findViewById(R.id.cpf_cliente_detalhes);
            cardViewAtras = itemView.findViewById(R.id.cardview_cliente_edicao);
            nomeClienteAtras = itemView.findViewById(R.id.nome_cliente_edicao);
            contatoClienteAtras = itemView.findViewById(R.id.contato_cliente_edicao);
            enderecoClienteAtras = itemView.findViewById(R.id.endereco_cliente_edicao);
            cpfClienteAtras = itemView.findViewById(R.id.cpf_cliente_edicao);
            btnEditar = itemView.findViewById(R.id.editar_cliente);
            btnCancelar = itemView.findViewById(R.id.cancelar_cliente);
            btnSalvarEdicao = itemView.findViewById(R.id.salvar_cliente);
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
