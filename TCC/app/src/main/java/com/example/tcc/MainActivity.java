package com.example.tcc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    //PARTES DO LAYOUT
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    //TITULOS DAS TABS
    private final String[] titulos = {"INICIO","PRODUTOS","VENDAS","CLIENTES"};

    //todo camera                                       X
    //TODO BARRA DE PESQUISA
    //TODO ARRUMAR CLIENTE configurar banco             X
    //TODO NOTIFICAÇÃO
    //TODO TELA INICIAL

    //TODO CLASSE PARA CRIAR O BANCO
    //TODO ATUALIZAR CLIENTES PAGOS
    //TODO QUANTIDADE ZERO
    //TODO PADROES                                       ?


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LINKA O MAIN ACTIVITY COM O ACTIVY_MAIN.XML
        setContentView(R.layout.activity_main);
        new CriarBancoSQL(this);

        Toolbar Toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(Toolbar);
        //ACHA OS COMPONENTES NO LAYOUT
        tabLayout = findViewById(R.id.TabLayout);
        viewPager = findViewById(R.id.view_Pager);
        //INICIALIZA O ADAPTADOR DO VIEWPAGER2
        VPAdapater vpAdapater = new VPAdapater(this);
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
}