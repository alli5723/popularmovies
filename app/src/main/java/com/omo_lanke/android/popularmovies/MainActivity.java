package com.omo_lanke.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.omo_lanke.android.popularmovies.Adapters.MovieAdapter;
import com.omo_lanke.android.popularmovies.api.TMDBService;
import com.omo_lanke.android.popularmovies.data.MoviesContract;
import com.omo_lanke.android.popularmovies.model.ApiResponse;
import com.omo_lanke.android.popularmovies.model.MovieDetail;
import com.omo_lanke.android.popularmovies.utils.AppConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
        , MovieAdapter.MovieAdapterOnClickHandler{

    @BindView(R.id.loadingView)
    LinearLayout loadingView;
    Context context;
    public static MovieDetail selectedMovie;

    @BindView(R.id.movieList)
    RecyclerView movieList;

    Menu menu;
    public static int SELECTED_MENU = 0;
    public static String USER_LIST_TYPE = "list_type";

    private String sortby = "popularity.desc";
    private String page = "1";
    public MovieAdapter myListAdapter;
    public static List<MovieDetail> moviesList = null;
    String TAG = MainActivity.class.getSimpleName();

    private static final int ID_FAVORITE_LOADER = 1;


    public static final String[] FAV_MOVIE_PROJECTION = {
            MoviesContract.MoviesEntry.COLUMN_NAME_ID,
            MoviesContract.MoviesEntry.COLUMN_NAME_BACKDROP,
            MoviesContract.MoviesEntry.COLUMN_NAME_OVERVIEW,
            MoviesContract.MoviesEntry.COLUMN_NAME_POSTER,
            MoviesContract.MoviesEntry.COLUMN_NAME_HOMEPAGE,
            MoviesContract.MoviesEntry.COLUMN_NAME_ORIGINAL_TITLE,
            MoviesContract.MoviesEntry.COLUMN_NAME_VOTE_AVERAGE,
            MoviesContract.MoviesEntry.COLUMN_NAME_RELEASE_DATE,
            MoviesContract.MoviesEntry.COLUMN_NAME_RUNTIME,
    };

    public static final int INDEX_ID = 0;
    public static final int INDEX_BACKDROP = 1;
    public static final int INDEX_OVERVIEW = 2;
    public static final int INDEX_POSTER = 3;
    public static final int INDEX_HOMEPAGE = 4;
    public static final int INDEX_ORIGINAL_TITLE = 5;
    public static final int INDEX_VOTE_AVERAGE = 6;
    public static final int INDEX_RELEASE_DATE = 7;
    public static final int INDEX_RUNTIME = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;

        myListAdapter = new MovieAdapter(context, this);

        final GridLayoutManager layoutManager = new GridLayoutManager(context,2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        movieList.setLayoutManager(layoutManager);
        movieList.setHasFixedSize(true);
        movieList.setAdapter(myListAdapter);

        if (savedInstanceState != null) {
            // Restore the list to what was
            SELECTED_MENU = savedInstanceState.getInt(USER_LIST_TYPE);

        }else{
            apiCall();
        }

    }

    public void apiCall(){
        loadingView.setVisibility(View.VISIBLE);
        Call<ApiResponse> popularMovies = new TMDBService().endpoints().listMovies(sortby,
                AppConstants.APIKey, page);

        popularMovies.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                try{
                    moviesList = response.body().getResults();
                    loadingView.setVisibility(View.GONE);
                }catch (Exception e){
                    e.printStackTrace();
                    Snackbar.make(loadingView, "Error Occured, Couldn't PARSE the result.",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                myListAdapter.resetData(moviesList);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("response error", "onFailure: " + t.getMessage() );
                loadingView.setVisibility(View.GONE);
                Snackbar.make(loadingView, "Unable to fetch movies at this time, please try again later.",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    public void showDetails(MovieDetail movie){
        Intent intent = new Intent(context, MovieInformationActivity.class);
        selectedMovie = movie;
        intent.putExtra("movie_id", movie.getId());
        startActivity(intent);
    }

    public void changeTitle(String name){
        getSupportActionBar().setTitle(name);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(USER_LIST_TYPE, SELECTED_MENU);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        SELECTED_MENU = savedInstanceState.getInt(USER_LIST_TYPE);
    }

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_details, menu);
    return true;
}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
            // Restore the list to what was
            MenuItem menuItem;
            switch (SELECTED_MENU){
                case 0 :
                    menuItem =  menu.findItem(R.id.action_popular);
                    break;
                case 1 :
                    menuItem =  menu.findItem(R.id.action_rating);
                    break;
                case 2 :
                    menuItem =  menu.findItem(R.id.action_favorite);
                    break;
                default:
                    menuItem = menu.findItem(R.id.action_popular);
                    break;
            }
            menuItem.setChecked(true);
            showList(SELECTED_MENU);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.i(TAG, "onOptionsItemSelected: settings");
        item.setChecked(true);
//        myListAdapter.clearData();

        if (id == R.id.action_popular) {
            SELECTED_MENU = 0;
        }else if(id == R.id.action_rating){
            SELECTED_MENU = 1;
        }else if(id == R.id.action_favorite){
            SELECTED_MENU = 2;
        }
        showList(SELECTED_MENU);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int index, MovieDetail movie) {
        showDetails(movie);
    }

    public void showList(int index){
        switch (index){
            case 0 :
                changeTitle("Popular Movies");
                Log.i(TAG, "onOptionsItemSelected: Popular");
                sortby = "popularity.desc";
                apiCall();
                break;
            case 1 :
                changeTitle("Highly Rated Movies");
                Log.i(TAG, "onOptionsItemSelected: Rating");
                sortby = "vote_average.desc";
                apiCall();
                break;
            case 2 :
                changeTitle("Favorites");
                Log.d(TAG, "onOptionsItemSelected: Favorite");
                //Fetch from DB
                getSupportLoaderManager().initLoader(ID_FAVORITE_LOADER, null, this);
                break;
            default:
                changeTitle("Popular Movies");
                Log.i(TAG, "onOptionsItemSelected: Popular");
                sortby = "popularity.desc";
                apiCall();
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {


        switch (loaderId) {

            case ID_FAVORITE_LOADER:
                Uri moviesQueryUri = MoviesContract.MoviesEntry.CONTENT_URI;
                String sortOrder = MoviesContract.MoviesEntry.COLUMN_NAME_ID + " ASC";

                return new CursorLoader(this,
                        moviesQueryUri,
                        FAV_MOVIE_PROJECTION,
                        null,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.getCount() != 0){
            myListAdapter.swapCursor(data);
        }else{
            Snackbar.make(loadingView, "You currently do not have any favorites selected.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus making its data unavailable.
     * The application should at this point remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*
         * Since this Loader's data is now invalid, we need to clear the Adapter that is
         * displaying the data.
         */
        myListAdapter.swapCursor(null);
    }
}
