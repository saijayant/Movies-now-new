package com.example.jayant_sai.moviesnow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.MoviesAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import custom.RichBottomNavigationView;
import custom.StatefulRecyclerView;
import model.Movies;
import networking.ApiInterface;
import networking.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.LisItemClickListener {

    @BindView(R.id.bottom_navigation)
    RichBottomNavigationView bottom_navigation;

    @BindView(R.id.rv_movies)
    StatefulRecyclerView mRvMovies;
    @BindView(R.id.tv_error_message_display)
    TextView mTvErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mPbLoadingIndicator;
    private ArrayList<Movies> moviesList;
    private ArrayList<Movies> popularMoviesList;
    private ArrayList<Movies> favoriteMoviesList;
    private GridLayoutManager gridLayoutManager;
    private MoviesAdapter mAdapter;
    private int positionBottomMenu = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        moviesList = new ArrayList<>();
        popularMoviesList = new ArrayList<>();
        favoriteMoviesList = new ArrayList<>();

        gridLayoutManager = new GridLayoutManager(this, 3);
        mRvMovies.setLayoutManager(gridLayoutManager);
        moviesList = new ArrayList<>();
        popularMoviesList = new ArrayList<>();
        favoriteMoviesList = new ArrayList<>();
        mAdapter = new MoviesAdapter(moviesList, this);
        mRvMovies.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            positionBottomMenu = savedInstanceState.getInt("selectedItem");
            moviesList = savedInstanceState.getParcelableArrayList("top");
            popularMoviesList = savedInstanceState.getParcelableArrayList("popular");
            favoriteMoviesList = savedInstanceState.getParcelableArrayList("fav");


        } else {
            getListOfTopRatedMovies();

        }

        bottom_navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_favorites:
                                Toast.makeText(MainActivity.this ,"fav",Toast.LENGTH_SHORT ).show();
                                mAdapter = new MoviesAdapter(moviesList, MainActivity.this);
                                mRvMovies.setAdapter(mAdapter);
                                getListOfTopRatedMovies();

                                break;
                            case R.id.action_schedules:
                                Toast.makeText(MainActivity.this ,"scedule",Toast.LENGTH_SHORT ).show();
                                mAdapter = new MoviesAdapter(popularMoviesList, MainActivity.this);
                                mRvMovies.setAdapter(mAdapter);
                                getListOfPopularMovies();

                                break;
                            case R.id.action_music:
                                Toast.makeText(MainActivity.this ,"music",Toast.LENGTH_SHORT ).show();
                                break;
                        }
                        return false;
                    }
                });
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedItem", positionBottomMenu);
        outState.putParcelableArrayList("top", moviesList);
        outState.putParcelableArrayList("popular", popularMoviesList);
        outState.putParcelableArrayList("fav", favoriteMoviesList);


    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        moviesList = savedInstanceState.getParcelableArrayList("top");
        popularMoviesList = savedInstanceState.getParcelableArrayList("popular");

    }
    private void getListOfPopularMovies() {
        if (Utils.isNetworkAvailble(this)) {
            mTvErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mRvMovies.setVisibility(View.VISIBLE);
            popularMoviesList.clear();
            mPbLoadingIndicator.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mTvErrorMessageDisplay.setVisibility(View.INVISIBLE);
            ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = api.getPopularMovies(BuildConfig.api_key);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                    if (BuildConfig.DEBUG) {
                        Log.d("movies", "onResponse:  response" + response.body().toString());
                    }

                    if (response.code() == 200) {

                        try {
                            JSONObject moviesResponse = new JSONObject(response.body().toString());

                            JSONArray moviesArray = moviesResponse.getJSONArray("results");


                            for (int i = 0; i < moviesArray.length(); i++) {

                                Movies movieData = new Movies();
                                JSONObject movieObj = moviesArray.getJSONObject(i);

                                if (BuildConfig.DEBUG) {
                                    Log.d("movies", "onResponse: " + movieObj.getString("poster_path"));
                                    Log.d("movies", "onResponse: title " + movieObj.getString("title"));
                                }
                                movieData.setMoviePosterUrl(movieObj.getString("poster_path"));
                                movieData.setTitle(movieObj.getString("title"));
                                movieData.setReleaseDate(movieObj.getString("release_date"));
                                movieData.setRatings(movieObj.getString("vote_average"));
                                movieData.setPlotSynopsis(movieObj.getString("overview"));
                                movieData.setId(movieObj.getString("id"));
                                popularMoviesList.add(movieData);


                            }

                            mAdapter.notifyDataSetChanged();
                            mPbLoadingIndicator.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                            if (BuildConfig.DEBUG) {

                                Log.d("movies", "onResponse: " + moviesResponse.getJSONArray("results").length());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mPbLoadingIndicator.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            mRvMovies.setVisibility(View.GONE);

                            mTvErrorMessageDisplay.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mPbLoadingIndicator.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        mRvMovies.setVisibility(View.GONE);
                        mTvErrorMessageDisplay.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {

                    if (BuildConfig.DEBUG) {
                        Log.d("movies", "onFailure: " + t.toString());
                        mRvMovies.setVisibility(View.GONE);
                        mTvErrorMessageDisplay.setVisibility(View.VISIBLE);
                        mPbLoadingIndicator.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    }

                }
            });

        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getListOfTopRatedMovies() {
        if (Utils.isNetworkAvailble(this)) {
            moviesList.clear();
            mPbLoadingIndicator.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mRvMovies.setVisibility(View.VISIBLE);
            mTvErrorMessageDisplay.setVisibility(View.INVISIBLE);
            ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = api.getTopRatedMovies(BuildConfig.api_key);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                    if (BuildConfig.DEBUG) {
                        Log.d("movies", "onResponse:  response" + response.body().toString());
                    }
                    if (response.code() == 200) {

                        try {
                            JSONObject moviesResponse = new JSONObject(response.body().toString());

                            JSONArray moviesArray = moviesResponse.getJSONArray("results");


                            for (int i = 0; i < moviesArray.length(); i++) {

                                Movies movieData = new Movies();
                                JSONObject movieObj = moviesArray.getJSONObject(i);
                                if (BuildConfig.DEBUG) {
                                    Log.d("movies", "onResponse: " + movieObj.getString("poster_path"));
                                }
                                movieData.setMoviePosterUrl(movieObj.getString("poster_path"));
                                movieData.setTitle(movieObj.getString("title"));
                                movieData.setReleaseDate(movieObj.getString("release_date"));
                                movieData.setRatings(movieObj.getString("vote_average"));
                                movieData.setPlotSynopsis(movieObj.getString("overview"));
                                movieData.setId(movieObj.getString("id"));
                                moviesList.add(movieData);


                            }

                            mAdapter.notifyDataSetChanged();
                            mPbLoadingIndicator.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            if (BuildConfig.DEBUG) {

                                Log.d("Movies", "onResponse: " + moviesResponse.getJSONArray("results").length());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mPbLoadingIndicator.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            mTvErrorMessageDisplay.setVisibility(View.VISIBLE);
                            mRvMovies.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        mRvMovies.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        mTvErrorMessageDisplay.setVisibility(View.VISIBLE);
                        mPbLoadingIndicator.setVisibility(View.INVISIBLE);

                    }

                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {

                    if (BuildConfig.DEBUG) {
                        Log.d("movies", "onFailure: " + t.toString());
                    }
                    mRvMovies.setVisibility(View.INVISIBLE);
                    mTvErrorMessageDisplay.setVisibility(View.VISIBLE);
                    mPbLoadingIndicator.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                }
            });

        } else {
            Toast.makeText(this, "No internet address", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListItemClick(int position) {
        switch (positionBottomMenu){

            case 0 :
                Intent i = new Intent(MainActivity.this, MoviesDetailsActivity.class);
                i.putExtra("title", moviesList.get(position).getTitle());
                i.putExtra("poster_url", moviesList.get(position).getMoviePosterUrl());
                i.putExtra("release_date", moviesList.get(position).getReleaseDate());
                i.putExtra("rating", moviesList.get(position).getRatings());
                i.putExtra("plot", moviesList.get(position).getPlotSynopsis());
                i.putExtra("id", moviesList.get(position).getId());
                startActivity(i);
                break;


            case 1 :
                Intent iPopular = new Intent(MainActivity.this, MoviesDetailsActivity.class);
                iPopular.putExtra("title", popularMoviesList.get(position).getTitle());
                iPopular.putExtra("poster_url", popularMoviesList.get(position).getMoviePosterUrl());
                iPopular.putExtra("release_date", popularMoviesList.get(position).getReleaseDate());
                iPopular.putExtra("rating", popularMoviesList.get(position).getRatings());
                iPopular.putExtra("plot", popularMoviesList.get(position).getPlotSynopsis());
                iPopular.putExtra("id", popularMoviesList.get(position).getId());
                startActivity(iPopular);
                break;

            case 2 :
                Intent iFav = new Intent(MainActivity.this, MoviesDetailsActivity.class);
                iFav.putExtra("title", favoriteMoviesList.get(position).getTitle());
                iFav.putExtra("poster_url", favoriteMoviesList.get(position).getMoviePosterUrl());
                iFav.putExtra("release_date", favoriteMoviesList.get(position).getReleaseDate());
                iFav.putExtra("rating", favoriteMoviesList.get(position).getRatings());
                iFav.putExtra("plot", favoriteMoviesList.get(position).getPlotSynopsis());
                iFav.putExtra("id", favoriteMoviesList.get(position).getId());
                startActivity(iFav);

        }
    }
}
