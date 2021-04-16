package edu.neu.madcourseworkupteam.workup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
                ds.getMovement("MYAvoJ9o4Rkv1jE__wk");
            }
        });
    }
}