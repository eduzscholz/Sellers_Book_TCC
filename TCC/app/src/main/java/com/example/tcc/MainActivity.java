package com.example.tcc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements Pagamento {

    String CHANNEL_ID = "PAGAMENTOS";
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
        createNotificationChannel();
        PeriodicWorkRequest uploadWorkRequest = new
                PeriodicWorkRequest.Builder(UploadWorker.class, 12, TimeUnit.HOURS,5,TimeUnit.MINUTES).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "verificaPagamentos",
                ExistingPeriodicWorkPolicy.KEEP,
                uploadWorkRequest);
         //LINKA O MAIN ACTIVITY COM O ACTIVY_MAIN.XML
        setContentView(R.layout.activity_main);
        new CriarBancoSQL(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Bem-Vindo");
        //PARTES DO LAYOUT
        TabLayout tabLayout = findViewById(R.id.TabLayout);
        ViewPager2 viewPager = findViewById(R.id.view_Pager);
        //INICIALIZA O ADAPTADOR DO VIEWPAGER2
        vpAdapater = new VPAdapater(this, this);
        //SETA O ADAPTADOR COM O VIEW PAGER
        viewPager.setAdapter(vpAdapater);
        //CONECTA O VIEWPAGER2 COM O TABLAYOUT
        new TabLayoutMediator(tabLayout, viewPager,(tab, position) -> tab.setText(titulos[position])).attach();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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