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
import com.book10.admin.book10.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12/2/14.
 */
public class RecommendBooksFragment extends ListFragment{

    private TextView bookTitle;
    private TextView bookAuthor;
    private Button deleteButton;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final BookListAdapter adapter = new BookListAdapter(getActivity());
        List<BooksModel> recommendedBooks = new ArrayList<BooksModel>();
        //get saved array of recommendations from local Parse save
        if (recommendedBooks.size() == 0) {
            sendToFavoriteBooks();
        }
        setListAdapter(adapter);
        //get list
        //set an dataLoaded interface for adapter
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public void sendToFavoriteBooks() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.send_to_favorites_toast)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FavoriteBooksFragment favoriteBooksFragment = new FavoriteBooksFragment();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, favoriteBooksFragment)
                                .addToBackStack("recommended books")
                                .commit();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class BookListAdapter extends ArrayAdapter<BooksModel> {

        public BookListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main_list_item, parent, false);
            bookTitle = (TextView) rowView.findViewById(R.id.book_title);
            bookAuthor = (TextView) rowView.findViewById(R.id.book_author);
            deleteButton = (Button) rowView.findViewById(R.id.delete_button);
            //set fields to arraylist
            return rowView;
        }
    }

}