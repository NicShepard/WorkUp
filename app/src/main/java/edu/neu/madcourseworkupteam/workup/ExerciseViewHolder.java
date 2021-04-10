package edu.neu.madcourseworkupteam.workup;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {

    public ImageView video;

    public ExerciseViewHolder(@NonNull View itemView) {
        super(itemView);
        video = itemView.findViewById(R.id.video_image);
    }
}
