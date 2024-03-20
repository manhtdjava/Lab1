package com.example.lab1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder>{
    Context context;
    private List<SanPhamModel> list;

    public SanPhamAdapter(Context context, List<SanPhamModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_sanpham, parent, false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        holder.txtTen.setText("Tên sp: "+list.get(position).getTen());
        holder.txtGia.setText("Giá sp: "+ list.get(position).getGia());
        holder.txtSoLuong.setText("Số lượng sp: " + list.get(position).getSoluong());
        Log.e("soluong",  holder.txtSoLuong.toString());

//        String tt = list.get(position).isTonKho() ? "Còn hàng" : "Hết hàng";
        holder.txtTonKho.setText("Tồn kho: " + list.get(position).getTonkho());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class SanPhamViewHolder extends RecyclerView.ViewHolder{
        TextView txtTen, txtGia, txtSoLuong, txtTonKho;
        CardView itemSanPham;
        ImageButton imgDelete;
        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.txtTen);
            txtGia = itemView.findViewById(R.id.txtGia);
            txtSoLuong = itemView.findViewById(R.id.txtSoLuong);
            txtTonKho = itemView.findViewById(R.id.txtTonKho);
            itemSanPham = itemView.findViewById(R.id.itemSP);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }


}
