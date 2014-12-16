package com.book10.admin.book10.Fragments;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.book10.admin.book10.APIcalls.GoogleBooksAPI;
import com.book10.admin.book10.Models.BooksModel;
import com.book10.admin.book10.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by admin on 12/6/14.
 */
public class FavoriteBooksFragment extends ListFragment{

    private BookListAdapter adapter;
    public ArrayList<BooksModel> favoriteBooks = new ArrayList<BooksModel>();
    private TextView bookTitle;
    private TextView bookAuthor;
    private Button deleteButton;
    private ParseObject bookObject;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new BookListAdapter(getActivity());
        numberOfFavoritesEnteredChecker();
        setListAdapter(adapter);


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public void addBookToFavorites(BooksModel favBook) {
        favoriteBooks.add(favBook);
    }

    public int getFavoriteBooksCount() {
        return favoriteBooks.size();
    }

    private void numberOfFavoritesEnteredChecker() {
        if (favoriteBooks.size() == 0) {
            noFavoritesEntered();
        } else if (favoriteBooks.size() > 0 && favoriteBooks.size() < 10) {
            goToEnterFavoriteBooks();
        }
    }

    private void noFavoritesEntered() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.no_favorites_entered_dialog)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        goToEnterFavoriteBooks();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void goToEnterFavoriteBooks() {
        EnterFavoriteBooks enterFavoriteBooks = new EnterFavoriteBooks(this);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, enterFavoriteBooks)
                .addToBackStack("enter favorite")
                .commit();
    }

    public class BookListAdapter extends ArrayAdapter<BooksModel> {

        public BookListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main_list_item, parent, false);
            bookTitle = (TextView) rowView.findViewById(R.id.book_title);
            bookAuthor = (TextView) rowView.findViewById(R.id.book_author);
            deleteButton = (Button) rowView.findViewById(R.id.delete_button);

            BooksModel currentBook = getItem(position);
            bookTitle.setText(currentBook.getBookTitle());
            bookAuthor.setText(currentBook.getBookAuthor());
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteBooks.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
            return rowView;
        }
    }
}
