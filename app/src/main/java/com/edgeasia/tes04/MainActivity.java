package com.edgeasia.tes04;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private int click = 0;
    private int btnId = 0;
    private RelativeLayout canvas;
    private EditText txtDestination;
    private Button btnSave;
    private Button btnLoad;
    private Button btnClear;
    private Button btnTag;
    private int xpos;
    private int ypos;
    private JSONObject parentJson = new JSONObject();
    private JSONObject childJson;// = new JSONObject();

    private ScaleGestureDetector SGD;
    private float scale = 1f;
    private int width = 264;
    private int height = 144;
    private boolean active = false;
    private View btnActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SGD = new ScaleGestureDetector(this,new ScaleListener());

        btnSave = (Button)findViewById(R.id.btnSave);
        btnLoad = (Button)findViewById(R.id.btnLoad);
        btnClear = (Button)findViewById(R.id.btnClear);
        txtDestination = (EditText)findViewById(R.id.txtDestination);
        canvas = (RelativeLayout)findViewById(R.id.canvas);
        canvas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SGD.onTouchEvent(event);
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
                        } else if (click == 2 && !txtDestination.getText().toString().matches("")) {
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

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 5.0f));

            if(active) {
                btnActive.setLayoutParams(new RelativeLayout.LayoutParams((int) (width * scale), (int) (height * scale)));
                //Log.i("scale", String.valueOf(btnTag.getWidth()) + ", " + String.valueOf(btnTag.getHeight()));
            }
            return true;
        }
    }

    private void createButton(){
        btnId = Integer.parseInt(txtDestination.getText().toString());
        txtDestination.setText("");
        //final Button btnTag = new Button(getApplicationContext());
        btnTag = new Button(getApplicationContext());
        btnTag.setText("Button " + btnId);
        btnTag.setId(btnId);
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

                try {
                    editJson(String.valueOf(btnTag.getText()), btnId, btnTag.getX(), btnTag.getY(), btnTag.getWidth(), btnTag.getHeight());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.removeAllViews();
            }
        });

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnTag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        try {
                            editJson(String.valueOf(btnTag.getText()), btnId, v.getX(), v.getY(), v.getWidth(), v.getHeight());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        click++;
                        Log.i("button number", String.valueOf(v.getId()));
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
                            if(!active){
                                btnActive = v;
                                btnActive.setBackgroundColor(Color.GRAY);
                                active = true;
                            }else {
                                btnActive = v;
                                btnActive.setBackgroundResource(android.R.drawable.btn_default);
                                active = false;
                            }
                        }

                        break;
                }
                if(!active){
                    xpos = (int) event.getRawX();
                    ypos = (int) event.getRawY();
                    v.setX(xpos - btnTag.getWidth()/2);
                    v.setY(ypos - btnTag.getHeight()*2);
                }
                return false;
            }
        });
    }

    private void editJson(String btnName, int destination, float posx, float posy, int btnWidth, int btnHeight) throws JSONException {
        try {
            childJson = new JSONObject();
            childJson.put("name", btnName);
            childJson.put("destination", destination);
            childJson.put("posx", posx);
            childJson.put("posy", posy);
            childJson.put("width", btnWidth);
            childJson.put("height", btnHeight);

            parentJson.put(String.valueOf(btnName), childJson);
            String jsonStr = parentJson.toString();
            System.out.println("jsonString: "+ jsonStr);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
