package com.omo_lanke.android.popularmovies.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omo_lanke.android.popularmovies.MainActivity;
import com.omo_lanke.android.popularmovies.R;
import com.omo_lanke.android.popularmovies.model.MovieDetail;
import com.omo_lanke.android.popularmovies.utils.AppConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omo_lanke on 14/04/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{
    private Context context;
    private List<MovieDetail> values;

    final private MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler{
        void onClick(int index, MovieDetail movie);
    }

    public MovieAdapter(@NonNull Context context, MovieAdapterOnClickHandler clickHandler) {
        this.context = context;
        mClickHandler = clickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public ImageView movieImage;
        public TextView movieId;
        MovieAdapterViewHolder(View view){
            super(view);
            name = (TextView)view.findViewById(R.id.movie_name);
            movieImage = (ImageView)view.findViewById(R.id.movie_image);
            movieId = (TextView)view.findViewById(R.id.movie_id);

            view.setOnClickListener(this);
        }

        public void onClick(View v){
            int pos = getAdapterPosition();
            mClickHandler.onClick(pos, values.get(pos));
            Log.w("Movie Clicked", values.get(pos).getOriginal_title());
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        final Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        int layoutIdForList = R.layout.movie_item;
        View view = inflater.inflate(layoutIdForList, viewGroup, shouldAttachToParentImmediately);
        view.setFocusable(true);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        String movieName = values.get(position).getOriginal_title();
        movieAdapterViewHolder.name.setText(movieName);
        movieAdapterViewHolder.movieId.setText(values.get(position).getId());
        Picasso.with(context)
        .load(AppConstants.IMAGE_URL + values.get(position).getPoster_path())
        .into(movieAdapterViewHolder.movieImage);
    }

    @Override
    public int getItemCount() {
        if(null == values) return 0;
        return values.size();
    }

    public void resetData(List<MovieDetail> movies) {
        values = movies;
        notifyDataSetChanged();
    }

    public void swapCursor(Cursor newCursor) {
//        mCursor = newCursor;
        if (newCursor == null){
            values = null;
            notifyDataSetChanged();
            return;
        }
        List<MovieDetail> m = new ArrayList<>();
        for (int i = 0;  i < newCursor.getCount(); i++){
            MovieDetail mm = new MovieDetail();
            newCursor.moveToPosition(i);
            mm.setId(newCursor.getString(MainActivity.INDEX_ID));
            mm.setBackdrop_path(newCursor.getString(MainActivity.INDEX_BACKDROP));
            mm.setOverview(newCursor.getString(MainActivity.INDEX_OVERVIEW));
            mm.setPoster_path(newCursor.getString(MainActivity.INDEX_POSTER));
            mm.setHomepage(newCursor.getString(MainActivity.INDEX_HOMEPAGE));
            mm.setOriginal_title(newCursor.getString(MainActivity.INDEX_ORIGINAL_TITLE));
            mm.setVote_average(newCursor.getString(MainActivity.INDEX_VOTE_AVERAGE));
            mm.setRelease_date(newCursor.getString(MainActivity.INDEX_RELEASE_DATE));
            mm.setRuntime(newCursor.getString(MainActivity.INDEX_RUNTIME));
            m.add(mm);
        }
        values = m;
        notifyDataSetChanged();
    }

    public void clearData() {
        values.clear();
        notifyDataSetChanged();
    }
}
