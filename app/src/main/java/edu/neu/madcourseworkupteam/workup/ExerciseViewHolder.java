package edu.neu.madcourseworkupteam.workup;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {

    public ImageView video;
    public TextView videoName;
    public TextView videoDesc;
    public CheckBox checkBox;

    public ExerciseViewHolder(@NonNull View itemView, final ItemClickListener listener) {
        super(itemView);
        video = itemView.findViewById(R.id.video_image);
        videoName = itemView.findViewById(R.id.video_title);
        videoDesc = itemView.findViewById(R.id.video_desc);
        this.checkBox = itemView.findViewById(R.id.checkbox);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    listener.onItemClick(position);
                }
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCheckBoxClick(position);
                    }
                }
            }
        });
    }
}
