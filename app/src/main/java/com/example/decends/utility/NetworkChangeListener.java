package com.example.decends.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.decends.SplashScreen;

import androidx.appcompat.app.AlertDialog;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!Connection.isConnectedToInternet(context))
        {
            AlertDialog.Builder builder =  new AlertDialog.Builder(context);
            builder.setTitle("Internet Connection");
            builder.setMessage("Do You Want to Retry?")
                    .setCancelable(false)
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            onReceive(context,intent);
                        }
                    })
                    .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            System.exit(0);
                        }
                    })
                    .show();
        }

    }
}
