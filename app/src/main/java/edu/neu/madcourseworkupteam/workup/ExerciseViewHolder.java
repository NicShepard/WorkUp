package edu.neu.madcourseworkupteam.workup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {

    public ImageView video;
    //public WebView video;
    public String videoURL;
    public TextView videoName;
    public TextView videoDesc;
    public CheckBox checkBox;

    private static final String ACTIVE_FRAGMENT = "ACTIVE_FRAGMENT";
    private Fragment activeFragment;

    public ExerciseViewHolder(@NonNull View itemView, final ItemClickListener listener) {
        super(itemView);
//        this.video = itemView.findViewById(R.id.img_view);
        this.videoName = itemView.findViewById(R.id.video_title);
        this.videoDesc = itemView.findViewById(R.id.video_desc);
        this.checkBox = itemView.findViewById(R.id.checkbox);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    listener.onItemClick(position);

                    Intent intent = new Intent(itemView.getContext(), VideoCard.class);
                    intent.putExtra("videoURL", videoURL);
                    intent.putExtra("videoTitle", videoName.getText().toString());
                    intent.putExtra("videoDesc", videoDesc.getText().toString());

                    itemView.getContext().startActivity(intent);

                    //Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURL));
                    //itemView.getContext().startActivity(webintent);
                }
            }
        });


        // TODO: add the video to the db of favorites for the user
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
