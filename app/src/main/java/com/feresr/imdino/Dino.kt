package com.feresr.imdino

import java.util.*

/**
 * Created by feresr on 23/10/2016.
 */

class Dino {
    var thinkValue = 0.0f
    var genome: FloatArray = FloatArray(12)
    var distanceRan = 0

    private val brain: Brain = Brain()

    constructor(genome: FloatArray) {
        this.genome = genome
        createBrain()
    }

    internal constructor() : super() {
        val random = Random()
        for (i in genome.indices) {
            genome[i] = getRandomSynapses(random)
        }
        createBrain()
    }

    private fun getRandomSynapses(random: Random): Float {
        return random.nextFloat()
    }

    private fun createBrain() {
        val r1 = NeuronRow()
        r1.addNeuron(Neuron(floatArrayOf(genome[0], genome[1])))
        r1.addNeuron(Neuron(floatArrayOf(genome[2], genome[3])))
        r1.addNeuron(Neuron(floatArrayOf(genome[4], genome[5])))
        r1.addNeuron(Neuron(floatArrayOf(genome[6], genome[7])))

        val r2 = NeuronRow()
        r2.addNeuron(Neuron(floatArrayOf(genome[8], genome[9], genome[10], genome[11])))

        brain.addNeuronRow(r1)
        brain.addNeuronRow(r2)
    }

    fun think(input: FloatArray): Boolean {
        thinkValue = brain.shouldJump(input)
        return thinkValue > .5
    }

    fun reproduce(): Dino {
        val random = Random()
        val childGenome = genome.clone()
        if (random.nextFloat() > .50) {
            for (i in genome.indices) {
                if (random.nextFloat() < .8) {
                    //father like mutation
                    childGenome[i] = (genome[i] + random.nextGaussian()).toFloat()
                } else {
                    //completely random mutation
                    childGenome[i] = random.nextFloat()
                }
            }
        }

        return Dino(childGenome)
    }
}
