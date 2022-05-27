package com.example.tcc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CriarBancoSQL extends SQLiteOpenHelper {

    private static final String DATABASE = "banco.db";
    private static final int VERSAO = 1;


    public CriarBancoSQL(@Nullable Context context) {
        super(context, DATABASE, null, VERSAO);
        getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        criaProduto(sqLiteDatabase);
        criaCliente(sqLiteDatabase);
        criaVenda(sqLiteDatabase);
        criaItemPedido(sqLiteDatabase);
    }

    public void criaVenda(SQLiteDatabase sqLiteDatabase){
        String TABELA = "vendas";
        String COL_ID = "vendaID";
        String COL_DATA_COMPRA = "dataCompra";
        String COL_ID_CLIENTE= "clienteID";
        String COL_DATA_PAGAMENTO = "dataPagamento";
        String COL_PREVISAO = "previsaoPagamento";
        String COL_NOME_CLIENTE = "nomeCliente";

        String createTable = "CREATE TABLE IF NOT EXISTS " + TABELA + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_ID_CLIENTE + " INT, "
                + COL_NOME_CLIENTE + " TEXT, "
                + COL_DATA_COMPRA + " DATE, "
                + COL_DATA_PAGAMENTO + " DATE, "
                + COL_PREVISAO + " DATE);";
        sqLiteDatabase.execSQL(createTable);
    }

    public void criaItemPedido(SQLiteDatabase sqLiteDatabase){
        String TABELA = "ItemPedido";
        String COL_ID = "itemPedidoID";
        String COL_QUANTIDADE = "quantidade";
        String COL_PRECO = "preco";
        String COL_PRODUTO_ID = "produtoID";
        String COL_VENDA_ID =  "vendaID";
        String COL_NOME_PRODUTO = "nomeProduto";

        String createTable =
                "CREATE TABLE IF NOT EXISTS " + TABELA + " ("
                        + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COL_NOME_PRODUTO + " TEXT, "
                        + COL_QUANTIDADE + " INT, "
                        + COL_PRECO + " REAL, "
                        + COL_PRODUTO_ID + " INT, "
                        + COL_VENDA_ID + " INT, "
                        + " FOREIGN KEY (" + COL_VENDA_ID + ") REFERENCES vendas (vendaID)"
                        + " ON DELETE CASCADE);" ;
        sqLiteDatabase.execSQL(createTable);
    }

    public void criaProduto(SQLiteDatabase sqLiteDatabase){
        String TABELA = "produtos";
        String COL_NOME = "nome";
        String COL_MARCA = "marca";
        String COL_COMPLEMENTO = "complemento";
        String COL_PRECO = "preco";
        String COL_MEDIDA = "Medida";
        String COL_QUANTIDADE = "quantidade";
        String COL_ID = "produtoID";
        String COL_TIPO = "tipo";
        String COL_IMG = "imagem";

        String createTable = "CREATE TABLE IF NOT EXISTS " + TABELA + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NOME + " TEXT, "
                + COL_MARCA + " TEXT, "
                + COL_PRECO + " REAL, "
                + COL_QUANTIDADE + " INT, "
                + COL_TIPO + " TEXT, "
                + COL_COMPLEMENTO + " TEXT, "
                + COL_MEDIDA + " TEXT, "
                + COL_IMG + " BLOB);";
        sqLiteDatabase.execSQL(createTable);
    }

    public void criaCliente(SQLiteDatabase sqLiteDatabase){
        String TABELA = "clientes";
        String COL_NOME = "nome";
        String COL_CONTATO = "contato";
        String COL_ENDERECO = "endereco";
        String COL_ID = "clienteID";
        String COL_CPF = "CPF";

        String createTable = "CREATE TABLE IF NOT EXISTS " + TABELA + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NOME + " TEXT, "
                + COL_CONTATO + " TEXT, "
                + COL_ENDERECO + " TEXT, "
                + COL_CPF + " TEXT);";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
