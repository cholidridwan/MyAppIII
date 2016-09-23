package com.edgeasia.tes04;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private int click = 0;
    private int btnId = 0;
    private RelativeLayout canvas;
    private int xpos;
    private int ypos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        canvas = (RelativeLayout)findViewById(R.id.canvas);
        canvas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        click++;
                        Handler handler = new Handler();
                        Runnable r = new Runnable() {

                            @Override
                            public void run() {
                                click = 0;
                            }
                        };

                        if (click == 1) {
                            //Single click
                            handler.postDelayed(r, 250);
                        } else if (click == 2) {
                            //Double click
                            click = 0;
                            xpos = (int) event.getRawX();
                            ypos = (int) event.getRawY();
                            createButton();
                            Log.i("x-y pos", String.valueOf(xpos) +","+String.valueOf(ypos));
                        }
                        break;
                }

                return true;
            }
        });
    }

    private void createButton(){
        btnId++;
        final Button btnTag = new Button(getApplicationContext());
        btnTag.setText("Button" + String.valueOf(btnId));
        btnTag.setId(btnId);
        btnTag.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("button number", String.valueOf(v.getId()));
            }
        });
        canvas.addView(btnTag, new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT)
        );


        btnTag.post(new Runnable() {
            @Override
            public void run() {
                btnTag.setX(xpos - btnTag.getWidth()/2);
                btnTag.setY(ypos - btnTag.getHeight() * 2);
                Log.i("x-y poss", String.valueOf(btnTag.getX()) +","+String.valueOf(btnTag.getY()));
            }
        });
    }
}
