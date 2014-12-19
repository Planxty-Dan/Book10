package com.book10.admin.book10.Fragments;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.book10.admin.book10.Models.BooksModel;
import com.book10.admin.book10.Models.FavoritesSingleton;
import com.book10.admin.book10.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12/6/14.
 */
public class FavoriteBooksFragment extends ListFragment {

    private final String FAVORITES_PARSE_KEY = "UserFavorites";
    private BookListAdapter adapter;
    public ArrayList<BooksModel> favoriteBooks;
    private TextView bookTitle;
    private TextView bookAuthor;
    private Button deleteButton;
    private Button addBook;
    private FavoritesSingleton favoritesSingleton;

    public static FavoriteBooksFragment newInstance() {
        FavoriteBooksFragment favoriteBooksFragment = new FavoriteBooksFragment();
        return favoriteBooksFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite_books, container, false);
        addBook = (Button) rootView.findViewById(R.id.add_book_button);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favoritesSingleton = FavoritesSingleton.getInstance();
        favoriteBooks = favoritesSingleton.getFavoritesList();
        adapter = new BookListAdapter(getActivity(), favoriteBooks);
        numberOfFavoritesEnteredChecker();
        setListAdapter(adapter);
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEnterFavoriteBooks();
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void numberOfFavoritesEnteredChecker() {
        if (favoriteBooks.size() == 0) {
            noFavoritesEntered();
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
        EnterFavoriteBooksFragment enterFavoriteBooks = EnterFavoriteBooksFragment.newInstance();
        getFragmentManager().beginTransaction()
                .replace(R.id.container, enterFavoriteBooks)
                .addToBackStack("enter favorite")
                .commit();
    }

    public class BookListAdapter extends ArrayAdapter<BooksModel> {

        public BookListAdapter(Context context, ArrayList<BooksModel> favoritesList) {
            super(context, 0, favoritesList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            View rowView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main_list_item, parent, false);
            bookTitle = (TextView) rowView.findViewById(R.id.book_title);
            bookAuthor = (TextView) rowView.findViewById(R.id.book_author);
            deleteButton = (Button) rowView.findViewById(R.id.delete_button);

            BooksModel currentBook = getItem(position);
            bookTitle.setText(currentBook.getBookTitle());
            bookAuthor.setText(currentBook.getBookAuthor());
            deleteButton.setTag(currentBook);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BooksModel bookToRemove = (BooksModel)v.getTag();
                    String id = bookToRemove.getGoogleBooksID();
                    removeFavoriteFromParse(id);
                    remove(bookToRemove);
                    favoritesSingleton.removeFromFavoritesList(pos);
                    adapter.notifyDataSetChanged();
                }
            });
            return rowView;
        }

        private void removeFavoriteFromParse(String googleID) {
            ParseQuery findBookToRemove = new ParseQuery("Book");
            findBookToRemove.whereEqualTo("googleID", googleID);
            ParseQuery favoriteToRemove = new ParseQuery(FAVORITES_PARSE_KEY);
            favoriteToRemove.whereEqualTo("user", ParseUser.getCurrentUser());
            favoriteToRemove.whereMatchesQuery("book", findBookToRemove);
            favoriteToRemove.getFirstInBackground(new GetCallback() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    parseObject.deleteInBackground();
                }
            });
        }
    }
}

