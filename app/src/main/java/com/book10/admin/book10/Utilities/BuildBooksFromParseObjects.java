package com.book10.admin.book10.Utilities;
import com.book10.admin.book10.Models.BooksModel;
import com.parse.ParseObject;
import java.util.ArrayList;

/**
 * Created by admin on 12/11/14.
 */
public class BuildBooksFromParseObjects {

    public ArrayList<BooksModel> build(ArrayList<ParseObject> parseBookObjects) {
        ArrayList<BooksModel> arrayOfBooks = new ArrayList<BooksModel>();
        for (ParseObject parseBooks: parseBookObjects) {
            ParseObject parseObject;
            if (parseBooks.getString("title") == null) {
                parseObject = parseBooks.getParseObject("book");
            } else {
                parseObject = parseBooks;
            }
            String googleId = parseObject.getString("googleID");
            String title = parseObject.getString("title");
            String author = parseObject.getString("author");
            String description = parseObject.getString("description");
            String genre = parseObject.getString("genre");
            String imageURL = parseObject.getString("imageUrl");
            BooksModel booksModel = new BooksModel(googleId, title, author, genre, description, imageURL);
            arrayOfBooks.add(booksModel);
        }
        return arrayOfBooks;
    }


}
