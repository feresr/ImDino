package com.feresr.imdino;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by feresr on 23/10/2016.
 */

public class Neuron {

    private float[] weights;

    public Neuron(float[] weights) {
        super();
        this.weights = weights;
    }

    public float process(float[] inputs) {
        if (inputs.length != weights.length) {
            Log.e("neuron", Arrays.toString(weights) + " " + Arrays.toString(inputs));
            throw new IllegalArgumentException("inputs dont match weights");
        }
        float v = 0f;
        for (int i = 0; i < inputs.length; i++) {
            v += weights[i] * inputs[i];
        }

        return (float) (1f / (1f + (Math.exp(-1.0 * v))));
    }

}
