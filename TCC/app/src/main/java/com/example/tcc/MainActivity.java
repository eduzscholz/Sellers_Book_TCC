package com.example.tcc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.tcc.vendas.VendasAdapter;
import com.example.tcc.vendas.VendasFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity implements VendasAdapter.Pagamento {

    //PARTES DO LAYOUT
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private VPAdapater vpAdapater;
    //TITULOS DAS TABS
    private final String[] titulos = {"INICIO","PRODUTOS","VENDAS","CLIENTES"};

    //TODO TELA INICIAL
    //TODO ARRUMAR CASO NAO INSIRA DADOS PRODUTO E CLIENTE
    //TODO NOTIFICAÇÃO

    //TODO ATUALIZAR CLIENTES PAGOS
    //TODO QUANTIDADE ZERO
    //TODO PADROES                                       ?
    //TODO CONSTRASTES
    //ANIMACOES
    //ARRUMAR PRODUTOS TABELA QUE NEM CLIENTE PARA VENDA

    //futuro
    //parcelas
    //pdf

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LINKA O MAIN ACTIVITY COM O ACTIVY_MAIN.XML
        setContentView(R.layout.activity_main);
        new CriarBancoSQL(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        toolbar.setTitle("Bem-Vindo");
        //ACHA OS COMPONENTES NO LAYOUT
        tabLayout = findViewById(R.id.TabLayout);
        viewPager = findViewById(R.id.view_Pager);
        //INICIALIZA O ADAPTADOR DO VIEWPAGER2
        vpAdapater = new VPAdapater(this, this::atualizaCliente);
        //SETA O ADAPTADOR COM O VIEW PAGER
        viewPager.setAdapter(vpAdapater);
        //viewPager.setOffscreenPageLimit(1);
        //CONECTA O VIEWPAGER2 COM O TABLAYOUT
        new TabLayoutMediator(tabLayout,viewPager,(tab, position) -> tab.setText(titulos[position])).attach();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public void atualizaCliente() {
        vpAdapater.notifyDataSetChanged();
    }
}