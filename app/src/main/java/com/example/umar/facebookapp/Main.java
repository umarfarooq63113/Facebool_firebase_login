package com.example.umar.facebookapp;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main extends AppCompatActivity {
    private TextView tvEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tvEmail = (TextView) findViewById(R.id.tvEmailProfile);
        tvEmail.setText(getIntent().getExtras().getString("Email"));
    }
}
