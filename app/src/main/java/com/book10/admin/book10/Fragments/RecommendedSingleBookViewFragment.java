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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by admin on 1/9/15.
 */
@EFragment(R.layout.fragment_recommended_single_book_view)
public class RecommendedSingleBookViewFragment extends Fragment {

    @ViewById (R.id.single_book_image)
    protected ImageView image;

    @ViewById (R.id.single_book_title)
    protected TextView title;

    @ViewById (R.id.single_book_author)
    protected TextView author;

    @ViewById (R.id.single_book_genre)
    protected TextView genre;

    @ViewById (R.id.single_book_description)
    protected TextView description;

    @AfterViews
    protected void setView() {
        BooksModel book = getArguments().getParcelable("recommended");
        title.setText(book.getBookTitle());
        author.setText(book.getBookAuthor());
        genre.setText(book.getBookGenre());
        description.setText(book.getBookDescription());
        if (book.getBookImage() != null) {
            Picasso.with(getActivity()).load(book.getBookImage()).resize(250, 375).into(image);
        }
    }
}
