package com.example.lab1;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    RecyclerView rcv;
    List<SanPhamModel> list;
    SanPhamAdapter adapter;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        rcv = findViewById(R.id.rcySanPham);
        fab = findViewById(R.id.fabSanPham);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAdd();
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIServer.DOMMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIServer apiService = retrofit.create(APIServer.class);

        Call<List<SanPhamModel>> call = apiService.getSanPham();

        call.enqueue(new Callback<List<SanPhamModel>>() {
            @Override
            public void onResponse(Call<List<SanPhamModel>> call, Response<List<SanPhamModel>> response) {
                if (response.isSuccessful()){
                    list = response.body();
                   loaddata();
                }
            }

            @Override
            public void onFailure(Call<List<SanPhamModel>> call, Throwable t) {
                Log.e("Main", t.getMessage() );
            }
        });

    }

    public void DialogAdd(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_addsanpham);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edtTen = dialog.findViewById(R.id.edtTenAdd);
        EditText edtGia = dialog.findViewById(R.id.edtGiaAdd);
        EditText edtSoLuong = dialog.findViewById(R.id.edtSoLuongAdd);
        EditText edtTonKho = dialog.findViewById(R.id.edtTonKhoAdd);
        Button btnCancel = dialog.findViewById(R.id.btnCanAdd);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmitAdd);



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten1 = String.valueOf(edtTen.getText().toString());
                int gia = Integer.parseInt(edtSoLuong.getText().toString());
                int sl = Integer.parseInt(edtGia.getText().toString());
                String tk = edtTonKho.getText().toString();

                SanPhamModel sanPhamModel = new SanPhamModel("", ten1, gia, sl, tk);
                addSanPham(sanPhamModel);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void addSanPham(SanPhamModel sanPham) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIServer.DOMMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIServer apiService = retrofit.create(APIServer.class);

        Call<Void> call = apiService.addSanPham(sanPham);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Thêm sản phẩm mới vào danh sách và cập nhật RecyclerView (nếu cần)
                    loaddata();
                    if (list != null) {
                        list.add(sanPham);
                        adapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                    }
                } else {
                    // Xử lý khi không thành công (ví dụ: hiển thị thông báo lỗi)
                    Log.e("AddProduct", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý khi gặp lỗi kết nối (ví dụ: hiển thị thông báo lỗi)
                Log.e("AddProduct", "Error: " + t.getMessage());
            }
        });
    }
    private void loaddata(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        rcv.setLayoutManager(layoutManager);
        adapter = new SanPhamAdapter(getApplicationContext(), list);
        rcv.setAdapter(adapter);
    }
}