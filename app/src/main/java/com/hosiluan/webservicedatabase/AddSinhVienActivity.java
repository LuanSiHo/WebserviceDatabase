package com.hosiluan.webservicedatabase;

import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
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

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddSinhVienActivity extends AppCompatActivity {

    private EditText edtHoten, edtDiachi, edtNamsinh;
    private Button btnThem, btnHuy;
    String urlInsert = "http://192.168.55.1/Android_Webservices/sinhvien.php";
    List<HashMap<String, String>> param = new ArrayList<>();

    private SinhVienService sinhVienService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sinh_vien);

        sinhVienService = ApiUtils.getSinhVienService();
        edtHoten = (EditText) findViewById(R.id.edt_hoten);
        edtDiachi = (EditText) findViewById(R.id.edt_diachi);
        edtNamsinh = (EditText) findViewById(R.id.edt_namsinh);

        btnThem = (Button) findViewById(R.id.btn_them);
        btnHuy = (Button) findViewById(R.id.btn_huy);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ten = edtHoten.getText().toString().trim();
                String diachi = edtDiachi.getText().toString().trim();
                String namsinh = edtNamsinh.getText().toString().trim();
                if (ten.isEmpty() || diachi.isEmpty() || namsinh.isEmpty()) {
                    Toast.makeText(AddSinhVienActivity.this, "empty", Toast.LENGTH_SHORT).show();
                } else {
//                    ThemSinhVien(urlInsert);

                    themSinhVienRetrofit("themSinhVien", ten, namsinh, diachi);
//                    new AddSV(urlInsert, param).execute();
                }
            }
        });
    }

    private void themSinhVienRetrofit(String function, String ten, String namsinh, String diachi) {

        Map<String, String> map = new HashMap<>();
        map.put("func", function);
        map.put("hotenSV", ten);
        map.put("namsinhSV", namsinh);
        map.put("diachiSV", diachi);

        sinhVienService.themSinhVien(map).enqueue(new Callback<ResponseBody>() {
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

    private void ThemSinhVien(String url) {

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddSinhVienActivity.this, response, Toast.LENGTH_SHORT).show();
                if (response.equals("success")) {
                    finish();
//                    startActivity(new Intent(AddSinhVienActivity.this,MainActivity.class));
                } else {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddSinhVienActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringStringMap = new HashMap<>();
                stringStringMap.put("func", "themSinhVien");
                stringStringMap.put("hotenSV", edtHoten.getText().toString().trim());
                stringStringMap.put("namsinhSV", edtNamsinh.getText().toString().trim());
                stringStringMap.put("diachiSV", edtDiachi.getText().toString().trim());
                return stringStringMap;
            }
        };

        requestQueue.add(stringRequest);
    }


    class AddSV extends AsyncTask<Void, Void, String> {
        private String url;
        private List<HashMap<String, String>> attributes;

        public AddSV(String url, List<HashMap<String, String>> hashMaps) {
            this.url = url;
            attributes = hashMaps;
        }

        @Override
        protected void onPreExecute() {
            HashMap<String, String> postParam = new HashMap<>();
            postParam.put("func", "themSinhVien");
            postParam.put("hotenSV", edtHoten.getText().toString().trim());
            postParam.put("namsinhSV", edtNamsinh.getText().toString().trim());
            postParam.put("diachiSV", edtDiachi.getText().toString().trim());
            attributes.add(postParam);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... strings) {
            String result = "";
            try {
                URL url = new URL(this.url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(60000);
                httpURLConnection.setReadTimeout(60000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                Uri.Builder builder = new Uri.Builder();
                for (int i = 0; i < attributes.size(); i++) {
                    for (Map.Entry entry : attributes.get(i).entrySet()) {
                        String key = (String) entry.getKey();
                        String value = (String) entry.getValue();
                        builder.appendQueryParameter(key, value);
                    }
                }
                String query = builder.build().getEncodedQuery();

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


        private String readStream(HttpURLConnection connection) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                connection.connect();

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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Luan", s);
        }


    }
}
