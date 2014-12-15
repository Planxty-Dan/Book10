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

    private final String FAVORITES_KEY = "UserFavorites";
    private final String BOOK_KEY = "Book";
    private BookListAdapter adapter;
    public ArrayList<BooksModel> favoriteBooks = new ArrayList<BooksModel>();
    private ArrayList<BooksModel> tempBookStorage = new ArrayList<BooksModel>();
    private TextView bookTitle;
    private TextView bookAuthor;
    private Button deleteButton;
    private int googleBooksArrayIndex = 0;
    private ParseObject bookObject;
    private String userEnteredTitle;
    private String userEnteredAuthor;

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

    private void numberOfFavoritesEnteredChecker() {
        if (favoriteBooks.size() == 0) {
            noFavoritesEntered();
        } else if (favoriteBooks.size() > 0 && favoriteBooks.size() < 10) {
            enterBooks();
        }
    }

    private void noFavoritesEntered() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.no_favorites_entered_dialog)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        enterBooks();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void enterBooks() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = View.inflate(getActivity(), R.layout.dialog_enter_favorite_book_form, null);
        builder.setView(rootView);
        final EditText enterTitle = (EditText) rootView.findViewById(R.id.enter_title);
        final EditText enterAuthor = (EditText) rootView.findViewById(R.id.enter_author);
        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userEnteredTitle = enterTitle.getText().toString();
                userEnteredAuthor = enterAuthor.getText().toString();
                checkEnteredBook();
                dialog.dismiss();
            }
        })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void checkEnteredBook() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = View.inflate(getActivity(), R.layout.dialog_confirm_added_favorite, null);
        builder.setView(rootView);
        final TextView bookTitle = (TextView) rootView.findViewById(R.id.confirmation_title);
        final TextView bookAuthor = (TextView) rootView.findViewById(R.id.confirm_author);
        GoogleBooksAPI googleBooksAPI = new GoogleBooksAPI(getActivity(), userEnteredTitle, userEnteredAuthor, new GoogleBooksAPI.OnGoogleBooksDataLoadedListener() {
            @Override
            public void dataLoaded(List<BooksModel> books) {
                bookTitle.setText(books.get(googleBooksArrayIndex).getBookTitle());
                bookAuthor.setText(books.get(googleBooksArrayIndex).getBookAuthor());
                tempBookStorage = (ArrayList<BooksModel>) books;
            }
        });
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                acceptedFavorites(dialog);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                declinedFavorites(dialog);
            }
        });
        googleBooksAPI.execute();
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void acceptedFavorites(DialogInterface dialog) {
        favoriteBooks.add(tempBookStorage.get(googleBooksArrayIndex));
        ParseQuery bookAlreadyAddedQuery = ParseQuery.getQuery(BOOK_KEY);
        bookAlreadyAddedQuery.whereEqualTo("googleID", tempBookStorage.get(googleBooksArrayIndex).getGoogleBooksID());
        bookAlreadyAddedQuery.getFirstInBackground( new GetCallback() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                BooksModel tempBook = favoriteBooks.get(googleBooksArrayIndex);
                saveFavoritesToParse(parseObject, tempBook);
                adapter.notifyDataSetChanged();
                tempBookStorage.clear();
                googleBooksArrayIndex = 0;
                if (favoriteBooks.size() < 10) {
                    enterBooks();
                } else {
                    Toast.makeText(getActivity(), R.string.finished_entering_favorites, Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void declinedFavorites(DialogInterface dialog) {
        if (googleBooksArrayIndex < tempBookStorage.size()) {
            googleBooksArrayIndex++;
            checkEnteredBook();
        } else {
            Toast.makeText(getActivity(), R.string.declined_favorites_confirmation, Toast.LENGTH_SHORT).show();
            enterBooks();
        }
        dialog.dismiss();
    }

    public void saveFavoritesToParse(ParseObject parseObject, BooksModel tempBook) {
        if (parseObject == null || parseObject.equals("")) {
            bookObject = new ParseObject(BOOK_KEY);
            bookObject.put("googleID", tempBook.getGoogleBooksID());
            bookObject.put("title", tempBook.getBookTitle());
            bookObject.put("author", tempBook.getBookAuthor());
            bookObject.put("genre", tempBook.getBookGenre());
            bookObject.put("description", tempBook.getBookDescription());
            bookObject.put("imageUrl", tempBook.getBookImage());
            bookObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        ParseObject userFavoriteObject = new ParseObject(FAVORITES_KEY);
                        userFavoriteObject.put("user", ParseUser.getCurrentUser());
                        userFavoriteObject.put("book", ParseObject.createWithoutData(BOOK_KEY, bookObject.getObjectId()));
                        userFavoriteObject.saveInBackground();
                    }
                }
            });
        } else {
            ParseObject userFavoriteObject = new ParseObject(FAVORITES_KEY);
            userFavoriteObject.put("user", ParseUser.getCurrentUser());
            userFavoriteObject.put("book", ParseObject.createWithoutData(BOOK_KEY, parseObject.getObjectId()));
            userFavoriteObject.saveInBackground();
        }
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
