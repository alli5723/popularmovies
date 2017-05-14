package com.omo_lanke.android.popularmovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omo_lanke.android.popularmovies.R;
import com.omo_lanke.android.popularmovies.model.ReviewsDetails;

import java.util.List;

/**
 * Created by omo_lanke on 12/05/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {
    private Context context;
    private List<ReviewsDetails> values;

    final private ReviewsAdapterOnClickHandler mClickHandler;

    public interface ReviewsAdapterOnClickHandler{
        void onClick(int index, ReviewsDetails review);
    }

    public ReviewsAdapter(@NonNull Context context, ReviewsAdapterOnClickHandler clickHandler, List<ReviewsDetails> r) {
        this.context = context;
        mClickHandler = clickHandler;
        values = r;
    }

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView author;
        public TextView review;
        ReviewsAdapterViewHolder(View view){
            super(view);
            author = (TextView)view.findViewById(R.id.textViewAuthor);
            review = (TextView)view.findViewById(R.id.textViewReview);

            view.setOnClickListener(this);
        }

        public void onClick(View v){
            int pos = getAdapterPosition();
            mClickHandler.onClick(pos, values.get(pos));
        }
    }

    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        final Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        int layoutIdForList = R.layout.reviews_list_item;
        View view = inflater.inflate(layoutIdForList, viewGroup, shouldAttachToParentImmediately);
        view.setFocusable(true);
        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapterViewHolder reviewsAdapterViewHolder, int position) {
        reviewsAdapterViewHolder.author.setText(values.get(position).getAuthor());
        reviewsAdapterViewHolder.review.setText(values.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if(null == values) return 0;
        return values.size();
    }

    public void resetData(List<ReviewsDetails> reviews) {
        Log.e("Reviews", "reseting reviews");
        values = reviews;
        notifyDataSetChanged();
    }
}
