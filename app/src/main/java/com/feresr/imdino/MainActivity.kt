package com.feresr.imdino

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.webkit.JavascriptInterface
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val keyEvent: KeyEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE)
    private val handler: Handler = Handler()
    private var maxScoreInGeneration = 0
    private var generation = 0
    private var currentDino = 0
    private var bestDino: Dino? = null
    private var jumps:Int = 0
    private val JUMP_PENALTY:Int = 100
    private var dinosaurs: ArrayList<Dino> = ArrayList()

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webview.settings.javaScriptEnabled = true
        webview.addJavascriptInterface(this, "Android")
        webview.loadUrl("file:///android_asset/index.html")

        val layoutInflater = LayoutInflater.from(this)
        for (i in 0..DINO_COUNT - 1) {
            dinosaurs.add(Dino())
            layoutInflater.inflate(R.layout.dino_image, dino_placeholder)
        }
    }

    @JavascriptInterface
    fun onJump() {
        jumps++ // keep track of jumps to punish jumpers
    }

    @JavascriptInterface
    fun onGameUpdate(obstacleX: Float, currentSpeed: Float) {
        if (dinosaurs[currentDino].think(floatArrayOf(obstacleX, currentSpeed))) {
            webview.dispatchKeyEvent(keyEvent)
        }
        handler.post {
            speedTxtView.text = currentSpeed.toString()
            distanceTxtView.text = obstacleX.toString()
            if (currentDino > 0 && currentDino < dinosaurs.size) {
                resultTxtView.text = dinosaurs[currentDino].thinkValue.toString()
            }
        }
    }

    @JavascriptInterface
    fun onGameStart() {
        Log.i("onGameStart", "Running dino: " + currentDino + " in generation " + generation + " " + Arrays.toString(dinosaurs[currentDino].genome))
        handler.post {
            if (currentDino == 0) {
                dino_placeholder.getChildAt(DINO_COUNT - 1).alpha = 0.25f
            } else {
                dino_placeholder.getChildAt(currentDino - 1).alpha = 0.25f
            }

            dino_placeholder.getChildAt(currentDino).alpha = 1.0f
            generationTxtView.text = "Generation: $generation • Max score in gen: $maxScoreInGeneration"
        }
    }

    @JavascriptInterface
    fun onGameOver(score: Int) {
        Log.i("onGameOver", "Running dino: $currentDino score: $score")

        dinosaurs[currentDino].distanceRan = score - (jumps * JUMP_PENALTY)
        jumps = 0
        if (score > maxScoreInGeneration) {
            maxScoreInGeneration = score
            bestDino = dinosaurs[currentDino]
        }

        if (currentDino < dinosaurs.size - 1) {
            currentDino++
            reloadGame()
        } else {
            toNextGeneration()
        }
    }

    private fun reloadGame() {
        handler.postDelayed({ webview.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_SPACE)) }, 1000)
    }

    private fun toNextGeneration() {
        Log.i("toNextGeneration", "GENERATION " + generation + "FINISHED best dino: " + bestDino?.distanceRan)

        val r = Random()
        val survivors = ArrayList<Dino>()

        val w = DoubleArray(DINO_COUNT)

        var best = 0
        dinosaurs.asSequence()
                .filter { it.distanceRan > best }
                .forEach { best = it.distanceRan }

        var wmax = 0.0
        for (i in 0..DINO_COUNT - 1) {
            w[i] = Utils.pdf(dinosaurs[i].distanceRan.toDouble(), best.toDouble(), 2.0)
            if (w[i] > wmax) {
                wmax = w[i]
            }
        }

        var b = 0.0

        var index = r.nextInt(DINO_COUNT)
        for (i in 0..DINO_COUNT - 1) {
            b += r.nextDouble() * 2.0 * wmax

            while (w[index % w.size] <= b) {
                b -= w[index % w.size]
                index += 1
            }
            survivors.add(dinosaurs[index % dinosaurs.size])
        }

        this.dinosaurs.clear()

        for (i in 0..DINO_COUNT - 1) {
            dinosaurs.add(survivors[i].reproduce())
        }
        generation++
        currentDino = 0
        maxScoreInGeneration = 0
        reloadGame()
    }

    companion object {
        val DINO_COUNT = 6
    }
}
