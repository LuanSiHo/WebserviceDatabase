package com.hosiluan.webservicedatabase;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hosiluan.webservicedatabase.remote.ApiUtils;
import com.hosiluan.webservicedatabase.remote.SinhVienService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateSinhVienActivity extends AppCompatActivity {

    private EditText edtHoten, edtDiachi, edtNamsinh;
    private Button btnThem, btnHuy;
    private SinhVienService sinhVienService;

    String url = "http://192.168.55.1/Android_Webservices/sinhvien.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sinh_vien);
        sinhVienService = ApiUtils.getSinhVienService();
        Intent intent = getIntent();
        final HashMap<String, String> hashMap = (HashMap<String, String>) intent.getSerializableExtra("sinhvien");

        edtHoten = (EditText) findViewById(R.id.edt_hoten);
        edtDiachi = (EditText) findViewById(R.id.edt_diachi);
        edtNamsinh = (EditText) findViewById(R.id.edt_namsinh);

        btnThem = (Button) findViewById(R.id.btn_them);
        btnHuy = (Button) findViewById(R.id.btn_huy);

        edtHoten.setText(hashMap.get("hoten"));
        edtDiachi.setText(hashMap.get("diachi"));
        edtNamsinh.setText(hashMap.get("namsinh"));

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hashMapupdate = new HashMap<String, String>();
                hashMapupdate.put("hoten", edtHoten.getText().toString());
                hashMapupdate.put("diachi", edtDiachi.getText().toString());
                hashMapupdate.put("namsinh", edtNamsinh.getText().toString());
                hashMapupdate.put("id", hashMap.get("id"));
                hashMapupdate.put("func", "suaSinhVien");

                List<HashMap<String, String>> param = new ArrayList<HashMap<String, String>>();
                param.add(hashMapupdate);

                if (hashMapupdate.get("hoten").isEmpty() || hashMapupdate.get("diachi").isEmpty() || hashMapupdate.get("namsinh").isEmpty()) {
                    Toast.makeText(UpdateSinhVienActivity.this, "please fill all the field", Toast.LENGTH_SHORT).show();
                } else if (hashMapupdate.get("hoten").equals(hashMap.get("hoten")) &&
                        hashMapupdate.get("diachi").equals(hashMap.get("diachi")) &&
                        hashMapupdate.get("namsinh").equals(hashMap.get("namsinh"))) {

                    Toast.makeText(UpdateSinhVienActivity.this, "nothing changed", Toast.LENGTH_SHORT).show();
                } else {
//                    capNhatSV(url, hashMapupdate);
                    CapNhaSV(hashMapupdate);

//                    new UpdateSinhVien(url,param).execute();
                    finish();
                }
            }
        });
    }

    private void capNhatSV(String url, final HashMap<String, String> stringStringHashMap) {

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {
                    Toast.makeText(UpdateSinhVienActivity.this, response, Toast.LENGTH_SHORT).show();
                    Log.d("Luan", response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateSinhVienActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringStringMap = new HashMap<>();
                stringStringMap.put("func", "suaSinhVien");
                stringStringMap.put("id", stringStringHashMap.get("id"));
                stringStringMap.put("hoten", stringStringHashMap.get("hoten"));
                stringStringMap.put("diachi", stringStringHashMap.get("diachi"));
                stringStringMap.put("namsinh", stringStringHashMap.get("namsinh"));
                return stringStringMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void CapNhaSV(Map<String, String> map) {
        sinhVienService.suaSinhVien(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("Luan", response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Luan", t.getMessage());
            }
        });
    }

    class UpdateSinhVien extends AsyncTask<Void, Void, String> {

        String url = "";
        List<HashMap<String, String>> params = new ArrayList<>();

        public UpdateSinhVien(String url, List<HashMap<String, String>> hashMaps) {
            this.url = url;
            this.params = hashMaps;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            try {
                URL url = new URL(this.url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.connect();
                httpURLConnection.setRequestMethod("PUT");
                httpURLConnection.setConnectTimeout(60000);
                httpURLConnection.setReadTimeout(60000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder();

                for (int i = 0; i < params.size(); i++) {
                    for (Map.Entry entry : params.get(i).entrySet()) {
                        String key = (String) entry.getKey();
                        String value = (String) entry.getValue();
                        builder.appendQueryParameter(key, value);
                    }
                }

                String query = builder.build().getEncodedQuery();
                Log.d("Luan", query);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

                bufferedWriter.write(query);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStreamWriter.close();
                outputStream.close();

                result = readStream(httpURLConnection);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Luan", s);
        }

        private String readStream(HttpURLConnection connection) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                String line = "";
                while ((line = br.readLine()) != null) {
                    stringBuilder.append(line);
                }

                br.close();
                isr.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }
    }


}
