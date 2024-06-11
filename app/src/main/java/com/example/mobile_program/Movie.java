package com.example.mobile_program;
public class Movie {
    private String title;
    private String rating;
    private String genre;
    private String releaseYear;
    private int imageResourceId;
    private String points;

    public Movie(String title, String rating, String genre, String releaseYear, int imageResourceId) {
        this.title = title;
        this.rating = rating;
        this.genre = genre;
        this.releaseYear = releaseYear;
//        this.imageResourceId = imageResourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getRating() {
        return rating;
    }

    public String getGenre() {
        return genre;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getPoints() {
        return points;
    }
}

