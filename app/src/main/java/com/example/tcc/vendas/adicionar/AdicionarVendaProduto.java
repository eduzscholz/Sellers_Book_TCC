package com.example.tcc.vendas.adicionar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdicionarVendaProduto extends AppCompatActivity  implements ProdutosVendaAdapter.OnClickProdutoListener,ItemPedidoVendaAdapter.OnClickItemPedidoListener{

    private ArrayList<Produto> produtos;
    private ArrayList<Produto> produtoItemPedidos = new ArrayList<>();
    private Cliente cliente;
    private ClientesDAO clientesDAO;
    private RecyclerView rvProdutos, rvItemPedido;
    private RecyclerView.LayoutManager layoutManagerItemPedido = new LinearLayoutManager(this),layoutManagerProdutos = new LinearLayoutManager(this);
    private RecyclerView.Adapter adpProdutos, adpItemPedido;
    private Button voltar, salvar;
    private ProdutosDAO produtosDAO;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venda_produto);
        getCliente();
        toolbar = findViewById(R.id.toolbar_add_venda_produto);
        toolbar.setTitle("Adicionar Venda - Produtos");

        produtosDAO = new ProdutosDAO(this);
        produtos = produtosDAO.readAllProduto();

        voltar = findViewById(R.id.btn_cancelar);
        salvar = findViewById(R.id.btn_salvar);
        rvItemPedido = findViewById(R.id.rv_itempedido_add_venda);
        rvProdutos = findViewById(R.id.rv_produto_add_venda);
        adpProdutos = new ProdutosVendaAdapter(this,produtos,this::onClickProduto);
        adpItemPedido = new ItemPedidoVendaAdapter(this, produtoItemPedidos,this::onClickItemPedido);
        rvProdutos.setLayoutManager(layoutManagerProdutos);
        rvItemPedido.setLayoutManager(layoutManagerItemPedido);
        rvProdutos.setAdapter(adpProdutos);
        rvItemPedido.setAdapter(adpItemPedido);
        salvar.setOnClickListener(datePicker);
        voltar.setOnClickListener(voltarListener);


    }

    private View.OnClickListener datePicker = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(produtoItemPedidos.size()==0){
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(view.getContext());
                dialog .setTitle("Voce precisa adicionar produtos para salvar")
                        .setPositiveButton("OK", null)
                        .show();
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

    View.OnClickListener voltarListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private DatePickerDialog.OnDateSetListener salvarListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            VendasDAO vendasDAO = new VendasDAO(getBaseContext());
            ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO(getBaseContext());
            Date dataPrevisao;
            Date dataAtual = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/DD");
            try {
                dataPrevisao = simpleDateFormat.parse(y+"/"+m+1+"/"+d);
                Venda v = new Venda(0, dataAtual, null, dataPrevisao, cliente.getID(), null);
                vendasDAO.createVenda(v);
                int idVenda= vendasDAO.ultimoID();
                for(int i = 0; i < produtoItemPedidos.size(); i++){
                    ItemPedido ip = new ItemPedido(0, produtoItemPedidos.get(i).getQuantidade(), produtoItemPedidos.get(i).getPreco(), produtoItemPedidos.get(i).getIDProduto(),idVenda);
                    itemPedidoDAO.createItemPedido(ip);
                    //TODO produtosDAO.updateReduzQuantidade(ip.getProdutoID(),ip.getQuantidade());
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
        clientesDAO = new ClientesDAO(this);
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
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        dialog .setTitle("Deseja retirar esse pedido da lista?")
                .setNegativeButton("Cancelar",null)
                .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        produtoItemPedidos.remove(position);
                        adpItemPedido.notifyDataSetChanged();
                    }
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

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Produto p = produtos.get(position);
                boolean chave=true;
                int valor = numberPicker.getValue();
                if(valor==0){
                    Toast.makeText(view.getContext(), "Valor invalido",Toast.LENGTH_LONG).show();
                }else{
                    for(int i = 0; i< produtoItemPedidos.size(); i++){
                        if(produtoItemPedidos.get(i).getIDProduto()==p.getIDProduto()){
                            produtoItemPedidos.get(i).setQuantidade(produtoItemPedidos.get(i).getQuantidade()+valor);
                            chave=false;
                            break;
                        }
                    }if (chave){
                        produtoItemPedidos.add(new Produto(p.getIDProduto(),p.getImg(),p.getNome(),p.getMarca(),p.getComplemento(),p.getMedida(),p.getPreco(),valor,p.getTipoDeProduto()));
                        //TODO VER COM O SOR SE TA CERTO
                    }
                    p.setQuantidade(p.getQuantidade()-valor);
                    adpItemPedido.notifyDataSetChanged();
                    adpProdutos.notifyDataSetChanged();
                    dialog.cancel();
                 }
            }
        });
        dialog.create();
        dialog.show();
    }
}