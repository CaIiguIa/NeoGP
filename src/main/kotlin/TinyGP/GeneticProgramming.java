package TinyGP;/*
 * Program:   tiny_gp.java
 *
 * Author:    Riccardo Poli (email: rpoli@essex.ac.uk)
 *
 */

import java.util.*;
import java.io.*;

public class GeneticProgramming {

    void setupFitness(String fileName) {
        try {
            int i, j;
            String line;

            BufferedReader in = new BufferedReader(new FileReader(fileName));
            line = in.readLine();
            StringTokenizer tokens = new StringTokenizer(line);
            Properties.varnumber = Integer.parseInt(tokens.nextToken().trim());
            Properties.randomnumber = Integer.parseInt(tokens.nextToken().trim());
            Properties.minrandom = Double.parseDouble(tokens.nextToken().trim());
            Properties.maxrandom = Double.parseDouble(tokens.nextToken().trim());
            Properties.fitnesscases = Integer.parseInt(tokens.nextToken().trim());
            Properties.targets = new double[Properties.fitnesscases][Properties.varnumber + 1];

            if (Properties.isOperation(Properties.varnumber + Properties.randomnumber))
                System.out.println("too many variables and constants");

            for (i = 0; i < Properties.fitnesscases; i++) {
                line = in.readLine();
                tokens = new StringTokenizer(line);
                for (j = 0; j <= Properties.varnumber; j++) {
                    Properties.targets[i][j] = Double.parseDouble(tokens.nextToken().trim());
                }
            }

            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Please provide a data file");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("ERROR: Incorrect data format");
            System.exit(0);
        }
    }

    public GeneticProgramming(String fileName, long s) {
        Properties.fitness = new double[Properties.POPSIZE];
        Properties.seed = s;

        if (Properties.seed >= 0)
            Properties.rd.setSeed(Properties.seed);

        setupFitness(fileName);

        for (int i = 0; i < Properties.FUNCTION_SET_START; i++)
            Properties.x[i] = (Properties.maxrandom - Properties.minrandom) * Properties.rd.nextDouble() + Properties.minrandom;

        Population.population = Population.createRandomPopulation(Properties.fitness);
    }

    public void evolve() {
        int individual;
        Individual newInd;

        Properties.printParameters();
        Properties.printStats(Properties.fitness, Population.population, 0);

        for (int gen = 1; gen < Properties.GENERATIONS; gen++) {
            if (Properties.bestFitness > Properties.bestFitnessThreshold) {
                System.out.print("PROBLEM SOLVED\n");
                return;
            }

            for (individual = 0; individual < Properties.POPSIZE; individual++) {
                newInd = Population.createNewIndividual();
                Population.replaceWorstIndividual(newInd);
            }
            Properties.printStats(Properties.fitness, Population.population, gen);
        }
        System.out.print("PROBLEM *NOT* SOLVED\n");
    }

    public static void main(String[] args) {
        String fname = "problem.dat";
        long s = -1;

        if (args.length == 2) {
            s = Integer.parseInt(args[0]);
            fname = args[1];
        }
        if (args.length == 1) {
            fname = args[0];
        }

        GeneticProgramming gp = new GeneticProgramming(fname, s);
        gp.evolve();
    }

}
