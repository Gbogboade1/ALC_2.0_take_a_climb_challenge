package com.ayomide.gbogboade.tic_tac_toe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author GBOGBOADE AYOMIDE MICHEAL
 * ay4allcrown@gmail.com
 * ALC 2.0
 * ANDROID BEGINNER: TAKE A CLIMB CHALLENGE
 */
public class HowToPlayActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
    }

    public  void  play_(View v){
        Intent intent = new Intent(HowToPlayActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
