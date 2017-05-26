package com.feresr.imdino

import java.util.ArrayList

/**
 * Created by feresr on 23/10/2016.
 */

class NeuronRow {

    private val neurons = ArrayList<Neuron>()

    fun addNeuron(neuron: Neuron) {
        neurons.add(neuron)
    }

    fun process(inputs: FloatArray): FloatArray {
        val result = FloatArray(neurons.size)
        for (i in neurons.indices) {
            result[i] = neurons[i].process(inputs)
        }

        return result
    }

    val numberOfSynapses: Int
        get() = neurons[0].numberOfSynapses * neurons.size
}
