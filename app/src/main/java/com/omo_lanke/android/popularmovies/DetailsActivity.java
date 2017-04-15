package com.omo_lanke.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.omo_lanke.android.popularmovies.api.TMDBService;
import com.omo_lanke.android.popularmovies.model.ApiResponse;
import com.omo_lanke.android.popularmovies.model.MovieDetail;
import com.omo_lanke.android.popularmovies.utils.AppConstants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.loadingView)
    LinearLayout loadingView;

    @BindView(R.id.movie_rating)
    RatingBar movie_rating;

    @BindView(R.id.overview)
    TextView overview;

    @BindView(R.id.movie_title)
    TextView movie_title;

    @BindView(R.id.release)
    TextView release;

    @BindView(R.id.length)
    TextView length;

    @BindView(R.id.rate)
    TextView rate;

    @BindView(R.id.toolbar_bg)
    ImageView toolbar_bg;

    @BindView(R.id.poster)
    ImageView poster;

    Context context;
    String movieShare = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        ButterKnife.bind(this);
        setName("");
        Intent intent = getIntent();

        if(intent != null){
            if(intent.hasExtra("movie_id")){
                Log.d("Movie selected", "onCreate: "+intent.getStringExtra("movie_id"));
                apiCall(intent.getStringExtra("movie_id"));
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareMovie();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void shareMovie(){
        Intent shareIntent = ShareCompat.IntentBuilder.from(DetailsActivity.this)
                .setText("We should check out this movie together sometime soon ;) "+movieShare)
                .setType("text/plain")
                .setChooserTitle("Nice Movie")
                .getIntent();
        startActivity(shareIntent);

    }

    public void setName(String name){
        getSupportActionBar().setTitle(name);
    }

    public void apiCall(final String movie){
        loadingView.setVisibility(View.VISIBLE);
        Call<MovieDetail> popularMovies = new TMDBService().endpoints().getDetails(movie, AppConstants.APIKey);

        popularMovies.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {

                MovieDetail moviesDetail = null;
                try{
                    moviesDetail = response.body();
                    movie_rating.setRating((Float.parseFloat(moviesDetail.getVote_average()))/2);
                    overview.setText(moviesDetail.getOverview());
                    movie_title.setText(moviesDetail.getOriginal_title());
                    release.setText( moviesDetail.getRelease_date());
                    length.setText(moviesDetail.getRuntime()+ "min");
                    rate.setText(moviesDetail.getVote_average() + "/" + 10);
                    setName(moviesDetail.getOriginal_title() );
                    movieShare = moviesDetail.getOriginal_title() + " " + moviesDetail.getHomepage();

                    Picasso.with(context)
                            .load(AppConstants.IMAGE_URL2 + moviesDetail.getBackdrop_path())
                            .into(toolbar_bg);
                    Picasso.with(context)
                            .load(AppConstants.IMAGE_URL+moviesDetail.getPoster_path())
                            .into(poster);
                    loadingView.setVisibility(View.GONE);

                }catch (Exception e){
                    e.printStackTrace();
                    Snackbar.make(loadingView, "Error occured while we try to load the movie details. Please, try later", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Log.e("response error", "onFailure: " + t.getMessage() );
                loadingView.setVisibility(View.GONE);
                Snackbar.make(loadingView, "Error occured while we try to load the movie details. Please try later", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
