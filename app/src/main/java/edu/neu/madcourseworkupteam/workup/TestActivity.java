package edu.neu.madcourseworkupteam.workup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private TextView mTextView;
    private Button mButton;
    private DataService ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mTextView = (TextView) findViewById(R.id.text);
        mButton = findViewById(R.id.test);
        ds = new DataService();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List list = ds.getMovements();
                Log.d("Size", "The size is");
                Log.d("Size", list.toString());
            }
        });
    }
}