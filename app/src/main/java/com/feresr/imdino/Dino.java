package com.feresr.imdino;

import java.util.Random;

/**
 * Created by feresr on 23/10/2016.
 */

public class Dino {
    private Brain brain;
    private float thinkValue = 0.0f;
    private int distanceRan = 0;
    private float[] genome;

        public Dino(float[] genome, int generation) {
        Random random = new Random();

        //tweak child genome to differ from parent a bit
        //for (int i = 0; i < genome.length; i++) {
        //    genome[i] *= (1 + (random.nextFloat() * 2 - 1));
        //}

        //mutations?
        if (random.nextInt(10) >= 3) {
            int howMany = random.nextInt(5) + 1;
            for (int i = 0; i < howMany; i++) { //alter one or two or genomes
                genome[random.nextInt(genome.length)] = random.nextFloat() * 2 - 1;
            }
        }
        this.genome = genome;

        createBrain();
    }

    public float[] getGenome() {
        return genome;
    }

    Dino() {
        super();
        brain = new Brain();

        Random random = new Random();
        genome = new float[9];
        for (int i = 0; i < genome.length; i++) {
            genome[i] = random.nextFloat() * 2 - 1;
        }
        createBrain();
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

    public float getThinkValue(){
        return thinkValue;
    }
    public int getDistanceRan() {
        return distanceRan;
    }

    public void setDistanceRan(int distanceRan) {
        this.distanceRan = distanceRan;
    }

    public Dino reproduce(int generation) {
        return new Dino(genome.clone(), generation);
    }
}
