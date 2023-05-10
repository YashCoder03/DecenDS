package com.example.decends;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

import com.example.decends.utility.NetworkChangeListener;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen  extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences splash = getSharedPreferences("peer",MODE_PRIVATE);
                boolean isLogin = splash.getBoolean("login",false);
                if(isLogin)
                {
                    startActivity(new Intent(SplashScreen.this,DashBoard.class));
                    finish();
                }
                else
                {
                    startActivity(new Intent(SplashScreen.this,onBoarding.class));
                    finish();
                }

            }
        },2000);




    }



}
