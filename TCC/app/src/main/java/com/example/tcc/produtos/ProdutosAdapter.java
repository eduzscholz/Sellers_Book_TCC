package com.example.tcc.produtos;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tcc.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProdutosAdapter extends RecyclerView.Adapter<ProdutosAdapter.ProdutosViewHolder> {

    private ArrayList<Produto> produtoArrayList;    //LISTA QUE GUARDA OS PRODUTOS
    Context context;    //CONTEXTO...

    //CONSTRUTOR
    public ProdutosAdapter(Context ct, ArrayList<Produto> produtoArrayList){
        this.context=ct;
        this.produtoArrayList = produtoArrayList;
    }

    //DEPOIS DE CRIAR ELE EMPACOTA E RETORNA O VIEW HOLDER
    @NonNull
    @Override
    public ProdutosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linha_produto,parent,false);
        return new ProdutosViewHolder(view);
    }

    //COLOCA DADOS DA LISTA NO VIEW HOLDER
    @Override
    public void onBindViewHolder(@NonNull ProdutosViewHolder holder, int position) {
        ProdutosDAO produtosDAO = new ProdutosDAO(context);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        holder.imgProdutof.setImageResource(produtoArrayList.get(position).getImg());
        holder.marcaProdutof.setText(produtoArrayList.get(position).getMarca());
        holder.nomeProdutof.setText(produtoArrayList.get(position).getNome());
        holder.precoProdutof.setText(decimalFormat.format(produtoArrayList.get(position).getPreco()));
        holder.qntProdutof.setText(String.valueOf(produtoArrayList.get(position).getQuantidade()));
        holder.tipof.setText(produtoArrayList.get(position).getTipoDeProduto());
        holder.descricaof.setText(produtoArrayList.get(position).getComplemento());
        holder.unidadef.setText(String.valueOf(produtoArrayList.get(position).getMedida()));

        //QUANDO CLICA CANCELAR, SOME O CARDVIEW DE EDIÇÃO
        holder.btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.cardViewAtras.getVisibility()!=view.GONE) {
                    holder.cardViewAtras.setVisibility(view.GONE);
                }
            }
        });
        //APARECE O CARDVIEW DE EDIÇÃO
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.cardViewAtras.getVisibility()!=view.VISIBLE) {
                    holder.cardViewAtras.setVisibility(view.VISIBLE);
                    holder.imgProdutoa.setImageResource(produtoArrayList.get(holder.getAdapterPosition()).getImg());
                    holder.marcaProdutoa.setText(produtoArrayList.get(holder.getAdapterPosition()).getMarca());
                    holder.nomeProdutoa.setText(produtoArrayList.get(holder.getAdapterPosition()).getNome());
                    holder.precoProdutoa.setText(String.valueOf(produtoArrayList.get(holder.getAdapterPosition()).getPreco()));
                    holder.descricaoa.setText(produtoArrayList.get(holder.getAdapterPosition()).getComplemento());
                    holder.unidadea.setText(String.valueOf(produtoArrayList.get(holder.getAdapterPosition()).getMedida()));
                    holder.qntProdutoa.setText(String.valueOf(produtoArrayList.get(holder.getAdapterPosition()).getQuantidade()));
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),R.array.tipo, android.R.layout.simple_spinner_dropdown_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    holder.tipoa.setAdapter(adapter);
                }
            }
        });
        //SALVA AS MUDANCAS FEITAS
        holder.btnSalvarEdicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
                dialog .setTitle("Deseja editar esse cliente?")
                        .setNegativeButton("Não",null)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText txtNomeProduto = holder.cardViewAtras.findViewById(R.id.nome_produto_edicao);
                                EditText txtValorProduto = holder.cardViewAtras.findViewById(R.id.valor_produto_atras);
                                EditText txtMarcaProduto = holder.cardViewAtras.findViewById(R.id.marca_produto_edicao);
                                EditText txtQuantidadeProduto = holder.cardViewAtras.findViewById(R.id.quantidade_produto_edicao);
                                EditText txtDescricaoProduto = holder.cardViewAtras.findViewById(R.id.complemento_produto_edicao);
                                EditText txtMedidaProduto = holder.cardViewAtras.findViewById(R.id.medida_produto_edicao);
                                Spinner spinTipoAtras = holder.cardViewAtras.findViewById(R.id.tipo_produto_edicao);

                                String nome = txtNomeProduto.getText().toString();
                                Double valor = Double.parseDouble(txtValorProduto.getText().toString());
                                String marca = txtMarcaProduto.getText().toString();
                                int quant = Integer.parseInt(txtQuantidadeProduto.getText().toString());
                                String complemento = txtDescricaoProduto.getText().toString();
                                String medida = txtMedidaProduto.getText().toString();
                                String tipo = spinTipoAtras.getSelectedItem().toString();
                                if(produtosDAO.updateProduto(produtoArrayList.get(holder.getAdapterPosition()).getIDProduto(),nome,marca,complemento,valor,medida,quant,tipo,R.drawable.img2)){
                                    produtoArrayList = produtosDAO.readAllProduto();
                                    notifyDataSetChanged();
                                }else{
                                    Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG);
                                }
                                holder.cardViewAtras.setVisibility(View.GONE);
                            }
                        });
                    dialog.show();
            }
        });
        //APAGA O PRODUTO DO BANCO
        holder.btnRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
                dialog .setTitle("Deseja remover esse cliente?")
                        .setNegativeButton("Não",null)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(produtosDAO.deleteOneProduto(produtoArrayList.get(holder.getAdapterPosition()).getIDProduto())){
                                    produtoArrayList = produtosDAO.readAllProduto();
                                    notifyDataSetChanged();
                                }else{
                                    Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG);
                                }
                                holder.cardViewAtras.setVisibility(View.GONE);
                            }
                        });
                dialog.show();
            }
        });
    }

    //PEGA TAMANHO DA LISTA
    @Override
    public int getItemCount() {
        return produtoArrayList.size();
    }


    //VIEW HOLDER GUARDA É O QUE GUARDA O CONTEUDO DAS LINHAS DA LISTVIEW
    public static class ProdutosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //COMPONENTES DA LINHA DA LISTA QUE O VIEW HOLDER DEVE PREENCHER EM ITEM_PRODUTO
        private final ImageView imgProdutof,imgProdutoa;
        private final TextView qntProdutof,  precoProdutof, marcaProdutof, nomeProdutof, descricaof, tipof, unidadef;
        private final CardView cardViewAtras;
        private final Button btnEditar,btnCancelar, btnRemover, btnSalvarEdicao;
        private final EditText  precoProdutoa, marcaProdutoa, nomeProdutoa, descricaoa, qntProdutoa, unidadea;
        private final Spinner tipoa;


        //CONTRUTOR CONECTA COM OS COMPONENTES DE ITEMVIEW(ITEM_PRODUTO)
        public ProdutosViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProdutof = itemView.findViewById(R.id.imagem_produto_detalhes);
            qntProdutof = itemView.findViewById(R.id.quantidade_produto_detalhes);
            precoProdutof = itemView.findViewById(R.id.valor_produto_detalhes);
            marcaProdutof = itemView.findViewById(R.id.marca_produto_detalhes);
            nomeProdutof = itemView.findViewById(R.id.nome_produto_detalhes);
            descricaof = itemView.findViewById(R.id.complemento_produto_detalhes);
            tipof = itemView.findViewById(R.id.tipo_produto_detalhes);
            unidadef = itemView.findViewById(R.id.unidade_produto_detalhes);
            cardViewAtras = itemView.findViewById(R.id.cardView_produto_edicao);
            imgProdutoa = itemView.findViewById(R.id.imagem_produto_edicao);
            qntProdutoa = itemView.findViewById(R.id.quantidade_produto_edicao);
            precoProdutoa = itemView.findViewById(R.id.valor_produto_atras);
            marcaProdutoa = itemView.findViewById(R.id.marca_produto_edicao);
            nomeProdutoa = itemView.findViewById(R.id.nome_produto_edicao);
            descricaoa = itemView.findViewById(R.id.complemento_produto_edicao);
            tipoa = itemView.findViewById(R.id.tipo_produto_edicao);
            unidadea = itemView.findViewById(R.id.medida_produto_edicao);
            btnEditar = itemView.findViewById(R.id.editar_produto);
            btnCancelar = itemView.findViewById(R.id.cancelar_produto);
            btnRemover = itemView.findViewById(R.id.remover_produto);
            btnSalvarEdicao = itemView.findViewById(R.id.salvar_produto);
            //ABRE OS DETALHES DO PRODUTO QUANDO CLICA NELE
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if(view.findViewById(R.id.detalhes_produto).getVisibility()==view.VISIBLE){
                view.findViewById(R.id.detalhes_produto).setVisibility(view.GONE);
            }else {
                view.findViewById(R.id.detalhes_produto).setVisibility(view.VISIBLE);
            }
        }
    }
}