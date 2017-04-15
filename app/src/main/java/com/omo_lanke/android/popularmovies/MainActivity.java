package com.omo_lanke.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omo_lanke.android.popularmovies.Adapters.MovieAdapter;
import com.omo_lanke.android.popularmovies.api.Endpoints;
import com.omo_lanke.android.popularmovies.api.TMDBService;
import com.omo_lanke.android.popularmovies.model.ApiResponse;
import com.omo_lanke.android.popularmovies.model.MovieItem;
import com.omo_lanke.android.popularmovies.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.loadingView)
    LinearLayout loadingView;
    Context context;

    @BindView(R.id.movieList)
    RecyclerView movieList;

    private String sortby = "popularity.desc";
    private String page = "1";
    public MovieAdapter myListAdapter;
    String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;

        myListAdapter = new MovieAdapter(context);

        final GridLayoutManager layoutManager = new GridLayoutManager(context,3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        movieList.setLayoutManager(layoutManager);
        movieList.setHasFixedSize(true);
        movieList.setAdapter(myListAdapter);

        apiCall();
    }

    public void apiCall(){
        loadingView.setVisibility(View.VISIBLE);
        Call<ApiResponse> popularMovies = new TMDBService().endpoints().listMovies(sortby, AppConstants.APIKey, page);

        popularMovies.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("Response", "onResponse: " + response.body().getTotal_pages());
                List<MovieItem> moviesList = null;
                try{
                    moviesList = response.body().getResults();
//                    myListAdapter = new MovieAdapter(context,moviesList);

//                    final GridLayoutManager layoutManager = new GridLayoutManager(context,3);
//                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                    movieList.setLayoutManager(layoutManager);
//                    movieList.setHasFixedSize(true);
//                    movieList.setAdapter(myListAdapter);

                    loadingView.setVisibility(View.GONE);

                }catch (Exception e){
                    e.printStackTrace();
                    Snackbar.make(loadingView, ".", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                myListAdapter.resetData(moviesList);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("response error", "onFailure: " + t.getMessage() );
                loadingView.setVisibility(View.GONE);
                Snackbar.make(loadingView, "Unable to fetch movies at this time, please try again later.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void showDetails(String movieId){
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("movie_id", movieId);
        startActivity(intent);
    }

    public void changeTitle(String name){
        getSupportActionBar().setTitle(name);
    }

@Override
public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
    MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
    inflater.inflate(R.menu.menu_details, menu);
        /* Return true so that the menu is displayed in the Toolbar */
    return true;
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.i(TAG, "onOptionsItemSelected: settings");
        item.setChecked(true);
        myListAdapter.clearData();

        if (id == R.id.action_popular) {
            changeTitle("Popular Movies");
            Log.i(TAG, "onOptionsItemSelected: Popular");
            sortby = "popularity.desc";
        }else if(id == R.id.action_rating){
            changeTitle("High rated Movies");
            Log.i(TAG, "onOptionsItemSelected: Rating");
            sortby = "vote_average.desc";
        }
        apiCall();
        return super.onOptionsItemSelected(item);
    }
}
