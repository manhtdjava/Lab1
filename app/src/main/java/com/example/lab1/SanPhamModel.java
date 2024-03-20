package com.example.lab1;

public class SanPhamModel {
    private String _id;
    private String ten;
   private int gia, soluong;
   private String tonkho;

    public SanPhamModel(String _id, String ten, int gia, int soLuong, String tonKho) {
        this._id = _id;
        this.ten = ten;
        this.gia = gia;
        this.soluong = soLuong;
        this.tonkho = tonKho;
    }

    public SanPhamModel() {

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public String getTonkho() {
        return tonkho;
    }

    public void setTonkho(String tonkho) {
        this.tonkho = tonkho;
    }
}
