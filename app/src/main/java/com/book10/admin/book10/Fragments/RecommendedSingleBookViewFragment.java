package com.book10.admin.book10.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.book10.admin.book10.Models.BooksModel;
import com.book10.admin.book10.R;
import com.squareup.picasso.Picasso;

/**
 * Created by admin on 1/9/15.
 */
public class RecommendedSingleBookViewFragment extends Fragment {

    public static RecommendedSingleBookViewFragment newInstance() {
        RecommendedSingleBookViewFragment recommendedSingleBookViewFragment = new RecommendedSingleBookViewFragment();
        return recommendedSingleBookViewFragment;
    }

    private ImageView image;
    private TextView title;
    private TextView author;
    private TextView genre;
    private TextView description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recommended_single_book_view, container, false);
        image = (ImageView) rootView.findViewById(R.id.single_book_image);
        title = (TextView) rootView.findViewById(R.id.single_book_title);
        author = (TextView) rootView.findViewById(R.id.single_book_author);
        genre = (TextView) rootView.findViewById(R.id.single_book_genre);
        description = (TextView) rootView.findViewById(R.id.single_book_description);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BooksModel book = getArguments().getParcelable("recommended");
        title.setText(book.getBookTitle());
        author.setText(book.getBookAuthor());
        genre.setText(book.getBookGenre());
        description.setText(book.getBookDescription());
        if (book.getBookImage() != null) {
            Picasso.with(getActivity()).load(book.getBookImage()).resize(50, 50).into(image);
        }
    }
}
