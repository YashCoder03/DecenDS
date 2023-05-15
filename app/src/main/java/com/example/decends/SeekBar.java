package com.example.decends;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.decends.Croller.Croller;
import com.example.decends.Croller.OnCrollerChangeListener;
import com.example.decends.utility.NetworkChangeListener;
import com.example.decends.utility.ServerAlert;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SeekBar extends AppCompatActivity {

    private TextView mTextView;

    private ImageButton mButton;

    private int mSelectedSize;

    String url;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seekbar);
        url = getString(R.string.url)+"/onboarding";
        mTextView = findViewById(R.id.textView);
        mButton = findViewById(R.id.button2);


        File internalStorage = Environment.getDataDirectory();
        long freeSpace = internalStorage.getFreeSpace();
        long gigabytes = (long) freeSpace / (1024 * 1024 * 1024);

        Croller croller = (Croller) findViewById(R.id.croller);
        croller.setIndicatorWidth(10);
        croller.setBackCircleColor(Color.parseColor("#EDEDED"));
        croller.setMainCircleColor(Color.WHITE);
        croller.setMax((int) gigabytes);
        croller.setStartOffset(45);
        croller.setIsContinuous(true);
        croller.setLabelColor(Color.BLACK);
        croller.setProgressPrimaryColor(Color.parseColor("#0B3C49"));
        croller.setIndicatorColor(Color.parseColor("#0B3C49"));
        croller.setProgressSecondaryColor(Color.parseColor("#EEEEEE"));
        //Toast.makeText(this, "seekbar on", Toast.LENGTH_SHORT).show();

        croller.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {
                // use the progress
                mSelectedSize = progress;
                mTextView.setText(String.format("%d GB", mSelectedSize));
            }

            @Override
            public void onStartTrackingTouch(Croller croller) {
                // tracking started
            }

            @Override
            public void onStopTrackingTouch(Croller croller) {
                // tracking stopped
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences peer = getSharedPreferences("peer",MODE_PRIVATE);
                SharedPreferences.Editor editor = peer.edit();
                editor.putBoolean("login",true);
                editor.apply();

                SeekBar.dialog d =new SeekBar.dialog(SeekBar.this);
                d.startLoadingdialog();

                new Thread(() -> {

                    RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest jsonObjectRequest;
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put( "storageRented",mSelectedSize * 1024);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    String mRequestBody = jsonObject.toString();

                    jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            ServerAlert serverAlert = new ServerAlert();
                            serverAlert.startAlert(SeekBar.this,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

                        }
                    })
                    {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() {
                            try {
                                return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                                return null;
                            }
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            String token = sh.getString("token", "");

                            Map<String,String> params = new HashMap<>();
                            params.put( "Authorization",token);
                            return  params;

                        }
                    };
                    mRequestQueue.add(jsonObjectRequest);
                    createFile(mSelectedSize);
                    try {

                        // code runs in a thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                d.dismissdialog();
                                startActivity(new Intent(SeekBar.this,DashBoard.class));
                                finish();
                            }
                        });
                    } catch (final Exception ex) {
                    }
                }).start();

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

            //layoutinflater object and use activity to get layout inflater
            LayoutInflater inflater = activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.loading, null));

            builder.setCancelable(false);


            dialog = builder.create();
            dialog.show();
        }

        // dismiss method
        void dismissdialog() {
            dialog.dismiss();
        }

    }

    void createFile(int size)
    {

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

