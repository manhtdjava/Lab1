package com.example.lab1;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIServer {
//     String DOMMAIN = "http://192.168.89.155:3000/";
//    String DOMMAIN = "http://192.168.1.5:3000/";
    String DOMMAIN = "http://10.24.58.250:3000/";

    @GET("/api/list")
    Call<List<SanPhamModel>> getSanPham();

    @POST("/api/add_sp")
    Call<SanPhamModel> addSanPham(@Body SanPhamModel sanPhamModel);


}
