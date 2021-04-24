package edu.neu.madcourseworkupteam.workup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {

    private ArrayList<ExerciseCard> exerciseList;
    private ItemClickListener listener;

    public ExerciseAdapter(ArrayList<ExerciseCard> cards) { this.exerciseList = cards; }

    public void setOnClickItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_card, parent,
                false);
        return new ExerciseViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        ExerciseCard currentCard = exerciseList.get(position);
        holder.videoName.setText(currentCard.getName());
        holder.videoDesc.setText(currentCard.getDesc());
        holder.videoURL = currentCard.getVideoUrl();

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }
}
