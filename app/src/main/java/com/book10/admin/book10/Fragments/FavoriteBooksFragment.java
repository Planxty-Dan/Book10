package com.book10.admin.book10.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.book10.admin.book10.Models.BooksModel;
import com.book10.admin.book10.R;

import java.util.List;

/**
 * Created by admin on 12/6/14.
 */
public class FavoriteBooksFragment extends ListFragment{

    private List<BooksModel> favoriteBooks;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get local saved favorites
        numberOfFavoritesEneterChecker();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    private void numberOfFavoritesEneterChecker() {
        if (favoriteBooks.size() == 0) {
            noFavoritesEntered();
        }
        else if (favoriteBooks.size() > 0 && favoriteBooks.size() < 10) {

        }
        else {

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_enter_favorite_book_form, null));
        builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //pull edittexts and search googleBooks api
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
