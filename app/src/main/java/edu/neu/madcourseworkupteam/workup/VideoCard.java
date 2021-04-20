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

public class VideoCard extends AppCompatActivity implements SensorEventListener {

    int steps;
    SensorManager sensorManager;
    Sensor stepCounter;
    TextView stepDisplay;
    Button startCounter;
    Boolean active;
    VideoCard sc = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_card);
        active = false;

        steps = 0;
        stepDisplay = findViewById(R.id.step_display);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        startCounter = findViewById(R.id.startPedometer);

        startCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(active){
                    startCounter.setText("Start Pedometer");
                    sensorManager.unregisterListener(sc, stepCounter);
                    active = false;
                } else {
                    startCounter.setText("Stop Pedometer");
                    if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
                        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                        sensorManager.registerListener(sc, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
                    }
                    active = true;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, stepCounter);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor == stepCounter){
            steps = (int) event.values[0];
            stepDisplay.setText(String.valueOf(steps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}