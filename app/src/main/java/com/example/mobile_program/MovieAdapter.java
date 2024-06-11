package com.example.mobile_program;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {
    private Context context;
    private List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
        this.context = context;
        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem, parent, false);
        }

        Movie movie = movies.get(position);

        TextView titleTextView = convertView.findViewById(R.id.title);
        TextView ratingTextView = convertView.findViewById(R.id.rating);
        TextView genreTextView = convertView.findViewById(R.id.genre);
        TextView releaseYearTextView = convertView.findViewById(R.id.releaseYear);
        ImageView imageView = convertView.findViewById(R.id.image);

        titleTextView.setText(movie.getTitle());
        ratingTextView.setText(movie.getRating());
        genreTextView.setText(movie.getGenre());
        releaseYearTextView.setText(movie.getReleaseYear());
        imageView.setImageResource(movie.getImageResourceId());

        return convertView;
    }
}

