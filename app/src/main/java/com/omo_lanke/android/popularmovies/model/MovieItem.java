package com.omo_lanke.android.popularmovies.model;

/**
 * Created by omo_lanke on 14/04/2017.
 */

public class MovieItem {
    String id;
    String poster_path;
    String original_title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }
}

