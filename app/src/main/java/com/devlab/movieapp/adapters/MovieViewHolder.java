package com.devlab.movieapp.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devlab.movieapp.R;
import com.devlab.movieapp.interfaces.OnMovieListener;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView title,category,duration;
    ImageView imageView;
    RatingBar ratingBar;
    OnMovieListener onMovieListener;

    public MovieViewHolder(@NonNull View itemView,OnMovieListener onMovieListener) {
        super(itemView);
        this.onMovieListener=onMovieListener;
        title=itemView.findViewById(R.id.movie_title);
        category=itemView.findViewById(R.id.movie_category);
        duration=itemView.findViewById(R.id.movie_duration);
        imageView=itemView.findViewById(R.id.movie_img);
        ratingBar=itemView.findViewById(R.id.rating_bar);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
onMovieListener.onMovieClick(getAdapterPosition());
    }
}
