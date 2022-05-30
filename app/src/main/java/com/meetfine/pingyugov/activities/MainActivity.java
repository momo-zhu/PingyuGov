package com.meetfine.pingyugov.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.meetfine.pingyugov.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                MainActivity.this.startActivity(i);
                MainActivity.this.finish();
            }
        }, 1500);

    }

}
