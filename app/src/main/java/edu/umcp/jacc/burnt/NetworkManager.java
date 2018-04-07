package edu.umcp.jacc.burnt;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/* NetworkManager
 *
 * Class that handles all network calls
 * There should only ever be one instance of this object
 *
 * **/
class NetworkManager {

    private static final String TAG = "NetworkManager";

    private static NetworkManager instance = null;

    private final RequestQueue q; // Volley Request Queue

    // NetworkManager private constructor
    private NetworkManager(Context ctx) {
        q = Volley.newRequestQueue(ctx);
    }

    static synchronized NetworkManager getInstance(Context ctx) {
        if (instance == null)
            instance = new NetworkManager(ctx);
        return instance;
    }

    //this is so you don't need to pass context each time
    static synchronized NetworkManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("NetworkManager is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    void makeRequest(final Context ctx, int requestMethod, final Map<String, String> headers, final Map<String, String> params, String url, final CustomListener<String> listener) {
        Log.d(TAG, "Attempting " + ((requestMethod == Request.Method.POST) ? "POST" : "GET") + " request to " + url);
        StringRequest r = new StringRequest(requestMethod, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        if (response != null)
                            listener.getResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            String response = null;
                            try {
                                response = new String(error.networkResponse.data, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "Error: " + response);
                            if (response != null)
                                listener.getResult(response);
                        } else if (error.getClass().equals(TimeoutError.class)) {
                            // Timeout error
                            Log.d(TAG, "Timeout Error - no internet connection!");
                            Toast.makeText(ctx, "No internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                if (headers != null) return headers;
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "UMCP");
                headers.put("Accept-Language", "en");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            public Map<String, String> getParams() {
                if (params != null) return params;
                Map<String, String> params = new HashMap<>();
                params.put("love", "bitcamp2018");
                return params;
            }
        };

        // should this be default?
        r.setRetryPolicy(new DefaultRetryPolicy(5000, 1, 1f));
        q.add(r);
    }
}
