package com.example.tcc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity implements Pagamento {

    //PARTES DO LAYOUT
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private VPAdapater vpAdapater;
    //TITULOS DAS TABS
    private final String[] titulos = {"INICIO","PRODUTOS","VENDAS","CLIENTES"};

    //TODO PADROES                                       ?
    //TODO TESTES UNITARIOS
    //TODO NOTIFICAÇÃO

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
        toolbar.setTitle("Bem-Vindo");
        //ACHA OS COMPONENTES NO LAYOUT
        tabLayout = findViewById(R.id.TabLayout);
        viewPager = findViewById(R.id.view_Pager);
        //INICIALIZA O ADAPTADOR DO VIEWPAGER2
        vpAdapater = new VPAdapater(this, this::atualizaViewPager);
        //SETA O ADAPTADOR COM O VIEW PAGER
        viewPager.setAdapter(vpAdapater);
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
    public void atualizaViewPager(int position) {

        if(position==0){
            vpAdapater.notifyItemChanged(2);
            vpAdapater.notifyItemChanged(3);
        }else if(position==2) {
            vpAdapater.notifyItemChanged(0);
            vpAdapater.notifyItemChanged(3);
        }
    }
}