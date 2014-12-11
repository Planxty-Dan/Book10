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

    private final String FAVORITES_KEY = "userFavorites";
    private final String BOOK_KEY = "book";
    private BookListAdapter adapter;
    public ArrayList<BooksModel> favoriteBooks = new ArrayList<BooksModel>();
    private ArrayList<BooksModel> tempBookStorage = new ArrayList<BooksModel>();
    private TextView bookTitle;
    private TextView bookAuthor;
    private Button deleteButton;
    private int googleBooksArrayIndex = 0;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new BookListAdapter(getActivity());
        ParseQuery<ParseObject> favoriteBooksParseQuery = ParseQuery.getQuery(FAVORITES_KEY);
        favoriteBooksParseQuery.whereExists(FAVORITES_KEY);
        favoriteBooksParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    favoriteBooks = (ArrayList) parseObjects;
                } else {
                    Log.d("Error", e.getMessage());
                }
            }
        });
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
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_enter_favorite_book_form, null))
            .setView(getView().findViewById(R.id.enter_title))
            .setView(getView().findViewById(R.id.enter_author))
            .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    View rootView = View.inflate(getActivity(), R.layout.dialog_enter_favorite_book_form, null);
                    EditText enterTitle = (EditText) rootView.findViewById(R.id.enter_title);
                    EditText enterAuthor = (EditText) rootView.findViewById(R.id.enter_author);
                    String title = enterTitle.getText().toString();
                    String author = enterAuthor.getText().toString();
                    checkEnteredBook(title, author);
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

    private void checkEnteredBook(String title, String author) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = View.inflate(getActivity(), R.layout.dialog_confirm_added_favorite, null);
        builder.setView(rootView);
        final TextView bookTitle = (TextView) rootView.findViewById(R.id.confirmation_title);
        final TextView bookAuthor = (TextView) rootView.findViewById(R.id.confirm_author);
        GoogleBooksAPI googleBooksAPI = new GoogleBooksAPI(getActivity(), title, author, new GoogleBooksAPI.OnGoogleBooksDataLoadedListener() {
            @Override
            public void dataLoaded(List<BooksModel> books) {
                bookTitle.setText(books.get(0).getBookTitle());
                bookAuthor.setText(books.get(0).getBookAuthor());
//                builder.setView(bookTitle);
//                builder.setView(bookAuthor);
                tempBookStorage.add(books.get(0));
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
        AlertDialog dialog = builder.create();
        dialog.show();
        googleBooksAPI.execute();
    }

    public void acceptedFavorites(DialogInterface dialog) {
        favoriteBooks.add(tempBookStorage.get(0));
        ParseQuery bookAlreadyAddedQuery = ParseQuery.getQuery(BOOK_KEY);
        bookAlreadyAddedQuery.whereEqualTo("googleID", tempBookStorage.get(0).getGoogleBooksID());
        bookAlreadyAddedQuery.getFirstInBackground(new GetCallback() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
              if (e == null) {
                    saveFavoritesToParse(parseObject);
                } else {
                  Log.d("Error", e.getMessage());
              }
            }
        });
        adapter.notifyDataSetChanged();
        tempBookStorage.clear();
        if (favoriteBooks.size() < 10) {
            enterBooks();
        } else {
            Toast.makeText(getActivity(), R.string.finished_entering_favorites, Toast.LENGTH_LONG).show();

        }
    }

    public void declinedFavorites(DialogInterface dialog) {
        tempBookStorage.remove(0);
        Toast.makeText(getActivity(), R.string.declined_favorites_confirmation, Toast.LENGTH_SHORT).show();
        enterBooks();
        dialog.dismiss();
    }

    public void saveFavoritesToParse(ParseObject parseObject) {
        if (parseObject == null) {
            final ParseObject bookObject = new ParseObject(BOOK_KEY);
            bookObject.put("googleID", tempBookStorage.get(0).getGoogleBooksID());
            bookObject.put("title", tempBookStorage.get(0).getBookTitle());
            bookObject.put("author", tempBookStorage.get(0).getBookAuthor());
            bookObject.put("genre", tempBookStorage.get(0).getBookGenre());
            bookObject.put("description", tempBookStorage.get(0).getBookDescription());
            bookObject.put("imageUrl", tempBookStorage.get(0).getBookImage());
            bookObject.put(ParseUser.getCurrentUser().toString(), bookObject);
            bookObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        ParseObject userFavoriteObject = new ParseObject(FAVORITES_KEY);
                        userFavoriteObject.put("user", ParseUser.getCurrentUser());
                        userFavoriteObject.put("book", bookObject.getObjectId());
                        userFavoriteObject.saveInBackground();
                    }
                }
            });
        } else {
            ParseObject userFavoriteObject = new ParseObject(FAVORITES_KEY);
            userFavoriteObject.put("user", ParseUser.getCurrentUser());
            userFavoriteObject.put("book", parseObject.getObjectId());
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
