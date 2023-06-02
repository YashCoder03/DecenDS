package com.example.decends;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.decends.utility.ApiCall;
import com.example.decends.utility.BackgroundService;
import com.example.decends.utility.NetworkChangeListener;
import com.example.decends.utility.ServerAlert;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static android.content.ContentValues.TAG;

public class DashBoard extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    TextView peer,sent,recived,bandwidth,username;
    private RequestQueue mRequestQueue;
    SwipeRefreshLayout swipeRefreshLayout;
    private StringRequest mStringRequest;

    private long startTime;
    private long activeTime;

    private String url = "";
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        url = getString(R.string.ipfsurl)+"/api/v0/stats/bw";

        Intent intent = new Intent(this, ApiCall.class);
        this.startService(intent);


        username = findViewById(R.id.coins);
        peer = findViewById(R.id.peer);
        sent = findViewById(R.id.sent);
        recived = findViewById(R.id.recived);
        bandwidth = findViewById(R.id.bandwidth);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String token = sh.getString("username", "");
        username.setText(token);

        startService(new Intent(this, BackgroundService.class));


        dialog dial = new dialog(this);
        dial.startLoadingdialog();


                RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jsonObjectRequest;
                JSONObject jsonObject =new JSONObject();

                String mRequestBody  = jsonObject.toString();

                final Boolean[] isError = {false};

                jsonObjectRequest =  new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //coins.setText(response.getString("coins_earned"));
                            peer.setText(response.getString("TotalOut").substring(0,4));
                            sent.setText(response.getString("RateOut").substring(0,4));
                            recived.setText(response.getString("RateIn").substring(0,4));
                            bandwidth.setText(response.getString("TotalIn").substring(0,4));
                            dial.dismissdialog();


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                       // Toast.makeText(DashBoard.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashBoard.this, error.toString(), Toast.LENGTH_SHORT).show();
                        dial.dismissdialog();
                        isError[0] = true;
                        ServerAlert serverAlert = new ServerAlert();
                        serverAlert.startAlert(DashBoard.this,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isError[0])
                {
                    mRequestQueue.add(jsonObjectRequest);
                    handler.postDelayed(this, 1000 * 60 ); // 10 seconds
                }
            }
        }, 0);




    }






    protected void onStart() {

        startService(new Intent(this, BackgroundService.class));

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
}
