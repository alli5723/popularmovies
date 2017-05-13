package com.omo_lanke.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.omo_lanke.android.popularmovies.Adapters.ReviewsAdapter;
import com.omo_lanke.android.popularmovies.Adapters.TrailerAdapter;
import com.omo_lanke.android.popularmovies.api.TMDBService;
import com.omo_lanke.android.popularmovies.data.MoviesContract;
import com.omo_lanke.android.popularmovies.model.MovieDetail;
import com.omo_lanke.android.popularmovies.model.ReviewsDetails;
import com.omo_lanke.android.popularmovies.model.ReviewsResponse;
import com.omo_lanke.android.popularmovies.model.TrailerDetails;
import com.omo_lanke.android.popularmovies.model.TrailerResponse;
import com.omo_lanke.android.popularmovies.utils.AppConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieInformationActivity extends AppCompatActivity {
    @BindView(R.id.loadingView)
    LinearLayout loadingView;

    @BindView(R.id.movie_rating)
    RatingBar movie_rating;

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

    @BindView(R.id.imageViewFav)
    ImageView favorite;

    @BindView(R.id.fab)
    FloatingActionButton fabTrailer;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    Context context;
    String movieShare = "";
    public static MovieDetail movieClicked;
    public static List<ReviewsDetails> reviews = new ArrayList<>();
    public static List<TrailerDetails> trailers = new ArrayList<>();

    public static ReviewsAdapter reviewsListAdapter;
    public static TrailerAdapter trailerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ButterKnife.bind(this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        setName("");
        Intent intent = getIntent();

        movieClicked = MainActivity.selectedMovie;
        loadView(movieClicked);

        if(intent != null){
            if(intent.hasExtra("movie_id")){
                apiCall(intent.getStringExtra("movie_id"));
            }else{
                apiCall(movieClicked.getId());
            }
        }

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMovieToFavorite(movieClicked);
            }
        });

        fabTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playTrailer();
            }
        });

    }

    void addMovieToFavorite(MovieDetail m){
        ContentValues cv = new ContentValues();
        Log.e("MovieInsert", "Adding "+m.getOriginal_title()+" to favorites");
        cv.put(MoviesContract.MoviesEntry.COLUMN_NAME_ID, Integer.parseInt(m.getId()));
        cv.put(MoviesContract.MoviesEntry.COLUMN_NAME_BACKDROP, m.getBackdrop_path());
        cv.put(MoviesContract.MoviesEntry.COLUMN_NAME_OVERVIEW, m.getOverview());
        cv.put(MoviesContract.MoviesEntry.COLUMN_NAME_POSTER, m.getPoster_path());
        cv.put(MoviesContract.MoviesEntry.COLUMN_NAME_HOMEPAGE, m.getHomepage());
        cv.put(MoviesContract.MoviesEntry.COLUMN_NAME_ORIGINAL_TITLE, m.getOriginal_title());
        cv.put(MoviesContract.MoviesEntry.COLUMN_NAME_VOTE_AVERAGE, m.getVote_average());
        cv.put(MoviesContract.MoviesEntry.COLUMN_NAME_RELEASE_DATE, m.getRelease_date());
        cv.put(MoviesContract.MoviesEntry.COLUMN_NAME_RUNTIME, m.getRuntime());

        try {
            Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, cv);
            if(uri != null){
                Snackbar.make(loadingView, "Movie has been Added as Favorite.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }catch (Exception ex){
            Snackbar.make(loadingView, "An error occured, couldn't add Movie to favorite at this time.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    void shareMovie(){
        Intent shareIntent = ShareCompat.IntentBuilder.from(MovieInformationActivity.this)
                .setText("We should check out this movie together sometime soon ;) "+movieShare)
                .setType("text/plain")
                .setChooserTitle("Nice Movie")
                .getIntent();
        startActivity(shareIntent);

    }

    void playTrailer(){
        if(trailers == null) {
            Snackbar.make(loadingView, "Sorry, but this movie does not have any trailer.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        String url = AppConstants.VIDEO_BASE + trailers.get(0).getKey();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void apiCall(final String movie){
        Call<MovieDetail> popularMovies = new TMDBService().endpoints().getDetails(movie, AppConstants.APIKey);

        popularMovies.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {

                MovieDetail moviesDetail = null;
                try{
                    moviesDetail = response.body();
                    length.setText(moviesDetail.getRuntime()+ "min");

                }catch (Exception e){
                    e.printStackTrace();
                    Snackbar.make(toolbar_bg, "Error occured while we try to load the movie details. Please, try later", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Log.e("response error", "onFailure: " + t.getMessage() );
                Snackbar.make(toolbar_bg, "Error occured while we try to load the movie details. Please try later", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        reviews = getReviews(movieClicked.getId());
        trailers = getTrailers(movieClicked.getId());
    }

    public List<TrailerDetails> getTrailers(String movie_id){
        final List<TrailerDetails> trailers = new ArrayList<>();
        Call<TrailerResponse> movieTrailers = new TMDBService().endpoints().getTrailers(movie_id, AppConstants.APIKey);

        movieTrailers.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                List<TrailerDetails> tt = null;
                try{
                    tt = response.body().getResults();
                    for(TrailerDetails trailer : tt){
                        trailers.add(trailer);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Snackbar.make(toolbar_bg, "Error occured while we try to load the movie trailers. Please, try later", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                fabTrailer.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Snackbar.make(toolbar_bg, "Error occured while we try to load the movie trailers. Please, try later", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return trailers;
    }

    public static List<ReviewsDetails> getReviews(String movie_id){
        final List<ReviewsDetails> reviews = new ArrayList<>();
        Call<ReviewsResponse> movieReviews = new TMDBService().endpoints().getReviews(movie_id, AppConstants.APIKey);

        movieReviews.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                List<ReviewsDetails> rr = null;
                try{
                    rr = response.body().getResults();
                    for(ReviewsDetails review : rr){
                        reviews.add(review);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
            }
        });
        return reviews;
    }

    public void setName(String name){
        getSupportActionBar().setTitle(name);
    }


    void loadView(MovieDetail moviesDetail){
        movie_rating.setRating((Float.parseFloat(moviesDetail.getVote_average()))/2);
        movie_title.setText(moviesDetail.getOriginal_title());
        release.setText( moviesDetail.getRelease_date());
        rate.setText(moviesDetail.getVote_average() + "/" + 10);
        movieShare = moviesDetail.getOriginal_title() + " " + moviesDetail.getHomepage();

        Picasso.with(context)
                .load(AppConstants.IMAGE_URL2 + moviesDetail.getBackdrop_path())
                .into(toolbar_bg);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements ReviewsAdapter.ReviewsAdapterOnClickHandler{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            reviewsListAdapter = new ReviewsAdapter(getContext(), this, reviews);
            trailerListAdapter = new TrailerAdapter(getContext(), trailers);

            View overviewRootView = inflater.inflate(R.layout.content_details, container, false);
            TextView overview = (TextView)overviewRootView.findViewById(R.id.overview);
//            View loadingView = (View)overviewRootView.findViewById(R.id.loadingView);
//            loadingView.setVisibility(View.GONE);
            overview.setText(movieClicked.getOverview());

            View trailerRootView = inflater.inflate(R.layout.trailer_list_tab, container, false);
            RecyclerView trailerList = (RecyclerView)trailerRootView.findViewById(R.id.trailerList);
            final LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(getContext());
            trailerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            trailerList.setLayoutManager(trailerLayoutManager);
            trailerList.setHasFixedSize(true);
            trailerList.setAdapter(trailerListAdapter);

            View reviewsRootView = inflater.inflate(R.layout.reviews_list_tab, container, false);
            RecyclerView reviewList = (RecyclerView)reviewsRootView.findViewById(R.id.reviewList);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            reviewList.setLayoutManager(layoutManager);
            reviewList.setHasFixedSize(true);
            reviewList.setAdapter(reviewsListAdapter);

Log.e("SectionID", getArguments().getInt(ARG_SECTION_NUMBER)+"");
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1 :
                    return overviewRootView;
                case 2 :
                    return trailerRootView;
                case 3 :
                    return reviewsRootView;
                default:
                    return overviewRootView;
            }
        }

        @Override
        public void onClick(int index, ReviewsDetails review) {

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Overview";
                case 1:
                    return "Trailers";
                case 2:
                    return "Reviews";
            }
            return null;
        }
    }
}
