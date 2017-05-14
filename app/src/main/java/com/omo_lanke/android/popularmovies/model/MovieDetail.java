package com.omo_lanke.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by omo_lanke on 14/04/2017.
 */

public class MovieDetail implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    public MovieDetail(){

    }

    private MovieDetail(Parcel in){
        id = in.readString();
        backdrop_path = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        homepage = in.readString();
        original_title = in.readString();
        vote_average = in.readString();
        release_date = in.readString();
        runtime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(backdrop_path);
        parcel.writeString(overview);
        parcel.writeString(poster_path);
        parcel.writeString(homepage);
        parcel.writeString(original_title);
        parcel.writeString(vote_average);
        parcel.writeString(release_date);
        parcel.writeString(runtime);
    }

    public static final Parcelable.Creator<MovieDetail> CREATOR = new Parcelable.ClassLoaderCreator<MovieDetail>() {

        @Override
        public MovieDetail createFromParcel(Parcel source) {
            return new MovieDetail(source);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }

        @Override
        public MovieDetail createFromParcel(Parcel source, ClassLoader loader) {
            return null;
        }
    };
}


