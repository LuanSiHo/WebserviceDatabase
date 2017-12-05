package com.hosiluan.webservicedatabase;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HoSiLuan on 8/5/2017.
 */

public class SinhVienAdapter extends ArrayAdapter {

    private Context mContext;
    private ArrayList<HashMap<String,String>> hashMaps;
    private SinhVienAdapterInterface sinhVienAdapterInterface;

    public SinhVienAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<HashMap<String,String>> objects, SinhVienAdapterInterface sinhVienAdapterInterface) {
        super(context, resource, objects);
        this.mContext = context;
        this.hashMaps =  objects;
        this.sinhVienAdapterInterface = sinhVienAdapterInterface;

    }



    @Override
    public int getCount() {
        return hashMaps.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder  = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sinhvien_item,parent,false);

            viewHolder = new ViewHolder();

            viewHolder.tvDiaChi = convertView.findViewById(R.id.tv_diachi);
            viewHolder.tvHoTen = convertView.findViewById(R.id.tv_hoten);
            viewHolder.tvNamSinh = convertView.findViewById(R.id.tv_namsinh);
            viewHolder.imgbtnDelete = convertView.findViewById(R.id.img_btn_delete);
            viewHolder.imgbtnEdit = convertView.findViewById(R.id.img_btn_edit);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final HashMap<String,String> hashMap = hashMaps.get(position);
        viewHolder.tvNamSinh.setText(hashMap.get("namsinh"));
        viewHolder.tvHoTen.setText(hashMap.get("hoten"));
        viewHolder.tvDiaChi.setText(hashMap.get("diachi"));
        
        viewHolder.imgbtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,UpdateSinhVienActivity.class);
                intent.putExtra("sinhvien",hashMap);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        
        viewHolder.imgbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sinhVienAdapterInterface.xoaSinhVien(position);
            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView tvHoTen,tvDiaChi,tvNamSinh;
        ImageButton imgbtnEdit, imgbtnDelete;

    }

    interface SinhVienAdapterInterface{
       void xoaSinhVien(int position);
    }
}
