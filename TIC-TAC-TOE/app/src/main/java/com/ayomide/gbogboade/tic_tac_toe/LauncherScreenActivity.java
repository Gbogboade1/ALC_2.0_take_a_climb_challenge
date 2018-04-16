package com.ayomide.gbogboade.tic_tac_toe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author GBOGBOADE AYOMIDE MICHEAL
 * ay4allcrown@gmail.com
 * ALC 2.0
 * ANDROID BEGINNER: TAKE A CLIMB CHALLENGE
 */
public class LauncherScreenActivity extends AppCompatActivity {
    Toast exitToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (exitToast == null) {
            exitToast = Toast.makeText(this,"Touch again to exit.", Toast.LENGTH_SHORT);
            exitToast.show();
        }else {
            exitToast.cancel();
        }
    }

    /**
     * ONCLICK METHOD FOR PLAY BUTTON IN activity_launcher
     * @param v
     */
    public void play(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * ONCLICK METHOD FOR ABOUT BUTTON IN activity_launcher
     * @param v
     */
    public void about(View v) {
        Intent intent = new Intent(LauncherScreenActivity.this, HowToPlayActivity.class);
        startActivity(intent);
    }

    /**
     * ONCLICK METHOD FOR EXIT BUTTON IN activity_launcher
     * @param v
     */
    public void exit(View v) {
        confirmExitDialog();
    }

    private void confirmExitDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.game_pause_dialog_layout, null);
        ((TextView) dialogLayout.findViewById(R.id.pause_textView)).setText("Confirm exit?");
        dialog.setView(dialogLayout);
        dialog.setNegativeButton("NO", null);
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });

        dialog.create();
        dialog.show();
    }
}
