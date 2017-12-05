package com.hosiluan.webservicedatabase.remote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by HoSiLuan on 8/11/2017.
 */
public class SinhVien {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("HoTen")
    @Expose
    private String hoTen;
    @SerializedName("DiaChi")
    @Expose
    private String diaChi;
    @SerializedName("NamSinh")
    @Expose
    private String namSinh;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(String namSinh) {
        this.namSinh = namSinh;
    }

}


