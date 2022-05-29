package com.example.tcc.vendas.adicionar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcc.MainActivity;
import com.example.tcc.R;
import com.example.tcc.clientes.Cliente;
import com.example.tcc.clientes.ClientesDAO;
import com.example.tcc.produtos.Produto;
import com.example.tcc.produtos.ProdutosDAO;
import com.example.tcc.vendas.Venda;
import com.example.tcc.vendas.VendasDAO;
import com.example.tcc.vendas.itemPedido.ItemPedido;
import com.example.tcc.vendas.itemPedido.ItemPedidoDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdicionarVendaProduto extends AppCompatActivity  implements ProdutosVendaAdapter.OnClickProdutoListener,ItemPedidoVendaAdapter.OnClickItemPedidoListener{

    private ArrayList<Produto> produtos;
    private final ArrayList<Produto> carrinho = new ArrayList<>();
    private Cliente cliente;
    private final RecyclerView.LayoutManager layoutManagerItemPedido = new LinearLayoutManager(this);
    private final RecyclerView.LayoutManager layoutManagerProdutos = new LinearLayoutManager(this);
    private RecyclerView.Adapter adpProdutos, adpItemPedido;
    private ProdutosDAO produtosDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venda_produto);
        getCliente();
        Toolbar toolbar = findViewById(R.id.toolbar_add_venda_produto);
        toolbar.setTitle("Adicionar Venda - Produtos");

        SearchView searchView = findViewById(R.id.sv_venda_produto);
        searchView.setOnQueryTextListener(buscaProduto);

        produtosDAO = new ProdutosDAO(this);
        produtos = produtosDAO.readAllProduto();

        Button voltar = findViewById(R.id.btn_cancelar);
        Button salvar = findViewById(R.id.btn_salvar);
        RecyclerView rvItemPedido = findViewById(R.id.rv_itempedido_add_venda);
        RecyclerView rvProdutos = findViewById(R.id.rv_produto_add_venda);
        adpProdutos = new ProdutosVendaAdapter(this,produtos, this);
        adpItemPedido = new ItemPedidoVendaAdapter(this, carrinho,this::onClickItemPedido);
        rvProdutos.setLayoutManager(layoutManagerProdutos);
        rvItemPedido.setLayoutManager(layoutManagerItemPedido);
        rvProdutos.setAdapter(adpProdutos);
        rvItemPedido.setAdapter(adpItemPedido);
        salvar.setOnClickListener(datePicker);
        voltar.setOnClickListener(voltarListener);
    }

    private final SearchView.OnQueryTextListener buscaProduto = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            ProdutosDAO produtosDAO = new ProdutosDAO(getBaseContext());
            produtos.clear();
            produtos.addAll(produtosDAO.readProdutoNome(s));
            adpProdutos.notifyDataSetChanged();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if(s.equals("")){
                ProdutosDAO produtosDAO = new ProdutosDAO(getBaseContext());
                produtos.clear();
                produtos.addAll(produtosDAO.readAllProduto());
                adpProdutos.notifyDataSetChanged();
            }
            return false;
        }
    };

    private final View.OnClickListener datePicker = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(carrinho.size()==0){
                Toast.makeText(getBaseContext(),"Você precisa adicionar pelo menos um produto",Toast.LENGTH_LONG).show();
            }else {
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), salvarListener,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setTitle("Selecione a data prevista para o pagamento");
                datePickerDialog.create();
                datePickerDialog.show();
            }
        }
    };

    View.OnClickListener voltarListener = view -> finish();

    private final DatePickerDialog.OnDateSetListener salvarListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            VendasDAO vendasDAO = new VendasDAO(getBaseContext());
            ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO(getBaseContext());
            Date dataPrevisao;
            Date dataAtual = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                dataPrevisao = simpleDateFormat.parse(y+"-"+(m+1)+"-"+d);
                Venda v = new Venda(0,cliente.getNome(), dataAtual, null, dataPrevisao, cliente.getID(), null);
                vendasDAO.createVenda(v);
                int idVenda= vendasDAO.ultimoID();
                for(int i = 0; i < carrinho.size(); i++){
                    ItemPedido ip = new ItemPedido(0, carrinho.get(i).getQuantidade(), carrinho.get(i).getPreco(), carrinho.get(i).getIDProduto(),idVenda, carrinho.get(i).getNome());
                    itemPedidoDAO.createItemPedido(ip);
                    produtosDAO.updateReduzQuantidade(ip.getProdutoID(),ip.getQuantidade());
                }
                Intent i=new Intent(getBaseContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(i);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    private void getCliente(){
        Bundle extras = getIntent().getExtras();
        ClientesDAO clientesDAO = new ClientesDAO(this);
        int i = extras.getInt("clienteID");
        if(extras!=null){
            cliente = clientesDAO.readOneClienteID(i);
        }
    }

    @Override
    public void onClickProduto(int position) {
        mostrarDialogo(position);
    }

    @Override
    public void onClickItemPedido(int position) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialogo_simples);

        TextView txt = dialog.findViewById(R.id.texto);
        txt.setText("Você tem certeza que deseja remover este produto do carrinho?");

        Button buttonNao = dialog.findViewById(R.id.nao);
        Button buttonSim = dialog.findViewById(R.id.sim);
        buttonNao.setOnClickListener(view1 -> dialog.cancel());
        buttonSim.setOnClickListener(view12 -> {
                    carrinho.remove(position);
                    adpItemPedido.notifyItemRemoved(position);
                    dialog.cancel();
                });
        dialog.show();
    }

    void mostrarDialogo(int position){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_produto_venda);

        Button buttonCancelar = dialog.findViewById(R.id.btn_cancelar_produto_venda);
        Button buttonEnviar = dialog.findViewById(R.id.btn_enviar_produto_venda);
        NumberPicker numberPicker = dialog.findViewById(R.id.nb_quantidade_produto);

        numberPicker.setMaxValue(produtos.get(position).getQuantidade());
        numberPicker.setMinValue(0);

        buttonCancelar.setOnClickListener(view -> dialog.cancel());
        buttonEnviar.setOnClickListener(view -> {
            Produto p = produtos.get(position);
            boolean chave=true;
            int valor = numberPicker.getValue();
            if(valor==0){
                Toast.makeText(view.getContext(), "Valor invalido",Toast.LENGTH_LONG).show();
            }else{
                for(int i = 0; i< carrinho.size(); i++){
                    if(carrinho.get(i).getIDProduto()==p.getIDProduto()){
                        carrinho.get(i).setQuantidade(carrinho.get(i).getQuantidade()+valor);
                        chave=false;
                        break;
                    }
                }if (chave){
                    carrinho.add(new Produto(p.getIDProduto(),p.getImg(),p.getNome(),p.getMarca(),p.getComplemento(),p.getMedida(),p.getPreco(),valor,p.getTipoDeProduto()));
                }
                p.setQuantidade(p.getQuantidade()-valor);
                adpItemPedido.notifyItemChanged(position);
                adpProdutos.notifyItemChanged(position);
                dialog.cancel();
             }
        });
        dialog.create();
        dialog.show();
    }
}