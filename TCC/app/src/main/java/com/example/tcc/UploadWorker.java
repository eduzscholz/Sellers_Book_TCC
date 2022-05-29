package com.example.tcc;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tcc.vendas.Venda;
import com.example.tcc.vendas.VendasDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UploadWorker extends Worker {

    String CHANNEL_ID = "PAGAMENTOS";
    Context context;
    WorkerParameters workerParams;

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.workerParams = workerParams;
    }

    @NonNull
    @Override
    public Result doWork() {
        VendasDAO vendasDAO = new VendasDAO(context);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date atual = new Date();
        ArrayList<Venda> vendas = vendasDAO.readVendasSemPagamento(simpleDateFormat.format(atual));

        if(!vendas.isEmpty()){
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_logosb_06)
                    .setContentTitle("Seller's Book")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            if(vendas.size()==1){
                builder.setContentText("O cliente "+vendas.get(0).getNomeCliente()+" deve pagar hoje");
            }else if(vendas.size()>1){
                builder.setContentText("Diversos clientes devem pagar hoje");
            }

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            notificationManager.notify(10, builder.build());
        }
        return Result.success();
    }
}
