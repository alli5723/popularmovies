package com.omo_lanke.android.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omo_lanke.android.popularmovies.MainActivity;
import com.omo_lanke.android.popularmovies.R;
import com.omo_lanke.android.popularmovies.model.MovieItem;
import com.omo_lanke.android.popularmovies.utils.AppConstants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by omo_lanke on 14/04/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{
    private Context context;
    private List<MovieItem> values;

    @BindView(R.id.movie_name)
    TextView name;
    @BindView(R.id.movie_image)
    ImageView movieImage;

    @BindView(R.id.movie_id)
    TextView movieId;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MovieAdapterViewHolder(View view){
            super(view);
        }

        public void onClick(View v){
            int pos = getAdapterPosition();
            String movieName = values.get(pos).getOriginal_title();
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        final Context context = viewGroup.getContext();
        int layoutIdForList = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForList, viewGroup, shouldAttachToParentImmediately);
        ButterKnife.bind(this, view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity) context).showDetails(((TextView)view.findViewById(R.id.movie_id)).getText().toString());

            }
        });
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        String movieName = values.get(position).getOriginal_title();
        name.setText(movieName);
        movieId.setText(values.get(position).getId());

        Picasso.with(context)
        .load(AppConstants.IMAGE_URL + values.get(position).getPoster_path())
        .into(movieImage);
    }

    @Override
    public int getItemCount() {
        if(null == values) return 0;
        return values.size();
    }

    public void resetData(List<MovieItem> movies) {
        values = movies;
        notifyDataSetChanged();
    }

    public void clearData() {
        values.clear();
        notifyDataSetChanged();
    }
}
