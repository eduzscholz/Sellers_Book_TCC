package com.example.tcc.produtos;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tcc.R;

import java.util.ArrayList;

public class ProdutosFragment extends Fragment {

    //RECYCLER VIEW E ADAPTER
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //COMPONENTES CARDVIEW DE ADICIONAR PRODUTO
    private Button btnSalvarProduto, btnCancelar;
    private CardView cvAdcionarProduto;
    private ImageButton btnAdcionarProduto;

    //ARRAYLIST DE PRODUTOS PARA PREENCHER O A LISTA
    private ArrayList<Produto> produtoArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //COMPACTA E PEGA O FRAGMENT_PRODUTOS.XML
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);
        ProdutosDAO produtosDAO = new ProdutosDAO(this.getContext());

        //ACHA E CONECTA COM OS COMPONENTES DA TELA
        recyclerView = view.findViewById(R.id.lista_produtos);
        btnAdcionarProduto = view.findViewById(R.id.adcionar_produto);
        btnSalvarProduto = view.findViewById(R.id.salvar_produto);
        btnCancelar = view.findViewById(R.id.cancelar_produto);
        cvAdcionarProduto = view.findViewById(R.id.cardview_adicionar_produto);

        //RECOMENDADO PELO GOOGLE
        recyclerView.setHasFixedSize(true);

        //INICIALIZA E SETA O LAYOUT MANAGER
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //ADICIONA PRODUTOS DO BANCO NO ARRAYLIST DE PRODUTOS
        produtoArrayList = produtosDAO.readAllProduto();
        //CRIA E SETA O ADAPTER
        mAdapter = new ProdutosAdapter(this.getContext(), produtoArrayList);
        recyclerView.setAdapter(mAdapter);
        //BOTAO QUE FAZ APARECER O CARDVIEW DE ADICIONAR PRODUTO
        btnAdcionarProduto.setOnClickListener(adicionarProduto);
        //BOTAO QUE FAZ SUMIR O CARDVIEW
        btnCancelar.setOnClickListener(cancelar);
        //SALVA O PROUTO NO BANCO DE DADOS
        btnSalvarProduto.setOnClickListener(salvarProduto);

        return view;
    }

    private View.OnClickListener adicionarProduto = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            cvAdcionarProduto.setVisibility(view.VISIBLE);
            Spinner spinner = cvAdcionarProduto.findViewById(R.id.tipo_produto_edicao);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),R.array.tipo, android.R.layout.simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            btnAdcionarProduto.setVisibility(view.GONE);
        }
    };

    private View.OnClickListener cancelar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(cvAdcionarProduto.getVisibility()==View.VISIBLE){
                cvAdcionarProduto.setVisibility(View.GONE);
                btnAdcionarProduto.setVisibility(view.VISIBLE);
            }
        }
    };

    private View.OnClickListener salvarProduto = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ProdutosDAO produtosDAO = new ProdutosDAO(view.getContext());

            EditText txtNomeProduto = cvAdcionarProduto.findViewById(R.id.nome_produto_edicao);
            EditText txtValorProduto = cvAdcionarProduto.findViewById(R.id.valor_produto_atras);
            EditText txtMarcaProduto = cvAdcionarProduto.findViewById(R.id.marca_produto_edicao);
            EditText txtQuantidadeProduto = cvAdcionarProduto.findViewById(R.id.quantidade_produto_edicao);
            EditText txtDescricaoProduto = cvAdcionarProduto.findViewById(R.id.complemento_produto_edicao);
            EditText txtMedidaProduto = cvAdcionarProduto.findViewById(R.id.medida_produto_edicao);
            Spinner spinTipoAtras = cvAdcionarProduto.findViewById(R.id.tipo_produto_edicao);

            String nome = txtNomeProduto.getText().toString();
            Double valor = Double.parseDouble(txtValorProduto.getText().toString());
            String marca = txtMarcaProduto.getText().toString();
            int quant = Integer.parseInt(txtQuantidadeProduto.getText().toString());
            String complemento = txtDescricaoProduto.getText().toString();
            String medida = txtMedidaProduto.getText().toString();
            String tipo = spinTipoAtras.getSelectedItem().toString();
            Produto p = new Produto(0,R.drawable.img1,nome,marca,complemento,medida,valor,quant,tipo);
            if(produtosDAO.createProduto(p)){
                produtoArrayList.add(p);
                mAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG);
            }
            txtDescricaoProduto.setText("");
            txtMarcaProduto.setText("");
            txtMedidaProduto.setText("");
            txtValorProduto.setText("");
            txtQuantidadeProduto.setText("");
            txtNomeProduto.setText("");
            cvAdcionarProduto.setVisibility(View.GONE);
            btnAdcionarProduto.setVisibility(view.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}