package com.book10.admin.book10.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 12/4/14.
 */
public class BooksModel implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.googleBooksID);
        dest.writeString(this.bookTitle);
        dest.writeString(this.bookAuthor);
        dest.writeString(this.bookGenre);
        dest.writeString(this.bookDescription);
        dest.writeString(this.bookImage);
    }

    private BooksModel(Parcel in) {
        this.googleBooksID = in.readString();
        this.bookTitle = in.readString();
        this.bookAuthor = in.readString();
        this.bookGenre = in.readString();
        this.bookDescription = in.readString();
        this.bookImage = in.readString();
    }

    public static final Parcelable.Creator<BooksModel> CREATOR = new Parcelable.Creator<BooksModel>() {
        public BooksModel createFromParcel(Parcel source) {
            return new BooksModel(source);
        }

        public BooksModel[] newArray(int size) {
            return new BooksModel[size];
        }
    };
}
