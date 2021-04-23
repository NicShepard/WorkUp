package edu.neu.madcourseworkupteam.workup;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;

/**
 * This class displays and individual activity including the title, video, and description. It also
 * has a step counter that users can use to track their steps while performing the activity.
 */
public class VideoCard extends AppCompatActivity implements SensorEventListener {

    // Keep track of steps to display to user, and the first value of the step counter which only
    // resets to zero after reboot so that we can display the number of steps taken while doing the
    // activity.
    int steps;
    int initialSteps;

    FirebaseUser user;
    SensorManager sensorManager;
    Sensor stepCounter;
    TextView stepDisplay;
    Button startCounter;
    Button addSteps;
    Boolean active;
    VideoCard videoCard;
    LocalDate ld;

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



        //Step counter active flag starts as inactive
        active = false;
        //Set the initial steps to -1 so that we can zero out the counter every time
        initialSteps = -1;
        //Assign the class to a variable so that it can be passed into the on click listener
        videoCard = this;
        //Start steps at 0 during load
        steps = 0;

        //Get views and hardware
        stepDisplay = findViewById(R.id.step_display);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        startCounter = findViewById(R.id.startPedometer);

        //Toggle the step counter on and off and update the UI to reflect the status
        startCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (active) {
                    startCounter.setText("Start Pedometer");
                    sensorManager.unregisterListener(videoCard, stepCounter);
                    active = false;
                } else {
                    startCounter.setText("Stop Pedometer");
                    if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
                        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                        sensorManager.registerListener(videoCard, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
                    }
                    active = true;
                }
            }
        });

        //Allow user to submit the steps to the database
        addSteps = findViewById(R.id.addSteps);
        addSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long stepsToSubmit = new Long(steps);
                updateStepsForDay(stepsToSubmit);
            }
        });

        WebSettings settings = video.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setBuiltInZoomControls(true);
        video.setWebViewClient(new VideoCard.Callback());
        video.loadUrl(videoURL);
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
    }


    //Reregister the listener
    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    //Unregister the listener
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, stepCounter);
    }

    //Calculate the original number of steps, and update the UI every time a new step is taken
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (initialSteps == -1) {
            initialSteps = (int) event.values[0];
        }

        if (event.sensor == stepCounter) {
            steps = (int) event.values[0];
            steps = steps - initialSteps;
            stepDisplay.setText(String.valueOf(steps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onPointerCaptureChanged(boolean havideoCardapture) {
    }

    /**
     * Update the steps for a given day
     * @param stepsToAdd the number of steps we'd like to add to the day
     */
    void updateStepsForDay(Long stepsToAdd) {

        //Reset the display and rezero the step count
        initialSteps = steps;
        steps = 0;
        stepDisplay.setText(String.valueOf(steps));

        //Set a boolean flag so the function only runs once
        final Boolean[] alreadyRun = {false};
        final Long[] existingSteps = new Long[1];

        //Get the user for their UID
        user = FirebaseAuth.getInstance().getCurrentUser();
        //Get the date to use as a key for the steps node
        ld = LocalDate.now();

        //Set the listener at the appropriate location
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(user.getUid()).child("dailySteps").child(ld.toString());
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Get Steps", "called");
                //Protect against referencing a null object and infinite loop
                if (dataSnapshot != null && !alreadyRun[0]) {
                    existingSteps[0] = (Long) dataSnapshot.getValue();
                    databaseReference.setValue(stepsToAdd + existingSteps[0]);
                    alreadyRun[0] = true;
                    //Create the entry if it doesn't exist already
                } else if (!alreadyRun[0]){
                    databaseReference.setValue(stepsToAdd);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Get Movement", "loadPost:onCancelled", databaseError.toException());
            }
        };

        databaseReference.addValueEventListener(userListener);
    }

    void getStepsForDay() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        ld = LocalDate.now();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Get Steps", "called");

                if (dataSnapshot != null) {
                    Log.d("Get Steps key", String.valueOf(dataSnapshot.getValue()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Get Movement", "loadPost:onCancelled", databaseError.toException());
            }
        };
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(user.getUid()).child("dailySteps").child(ld.toString());
        databaseReference.addValueEventListener(userListener);
    }

    void setStepsForDay() {
        ld = LocalDate.now();
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(user.getUid()).child("dailySteps").child(ld.toString());
        databaseReference.setValue("0");
    }
}