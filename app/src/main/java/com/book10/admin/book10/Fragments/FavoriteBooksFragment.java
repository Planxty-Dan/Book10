package com.book10.admin.book10.Fragments;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.book10.admin.book10.Models.BooksModel;
import com.book10.admin.book10.Models.FavoritesSingleton;
import com.book10.admin.book10.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by admin on 12/6/14.
 */
@EFragment (R.layout.fragment_booklists)
public class FavoriteBooksFragment extends ListFragment {

    private ArrayList<BooksModel> favoriteBooks;
    private FavoritesSingleton favoritesSingleton;
    private BookListAdapter adapter;

    @ViewById (R.id.list_button)
    protected Button addBookButton;

    @AfterViews
    protected void setButtonText() {
        addBookButton.setText(R.string.add_book_button);
    }

    @Click (R.id.list_button)
    protected void addBookClicked() {
        if (favoriteBooks.size() < 10) {
            goToEnterFavoriteBooks();
        } else {
            Toast.makeText(getActivity(), R.string.ten_favorites, Toast.LENGTH_SHORT).show();
        }
    }

    @AfterViews
    protected void setAdapter() {
        favoritesSingleton = FavoritesSingleton.getInstance();
        favoriteBooks = favoritesSingleton.getFavoritesList();
        adapter = new BookListAdapter(getActivity(), favoriteBooks);
        numberOfFavoritesEnteredChecker();
        setListAdapter(adapter);
    }

    private void numberOfFavoritesEnteredChecker() {
        if (favoriteBooks != null) {
            if (favoriteBooks.size() == 0) {
                noFavoritesEntered();
            }
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
                .replace(R.id.main_container, enterFavoriteBooks)
                .addToBackStack("enter favorite")
                .commit();
    }

    public class BookListAdapter extends ArrayAdapter<BooksModel> {

        private final String FAVORITES_PARSE_KEY = "UserFavorites";
        private Context context;

        public BookListAdapter(Context context, ArrayList<BooksModel> favoritesList) {
            super(context, 0, favoritesList);
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.fragment_main_list_item, parent, false);
            }
            TextView bookTitle = (TextView) convertView.findViewById(R.id.book_title);
            TextView bookAuthor = (TextView) convertView.findViewById(R.id.book_author);
            Button deleteButton = (Button) convertView.findViewById(R.id.delete_button);
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
                    favoritesSingleton.removeFromFavoritesList(position);
                    adapter.notifyDataSetChanged();
                }
            });
            return convertView;
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

