package com.example.tcc.produtos;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tcc.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ProdutosFragment extends Fragment implements ProdutosAdapter.OnClickImagemListener{

    //RECYCLER VIEW E ADAPTER
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private SearchView searchView;

    //COMPONENTES CARDVIEW DE ADICIONAR PRODUTO
    private ImageButton btnAdcionarProduto;
    private ImageView imageView;

    //ARRAYLIST DE PRODUTOS PARA PREENCHER O A LISTA
    private ArrayList<Produto> produtoArrayList = new ArrayList<>();

    private ActivityResultLauncher<String> buscaGaleria;
    private ActivityResultLauncher<Intent> tirarFoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //COMPACTA E PEGA O FRAGMENT_PRODUTOS.XML
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);
        ProdutosDAO produtosDAO = new ProdutosDAO(this.getContext());

        searchView = view.findViewById(R.id.sv_produtos);
        searchView.setOnQueryTextListener(buscaProduto);

        //ACHA E CONECTA COM OS COMPONENTES DA TELA
        recyclerView = view.findViewById(R.id.lista_produtos);
        btnAdcionarProduto = view.findViewById(R.id.adcionar_produto);

        tirarFoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getData()!=null){
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    ImageView iV = imageView;
                    iV.setImageBitmap(bitmap);
                }
            }
        });
        buscaGaleria = registerForActivityResult(
                new ActivityResultContracts.GetContent(), result -> {
                    ImageView iV = imageView;
                    iV.setImageURI(result);
                    iV.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
        );

        //RECOMENDADO PELO GOOGLE
        recyclerView.setHasFixedSize(true);

        //INICIALIZA E SETA O LAYOUT MANAGER
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //ADICIONA PRODUTOS DO BANCO NO ARRAYLIST DE PRODUTOS
        produtoArrayList = produtosDAO.readAllProduto();
        //CRIA E SETA O ADAPTER
        mAdapter = new ProdutosAdapter(this.getContext(), produtoArrayList, this::onClickImagem);
        recyclerView.setAdapter(mAdapter);
        //BOTAO QUE FAZ APARECER O CARDVIEW DE ADICIONAR PRODUTO
        btnAdcionarProduto.setOnClickListener(adicionarProduto);

        return view;
    }

    private View.OnClickListener adicionarProduto = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_adicionar_produto);

            ImageView imageView = dialog.findViewById(R.id.imagem_produto_edicao);
            imageView.setOnClickListener(view1 -> onClickImagem(imageView));

            AutoCompleteTextView spinner = dialog.findViewById(R.id.tipo_produto_edicao);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),R.array.tipo, android.R.layout.simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            Button buttonCancelar = dialog.findViewById(R.id.cancelar_produto);
            buttonCancelar.setOnClickListener(view12 -> dialog.cancel());

            Button buttonSalvar = dialog.findViewById(R.id.salvar_produto);
            buttonSalvar.setOnClickListener(view13 -> {

                TextInputEditText txtNomeProduto = dialog.findViewById(R.id.nome_produto_edicao);
                TextInputEditText txtValorProduto = dialog.findViewById(R.id.valor_produto_atras);
                TextInputEditText txtMarcaProduto = dialog.findViewById(R.id.marca_produto_edicao);
                TextInputEditText txtQuantidadeProduto = dialog.findViewById(R.id.quantidade_produto_edicao);
                TextInputEditText txtDescricaoProduto = dialog.findViewById(R.id.complemento_produto_edicao);
                TextInputEditText txtMedidaProduto = dialog.findViewById(R.id.medida_produto_edicao);
                AutoCompleteTextView spinTipoProduto = dialog.findViewById(R.id.tipo_produto_edicao);
                ImageView imageViewProduto = dialog.findViewById(R.id.imagem_produto_edicao);

                boolean teste = false;
                if(txtNomeProduto.getText().toString().equals("") || txtNomeProduto.getText().equals(null)){
                    txtNomeProduto.setError("Este campo precisa estar preenchido");
                    teste=true;
                }if(txtValorProduto.getText().toString().equals("") || txtValorProduto.getText().toString().equals(null)){
                    txtValorProduto.setError("Este campo precisa estar preenchido");
                    teste=true;
                }if(txtQuantidadeProduto.getText().toString().equals("") || txtQuantidadeProduto.getText().toString().equals(null)){
                    txtQuantidadeProduto.setError("Este campo precisa estar preenchido");
                    teste=true;
                }try{
                    imageViewProduto.getDrawable();
                }catch(Exception e){
                    Toast.makeText(view.getContext(), "O Produto precisa de uma imagem", Toast.LENGTH_LONG);
                    return;
                }
                if(teste){
                    return;
                }
                ProdutosDAO produtosDAO = new ProdutosDAO(view13.getContext());

                BitmapDrawable drawable = (BitmapDrawable) imageViewProduto.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,10,stream);
                byte imagemBytes[] = stream.toByteArray();

                String nome = txtNomeProduto.getText().toString();
                Double valor = Double.parseDouble(txtValorProduto.getText().toString());
                String marca = txtMarcaProduto.getText().toString();
                int quant = Integer.parseInt(txtQuantidadeProduto.getText().toString());
                String complemento = txtDescricaoProduto.getText().toString();
                String medida = txtMedidaProduto.getText().toString();
                String tipo = spinTipoProduto.getText().toString();
                Produto p = new Produto(0,imagemBytes,nome,marca,complemento,medida,valor,quant,tipo);
                if(produtosDAO.createProduto(p)){
                    p.setIDProduto(produtosDAO.ultimoID());
                    produtoArrayList.add(p);
                    mAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(view13.getContext(),"Algo deu errado",Toast.LENGTH_LONG);
                }
                txtDescricaoProduto.setText("");
                txtMarcaProduto.setText("");
                txtMedidaProduto.setText("");
                txtValorProduto.setText("");
                txtQuantidadeProduto.setText("");
                txtNomeProduto.setText("");
                imageViewProduto.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
                dialog.cancel();
                mAdapter.notifyDataSetChanged();
            });
            dialog.create();
            dialog.show();
        }
    };

    private SearchView.OnQueryTextListener buscaProduto = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            ProdutosDAO produtosDAO = new ProdutosDAO(getContext());
            produtoArrayList.clear();
            produtoArrayList.addAll(produtosDAO.readProdutoNome(s));
            mAdapter.notifyDataSetChanged();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if(s.equals("")){
                ProdutosDAO produtosDAO = new ProdutosDAO(getContext());
                produtoArrayList.clear();
                produtoArrayList.addAll(produtosDAO.readAllProduto());
                mAdapter.notifyDataSetChanged();
            }
            return false;
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClickImagem(ImageView imageView) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialogo_imagem);

        TextView txtFoto = dialog.findViewById(R.id.txt_foto);
        txtFoto.setOnClickListener(view12 -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                tirarFoto.launch(intent);
            dialog.cancel();
        });
        TextView txtGaleria = dialog.findViewById(R.id.txt_galeria);
        txtGaleria.setOnClickListener(view1 -> {
            buscaGaleria.launch("image/*");
            dialog.cancel();
        });
        this.imageView = imageView;
        dialog.show();
    }
}