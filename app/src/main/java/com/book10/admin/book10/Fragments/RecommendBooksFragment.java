package com.book10.admin.book10.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.book10.admin.book10.Models.BooksModel;
import com.book10.admin.book10.Models.RecommendedSingleton;
import com.book10.admin.book10.R;
import com.book10.admin.book10.Utilities.UpdateRecommendedBooks;

import java.util.ArrayList;

/**
 * Created by admin on 12/2/14.
 */
public class RecommendBooksFragment extends ListFragment{

    private Button updateButton;
    private ArrayList<BooksModel> recommendedBooks;
    private ArrayList<BooksModel> backUpRecommendations;
    private RecommendedSingleton recommendedSingleton;

    public static RecommendBooksFragment newInstance() {
        RecommendBooksFragment recommendBooksFragment = new RecommendBooksFragment();
        return recommendBooksFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_booklists, container, false);
        updateButton = (Button) rootView.findViewById(R.id.list_button);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListFromSingleton();
        final BookListAdapter adapter = new BookListAdapter(getActivity(), recommendedBooks);
        if (recommendedBooks.size() == 0) {
            Toast.makeText(getActivity(), R.string.no_recommendations, Toast.LENGTH_SHORT).show();
        }
        updateButton.setText(R.string.update_recommended_list_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateRecommendedBooks updateRecommendedBooks = new UpdateRecommendedBooks();
                updateRecommendedBooks.getUserFavorites();
            }
        });
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public void setListFromSingleton() {
        recommendedSingleton = RecommendedSingleton.getInstance();
        if (recommendedSingleton.getRecommendedList().size() > 0) {
            recommendedBooks = (ArrayList<BooksModel>) recommendedSingleton.getRecommendedList().subList(0, 10);
            backUpRecommendations = (ArrayList<BooksModel>) recommendedSingleton.getRecommendedList().subList(10, 20);
        } else {
            recommendedBooks = new ArrayList<BooksModel>();
            backUpRecommendations = new ArrayList<BooksModel>();
        }
    }

    public class BookListAdapter extends ArrayAdapter<BooksModel> {

        public BookListAdapter(Context context, ArrayList<BooksModel> recommendedList) {
            super(context, 0, recommendedList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            View rowView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main_list_item, parent, false);
            TextView bookTitle = (TextView) rowView.findViewById(R.id.book_title);
            TextView bookAuthor = (TextView) rowView.findViewById(R.id.book_author);
            Button deleteButton = (Button) rowView.findViewById(R.id.delete_button);

            BooksModel currentBook = getItem(position);
            bookTitle.setText(currentBook.getBookTitle());
            bookAuthor.setText(currentBook.getBookAuthor());
            deleteButton.setTag(currentBook);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    BooksModel bookToRemove = (BooksModel)v.getTag();
//                    String id = bookToRemove.getGoogleBooksID();
//                    removeFavoriteFromParse(id);
//                    remove(bookToRemove);
//                    favoritesSingleton.removeFromFavoritesList(pos);
//                    adapter.notifyDataSetChanged();
                }
            });
            return rowView;
        }
    }

}
