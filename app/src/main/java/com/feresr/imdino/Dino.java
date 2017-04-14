package com.feresr.imdino;

import java.util.Random;

/**
 * Created by feresr on 23/10/2016.
 */

public class Dino {
    private final static float MUTATION_RATE = .75f;
    private float thinkValue = 0.0f;
    private float[] genome;
    private int distanceRan = 0;
    private Brain brain;

    public Dino(float[] genome) {
        Random random = new Random();

        //should mutate some genes or duplicate exactly?
        if (random.nextFloat() <= MUTATION_RATE) {
            //mutate at most half of parents genes
            int howManyGenes = random.nextInt(genome.length / 2) + 1;
            for (int i = 0; i < howManyGenes; i++) { //alter genomes randomly
                genome[random.nextInt(genome.length)] = getRandomSynapses(random);
            }
        }
        this.genome = genome;
        createBrain();
    }

    Dino() {
        super();
        brain = new Brain();

        Random random = new Random();
        genome = new float[9];
        for (int i = 0; i < genome.length; i++) {
            genome[i] = getRandomSynapses(random);
        }
        createBrain();
    }

    public float[] getGenome() {
        return genome;
    }

    private float getRandomSynapses(Random random) {
        return random.nextFloat() * 2 - 1;
    }

    private void createBrain() {
        NeuronRow r1 = new NeuronRow();
        r1.addNeuron(new Neuron(new float[]{genome[0], genome[1]}));
        r1.addNeuron(new Neuron(new float[]{genome[2], genome[3]}));
        r1.addNeuron(new Neuron(new float[]{genome[4], genome[5]}));

        NeuronRow r2 = new NeuronRow();
        r2.addNeuron(new Neuron(new float[]{genome[6], genome[7], genome[8]}));

        brain = new Brain();
        brain.addNeuronRow(r1);
        brain.addNeuronRow(r2);
    }

    public boolean think(float[] input) {
        thinkValue = brain.shouldJump(input);
        return thinkValue > .5;
    }

    public float getThinkValue() {
        return thinkValue;
    }

    public int getDistanceRan() {
        return distanceRan;
    }

    public void setDistanceRan(int distanceRan) {
        this.distanceRan = distanceRan;
    }

    public Dino reproduce() {
        return new Dino(genome.clone());
    }
}
