package com.hosiluan.webservicedatabase;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hosiluan.webservicedatabase.remote.ApiUtils;
import com.hosiluan.webservicedatabase.remote.RetrofitClient;
import com.hosiluan.webservicedatabase.remote.SinhVien;
import com.hosiluan.webservicedatabase.remote.SinhVienService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements SinhVienAdapter.SinhVienAdapterInterface {

    private ListView listView;
    private SinhVienAdapter sinhVienAdapter;
    ArrayList<HashMap<String, String>> danhSachSV;
    List<HashMap<String, String>> param = new ArrayList<>();
    private SinhVienService sinhVienService = ApiUtils.getSinhVienService();
    TextView tvTest;
    private String url = "http://192.168.51.1/Android_Webservices/sinhvien.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.lv_sinhvien);
        danhSachSV = new ArrayList<>();
        sinhVienAdapter = new SinhVienAdapter(getApplicationContext(), R.layout.sinhvien_item, danhSachSV, this);
        listView.setAdapter(sinhVienAdapter);
        tvTest = (TextView) findViewById(R.id.tv_test);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        layDanhSachSinhVienVolley();
//        layDanhSachSVRetrofit();
//        new ReadJson().execute("http://192.168.51.1/FoodAndDrink/getDetail.php?func=getDetail&url=https://www.deliverynow.vn/ho-chi-minh/hanuri-quan-an-han-quoc-xo-viet-nghe-tinh");
        new ReadJson().execute("http://foodanddrink.dx.am/typeofplace.php?func=getTypeOfPlace");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add: {
                startActivity(new Intent(this, AddSinhVienActivity.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void xoaSinhVien(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, danhSachSV.get(position).toString(), Toast.LENGTH_SHORT).show();
                String id = danhSachSV.get(position).get("id");
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id", id);
                hashMap.put("func", "xoaSinhVien");
                param.add(hashMap);
//                new XoaSinhVien(url, param).execute();
//                xoaSinhVienVolley(id);
                xoaSinhVienRetrofit("xoaSinhVien",id);
            }
        }).show();
    }

    class ReadJson extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
//                URL url = new URL(strings[0] + "?func=layDanhSachSinhVien");
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(600000);
                httpURLConnection.setConnectTimeout(60000);
                httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Accept","application/json");


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("Luan","hello");
            Log.d("Luan",s);
            tvTest.setText(s);
//            try {
//                JSONArray array = new JSONArray(s);
//                danhSachSV.clear();
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject object = array.getJSONObject(i);
//                    HashMap<String, String> hashMap = new HashMap<>();
//                    hashMap.put("id", object.getString("Id"));
//                    hashMap.put("hoten", object.getString("HoTen"));
//                    hashMap.put("diachi", object.getString("DiaChi"));
//                    hashMap.put("namsinh", object.getString("NamSinh"));
//                    danhSachSV.add(hashMap);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            sinhVienAdapter.notifyDataSetChanged();
        }
    }

    class XoaSinhVien extends AsyncTask<Void, Void, String> {

        String url = "";
        List<HashMap<String, String>> param;

        public XoaSinhVien(String url, List<HashMap<String, String>> param) {
            this.url = url;
            this.param = param;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";

            try {
                URL url = new URL(this.url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("DELETE");
                httpURLConnection.setReadTimeout(600000);
                httpURLConnection.setConnectTimeout(60000);

                Uri.Builder builder = new Uri.Builder();
                for (int i = 0; i < param.size(); i++) {
                    for (Map.Entry entry : param.get(i).entrySet()) {
                        String key = (String) entry.getKey();
                        String value = (String) entry.getValue();
                        builder.appendQueryParameter(key, value);
                    }
                }
                String param = builder.build().getEncodedQuery();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

                bufferedWriter.write(param);
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
    }

    public void xoaSinhVienVolley(final String id) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Hello", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Hello", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("func", "xoaSinhVien");
                hashMap.put("id", id);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void xoaSinhVienRetrofit(String function, String id) {
        sinhVienService.xoaSinhVien(function,id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.d("Luan",response.body().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Luan",t.getMessage());
            }
        });
    }

    public void layDanhSachSinhVienVolley() {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "?func=layDanhSachSinhVien", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Luan", response);
                try {
                    JSONArray array = new JSONArray(response);
                    danhSachSV.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", object.getString("Id"));
                        hashMap.put("hoten", object.getString("HoTen"));
                        hashMap.put("diachi", object.getString("DiaChi"));
                        hashMap.put("namsinh", object.getString("NamSinh"));
                        danhSachSV.add(hashMap);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                sinhVienAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Luan", error.toString());
            }
        });

        requestQueue.add(stringRequest);
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

    public void layDanhSachSVRetrofit() {
        sinhVienService.layDanhSachSinhVien("layDanhSachSinhVien").enqueue(new Callback<List<SinhVien>>() {
            @Override
            public void onResponse(Call<List<SinhVien>> call, retrofit2.Response<List<SinhVien>> response) {
                ArrayList<SinhVien> sinhViens = new ArrayList<SinhVien>();
                sinhViens = (ArrayList<SinhVien>) response.body();
                if (sinhViens.size() > 0){
                    Log.d("Haiiii", sinhViens.size() + " size");
                }
            }

            @Override
            public void onFailure(Call<List<SinhVien>> call, Throwable t) {
                Log.d("Haiiii", t.getMessage());

            }
        });
    }
}
