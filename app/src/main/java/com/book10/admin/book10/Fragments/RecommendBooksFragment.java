package com.book10.admin.book10.Fragments;

import android.app.FragmentTransaction;
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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;

/**
 * Created by admin on 12/2/14.
 */
@EFragment
public class RecommendBooksFragment extends ListFragment{

    private Button updateButton;
    private ArrayList<BooksModel> recommendedBooks = new ArrayList<BooksModel>();
    private ArrayList<BooksModel> backUpRecommendations =  new ArrayList<BooksModel>();
    private RecommendedSingleton recommendedSingleton = RecommendedSingleton.getInstance();;
    private BookListAdapter adapter;
    private UpdateRecommendedBooks updateRecommendedBooks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_booklists, container, false);
        updateButton = (Button) rootView.findViewById(R.id.list_button);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new BookListAdapter(getActivity(), recommendedBooks);
        setListAdapter(adapter);
        setListFromSingleton();
        if (recommendedBooks.size() == 0) {
            Toast.makeText(getActivity(), R.string.no_recommendations, Toast.LENGTH_SHORT).show();
        }
        updateRecommendedBooks = new UpdateRecommendedBooks(getActivity(), new UpdateRecommendedBooks.OnRecommendationsUpdate() {
            @Override
            public void recommendationsUpdated() {
                setListFromSingleton();
            }
        });
        updateButton.setText(R.string.update_recommended_list_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRecommendedBooks.getUserFavorites();
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        RecommendedSingleBookViewFragment recommendedSingleBookViewFragment = RecommendedSingleBookViewFragment.newInstance();
        BooksModel book = (BooksModel) getListAdapter().getItem(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable("recommended", book);
        recommendedSingleBookViewFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
            .replace(R.id.main_container, recommendedSingleBookViewFragment)
            .addToBackStack("recommendations")
            .commit();
    }



    public void setListFromSingleton() {
        if (recommendedSingleton.getRecommendedList().size() > 0 && recommendedSingleton.getRecommendedList().size() > 10) {
            recommendedBooks = new ArrayList<BooksModel>(recommendedSingleton.getRecommendedList().subList(0, 10));
            backUpRecommendations = new ArrayList<BooksModel>(recommendedSingleton.getRecommendedList().subList(10, 20));
            if (adapter != null) {
                adapter.clear();
            }
            adapter.addAll(recommendedBooks);
            adapter.notifyDataSetChanged();
        } else if (recommendedSingleton.getRecommendedList().size() > 0) {
            recommendedBooks = recommendedSingleton.getRecommendedList();
            if (adapter != null) {
                adapter.clear();
            }
            adapter.addAll(recommendedBooks);
            adapter.notifyDataSetChanged();
        } else {
            recommendedBooks = new ArrayList<BooksModel>();
            backUpRecommendations = new ArrayList<BooksModel>();
        }
    }

    public class BookListAdapter extends ArrayAdapter<BooksModel> {

        private final static String RECOMMENDED_LIST_KEY = "UserRecommendations";
        private TextView bookTitle;
        private TextView bookAuthor;
        private Button deleteButton;

        public BookListAdapter(Context context, ArrayList<BooksModel> recommendedList) {
            super(context, 0, recommendedList);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main_list_item, parent, false);
            }
            bookTitle = (TextView) convertView.findViewById(R.id.book_title);
            bookAuthor = (TextView) convertView.findViewById(R.id.book_author);
            deleteButton = (Button) convertView.findViewById(R.id.delete_button);

            BooksModel currentBook = getItem(position);
            bookTitle.setText(currentBook.getBookTitle());
            bookAuthor.setText(currentBook.getBookAuthor());
            deleteButton.setTag(currentBook);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BooksModel bookToRemove = (BooksModel) v.getTag();
                    String id = bookToRemove.getGoogleBooksID();
                    removeFavoriteFromParse(id);
                    remove(bookToRemove);
                    recommendedSingleton.removeFromRecommendedList(position);
                    if(backUpRecommendations.size() > 0) {
                        recommendedSingleton.addToRecommendedList(backUpRecommendations.get(0));
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }

        private void removeFavoriteFromParse(String googleID) {
            ParseQuery findBookToRemove = new ParseQuery("Book");
            findBookToRemove.whereEqualTo("googleID", googleID);
            ParseQuery recommendationToRemove = new ParseQuery(RECOMMENDED_LIST_KEY);
            recommendationToRemove.whereEqualTo("user", ParseUser.getCurrentUser());
            recommendationToRemove.whereMatchesQuery("book", findBookToRemove);
            recommendationToRemove.getFirstInBackground(new GetCallback() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    parseObject.deleteInBackground();
                }
            });
        }
    }
}
