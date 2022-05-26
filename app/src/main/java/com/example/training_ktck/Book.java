package com.example.training_ktck;

public class Book {
    private String tenSach;
    private String theLoai;
    private String gia;

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public Book(String tenSach, String theLoai, String gia) {
        this.tenSach = tenSach;
        this.theLoai = theLoai;
        this.gia = gia;
    }

    public Book(String tenSach) {
        this.tenSach = tenSach;
    }

    public Book() {
    }
}
