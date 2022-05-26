package com.example.tcc.vendas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tcc.CriarBancoSQL;
import com.example.tcc.clientes.ClientesDAO;
import com.example.tcc.vendas.itemPedido.ItemPedido;
import com.example.tcc.vendas.itemPedido.ItemPedidoDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VendasDAO extends SQLiteOpenHelper {

    private static final String DATABASE = "banco.db";
    private static final String TABELA = "vendas";
    private static final int VERSAO = 1;

    public static final String COL_ID = "vendaID";
    public static final String COL_DATA_COMPRA = "dataCompra";
    public static final String COL_ID_CLIENTE= "clienteID";
    public static final String COL_DATA_PAGAMENTO = "dataPagamento";
    public static final String COL_PREVISAO = "previsaoPagamento";
    public static final String COL_NOME_CLIENTE = "nomeCliente";

    private static final String TABELA_CLIENTE = "clientes";
    private static final String FKID_CLIENTE = "clienteID";

    private Context context;

    public VendasDAO(@Nullable Context context) {
        super(context, DATABASE, null, VERSAO);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        CriarBancoSQL criarBancoSQL = new CriarBancoSQL(context);
        criarBancoSQL.criaVenda(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public boolean createVenda(Venda venda){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        cv.put(COL_ID_CLIENTE,venda.getClienteID());
        cv.put(COL_DATA_COMPRA,simpleDateFormat.format(venda.getDataCompra()));
        cv.put(COL_NOME_CLIENTE,venda.getNomeCliente());
        if(venda.getDataPagamento()==null){
            cv.put(COL_DATA_PAGAMENTO, "");
        }else {
            cv.put(COL_DATA_PAGAMENTO, simpleDateFormat.format(venda.getDataPagamento()));
        }
        cv.put(COL_PREVISAO,simpleDateFormat.format(venda.getPrevisao()));

        long insert = db.insert(TABELA, null, cv);
        db.close();
        return insert != -1;
    }

    public int ultimoID(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABELA+" WHERE "+COL_ID+" = (SELECT MAX("+COL_ID+")  FROM "+TABELA+");", null);

        cursor.moveToNext();

        return cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
    }

    public ArrayList<Venda> readAllVenda(){
        ArrayList<Venda> vendaArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        ClientesDAO clientesDAO = new ClientesDAO(context);

        Cursor cursor = db.query(TABELA, null, null, null, null, null, COL_ID + " DESC");
        Date dataPagamento, dataCompra, previsao;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO(context);

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            try {
                dataCompra = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA_COMPRA)));
                if(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA_PAGAMENTO)).equals("")){
                    dataPagamento = null;
                }else {
                    dataPagamento = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA_PAGAMENTO)));
                }
                previsao = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COL_PREVISAO)));
                int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_CLIENTE));
                String nomeCliente = clientesDAO.readOneClienteIDNome(idCliente);
                ArrayList<ItemPedido> itemPedidoArrayList = itemPedidoDAO.readManyItemPedidoVendaID(id);
                vendaArrayList.add(new Venda(id,nomeCliente,dataCompra,dataPagamento,previsao,idCliente,itemPedidoArrayList));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return vendaArrayList;
    }

    public ArrayList<Venda> readManyVendaNome(String nome){
        ArrayList<Venda> vendaArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        ClientesDAO clientesDAO = new ClientesDAO(context);

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABELA+" AS v INNER JOIN clientes AS c ON c.clienteID=v.clienteID AND c.nome LIKE ?",new String[] {"%"+nome+"%"});

        Date dataPagamento, dataCompra, previsao;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO(context);

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            try {
                dataCompra = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA_COMPRA)));
                dataPagamento = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA_PAGAMENTO)));
                previsao = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COL_PREVISAO)));
                int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_CLIENTE));
                String nomeCliente = clientesDAO.readOneClienteIDNome(idCliente);
                ArrayList<ItemPedido> itemPedidoArrayList = itemPedidoDAO.readManyItemPedidoVendaID(id);
                vendaArrayList.add(new Venda(id,nomeCliente,dataCompra,dataPagamento,previsao,idCliente,itemPedidoArrayList));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return vendaArrayList;
    }

    public boolean updateVenda(int id, String[] coluna, String[] dados){
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

    public boolean updateVenda(int id, Date dataCompra, Date previsão, Date dataPagamento, int CID, String nomeCliente){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dtC = simpleDateFormat.format(dataCompra);
        String dtP = simpleDateFormat.format(dataPagamento);
        String pre = simpleDateFormat.format(previsão);
        String cid = String.valueOf(CID);

        return updateVenda(id, new String[] {COL_DATA_COMPRA,COL_PREVISAO,COL_DATA_PAGAMENTO,COL_ID_CLIENTE,COL_NOME_CLIENTE}, new String[] {dtC,pre,dtP,cid,nomeCliente});
    }

    public boolean deleteOneVenda(int id){
        String sql = COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        SQLiteDatabase db = getWritableDatabase();

        ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO(context);
        if(itemPedidoDAO.deleteManyItemPedidoIdVenda(id)==0){
            return false;
        }else {
            int delete = db.delete(TABELA, sql, selectionArgs);
            db.close();
            return delete != 0;
        }
    }

    public int deleteManyVenda(@NonNull int[] id){
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

    public boolean verificarPagamentoCliente(int buscaID){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA, null,COL_ID_CLIENTE + " = ?", new String[] {String.valueOf(buscaID)}, null, null, null);

        while (cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA_PAGAMENTO)).equals("")){
                return false;
            }
        }
        return true;
    }

    public ArrayList<Venda> readVendaMes(String mes){
        ArrayList<Venda> vendaArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        ClientesDAO clientesDAO = new ClientesDAO(context);
        String query = "SELECT * FROM "+TABELA+" where strftime('%m', "+COL_DATA_PAGAMENTO+") = '"+mes+"' " +
                        "OR strftime('%m', "+COL_PREVISAO+") = '"+mes+"' " +
                        "OR ("+COL_PREVISAO+" < date('now') and "+COL_DATA_PAGAMENTO+" = null) ORDER BY "+COL_PREVISAO+" ASC;";
        Cursor cursor = db.rawQuery(query,null);

        Date dataPagamento, dataCompra, previsao;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO(context);

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            try {
                dataCompra = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA_COMPRA)));
                if(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA_PAGAMENTO)).equals("")){
                    dataPagamento = null;
                }else {
                    dataPagamento = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA_PAGAMENTO)));
                }
                previsao = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(COL_PREVISAO)));
                int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_CLIENTE));
                String nomeCliente = clientesDAO.readOneClienteIDNome(idCliente);
                ArrayList<ItemPedido> itemPedidoArrayList = itemPedidoDAO.readManyItemPedidoVendaID(id);
                vendaArrayList.add(new Venda(id,nomeCliente,dataCompra,dataPagamento,previsao,idCliente,itemPedidoArrayList));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return vendaArrayList;
    }
}
