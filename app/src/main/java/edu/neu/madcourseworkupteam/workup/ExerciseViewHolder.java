package edu.neu.madcourseworkupteam.workup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {

    //public ImageView video;
    public WebView video;
    public String videoURL;
    public TextView videoName;
    public TextView videoDesc;
    public CheckBox checkBox;

    public ExerciseViewHolder(@NonNull View itemView, final ItemClickListener listener) {
        super(itemView);
        video = itemView.findViewById(R.id.video_view);
        videoName = itemView.findViewById(R.id.video_title);
        videoDesc = itemView.findViewById(R.id.video_desc);
        this.checkBox = itemView.findViewById(R.id.checkbox);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    listener.onItemClick(position);

                    video.getSettings().setJavaScriptEnabled(true);
                    video.setWebChromeClient(new WebChromeClient());
                    video.loadUrl(videoURL);


                    //Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURL));
                    //itemView.getContext().startActivity(webintent);
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
