package com.example.tcc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CriarBancoSQL extends SQLiteOpenHelper {

    private static final String DATABASE = "banco.db";
    private static final String TABELA_PRODUTO = "produtos";
    private static final String TABELA_CLIENTE = "clientes";
    private static final String TABELA_ITEM_PEDIDO = "ItemPedido";
    private static final String TABELA_VENDAS = "vendas";
    private static final int VERSAO = 1;

    public CriarBancoSQL(@Nullable Context context) {
        super(context, DATABASE, null, VERSAO);;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
