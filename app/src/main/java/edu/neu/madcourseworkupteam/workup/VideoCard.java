package edu.neu.madcourseworkupteam.workup;

import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * This class displays and individual activity including the title, video, and devideoCardription. It also
 * has a step counter that users can use to track their steps while performing the activity.
 */
public class VideoCard extends AppCompatActivity implements SensorEventListener {

    // Keep track of steps to display to user, and the first value of the step counter which only
    // resets to zero after reboot so that we can display the number of steps taken while doing the
    // activity.
    int steps;
    int initialSteps;

    SensorManager sensorManager;
    Sensor stepCounter;
    TextView stepDisplay;
    Button startCounter;
    Boolean active;
    VideoCard videoCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_card);

        //Step counter active flag starts as inactive
        active = false;
        //Set the initial steps to -1 so that we can zero out the counter every time
        initialSteps =  -1;
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

                if(active){
                    startCounter.setText("Start Pedometer");
                    sensorManager.unregisterListener(videoCard, stepCounter);
                    active = false;
                } else {
                    startCounter.setText("Stop Pedometer");
                    if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
                        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                        sensorManager.registerListener(videoCard, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
                    }
                    active = true;
                }
            }
        });
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

    //Calculate the original number of steps, and update the UI everytime a new step is taken
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(initialSteps == -1){
            initialSteps = (int) event.values[0];
        }

        if(event.sensor == stepCounter){
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
}