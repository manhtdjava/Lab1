package com.example.lab1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
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
    APIServer apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        rcv = findViewById(R.id.rcySanPham);
        fab = findViewById(R.id.fabSanPham);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        rcv.setLayoutManager(layoutManager);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIServer.DOMMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIServer.class);
        list = new ArrayList<>(); // Khởi tạo danh sách sản phẩm
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAdd();
            }
        });
        Call<List<SanPhamModel>> call = apiService.getSanPham();

        call.enqueue(new Callback<List<SanPhamModel>>() {
            @Override
            public void onResponse(Call<List<SanPhamModel>> call, Response<List<SanPhamModel>> response) {
                if (response.isSuccessful()) {
                    list = response.body(); // Cập nhật danh sách sản phẩm từ phản hồi của API
                    adapter = new SanPhamAdapter(getApplicationContext(), list);
                    rcv.setAdapter(adapter);
                   adapter.setOnDeleteClickListener(new SanPhamAdapter.OnDeleteClickListener() {
                       @Override
                       public void onDeleteClick(int pos) {
                           AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this
                           );
                           builder.setIcon(R.drawable.node);
                           builder.setTitle("Thông báo");
                           builder.setMessage("Bạn có muốn xóa sản phẩm " + " không");
                           builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   delete(pos);
                               }
                           });
                           builder.setNegativeButton("không", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   Toast.makeText(MainActivity.this, "Không xóa", Toast.LENGTH_SHORT).show();
                               }
                           });
                           AlertDialog dialog = builder.create();
                           dialog.show();
                       }
                   });
                   adapter.setOnUpdateClickListener(new SanPhamAdapter.OnUpdateClickListener() {
                       @Override
                       public void onUpdateClick(int pos) {
                           update(list.get(pos));
                       }
                   });
                }
            }

            @Override
            public void onFailure(Call<List<SanPhamModel>> call, Throwable t) {
                Log.e("Main", "Error fetching products: " + t.getMessage());
            }
        });

    }

    public void DialogAdd() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_addsanpham);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edtTen = dialog.findViewById(R.id.edtTenAdd);
        EditText edtGia = dialog.findViewById(R.id.edtGiaAdd);
        EditText edtSoLuong = dialog.findViewById(R.id.edtSoLuongAdd);
        TextView edtTonKho = dialog.findViewById(R.id.edtTonKhoAdd);
        edtTonKho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTonKhoDialog(edtTonKho);
            }
        });
        Button btnCancel = dialog.findViewById(R.id.btnCanAdd);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmitAdd);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten1 = edtTen.getText().toString();
                String giaStr = edtSoLuong.getText().toString();
                String soLuongStr = edtGia.getText().toString();
                String tk = edtTonKho.getText().toString();
//                Log.e("loi", ten1 + gia + sl + tk);

                if (!validate(ten1,giaStr,soLuongStr, tk)) return;
                int gia = Integer.parseInt(giaStr);
                int soLuong = Integer.parseInt(soLuongStr);
                    SanPhamModel sanPhamModel = new SanPhamModel(ten1, gia, soLuong, tk);

                    Call<SanPhamModel> call = apiService.addSanPham(sanPhamModel);
                    call.enqueue(new Callback<SanPhamModel>() {
                        @Override
                        public void onResponse(Call<SanPhamModel> call, Response<SanPhamModel> response) {
                            if (response.isSuccessful()) {
                                SanPhamModel addedSanPham = response.body(); // Lấy sản phẩm đã thêm từ phản hồi của API
                                list.add(addedSanPham); // Thêm sản phẩm vào danh sách
                                adapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                                Toast.makeText(MainActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                dialog.dismiss(); // Đóng dialog sau khi thêm thành công
                            } else {
                                // Xử lý lỗi nếu yêu cầu không thành công
                                Toast.makeText(MainActivity.this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SanPhamModel> call, Throwable t) {
                            Log.e("AddActivity", "Error adding sản phẩm: " + t.getMessage());
                        }
                    });


            }
        });
        dialog.show();
    }

    public void delete(int pos){
        String id = list.get(pos).get_id();
        Call<Void> call = apiService.deleteSanpham(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa sản phẩm thành công, cập nhật lại danh sách sản phẩm và refresh RecyclerView
                    list.remove(pos);
                    adapter.notifyItemRemoved(pos);
                    Toast.makeText(MainActivity.this, "xoa thanh cong", Toast.LENGTH_SHORT).show();
                } else {
                    // Xóa sản phẩm thất bại
                    Log.e("Delete", "Xóa sản phẩm thất bại: " + response.message());
                    Toast.makeText(MainActivity.this, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Delete", t.getMessage());
                Toast.makeText(MainActivity.this, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void update(SanPhamModel sanPham) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_sanpham);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edtTen = dialog.findViewById(R.id.edtTenEdit);
        EditText edtGia = dialog.findViewById(R.id.edtGiaEdit);
        EditText edtSoLuong = dialog.findViewById(R.id.edtSoLuongEdit);
        TextView edtTonKho = dialog.findViewById(R.id.edtTonKhoEdit);
        edtTonKho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTonKhoDialog(edtTonKho);
            }
        });
        Button btnCancel = dialog.findViewById(R.id.btnCanEdit);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmitEdit);

        // Set thông tin của sản phẩm cần cập nhật vào các trường của dialog

        edtTen.setText(sanPham.getTen());
        edtGia.setText(String.valueOf(sanPham.getGia()));
        edtSoLuong.setText(String.valueOf(sanPham.getSoluong()));
        edtTonKho.setText(sanPham.getTonkho());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten = edtTen.getText().toString();
                String gia = edtGia.getText().toString();
                String soLuong = edtSoLuong.getText().toString();
                String tonKho = edtTonKho.getText().toString();




               if (!validate(ten, gia, soLuong, tonKho)) return;

                int giatr = Integer.parseInt(gia);
                int soLuongtr = Integer.parseInt(soLuong);

                sanPham.setTen(ten);
                sanPham.setGia(giatr);
                sanPham.setSoluong(soLuongtr);
                sanPham.setTonkho(tonKho);

                // Gọi API cập nhật sản phẩm
                Call<SanPhamModel> call = apiService.updateSanpham(sanPham.get_id(), sanPham);
                call.enqueue(new Callback<SanPhamModel>() {
                    @Override
                    public void onResponse(Call<SanPhamModel> call, Response<SanPhamModel> response) {
                        if (response.isSuccessful()) {
                            // Cập nhật lại sản phẩm trong danh sách
                            adapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                            Toast.makeText(MainActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this, "Cập nhật sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SanPhamModel> call, Throwable t) {
                        Log.e("Update", "Error updating product: " + t.getMessage());
                        Toast.makeText(MainActivity.this, "Lỗi khi cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();
    }

    private boolean validate(String edtName, String giaStr, String soLuongStr,String tonKho) {


        if (edtName.isEmpty()) {
            Toast.makeText(this, "Không được để trống tên sp!", Toast.LENGTH_SHORT).show();
            return false;
        }if (giaStr.isEmpty() || soLuongStr.isEmpty()) {
            Toast.makeText(this, "Không được để trống giá hoặc số lượng!", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            int gia = Integer.parseInt(giaStr);
            int soLuong = Integer.parseInt(soLuongStr);
            if (gia < 0 || soLuong < 0) {
                Toast.makeText(this, "Giá và số lượng phải lớn hơn 0!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá và số lượng phải là số!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tonKho.isEmpty()) {
            Toast.makeText(this, "Không được để trống tồn kho sp!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void showTonKhoDialog(final TextView edtonkho) {
        final CharSequence[] items = {"Còn hàng", "Hết hàng"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Chọn giá trị cho trường tồn kho");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Set giá trị đã chọn vào EditText
                edtonkho.setText(items[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
