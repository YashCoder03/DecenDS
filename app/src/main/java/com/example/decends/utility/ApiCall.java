package com.example.decends.utility;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.decends.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class ApiCall extends Service {

    //private static final String API_URL = getString(R.string.url)+"/active_time";
    private RequestQueue requestQueue;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            makeApiCall();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return START_STICKY;
    }
    private void makeApiCall() throws JSONException {

        Log.d("Apicall","api call");
        JSONObject jsonObject = new JSONObject();
        Handler handler = new Handler();
        JSONObject jsonObject1 = new JSONObject();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    jsonObject.put("active_time", "1");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                final String[] mRequestBody = {jsonObject1.toString()};

                String finalMRequestBody = mRequestBody[0];
                JsonObjectRequest getipfs = new JsonObjectRequest(Request.Method.POST, getString(R.string.ipfsurl) + "/api/v0/stats/bw", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            jsonObject1.put("TotalOut",response.getString("TotalOut"));
                            jsonObject1.put("TotalIn",response.getString("TotalIn"));
                            jsonObject1.put("RateOut",response.getString("RateOut"));
                            jsonObject1.put("RateIn",response.getString("RateIn"));

                            String MRequestBody = jsonObject1.toString();
                            JsonObjectRequest ipfs = new JsonObjectRequest(Request.Method.POST, getString(R.string.url) + "/rateinout", null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(ApiCall.this, error.toString(), Toast.LENGTH_SHORT).show();

                                }
                            })
                            {
                                @Override
                                public byte[] getBody() {
                                    try {
                                        return MRequestBody == null ? null : MRequestBody.getBytes("utf-8");
                                    } catch (UnsupportedEncodingException uee) {
                                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", MRequestBody, "utf-8");
                                        return null;
                                    }
                                }

                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                    String token = sh.getString("token", "");
                                    Map<String, String> params = new HashMap<>();
                                    params.put("Authorization", token);
                                    return params;
                                }
                            };
                            requestQueue.add(ipfs);


                        }
                        catch (Exception e)
                        {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ApiCall.this, error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
                requestQueue.add(getipfs);






                mRequestBody[0] = jsonObject.toString();

                String finalMRequestBody1 = mRequestBody[0];
                StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.url) + "/active_time",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle response from API
                                // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }


                    @Override
                    public byte[] getBody() {
                        try {
                            return finalMRequestBody1 == null ? null : finalMRequestBody1.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", finalMRequestBody1, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        String token = sh.getString("token", "");

                        Map<String, String> params = new HashMap<>();
                        params.put("Authorization", token);
                        return params;
                    }
                };

                requestQueue.add(request);
                handler.postDelayed(this, (10000 * 60));
            }
        };
        handler.post(runnable);


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
