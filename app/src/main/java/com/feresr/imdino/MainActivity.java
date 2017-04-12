package com.feresr.imdino;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView speedTextView;
    private TextView distanceTextView;
    private TextView resultTextView;
    private WebView webView;
    private KeyEvent keyEvent;
    private int maxScoreInGeneration = 0;
    private int minScoreInGeneration = Integer.MAX_VALUE;
    private int generation = 0;
    private int currentDino = 0;
    private Dino bestDino;
    public static final int DINO_COUNT = 5;
    private Handler handler;

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

        keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE);

        handler = new Handler();

        dinosaurs = new ArrayList<>();
        for (int i = 0; i < DINO_COUNT; i++) {
            dinosaurs.add(new Dino());
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
        Log.e("info", "Running dino: " + currentDino + " in generation " + generation + " " + Arrays.toString(dinosaurs.get(currentDino).getGenome()));
        handler.post(new Runnable() {
            @Override
            public void run() {
                /*generationTextView.setText(String.valueOf(generation));
                dinoTextView.setText(String.valueOf(currentDino));
                genomeTextView.setText(Arrays.toString(dinosaurs.get(currentDino).getGenome()));*/
            }
        });
    }

    @JavascriptInterface
    public void onGameOver(int score) {
        Log.e("info", "Running dino: " + currentDino + " score: " + score);

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
            currentDino = 0;
            killDinos();
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


    private Dino saveDino(ArrayList<Dino> dinosaurs) {
        int scoreSum = 0;
        for (Dino dino : dinosaurs) {
            scoreSum += dino.getDistanceRan();
        }

        Random random = new Random();
        int kill = random.nextInt(scoreSum);
        int score = 0;
        for (Dino dino : dinosaurs) {
            score += dino.getDistanceRan();
            if (kill < score) {
                Log.e("info", "A dino was saved SCORE: " + dino.getDistanceRan() + " GENOME: " + Arrays.toString(dino.getGenome()));
                return dino;
            }
        }

        return null;
    }

    private void killDinos() {

        Log.e("info", "GENERATION " + generation + "FINISHED best dino: " + bestDino.getDistanceRan());

        Random r = new Random();
        ArrayList<Dino> survivers = new ArrayList<>();
        for (int i = 0; i < DINO_COUNT; i++) {
            if (r.nextInt(maxScoreInGeneration - minScoreInGeneration) < dinosaurs.get(i).getDistanceRan() - minScoreInGeneration) {
                survivers.add(dinosaurs.get(i));
            } else {
                Log.e("info", "dino killed: " + dinosaurs.get(i).getDistanceRan());
            }
        }

        for (int i = survivers.size(); i < DINO_COUNT; i++) {
            survivers.add(bestDino.reproduce(generation));
        }

        generation++;
        maxScoreInGeneration = 0;
        minScoreInGeneration = Integer.MAX_VALUE;
        this.dinosaurs = survivers;
        reloadGame();
    }
}
