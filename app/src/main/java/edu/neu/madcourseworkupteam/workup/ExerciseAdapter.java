package edu.neu.madcourseworkupteam.workup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {

    private final ArrayList<ExerciseCard> exerciseList;

    public ExerciseAdapter(ArrayList<ExerciseCard> cards) { this.exerciseList = cards; }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_card, parent,
                false);
        return new ExerciseViewHolder(view);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        ExerciseCard currentCard = exerciseList.get(position);
        holder.video.setImageResource(currentCard.getImageSource());

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }
}
