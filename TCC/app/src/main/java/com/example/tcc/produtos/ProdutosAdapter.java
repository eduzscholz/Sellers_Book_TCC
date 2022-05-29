package com.example.tcc.produtos;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tcc.R;
import com.example.tcc.sumirBotaoAdc;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class ProdutosAdapter extends RecyclerView.Adapter<ProdutosAdapter.ProdutosViewHolder> {

    protected static ArrayList<Produto> produtoArrayList;    //LISTA QUE GUARDA OS PRODUTOS
    Context context;    //CONTEXTO...
    OnClickImagemListener onClickImagemListener;
    sumirBotaoAdc sumirBotaoAdc;

    //CONSTRUTOR
    public ProdutosAdapter(Context ct, ArrayList<Produto> produtoArrayList, ProdutosFragment onClickImagemListener, sumirBotaoAdc sumirBotaoAdc){
        this.context=ct;
        ProdutosAdapter.produtoArrayList = produtoArrayList;
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
        boolean estaAberto = produtoArrayList.get(position).isAberto();
        holder.cardViewFrente.findViewById(R.id.detalhes_produto).setVisibility(estaAberto ? View.VISIBLE : View.GONE);
        if(!produtoArrayList.get(position).getMarca().equals(""))holder.marcaProdutof.setText(produtoArrayList.get(position).getMarca());
        holder.nomeProdutof.setText(produtoArrayList.get(position).getNome());
        holder.precoProdutof.setText(decimalFormat.format(produtoArrayList.get(position).getPreco()));
        holder.qntProdutof.setText(String.valueOf(produtoArrayList.get(position).getQuantidade()));
        if(!produtoArrayList.get(position).getTipoDeProduto().equals(""))holder.tipof.setText(produtoArrayList.get(position).getTipoDeProduto());
        if(!produtoArrayList.get(position).getComplemento().equals(""))holder.descricaof.setText(produtoArrayList.get(position).getComplemento());
        if(!produtoArrayList.get(position).getMedida().equals(""))holder.unidadef.setText(String.valueOf(produtoArrayList.get(position).getMedida()));

        //QUANDO CLICA CANCELAR, SOME O CARDVIEW DE EDIÇÃO
        holder.btnCancelar.setOnClickListener(view -> {
            if(holder.cardViewAtras.getVisibility()!= View.GONE) {
                holder.cardViewAtras.setVisibility(View.GONE);
                holder.cardViewFrente.setClickable(true);
                sumirBotaoAdc.sumir();
            }
        });
        //APARECE O CARDVIEW DE EDIÇÃO
        holder.btnEditar.setOnClickListener(view -> {
            if(holder.cardViewAtras.getVisibility()!= View.VISIBLE) {

                if(produtoArrayList.get(holder.getAdapterPosition()).getImg()!=null) {
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(produtoArrayList.get(holder.getAdapterPosition()).getImg());
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    holder.imgProdutof.setImageBitmap(bitmap);
                }else{
                    holder.imgProdutof.setImageResource(R.drawable.ic_baseline_image_24);
                }
                holder.cardViewAtras.setVisibility(View.VISIBLE);
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
                holder.cardViewFrente.setClickable(false);
                sumirBotaoAdc.sumir();
            }
        });
        //SALVA AS MUDANCAS FEITAS
        holder.btnSalvarEdicao.setOnClickListener(view -> {
            boolean teste = false;
            if(Objects.requireNonNull(holder.nomeProdutoa.getText()).toString().equals("")){
                holder.nomeProdutoa.setError("O produto precisa de um nome");
                teste=true;
            }if(Objects.requireNonNull(holder.precoProdutoa.getText()).toString().equals("")){
                holder.precoProdutoa.setError("O produto precisa de um preço");
                teste=true;
            }if(Objects.requireNonNull(holder.qntProdutoa.getText()).toString().equals("")) {
                holder.qntProdutoa.setError("O produto precisa de uma quantidade");
                teste = true;
            }
            if(teste){
                return;
            }
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialogo_simples);

            TextView txt = dialog.findViewById(R.id.texto);
            txt.setText("Você tem certeza que deseja editar este produto?");

            Button buttonNao = dialog.findViewById(R.id.nao);
            Button buttonSim = dialog.findViewById(R.id.sim);
            buttonNao.setOnClickListener(view1 -> dialog.cancel());
            buttonSim.setOnClickListener(view12 -> {
                        byte[] imagemBytes;
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
                        String marca = Objects.requireNonNull(holder.marcaProdutoa.getText()).toString();
                        int quant = Integer.parseInt(holder.qntProdutoa.getText().toString());
                        String complemento = Objects.requireNonNull(holder.descricaoa.getText()).toString();
                        String medida = Objects.requireNonNull(holder.medida.getText()).toString();
                        String tipo = holder.tipoa.getText().toString();
                        Produto p = produtosDAO.updateProduto(produtoArrayList.get(holder.getAdapterPosition()).getIDProduto(),nome,marca,complemento,valor,medida,quant,tipo,imagemBytes);
                        if(p!=null){
                            holder.cardViewFrente.setClickable(true);
                            sumirBotaoAdc.sumir();
                            produtoArrayList.set(holder.getAdapterPosition(),p);
                            notifyItemChanged(holder.getAdapterPosition());
                        }else{
                            Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG).show();
                        }
                        holder.cardViewAtras.setVisibility(View.GONE);
                        dialog.cancel();
                    });
                dialog.show();
        });
        //APAGA O PRODUTO DO BANCO
        holder.btnRemover.setOnClickListener(view -> {
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialogo_simples);

            TextView txt = dialog.findViewById(R.id.texto);
            txt.setText("Você tem certeza que deseja remover este produto?");

            Button buttonNao = dialog.findViewById(R.id.nao);
            Button buttonSim = dialog.findViewById(R.id.sim);
            buttonNao.setOnClickListener(view1 -> dialog.cancel());
            buttonSim.setOnClickListener(view12 -> {
                if(produtosDAO.deleteOneProduto(produtoArrayList.get(holder.getAdapterPosition()).getIDProduto())){
                    holder.cardViewFrente.findViewById(R.id.detalhes_produto).setVisibility(View.GONE);
                    produtoArrayList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }else{
                    Toast.makeText(view.getContext(),"Algo deu errado",Toast.LENGTH_LONG).show();
                }
                holder.cardViewAtras.setVisibility(View.GONE);
                dialog.cancel();
            });
            dialog.show();
        });
    }

    //PEGA TAMANHO DA LISTA
    @Override
    public int getItemCount() {
        return produtoArrayList.size();
    }

    //VIEW HOLDER GUARDA É O QUE GUARDA O CONTEUDO DAS LINHAS DA LISTVIEW
    public class ProdutosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //COMPONENTES DA LINHA DA LISTA QUE O VIEW HOLDER DEVE PREENCHER EM ITEM_PRODUTO
        private final ImageView imgProdutof, imgProdutoa;
        private final TextView qntProdutof,  precoProdutof, marcaProdutof, nomeProdutof, descricaof, tipof, unidadef;
        private final CardView cardViewAtras, cardViewFrente;
        private final Button btnEditar,btnCancelar, btnRemover, btnSalvarEdicao;
        private final TextInputEditText nomeProdutoa, precoProdutoa, marcaProdutoa, descricaoa, qntProdutoa, medida;
        private final AutoCompleteTextView tipoa;


        //CONTRUTOR CONECTA COM OS COMPONENTES DE ITEMVIEW(ITEM_PRODUTO)
        public ProdutosViewHolder(@NonNull View itemView, OnClickImagemListener onClickImagemListener) {
            super(itemView);
            cardViewFrente = itemView.findViewById(R.id.cardView_produto_detalhes);
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
            imgProdutoa.setOnClickListener(view -> onClickImagemListener.onClickImagem(imgProdutoa));
            //ABRE OS DETALHES DO PRODUTO QUANDO CLICA NELE
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            Produto p = produtoArrayList.get(getAdapterPosition());
            p.setAberto(!p.isAberto());
            cardViewFrente.findViewById(R.id.detalhes_produto).setVisibility(p.isAberto()? View.VISIBLE : View.GONE);
            notifyItemChanged(getAdapterPosition());
        }
    }
    interface OnClickImagemListener{
        void onClickImagem(ImageView imageView);
    }
}