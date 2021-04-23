package edu.neu.madcourseworkupteam.workup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class VideoActivity extends AppCompatActivity {

    public WebView video;
    String videoURL;
    TextView videoTitle;
    TextView videoDesc;
    private static final String TAG = "VideoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_video);
        Log.d(TAG, "onCreate: Starting.");
        video = (WebView) findViewById(R.id.video_view);
        videoTitle = findViewById(R.id.video_title);
        videoDesc = findViewById(R.id.video_desc);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
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

        WebSettings settings = video.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setBuiltInZoomControls(true);
        video.setWebViewClient(new Callback());
        video.loadUrl(videoURL);

    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
    }

}