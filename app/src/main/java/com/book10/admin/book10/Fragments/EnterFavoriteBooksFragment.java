package com.book10.admin.book10.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.book10.admin.book10.APIcalls.GoogleBooksAPI;
import com.book10.admin.book10.Models.BooksModel;
import com.book10.admin.book10.Models.FavoritesSingleton;
import com.book10.admin.book10.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12/16/14.
 */
public class EnterFavoriteBooksFragment extends Fragment{

    private final String FAVORITES_KEY = "UserFavorites";
    private final String BOOK_KEY = "Book";
    private EditText enterTitle;
    private EditText enterAuthor;
    private Button submitBookButton;
    private TextView confirmTitle;
    private TextView confirmAuthor;
    private String userEnteredTitle;
    private String userEnteredAuthor;
    private int googleBooksArrayIndex = 0;
    private ArrayList<BooksModel> tempBookStorage = new ArrayList<BooksModel>();
    private GoogleBooksAPI googleBooksAPI;
    private BooksModel tempBook;
    private ParseObject bookObject;
    private FavoritesSingleton favoritesSingleton;

    public static EnterFavoriteBooksFragment newInstance() {
        EnterFavoriteBooksFragment enterFavoriteBooks = new EnterFavoriteBooksFragment();
        return enterFavoriteBooks;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        favoritesSingleton = FavoritesSingleton.getInstance();
        View rootView = View.inflate(getActivity(), R.layout.fragment_enter_favorite_book_form, null);
        enterTitle = (EditText) rootView.findViewById(R.id.enter_title);
        enterAuthor = (EditText) rootView.findViewById(R.id.enter_author);
        submitBookButton = (Button) rootView.findViewById(R.id.submit_favorite_book_button);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submitBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEnteredTitle = enterTitle.getText().toString();
                userEnteredAuthor = enterAuthor.getText().toString();
                if (userEnteredTitle.length() == 0 || userEnteredAuthor.length() == 0) {
                    Toast.makeText(getActivity(), R.string.need_title_author_submit_favorites_message, Toast.LENGTH_SHORT).show();
                } else {
                    getBooksFromGoogleAPI();
                }
            }
        });
    }

    private void getBooksFromGoogleAPI() {
        googleBooksAPI = new GoogleBooksAPI(getActivity(), userEnteredTitle, userEnteredAuthor, new GoogleBooksAPI.OnGoogleBooksDataLoadedListener() {
            @Override
            public void dataLoaded(List<BooksModel> books) {
                tempBookStorage.clear();
                googleBooksArrayIndex = 0;
                if (books.size() == 0) {
                    Toast.makeText(getActivity(), R.string.no_results, Toast.LENGTH_SHORT).show();
                } else {
                    tempBookStorage = (ArrayList<BooksModel>) books;
                    checkEnteredBook();
                }
            }
        });
        googleBooksAPI.execute();
    }

    private void checkEnteredBook() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = View.inflate(getActivity(), R.layout.dialog_confirm_added_favorite, null);
        builder.setView(rootView);
        confirmTitle = (TextView) rootView.findViewById(R.id.confirmation_title);
        confirmAuthor = (TextView) rootView.findViewById(R.id.confirm_author);
        confirmTitle.setText(tempBookStorage.get(googleBooksArrayIndex).getBookTitle());
        confirmAuthor.setText(tempBookStorage.get(googleBooksArrayIndex).getBookAuthor());
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
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                tempBookStorage.clear();
                googleBooksArrayIndex = 0;
            }
        });
        dialog.show();
    }

    private void acceptedFavorites(DialogInterface dialog) {
        tempBook = tempBookStorage.get(googleBooksArrayIndex);
        favoritesSingleton.addToFavoritesList(tempBook);
        ParseQuery bookAlreadyAddedQuery = ParseQuery.getQuery(BOOK_KEY);
        bookAlreadyAddedQuery.whereEqualTo("googleID", tempBook.getGoogleBooksID());
        bookAlreadyAddedQuery.getFirstInBackground( new GetCallback() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                saveFavoritesToParse(parseObject, tempBook);
                tempBookStorage.clear();
                googleBooksArrayIndex = 0;
                getFragmentManager().popBackStack();
            }
        });

    }

    private void declinedFavorites(DialogInterface dialog) {
        if (googleBooksArrayIndex < tempBookStorage.size()) {
            googleBooksArrayIndex++;
            checkEnteredBook();
        } else {
            Toast.makeText(getActivity(), R.string.declined_favorites_confirmation, Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }

    private void saveFavoritesToParse(ParseObject parseObject, BooksModel tempBook) {
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
}
