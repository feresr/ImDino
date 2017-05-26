package com.feresr.imdino

import android.util.Log
import java.util.*

/**
 * Created by feresr on 23/10/2016.
 */

class Neuron(private val weights: FloatArray) {

    val numberOfSynapses: Int
        get() = weights.size

    fun process(inputs: FloatArray): Float {
        if (inputs.size != weights.size) {
            Log.e("neuron", Arrays.toString(weights) + " " + Arrays.toString(inputs))
            throw IllegalArgumentException("inputs don't match weights")
        }

        val v = inputs.indices
                .map { weights[it] * inputs[it] }
                .sum()

        //apply sigmoid fucntion
        return sigmoid(v)
    }

    private fun sigmoid(v: Float): Float {
        return (1.0f / (1.0f + Math.exp(-1.0 * v))).toFloat()
    }
}
