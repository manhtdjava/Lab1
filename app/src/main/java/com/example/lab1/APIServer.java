package com.example.lab1;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIServer {
     String DOMMAIN = "http://192.168.89.155:3000/";
//    String DOMMAIN = "http://192.168.1.5:3000/";
//    String DOMMAIN = "http://10.24.36.15:3000/";

    @GET("/api/list")
    Call<List<SanPhamModel>> getSanPham();

    @POST("/add_sp")
    Call<SanPhamModel> addSanPham(@Body SanPhamModel sanPhamModel);


    @DELETE("/delete_sp/{id}")
    Call<Void> deleteSanpham(@Path("id") String id);
    @PUT("/update_sp/{id}")
    Call<SanPhamModel> updateSanpham(@Path("id") String id, @Body SanPhamModel sanPham);

}
