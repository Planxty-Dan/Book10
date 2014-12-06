package com.book10.admin.book10.Models;

/**
 * Created by admin on 12/4/14.
 */
public class BooksModel {
    private String googleBooksID;
    private String bookTitle;
    private String bookAuthor;
    private String bookGenre;
    private String bookDescription;
    private String bookImage;

    public BooksModel(String googleBooksID, String bookTitle, String bookAuthor, String bookGenre, String bookDescription, String bookImage) {
        this.googleBooksID = googleBooksID;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookGenre = bookGenre;
        this.bookDescription = bookDescription;
        this.bookImage = bookImage;
    }

    public String getGoogleBooksID() {
        return googleBooksID;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookGenre() {
        return bookGenre;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public String getBookImage() {
        return bookImage;
    }
}
