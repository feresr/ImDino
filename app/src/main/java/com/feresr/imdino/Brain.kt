package com.feresr.imdino

import java.util.*

/**
 * Created by feresr on 23/10/2016.
 */

class Brain internal constructor() {

    private val neuronRows = ArrayList<NeuronRow>()

    internal fun addNeuronRow(neuronRow: NeuronRow) {
        neuronRows.add(neuronRow)
    }

    internal val numberOfSynapses: Int
        get() {
            val synapses = neuronRows.indices.sumBy { neuronRows[it].numberOfSynapses }

            return synapses
        }

    internal fun shouldJump(inputs: FloatArray): Float {
        var result = inputs

        for (i in neuronRows.indices) {
            result = neuronRows[i].process(result)
        }
        return result[0]
    }
}
