package com.example.tcc.vendas.itemPedido;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ItemPedidoDAO extends SQLiteOpenHelper{

    private static final String DATABASE = "banco.db";
    private static final String TABELA = "ItemPedido";
    private static final int VERSAO = 1;

    private static final String COL_ID = "itemPedidoID";
    private static final String COL_QUANTIDADE = "quantidade";
    private static final String COL_PRECO= "preco";
    private static final String COL_PRODUTO_ID = "produtoID";
    private static final String COL_VENDA_ID =  "vendaID";

    private Context context;

    public ItemPedidoDAO(@Nullable Context context) {
        super(context, DATABASE, null, VERSAO);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable =
                "CREATE TABLE " + TABELA + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_QUANTIDADE + " INT, "
                + COL_PRECO + " REAL, "
                + COL_PRODUTO_ID + " INT, "
                + COL_VENDA_ID + " INT, "
                + " FOREIGN KEY (" + COL_PRODUTO_ID + ") REFERENCES produtos(produtoID),"
                + " FOREIGN KEY (" + COL_VENDA_ID + ") REFERENCES vendas(vendaID));" ;
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABELA;

        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public boolean createItemPedido(ItemPedido itemPedido){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_PRECO, itemPedido.getPreco());
        cv.put(COL_QUANTIDADE, itemPedido.getQuantidade());
        cv.put(COL_PRODUTO_ID, itemPedido.getProdutoID());
        cv.put(COL_VENDA_ID, itemPedido.getVendaID());

        long insert = db.insert(TABELA, null, cv);
        db.close();
        return insert != -1;
    }

    public ArrayList<ItemPedido> readAllItemPedido(){
        ArrayList<ItemPedido> itemPedidoArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA,null,null,null,null,null, COL_ID + "ASC");

        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            double preco = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRECO));
            int quant = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTIDADE));
            int idprod = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRODUTO_ID));
            int idvenda = cursor.getInt(cursor.getColumnIndexOrThrow(COL_VENDA_ID));
            itemPedidoArrayList.add(new ItemPedido(id,quant,preco,idprod,idvenda));
        }
        cursor.close();
        return itemPedidoArrayList;
    }

    public ArrayList<ItemPedido> readManyItemPedidoVendaID(int vendaID){
        ArrayList<ItemPedido> itemPedidoArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA,null,COL_VENDA_ID + " = ?",new String[] {String.valueOf(vendaID)},null,null, COL_ID + " ASC");

        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            double preco = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRECO));
            int quant = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTIDADE));
            int idprod = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRODUTO_ID));
            int idvenda = cursor.getInt(cursor.getColumnIndexOrThrow(COL_VENDA_ID));
            itemPedidoArrayList.add(new ItemPedido(id,quant,preco,idprod,idvenda));        }
        cursor.close();
        return itemPedidoArrayList;
    }

    public ArrayList<Integer> readManyIDsVendaID(int vendaID){
        ArrayList<ItemPedido> itemPedidoArrayList = readManyItemPedidoVendaID(vendaID);
        ArrayList<Integer> ids = new ArrayList<>();
        for(int i=0; i<itemPedidoArrayList.size();i++){
            ids.add(itemPedidoArrayList.get(i).getItemPedidoID());
        }
        return ids;
    }

    public boolean updateItemPedido(int id, String[] coluna, String[] dados){
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

    public boolean updateItemPedido(int id, int idProd, int idVenda, double preco, int quant){
        String idP = String.valueOf(idProd);
        String idV = String.valueOf(idVenda);
        String Pre = String.valueOf(preco);
        String qua = String.valueOf(quant);
        return updateItemPedido(id,new String[] {COL_PRODUTO_ID,COL_VENDA_ID,COL_PRECO,COL_QUANTIDADE},new String[] {idP,idV,Pre,qua});
    }

    public boolean deleteOneItemPedido(int id){
        String sql = COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        SQLiteDatabase db = getWritableDatabase();

        int delete = db.delete(TABELA,sql,selectionArgs);
        db.close();
        return delete != 0;
    }

    public int deleteManyItemPedido(@NonNull int[] id){
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

    public int deleteManyItemPedidoIdVenda(int id){
        String sql = COL_VENDA_ID + " = ?";
        SQLiteDatabase db = getWritableDatabase();
        int delete;
        delete = db.delete(TABELA, sql, new String[] {String.valueOf(id)});
        db.close();
        return delete;
    }
}
