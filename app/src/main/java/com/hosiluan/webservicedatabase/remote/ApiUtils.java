package com.hosiluan.webservicedatabase.remote;

/**
 * Created by HoSiLuan on 8/11/2017.
 */


public class ApiUtils {

    public static final String baseURL = "http://192.168.55.1/Android_Webservices/";

    public static SinhVienService getSinhVienService(){
        return RetrofitClient.getClient(baseURL).create(SinhVienService.class);
    }
}
