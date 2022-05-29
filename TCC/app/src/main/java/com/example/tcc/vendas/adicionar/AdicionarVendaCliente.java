package com.example.tcc.vendas.adicionar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.tcc.R;
import com.example.tcc.clientes.Cliente;
import com.example.tcc.clientes.ClientesDAO;

import java.util.ArrayList;

public class AdicionarVendaCliente extends AppCompatActivity implements ClienteVendaAdapter.OnClickClienteListener{

    private RecyclerView.Adapter mAdapter;
    private ArrayList<Cliente> clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venda_cliente);

        Toolbar toolbar = findViewById(R.id.toolbar_add_venda_cliente);
        toolbar.setTitle("Adicionar Venda - Cliente");

        SearchView searchView = findViewById(R.id.busca_cliente_venda);
        searchView.setOnQueryTextListener(buscaCliente);

        Button btnCancelar = findViewById(R.id.cancelar_venda);
        btnCancelar.setOnClickListener(cancelarListener);
        ClientesDAO clientesDAO = new ClientesDAO(this);
        clientes = clientesDAO.readAllClientes();
        RecyclerView recyclerViewClientes = findViewById(R.id.lista_clientes_venda);
        recyclerViewClientes.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new ClienteVendaAdapter(this, clientes, this);

        recyclerViewClientes.setLayoutManager(layoutManager);
        recyclerViewClientes.setAdapter(mAdapter);

    }

    public SearchView.OnQueryTextListener buscaCliente = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            ClientesDAO clientesDAO = new ClientesDAO(getBaseContext());
            clientes.clear();
            clientes.addAll(clientesDAO.readClienteNome(s));
            mAdapter.notifyDataSetChanged();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if(s.equals("")){
                ClientesDAO clientesDAO = new ClientesDAO(getBaseContext());
                clientes.clear();
                clientes.addAll(clientesDAO.readAllClientes());
                mAdapter.notifyDataSetChanged();
            }
            return false;
        }
    };

    private final View.OnClickListener cancelarListener = view -> finish();

    @Override
    public void onClickCliente(int position) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialogo_simples);

        TextView txt = dialog.findViewById(R.id.texto);
        txt.setText("Selecionar o cliente: "+clientes.get(position).getNome()+"?");

        Button buttonNao = dialog.findViewById(R.id.nao);
        Button buttonSim = dialog.findViewById(R.id.sim);
        buttonNao.setOnClickListener(view1 -> dialog.cancel());
        buttonSim.setOnClickListener(view12 -> {
                    Intent intent = new Intent(AdicionarVendaCliente.this, AdicionarVendaProduto.class);
                    intent.putExtra("clienteID", clientes.get(position).getID());
                    startActivity(intent);
                });
        dialog.create();
        dialog.show();
    }
}