package com.example.tcc.vendas.adicionar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.widget.Toolbar;

import com.example.tcc.R;
import com.example.tcc.clientes.Cliente;
import com.example.tcc.clientes.ClientesDAO;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class AdicionarVendaCliente extends AppCompatActivity implements ClienteVendaAdapter.OnClickClienteListener{

    private RecyclerView recyclerViewClientes;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ClientesDAO clientesDAO;
    private ArrayList<Cliente> clientes;
    private Button btnCancelar;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venda_cliente);

        Toolbar toolbar = findViewById(R.id.toolbar_add_venda_cliente);
        toolbar.setTitle("Adicionar Venda - Cliente");

        searchView = findViewById(R.id.busca_cliente_venda);
        searchView.setOnQueryTextListener(buscaCliente);

        btnCancelar = findViewById(R.id.cancelar_venda);
        btnCancelar.setOnClickListener(cancelarListener);
        clientesDAO = new ClientesDAO(this);
        clientes = clientesDAO.readAllClientes();
        recyclerViewClientes = findViewById(R.id.lista_clientes_venda);
        recyclerViewClientes.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mAdapter = new ClienteVendaAdapter(this, clientes, this::onClickCliente);

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

    private View.OnClickListener cancelarListener = view -> finish();

    @Override
    public void onClickCliente(int position) {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        dialog .setTitle("Confimação")
                .setMessage("Selecionar o cliente: "+clientes.get(position).getNome()+"?")
                .setNegativeButton("Cancelar",null)
                .setPositiveButton("Continuar", (dialogInterface, i) -> {
                    Intent intent = new Intent(AdicionarVendaCliente.this, AdicionarVendaProduto.class);
                    intent.putExtra("clienteID", clientes.get(position).getID());
                    startActivity(intent);
                });
        dialog.create();
        dialog.show();
    }
}