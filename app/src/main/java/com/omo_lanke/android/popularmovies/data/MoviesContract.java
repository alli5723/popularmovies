package com.omo_lanke.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by omo_lanke on 06/05/2017.
 */

public class MoviesContract {

    public static final String AUTHORITY = "com.omo_lanke.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    private MoviesContract(){}

    public static final class MoviesEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_BACKDROP = "backdrop_path";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_POSTER = "poster_path";
        public static final String COLUMN_NAME_HOMEPAGE = "homepage";
        public static final String COLUMN_NAME_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
        public static final String COLUMN_NAME_RUNTIME = "runtime";
    }
}
