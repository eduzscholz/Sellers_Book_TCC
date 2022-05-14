package com.example.tcc.produtos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProdutosDAO extends SQLiteOpenHelper{

    private static final String DATABASE = "banco.db";
    private static final String TABELA = "produtos";
    private static final int VERSAO = 1;

    private static final String COL_NOME = "nome";
    private static final String COL_MARCA = "marca";
    private static final String COL_COMPLEMENTO = "complemento";
    private static final String COL_PRECO = "preco";
    private static final String COL_MEDIDA = "Medida";
    private static final String COL_QUANTIDADE = "quantidade";
    private static final String COL_ID = "produtoID";
    private static final String COL_TIPO = "tipo";
    private static final String COL_IMG = "imagem";

    public ProdutosDAO(@Nullable Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABELA + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                             + COL_NOME + " TEXT, "
                                                             + COL_MARCA + " TEXT, "
                                                             + COL_PRECO + " REAL, "
                                                             + COL_QUANTIDADE + " INT, "
                                                             + COL_TIPO + " TEXT, "
                                                             + COL_COMPLEMENTO + " TEXT, "
                                                             + COL_MEDIDA + " TEXT, "
                                                             + COL_IMG + " INT);";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABELA;

        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    //INSERE LINHA NA TABELA
    public boolean createProduto(Produto produto){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_NOME, produto.getNome());
        cv.put(COL_MARCA, produto.getMarca());
        cv.put(COL_PRECO, produto.getPreco());
        cv.put(COL_QUANTIDADE, produto.getQuantidade());
        cv.put(COL_TIPO, produto.getTipoDeProduto());
        cv.put(COL_COMPLEMENTO, produto.getComplemento());
        cv.put(COL_MEDIDA, produto.getMedida());
        cv.put(COL_IMG, produto.getImg());

        long insert = db.insert(TABELA, null, cv);
        db.close();
        return insert != -1;
    }

    //LE TODAS AS LINHAS DA TABELA
    public ArrayList<Produto> readAllProduto(){
        ArrayList<Produto> produtoArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA, null,null, null, COL_MARCA, null, COL_NOME + " ASC");

        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            int img = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMG));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(COL_COMPLEMENTO));
            String marca = cursor.getString(cursor.getColumnIndexOrThrow(COL_MARCA));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME));
            Double preco = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRECO));
            int quant = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTIDADE));
            String tipo = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIPO));
            String unidadeMedida = cursor.getString(cursor.getColumnIndexOrThrow(COL_MEDIDA));
            produtoArrayList.add(new Produto(id,img,nome,marca,desc,unidadeMedida,preco,quant,tipo));
        }

        cursor.close();
        return produtoArrayList;
    }
    //LE UMA LINHA DA TABELA POR ID
    public Produto readOneProdutoID(int buscaID){
        Produto produto;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA, null,COL_ID + " = ?", new String[] {String.valueOf(buscaID)}, null, null, null);
        cursor.moveToNext();

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
        int img = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMG));
        String desc = cursor.getString(cursor.getColumnIndexOrThrow(COL_COMPLEMENTO));
        String marca = cursor.getString(cursor.getColumnIndexOrThrow(COL_MARCA));
        String nome = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME));
        Double preco = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRECO));
        int quant = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTIDADE));
        String tipo = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIPO));
        String unidadeMedida = cursor.getString(cursor.getColumnIndexOrThrow(COL_MEDIDA));
        produto = new Produto(id,img,nome,marca,desc,unidadeMedida,preco,quant,tipo);

        cursor.close();
        return produto;
    }
    //LE VARIAS LINHAS DA TABELA POR NOME
    public ArrayList<Produto> readProdutoNome(String buscaNome){
        ArrayList<Produto> produtoArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA, null,COL_NOME + " LIKE ? OR " + COL_MARCA + " LIKE ?", new String[] {"%"+buscaNome+"%","%"+buscaNome+"%"}, COL_MARCA, null, COL_NOME + " ASC");

        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            int img = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMG));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(COL_COMPLEMENTO));
            String marca = cursor.getString(cursor.getColumnIndexOrThrow(COL_MARCA));
            String nome = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME));
            Double preco = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRECO));
            int quant = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTIDADE));
            String tipo = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIPO));
            String unidadeMedida = cursor.getString(cursor.getColumnIndexOrThrow(COL_MEDIDA));
            produtoArrayList.add(new Produto(id,img,nome,marca,desc,unidadeMedida,preco,quant,tipo));
        }

        cursor.close();
        db.close();
        return produtoArrayList;
    }
    //ATUALIZA UMA LINHA COM ESCOLHA DOS DADOS
    public boolean updateProduto(int id, String[] coluna, String[] dados){
        ContentValues cv = new ContentValues();
        String selection = COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        SQLiteDatabase db = getWritableDatabase();

        for(int i=0;i< coluna.length;i++){
            cv.put(coluna[i],dados[i]);
        }
        int count = db.update(TABELA,cv,selection,selectionArgs);

        db.close();
        return count!=0;
    }
    //ATUALIZA TODOS OS DADOS DE UMA LINHA
    public boolean updateProduto(int id, String nome, String marca, String complemento, Double preco, String medida, int quantidade, String tipo, int imagem){

        String qua = String.valueOf(quantidade);
        String img = String.valueOf(imagem);
        String pre = String.valueOf(preco);

        return  updateProduto(id, new String[] {COL_NOME,COL_MARCA,COL_COMPLEMENTO,COL_PRECO,COL_MEDIDA,COL_QUANTIDADE,COL_TIPO,COL_IMG}, new String[] {nome,marca,complemento,pre,medida,qua,tipo,img});
    }
    //DELETA UM PRODUTO POR ID
    public boolean deleteOneProduto(int id){
        String sql = COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        SQLiteDatabase db = getWritableDatabase();

        int delete = db.delete(TABELA,sql,selectionArgs);
        db.close();
        return delete != 0;
    }
    //DELETA VARIOS PRODUTOS POR ID
    public int deleteManyProduto(@NonNull int[] id){
        String sql = COL_ID + " = ?";
        SQLiteDatabase db = getWritableDatabase();
        int delete=0;

        for (int j : id) {
            String[] selectionArgs = {String.valueOf(j)};
            delete = delete + db.delete(TABELA, sql, selectionArgs);
        }

        db.close();
        return delete;
    }
}
