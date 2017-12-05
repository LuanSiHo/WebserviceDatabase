package com.hosiluan.webservicedatabase.remote;

import java.util.List;
import java.util.Map;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by HoSiLuan on 8/11/2017.
 */

public interface SinhVienService {

    @GET("SinhVien.php")
    Call<List<SinhVien>> layDanhSachSinhVien(@Query("func") String function);

    @FormUrlEncoded
    @POST("SinhVien.php")
    Call<ResponseBody> themSinhVien(
            @Field("func") String function,
            @Field("hotenSV") String hoten,
            @Field("namsinhSV") String namsinh,
            @Field("diachiSV") String diachi);

    @FormUrlEncoded
    @POST("SinhVien.php")
    Call<ResponseBody> themSinhVien(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @PUT("SinhVien.php")
    Call<ResponseBody> suaSinhVien(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "SinhVien.php", hasBody = true)
    Call<String> xoaSinhVien(@Field("func") String function, @Field("id") String id);

}
