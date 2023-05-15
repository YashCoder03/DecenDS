package com.example.decends.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;

public class ServerAlert {


    public void startAlert(Context myActivity, IntentFilter intent)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
        builder.setTitle("Server Error");
        builder.setMessage("Do You Want to Retry")
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startAlert(myActivity,intent);
                    }
                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                }).show();
    }

}
