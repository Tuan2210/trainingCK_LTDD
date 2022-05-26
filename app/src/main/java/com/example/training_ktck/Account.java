package com.example.training_ktck;

public class Account {
    private String email;
    private String matKhau;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public Account(String email, String matKhau) {
        this.email = email;
        this.matKhau = matKhau;
    }

    public Account(String email) {
        this.email = email;
    }

    //để insert vào sqlite
    public Account() {
    }
}
