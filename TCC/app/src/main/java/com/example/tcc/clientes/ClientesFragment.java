package com.example.tcc.clientes;

import android.app.Dialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tcc.R;

import java.util.ArrayList;

public class ClientesFragment extends Fragment {

    //VARIAVEIS NECESSARIAS PARA O FUNCIONAMENTO DO RECYCLERVIEW
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ImageButton btnAdcionarCliente;

    //ARRAYLIST DE CLIENTES PARA PREENCHER O A LISTA
    private ArrayList<Cliente> clienteArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //COMPACTA E PEGA O FRAGMENT_PRODUTOS.XML
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);
        ClientesDAO clientesDAO = new ClientesDAO(this.getContext());

        //ACHA O RECYCLER VIEW E OS COMPONENTES
        recyclerView = view.findViewById(R.id.lista_cliente);
        btnAdcionarCliente = view.findViewById(R.id.adcionar_cliente);

        //RECOMENDADO PELO GOOGLE
        recyclerView.setHasFixedSize(true);

        //INICIALIZA E SETA O LAYOUT MANAGER
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        clienteArrayList = clientesDAO.readAllClientes();
        btnAdcionarCliente.setOnClickListener(adicionarCliente);

        //CRIA E SETA O ADAPTER
        mAdapter = new ClientesAdapter(this.getContext(), clienteArrayList);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    private View.OnClickListener adicionarCliente = new View.OnClickListener() {
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
                EditText txtNomeCliente = dialog.findViewById(R.id.nome_cliente_edicao);
                EditText txtContatoCliente = dialog.findViewById(R.id.contato_cliente_edicao);
                EditText txtEnderecoCliente = dialog.findViewById(R.id.endereco_cliente_edicao);
                EditText txtCpfCliente = dialog.findViewById(R.id.cpf_cliente_edicao);

                String nome = txtNomeCliente.getText().toString();
                String contato = txtContatoCliente.getText().toString();
                String endereco = txtEnderecoCliente.getText().toString();
                String cpf = txtCpfCliente.getText().toString();

                Cliente c = new Cliente(0,nome,cpf,endereco,contato);
                if(clientesDAO.createCliente(c)){
                    c.setID(clientesDAO.ultimoID());
                    clienteArrayList.add(c);
                    mAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG);
                }
                txtNomeCliente.setText("");
                txtContatoCliente.setText("");
                txtEnderecoCliente.setText("");
                txtCpfCliente.setText("");
                dialog.cancel();

                mAdapter.notifyDataSetChanged();
            });
            dialog.create();
            dialog.show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}