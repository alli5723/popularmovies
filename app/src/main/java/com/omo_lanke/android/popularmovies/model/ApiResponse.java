package com.omo_lanke.android.popularmovies.model;

import java.util.List;

/**
 * Created by omo_lanke on 14/04/2017.
 */

public class ApiResponse {
    String page;
    List<MovieItem> results;
    int total_results;
    int total_pages;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<MovieItem> getResults() {
        return results;
    }

    public void setResults(List<MovieItem> results) {
        this.results = results;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }
}
//}
//{
//        "page": 1,
//        "results": [
//        {
//        "poster_path": "/67NXPYvK92oQgEQvLppF2Siol9q.jpg",
//        "adult": false,
//        "overview": "A story about how a new baby's arrival impacts a family, told from the point of view of a delightfully unreliable narrator, a wildly imaginative 7 year old named Tim.",
//        "release_date": "2017-03-23",
//        "genre_ids": [
//        16,
//        35,
//        10751
//        ],
//        "id": 295693,
//        "original_title": "The Boss Baby",
//        "original_language": "en",
//        "title": "The Boss Baby",
//        "backdrop_path": "/bTFeSwh07oX99ofpDI4O2WkiFJ.jpg",
//        "popularity": 295.059691,
//        "vote_count": 384,
//        "video": false,
//        "vote_average": 5.8
//        },