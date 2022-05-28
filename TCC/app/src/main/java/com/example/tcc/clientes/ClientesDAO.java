package com.example.tcc.clientes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tcc.vendas.VendasDAO;

import java.util.ArrayList;

public class ClientesDAO extends SQLiteOpenHelper {

    private static final String DATABASE = "banco.db";
    private static final String TABELA = "clientes";
    private static final int VERSAO = 1;
    private final Context context;

    public static final String COL_NOME = "nome";
    public static final String COL_CONTATO = "contato";
    public static final String COL_ENDERECO = "endereco";
    public static final String COL_ID = "clienteID";
    public static final String COL_CPF = "CPF";

    public ClientesDAO(@Nullable Context context) {
        super(context, DATABASE, null, VERSAO);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABELA + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                             + COL_NOME + " TEXT, "
                                                             + COL_CONTATO + " TEXT, "
                                                             + COL_ENDERECO + " TEXT, "
                                                             + COL_CPF + " TEXT);";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean createCliente(Cliente cliente){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_NOME, cliente.getNome());
        cv.put(COL_CONTATO, cliente.getContato());
        cv.put(COL_CPF, cliente.getCPF());
        cv.put(COL_ENDERECO, cliente.getEndereco());

        long insert = db.insert(TABELA, null, cv);
        db.close();
        return insert != -1;
    }

    public ArrayList<Cliente> readAllClientes(){
        ArrayList<Cliente> clienteArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA, null,null, null, null, null, COL_NOME + " ASC");

        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            String nome= cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME));
            String contato = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTATO));
            String cpf = cursor.getString(cursor.getColumnIndexOrThrow(COL_CPF));
            String endereco= cursor.getString(cursor.getColumnIndexOrThrow(COL_ENDERECO));
            clienteArrayList.add(new Cliente(id,nome,cpf,endereco,contato));
        }
        cursor.close();
        return clienteArrayList;
    }

    public int ultimoID(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABELA+" WHERE "+COL_ID+" = (SELECT MAX("+COL_ID+")  FROM "+TABELA+");", null);

        cursor.moveToNext();

        int i = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
        cursor.close();
        return i;
    }

    public Cliente readOneClienteID(int buscaID){
        Cliente cli;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA, null,COL_ID + " = ?", new String[] {String.valueOf(buscaID)}, null, null, null);
        cursor.moveToNext();

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
        String nome= cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME));
        String contato = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTATO));
        String cpf = cursor.getString(cursor.getColumnIndexOrThrow(COL_CPF));
        String endereco= cursor.getString(cursor.getColumnIndexOrThrow(COL_ENDERECO));

        cli = new Cliente(id,nome,cpf,endereco,contato);

        cursor.close();
        return cli;
    }

    public String readOneClienteIDNome(int buscaID){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA, new String[]{COL_NOME},COL_ID + " = ?", new String[] {String.valueOf(buscaID)}, null, null, null);
        cursor.moveToNext();

        String nome= cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME));

        cursor.close();
        return nome;
    }

    //BUSCA POR NOME
    public ArrayList<Cliente> readClienteNome(String buscaNome){
        ArrayList<Cliente> clienteArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA, null,COL_NOME + " LIKE ?", new String[] {"%"+buscaNome+"%"}, null, null, COL_NOME + " ASC");

        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            String nome= cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME));
            String contato = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTATO));
            String cpf = cursor.getString(cursor.getColumnIndexOrThrow(COL_CPF));
            String endereco= cursor.getString(cursor.getColumnIndexOrThrow(COL_ENDERECO));
            clienteArrayList.add(new Cliente(id,nome,cpf,endereco,contato));
        }
        cursor.close();
        return clienteArrayList;
    }

    public boolean updateCliente(int id, String[] coluna, String[] dados){
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

    public Cliente updateCliente(int id,String nome, String contato, String endereco, String CPF){
        if(updateCliente(id,new String[] {COL_NOME,COL_CPF,COL_CONTATO,COL_ENDERECO},new String[] {nome,CPF,contato,endereco}))
        {
            return new Cliente(id,nome,CPF,endereco,contato);
        }else
            return null;
    }

    public boolean deleteOneCliente(int id){
        String sql = COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        SQLiteDatabase db = getWritableDatabase();

        int delete = db.delete(TABELA,sql,selectionArgs);
        db.close();
        return delete != 0;
    }

    public int deleteManyCliente(@NonNull int[] id){
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

    public boolean verificarPagamentoCliente(int id){
        VendasDAO vendasDAO = new VendasDAO(context);
        return vendasDAO.verificarPagamentoCliente(id);
    }
}
