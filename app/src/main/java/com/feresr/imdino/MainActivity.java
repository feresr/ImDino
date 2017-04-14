package com.feresr.imdino;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final int DINO_COUNT = 5;
    private TextView speedTextView;
    private TextView distanceTextView;
    private TextView resultTextView;
    private TextView generationTextView;
    private WebView webView;
    private KeyEvent keyEvent;
    private int maxScoreInGeneration = 0;
    private int minScoreInGeneration = Integer.MAX_VALUE;
    private int generation = 0;
    private int currentDino = 0;
    private Dino bestDino;
    private Handler handler;
    private LinearLayout dinoPlaceholderView;

    private ArrayList<Dino> dinosaurs;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "Android");
        webView.loadUrl("file:///android_asset/index.html");

        speedTextView = (TextView) findViewById(R.id.speed);
        distanceTextView = (TextView) findViewById(R.id.distance);
        resultTextView = (TextView) findViewById(R.id.result);
        generationTextView = (TextView) findViewById(R.id.generation);

        keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE);

        handler = new Handler();

        dinosaurs = new ArrayList<>();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        dinoPlaceholderView = (LinearLayout) findViewById(R.id.dino_placeholder);
        for (int i = 0; i < DINO_COUNT; i++) {
            dinosaurs.add(new Dino());
            layoutInflater.inflate(R.layout.dino_image, dinoPlaceholderView);
        }
    }

    @JavascriptInterface
    public void onGameUpdate(final float obstacleX, final float currentSpeed) {
        if (dinosaurs.get(currentDino).think(new float[]{obstacleX, currentSpeed})) {
            webView.dispatchKeyEvent(keyEvent);
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                speedTextView.setText(String.valueOf(currentSpeed));
                distanceTextView.setText(String.valueOf(obstacleX));
                resultTextView.setText(String.valueOf(dinosaurs.get(currentDino).getThinkValue()));
            }
        });
    }

    @JavascriptInterface
    public void onGameStart() {
        Log.i("onGameStart", "Running dino: " + currentDino + " in generation " + generation + " " + Arrays.toString(dinosaurs.get(currentDino).getGenome()));
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (currentDino == 0) {
                    dinoPlaceholderView.getChildAt(DINO_COUNT - 1).setAlpha(0.25f);
                } else {
                    dinoPlaceholderView.getChildAt(currentDino - 1).setAlpha(0.25f);
                }

                dinoPlaceholderView.getChildAt(currentDino).setAlpha(1.0f);
                generationTextView.setText("Generation: " + String.valueOf(generation) + " • Max score in gen: " + maxScoreInGeneration);
            }
        });
    }

    @JavascriptInterface
    public void onGameOver(int score) {
        Log.i("onGameOver", "Running dino: " + currentDino + " score: " + score);

        dinosaurs.get(currentDino).setDistanceRan(score);
        if (score > maxScoreInGeneration) {
            maxScoreInGeneration = score;
            bestDino = dinosaurs.get(currentDino);
        }

        if (score < minScoreInGeneration) {
            minScoreInGeneration = score;
        }

        if (currentDino < dinosaurs.size() - 1) {
            currentDino++;
            reloadGame();
        } else {
            toNextGeneration();
        }

    }

    private void reloadGame() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_SPACE));
            }
        }, 1000);
    }


    private void toNextGeneration() {
        Log.i("toNextGeneration", "GENERATION " + generation + "FINISHED best dino: " + bestDino.getDistanceRan());

        Random r = new Random();
        ArrayList<Dino> survivers = new ArrayList<>();
        for (int i = 0; i < DINO_COUNT; i++) {
            if (r.nextInt(maxScoreInGeneration - minScoreInGeneration) < dinosaurs.get(i).getDistanceRan() - minScoreInGeneration) {
                survivers.add(dinosaurs.get(i));
            } else {
                Log.i("Dino", "dino killed: " + dinosaurs.get(i).getDistanceRan());
            }
        }

        for (int i = survivers.size(); i < DINO_COUNT; i++) {
            survivers.add(survivers.get(r.nextInt(survivers.size())).reproduce());
        }

        generation++;
        currentDino = 0;
        maxScoreInGeneration = 0;
        minScoreInGeneration = Integer.MAX_VALUE;
        this.dinosaurs = survivers;
        reloadGame();
    }
}
