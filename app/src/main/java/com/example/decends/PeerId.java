package com.example.decends;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.decends.utility.NetworkChangeListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PeerId extends AppCompatActivity {

    Button pbtn;
    TextView ptext;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peer_id);

        pbtn = findViewById(R.id.pbtn);
        ptext = findViewById(R.id.ptxt);

        pbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String peerID =  ptext.getText().toString();

                if(check(peerID))
                {

                    dialog d =new dialog(PeerId.this);
                    d.startLoadingdialog();
                    Toast.makeText(PeerId.this, "progressbar started", Toast.LENGTH_SHORT).show();
                    new Thread(() -> {

                        createFile();
                        try {

                            // code runs in a thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    d.dismissdialog();
                                    SharedPreferences peer = getSharedPreferences("peer",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = peer.edit();
                                    editor.putBoolean("login",true);
                                    editor.apply();
                                    startActivity(new Intent(PeerId.this,DashBoard.class));
                                    finish();
                                }
                            });
                        } catch (final Exception ex) {
                            //Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }).start();


                }
                else
                {
                    Toast.makeText(PeerId.this, "Wrong PeerID", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onStart() {

        IntentFilter intentFilter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);
        super.onStart();
    }
    protected void onStop(){
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    boolean check(String peerId)
    {
        return peerId.equals("123456");
    }


    public class dialog {
        // 2 objects activity and dialog
        private Activity activity;
        private AlertDialog dialog;

        // constructor of dialog class
        // with parameter activity
        dialog(Activity myActivity) {
            activity = myActivity;
        }

        @SuppressLint("InflateParams")
        void startLoadingdialog() {

            // adding ALERT Dialog builder object and passing activity as parameter
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            // layoutinflater object and use activity to get layout inflater
            LayoutInflater inflater = activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.loading, null));
            builder.setCancelable(true);

            dialog = builder.create();
            dialog.show();
        }

        // dismiss method
        void dismissdialog() {
            dialog.dismiss();
        }

    }


    void createFile()
    {
        Bundle extras = getIntent().getExtras();
        int size = 0;
        if(extras != null)
        {
            size = extras.getInt("size");
        }
        //Do long operation stuff here search stuff
        Context context = getBaseContext();
        //Toast.makeText(this, "Creating File", Toast.LENGTH_SHORT).show();
        String filename = "testfile.dat";
        long filesize = (long) size * 1024 * 1024 * 1024 * 100 / 100; // Calculate the selected size in bytes.
        Random random = new Random();
        byte[] buffer = new byte[1024];
        FileOutputStream outputStream = null;
        try {
            //Toast.makeText(this, "Into try Block", Toast.LENGTH_SHORT).show();
            File file = new File( context.getFilesDir()+filename);
            outputStream = new FileOutputStream(file);
            //Toast.makeText(this, "Mid try Block", Toast.LENGTH_SHORT).show();
            long bytesWritten = 0;
            while (bytesWritten < filesize) {
                random.nextBytes(buffer);
                outputStream.write(buffer);
                bytesWritten += buffer.length;
            }
            outputStream.flush();
            outputStream.close();

            //mTextView.setText("File created successfully!");
            //Toast.makeText(this, "Successfull", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            //Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            //mTextView.setText("Failed to create file.");
        }
    }


}
