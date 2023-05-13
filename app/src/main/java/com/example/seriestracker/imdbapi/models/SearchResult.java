package com.example.seriestracker.imdbapi.models;

public class SearchResult
{
    private String id;
    private String image;
    private String title;
    private String description;
    private String genres;
    private String status;
    private int rating;

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenres() {
        return genres;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}