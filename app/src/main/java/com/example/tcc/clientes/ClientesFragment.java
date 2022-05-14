package com.example.tcc.clientes;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private Button btnSalvarCliente, btnCancelar;
    private CardView cvAdcionarCliente;
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
        btnSalvarCliente = view.findViewById(R.id.salvar_cliente);
        cvAdcionarCliente = view.findViewById(R.id.cardview_adcionar_cliente);
        btnCancelar = view.findViewById(R.id.cancelar_cliente);

        //RECOMENDADO PELO GOOGLE
        recyclerView.setHasFixedSize(true);

        //INICIALIZA E SETA O LAYOUT MANAGER
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        clienteArrayList = clientesDAO.readAllClientes();

        btnAdcionarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cvAdcionarCliente.getVisibility()==View.GONE){
                    cvAdcionarCliente.setVisibility(view.VISIBLE);
                    btnAdcionarCliente.setVisibility(view.GONE);
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cvAdcionarCliente.getVisibility()==View.VISIBLE){
                    cvAdcionarCliente.setVisibility(View.GONE);
                    btnAdcionarCliente.setVisibility(view.VISIBLE);
                }
            }
        });

        btnSalvarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txtNomeCliente = cvAdcionarCliente.findViewById(R.id.nome_cliente_edicao);
                EditText txtContatoCliente = cvAdcionarCliente.findViewById(R.id.contato_cliente_edicao);
                EditText txtEnderecoCliente = cvAdcionarCliente.findViewById(R.id.endereco_cliente_edicao);
                EditText txtCpfCliente = cvAdcionarCliente.findViewById(R.id.cpf_cliente_edicao);

                String nome = txtNomeCliente.getText().toString();
                String contato = txtContatoCliente.getText().toString();
                String endereco = txtEnderecoCliente.getText().toString();
                String cpf = txtCpfCliente.getText().toString();

                Cliente c = new Cliente(0,nome,cpf,endereco,contato);
                if(clientesDAO.createCliente(c)){
                    clienteArrayList.add(c);
                    mAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG);
                }
                txtNomeCliente.setText("");
                txtContatoCliente.setText("");
                txtEnderecoCliente.setText("");
                txtCpfCliente.setText("");
                cvAdcionarCliente.setVisibility(View.GONE);
                btnAdcionarCliente.setVisibility(view.VISIBLE);
            }
        });

        //CRIA E SETA O ADAPTER
        mAdapter = new ClientesAdapter(this.getContext(), clienteArrayList);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}