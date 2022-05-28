package com.example.tcc.clientes;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.tcc.R;
import com.example.tcc.sumirBotaoAdc;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

public class ClientesFragment extends Fragment implements sumirBotaoAdc {

    private RecyclerView.Adapter mAdapter;

    private ImageButton btnAdcionarCliente;

    //ARRAYLIST DE CLIENTES PARA PREENCHER O A LISTA
    private ArrayList<Cliente> clienteArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //COMPACTA E PEGA O FRAGMENT_PRODUTOS.XML
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);
        ClientesDAO clientesDAO = new ClientesDAO(this.getContext());

        //ACHA O RECYCLER VIEW E OS COMPONENTES
        //VARIAVEIS NECESSARIAS PARA O FUNCIONAMENTO DO RECYCLERVIEW
        RecyclerView recyclerView = view.findViewById(R.id.lista_cliente);
        btnAdcionarCliente = view.findViewById(R.id.adcionar_cliente);

        SearchView searchView = view.findViewById(R.id.sv_cliente);
        searchView.setOnQueryTextListener(buscaCliente);

        //RECOMENDADO PELO GOOGLE
        recyclerView.setHasFixedSize(true);

        //INICIALIZA E SETA O LAYOUT MANAGER
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        clienteArrayList = clientesDAO.readAllClientes();
        btnAdcionarCliente.setOnClickListener(adicionarCliente);

        //CRIA E SETA O ADAPTER
        mAdapter = new ClientesAdapter(this.getContext(), clienteArrayList, this);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    private final View.OnClickListener adicionarCliente = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ClientesDAO clientesDAO = new ClientesDAO(getContext());

            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_adicionar_cliente);

            Button buttonCancelar = dialog.findViewById(R.id.cancelar_cliente);
            buttonCancelar.setOnClickListener(view12 -> dialog.cancel());
            Button buttonSalvar = dialog.findViewById(R.id.salvar_cliente);
            buttonSalvar.setOnClickListener(view13 -> {
                TextInputEditText txtNomeCliente = dialog.findViewById(R.id.nome_cliente_edicao);
                TextInputEditText txtContatoCliente = dialog.findViewById(R.id.contato_cliente_edicao);
                TextInputEditText txtEnderecoCliente = dialog.findViewById(R.id.endereco_cliente_edicao);
                TextInputEditText txtCpfCliente = dialog.findViewById(R.id.cpf_cliente_edicao);

                String nome = Objects.requireNonNull(txtNomeCliente.getText()).toString();
                String contato = Objects.requireNonNull(txtContatoCliente.getText()).toString();
                String endereco = Objects.requireNonNull(txtEnderecoCliente.getText()).toString();
                String cpf = Objects.requireNonNull(txtCpfCliente.getText()).toString();

                Cliente c = new Cliente(0,nome,cpf,endereco,contato);
                if(clientesDAO.createCliente(c)){
                    c.setID(clientesDAO.ultimoID());
                    clienteArrayList.add(c);
                    mAdapter.notifyItemInserted(clienteArrayList.size()+1);
                }else{
                    Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG).show();
                }
                txtNomeCliente.setText("");
                txtContatoCliente.setText("");
                txtEnderecoCliente.setText("");
                txtCpfCliente.setText("");
                dialog.cancel();
            });
            dialog.create();
            dialog.show();
        }
    };

    private final SearchView.OnQueryTextListener buscaCliente = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            ClientesDAO clientesDAO = new ClientesDAO(getContext());
            clienteArrayList.clear();
            clienteArrayList.addAll(clientesDAO.readClienteNome(s));
            mAdapter.notifyDataSetChanged();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if(s.equals("")){
                ClientesDAO clientesDAO = new ClientesDAO(getContext());
                clienteArrayList.clear();
                clienteArrayList.addAll(clientesDAO.readAllClientes());
                mAdapter.notifyDataSetChanged();
            }
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void sumir() {
        if(btnAdcionarCliente.getVisibility()==View.GONE){
            btnAdcionarCliente.setVisibility(View.VISIBLE);
        }else{
            btnAdcionarCliente.setVisibility(View.GONE);
        }
    }
}