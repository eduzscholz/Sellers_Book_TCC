package com.example.tcc.produtos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tcc.R;
import com.example.tcc.sumirBotaoAdc;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProdutosAdapter extends RecyclerView.Adapter<ProdutosAdapter.ProdutosViewHolder> {

    private ArrayList<Produto> produtoArrayList;    //LISTA QUE GUARDA OS PRODUTOS
    Context context;    //CONTEXTO...
    OnClickImagemListener onClickImagemListener;
    sumirBotaoAdc sumirBotaoAdc;

    private ActivityResultLauncher<String> buscaGaleria;
    private ActivityResultLauncher<Intent> tirarFoto;

    //CONSTRUTOR
    public ProdutosAdapter(Context ct, ArrayList<Produto> produtoArrayList, ProdutosFragment onClickImagemListener, sumirBotaoAdc sumirBotaoAdc){
        this.context=ct;
        this.produtoArrayList = produtoArrayList;
        this.onClickImagemListener = onClickImagemListener;
        this.sumirBotaoAdc = sumirBotaoAdc;
    }

    //DEPOIS DE CRIAR ELE EMPACOTA E RETORNA O VIEW HOLDER
    @NonNull
    @Override
    public ProdutosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.linha_produto,parent,false);
        return new ProdutosViewHolder(view, onClickImagemListener);
    }

    //COLOCA DADOS DA LISTA NO VIEW HOLDER
    @Override
    public void onBindViewHolder(@NonNull ProdutosViewHolder holder, int position) {
        ProdutosDAO produtosDAO = new ProdutosDAO(context);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        if(produtoArrayList.get(position).getImg()!=null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(produtoArrayList.get(position).getImg());
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            holder.imgProdutof.setImageBitmap(bitmap);
        }else{
            holder.imgProdutof.setImageResource(R.drawable.ic_baseline_image_24);
        }
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
                    sumirBotaoAdc.sumir();
                }
            }
        });
        //APARECE O CARDVIEW DE EDIÇÃO
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.cardViewAtras.getVisibility()!=view.VISIBLE) {

                    if(produtoArrayList.get(holder.getAdapterPosition()).getImg()!=null) {
                        ByteArrayInputStream imageStream = new ByteArrayInputStream(produtoArrayList.get(holder.getAdapterPosition()).getImg());
                        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                        holder.imgProdutof.setImageBitmap(bitmap);
                    }else{
                        holder.imgProdutof.setImageResource(R.drawable.ic_baseline_image_24);
                    }
                    sumirBotaoAdc.sumir();
                    holder.cardViewAtras.setVisibility(view.VISIBLE);
                    holder.marcaProdutoa.setText(produtoArrayList.get(holder.getAdapterPosition()).getMarca());
                    holder.nomeProdutoa.setText(produtoArrayList.get(holder.getAdapterPosition()).getNome());
                    holder.precoProdutoa.setText(String.valueOf(produtoArrayList.get(holder.getAdapterPosition()).getPreco()));
                    holder.descricaoa.setText(produtoArrayList.get(holder.getAdapterPosition()).getComplemento());
                    holder.medida.setText(String.valueOf(produtoArrayList.get(holder.getAdapterPosition()).getMedida()));
                    holder.qntProdutoa.setText(String.valueOf(produtoArrayList.get(holder.getAdapterPosition()).getQuantidade()));
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),R.array.tipo, android.R.layout.simple_spinner_dropdown_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    holder.tipoa.setText(holder.tipof.getText());
                    holder.tipoa.setAdapter(adapter);
                }
            }
        });
        //SALVA AS MUDANCAS FEITAS
        holder.btnSalvarEdicao.setOnClickListener(view -> {
            boolean teste = false;
            if(holder.nomeProdutoa.getText().toString().equals("") || holder.nomeProdutoa.getText().equals(null)){
                holder.nomeProdutoa.setError("O produto precisa de um nome");
                teste=true;
            }if(holder.precoProdutoa.getText().toString().equals("") || holder.precoProdutoa.getText().toString().equals(null)){
                holder.precoProdutoa.setError("O produto precisa de um preço");
                teste=true;
            }if(holder.qntProdutoa.getText().toString().equals("") || holder.qntProdutoa.getText().toString().equals(null)) {
                holder.qntProdutoa.setError("O produto precisa de uma quantidade");
                teste = true;
            }
            if(teste){
                return;
            }
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
            dialog .setTitle("Deseja editar esse cliente?")
                    .setNegativeButton("Não",null)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            byte imagemBytes[];
                            try{
                                BitmapDrawable drawable = (BitmapDrawable) holder.imgProdutoa.getDrawable();
                                Bitmap bitmap = drawable.getBitmap();

                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG,10,stream);
                                imagemBytes = stream.toByteArray();
                            }catch(Exception e){
                                imagemBytes=null;
                            }
                            String nome = holder.nomeProdutoa.getText().toString();
                            Double valor = Double.parseDouble(holder.precoProdutoa.getText().toString());
                            String marca = holder.marcaProdutoa.getText().toString();
                            int quant = Integer.parseInt(holder.qntProdutoa.getText().toString());
                            String complemento = holder.descricaoa.getText().toString();
                            String medida = holder.medida.getText().toString();
                            String tipo = holder.tipoa.getText().toString();
                            if(produtosDAO.updateProduto(produtoArrayList.get(holder.getAdapterPosition()).getIDProduto(),nome,marca,complemento,valor,medida,quant,tipo,imagemBytes)){
                                sumirBotaoAdc.sumir();
                                produtoArrayList = produtosDAO.readAllProduto();
                                notifyDataSetChanged();
                            }else{
                                Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG);
                            }
                            holder.cardViewAtras.setVisibility(View.GONE);
                        }
                    });
                dialog.show();
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
                                    holder.cardViewFrente.findViewById(R.id.detalhes_produto).setVisibility(View.GONE);
                                    produtoArrayList.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
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
        private final ImageView imgProdutof, imgProdutoa;
        private final TextView qntProdutof,  precoProdutof, marcaProdutof, nomeProdutof, descricaof, tipof, unidadef;
        private final CardView cardViewAtras, cardViewFrente;
        private final Button btnEditar,btnCancelar, btnRemover, btnSalvarEdicao;
        private final TextInputEditText nomeProdutoa, precoProdutoa, marcaProdutoa, descricaoa, qntProdutoa, medida;
        private final TextInputLayout textInputLayout;
        private final AutoCompleteTextView tipoa;
        private OnClickImagemListener onClickImagemListener;


        //CONTRUTOR CONECTA COM OS COMPONENTES DE ITEMVIEW(ITEM_PRODUTO)
        public ProdutosViewHolder(@NonNull View itemView, OnClickImagemListener onClickImagemListener) {
            super(itemView);
            this.onClickImagemListener = onClickImagemListener;
            cardViewFrente = itemView.findViewById(R.id.cardView_produto_detalhes);
            textInputLayout = itemView.findViewById(R.id.txt_inpt_nome_produto_edicao);
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
            medida = itemView.findViewById(R.id.medida_produto_edicao);
            btnEditar = itemView.findViewById(R.id.editar_produto);
            btnCancelar = itemView.findViewById(R.id.cancelar_produto);
            btnRemover = itemView.findViewById(R.id.remover_produto);
            btnSalvarEdicao = itemView.findViewById(R.id.salvar_produto);
            imgProdutoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickImagemListener.onClickImagem(imgProdutoa);
                }
            });
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
    interface OnClickImagemListener{
        void onClickImagem(ImageView imageView);
    }
}