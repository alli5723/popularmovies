package com.omo_lanke.android.popularmovies.model;

/**
 * Created by omo_lanke on 14/04/2017.
 */

public class MovieDetail {
    String id;
    String backdrop_path;
    String overview;
    String poster_path;
    String homepage;
    String original_title;
    String vote_average;
    String release_date;
    String runtime;

    public String getId() {
        return id;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }
}


