package com.example.decends;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.decends.utility.NetworkChangeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class DashBoard extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    Button mbutton;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "https://85e1-103-82-43-60.ngrok-free.app/register";
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        mbutton = (Button) findViewById(R.id.button);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getData();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    void getData() throws JSONException {
        mRequestQueue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("username", "yash");
        jsonBody.put("password", "yash123");
        jsonBody.put("email","swappu");

        final String mRequestBody = jsonBody.toString();
        
        mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(),"hey you click me",Toast.LENGTH_SHORT).show();

                Toast.makeText(getApplicationContext(), "Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();

                Log.i(TAG, "Error :" + error.toString());
            }
           }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {

                    responseString = String.valueOf(response.statusCode);

                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }

    };

        mRequestQueue.add(mStringRequest);
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
}
