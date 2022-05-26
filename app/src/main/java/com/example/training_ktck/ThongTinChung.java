package com.example.training_ktck;

public class ThongTinChung {
    private Account account;
    private Book book;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public ThongTinChung(Account account, Book book) {
        this.account = account;
        this.book = book;
    }

    public ThongTinChung(Book book) {
        this.book = book;
    }

    public ThongTinChung() {
    }

    @Override
    public String toString() {
        return "ThongTinChung{" +
                "account=" + account +
                ", book=" + book +
                '}';
    }
}
