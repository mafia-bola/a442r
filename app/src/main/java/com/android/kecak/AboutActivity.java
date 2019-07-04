package com.android.kecak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Intent back = new Intent(AboutActivity.this, MenuActivity.class);
        startActivity(back);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
