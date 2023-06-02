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
import android.widget.ImageButton;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.decends.utility.NetworkChangeListener;
import com.example.decends.utility.ServerAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PeerId extends AppCompatActivity {

    ImageButton rent;
    TextView peerIdtxt;
    TextView userNametxt;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    String url = "";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peer_id);
        url = getString(R.string.url)+"/android_auth";
        //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        rent = findViewById(R.id.rent);
        userNametxt = findViewById(R.id.username);
        peerIdtxt = findViewById(R.id.peerid);


        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String peerID = "";
                String userName = "";

                userName = userNametxt.getText().toString();
                peerID =  peerIdtxt.getText().toString();

                RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jsonObjectRequest ;
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username",userName);
                    jsonObject.put("peerId",peerID);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }



                String mRequestBody = jsonObject.toString();


                jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String token = response.getString("token");
                            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sh.edit();
                            myEdit.putString("username",jsonObject.getString("username"));
                            myEdit.putString("token",token);
                            myEdit.apply();

                            dialog d =new dialog(PeerId.this);
                            d.startLoadingdialog();

                            //Toast.makeText(PeerId.this, "progressbar started", Toast.LENGTH_SHORT).show();
                            new Thread(() -> {

                                try {

                                    // code runs in a thread
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            d.dismissdialog();

                                            startActivity(new Intent(PeerId.this,SeekBar.class));
                                            finish();
                                        }
                                    });
                                } catch (final Exception ex) {
                                }
                            }).start();


                            //Toast.makeText(getApplicationContext(),token,Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {

                            Toast.makeText(getApplicationContext(),"Wrong UserName and PeerID",Toast.LENGTH_SHORT).show();
                            throw new RuntimeException(e);

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),"Wrong UserName and PeerID",Toast.LENGTH_SHORT).show();


                    }
                })
                {
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        return super.parseNetworkResponse(response);
                    }

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
                };

                mRequestQueue.add(jsonObjectRequest);


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

    boolean check(String userName,String peerId) throws JSONException {

        final Boolean[] isCheck = {false};

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest ;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username",userName);
        jsonObject.put("peerId",peerId);




        String mRequestBody = jsonObject.toString();


        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String token = response.getString("token");
                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sh.edit();

                    myEdit.putString("token",token);
                    myEdit.putString("username",userName.toString());
                    myEdit.apply();

                    dialog d =new dialog(PeerId.this);
                    d.startLoadingdialog();

                    //Toast.makeText(PeerId.this, "progressbar started", Toast.LENGTH_SHORT).show();
                    new Thread(() -> {

                        try {

                            // code runs in a thread

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    d.dismissdialog();

                                    startActivity(new Intent(PeerId.this,SeekBar.class));
                                    finish();
                                }
                            });
                        } catch (final Exception ex) {
                        }
                    }).start();


                    //Toast.makeText(getApplicationContext(),token,Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                isCheck[0] = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                isCheck[0] = false;
                ServerAlert serverAlert = new ServerAlert();
                serverAlert.startAlert(PeerId.this,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


            }
        })
        {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

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
        };

        mRequestQueue.add(jsonObjectRequest);

        return  isCheck[0];
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

            builder.setCancelable(true);


            dialog = builder.create();
            dialog.show();
        }

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
