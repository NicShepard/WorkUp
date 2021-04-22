package edu.neu.madcourseworkupteam.workup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class VideoActivity extends YouTubeBaseActivity {

    YouTubePlayerView playerView;
    YouTubePlayer.OnInitializedListener videoListener;
    String videoURL = "W4hTJybfU7s";
    TextView videoTitle;
    TextView videoDesc;
    Button buttonPlay;
    private static final String TAG = "VideoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_video);
        Log.d(TAG, "onCreate: Starting.");
        playerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerView);
        videoListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onClick: Done initializing.");
                youTubePlayer.loadVideo(videoURL);

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onClick: Failed to initialize.");

            }
        };
        videoTitle = findViewById(R.id.video_title);
        videoDesc = findViewById(R.id.video_desc);
        buttonPlay = findViewById(R.id.buttonPlay);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                videoURL = null;
                videoTitle = null;
            } else {
                videoURL = extras.getString("videoURL");
                String title = extras.getString("videoTitle");
                String desc = extras.getString("videoDesc");
                videoTitle.setText(title);
                videoDesc.setText(desc);
            }
        } else {
            videoURL = (String) savedInstanceState.getSerializable("videoURL");
        }

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Initializing YouTube Player.");
                playerView.initialize(YouTubeConfig.getApiKey(), videoListener);
            }
        });

    }

}