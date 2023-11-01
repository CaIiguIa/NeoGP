package TinyGP;/*
 * Program:   tiny_gp.java
 *
 * Author:    Riccardo Poli (email: rpoli@essex.ac.uk)
 *
 */

import java.util.*;
import java.io.*;

public class GeneticProgramming {

    double run() { /* Interpreter */
        char primitive = Properties.program.data[Properties.PC++];

        if (primitive < Properties.FUNCTION_SET_START)
            return (Properties.x[primitive]);

        switch (primitive) {
            case Properties.ADD:
                return (run() + run());
            case Properties.SUB:
                return (run() - run());
            case Properties.MUL:
                return (run() * run());
            case Properties.DIV: {
                double num = run(), den = run();
                if (Math.abs(den) <= 0.001)
                    return (num);
                else
                    return (num / den);
            }
        }

        return (0.0); // should never get here
    }

    void setup_fitness(String fname) {
        try {
            int i, j;
            String line;

            BufferedReader in =
                    new BufferedReader(
                            new
                                    FileReader(fname));
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

    double calculateFitness(Individual individual) {
        double result, fit = 0.0;

        for (int i = 0; i < Properties.fitnesscases; i++) {
            if (Properties.varnumber >= 0)
                System.arraycopy(Properties.targets[i], 0, Properties.x, 0, Properties.varnumber);
            Properties.program = individual;
            Properties.PC = 0;
            result = run();
            fit += Math.abs(result - Properties.targets[i][Properties.varnumber]);
        }

        return (-fit);
    }

    int grow(Individual buffer, int pos, int max, int depth) {
        char prim = (char) Properties.rd.nextInt(2);
        int one_child;

        if (pos >= max)
            return (-1);

        if (pos == 0)
            prim = 1;

        if (prim == 0 || depth == 0) {
            prim = (char) Properties.rd.nextInt(Properties.varnumber + Properties.randomnumber);
            buffer.data[pos] = prim;
            return (pos + 1);
        } else {
            prim = (char) (Properties.rd.nextInt(Properties.FUNCTION_SET_END - Properties.FUNCTION_SET_START + 1) + Properties.FUNCTION_SET_START);
            switch (prim) {
                case Properties.ADD, Properties.SUB, Properties.MUL, Properties.DIV -> {
                    buffer.data[pos] = prim;
                    one_child = grow(buffer, pos + 1, max, depth - 1);
                    if (one_child < 0)
                        return (-1);
                    return (grow(buffer, one_child, max, depth - 1));
                }
            }
        }

        return (0); // should never get here
    }

    static Individual buffer = new Individual(new char[Properties.MAX_LEN]);

    Individual createRandomIndividual() {
        Individual ind;
        int len;

        len = grow(buffer, 0, Properties.MAX_LEN, Properties.DEPTH);

        while (len < 0)
            len = grow(buffer, 0, Properties.MAX_LEN, Properties.DEPTH);

        ind = new Individual(new char[len]);

        System.arraycopy(buffer.data, 0, ind.data, 0, len);

        return (ind);
    }

    Individual[] createRandomPopulation(double[] fitness) {
        Individual[] population = new Individual[Properties.POPSIZE];
        int i;

        for (i = 0; i < Properties.POPSIZE; i++) {
            population[i] = createRandomIndividual();
            fitness[i] = calculateFitness(population[i]);
        }

        return (population);
    }

    int findBest(double[] fitness) {
        int best = Properties.rd.nextInt(Properties.POPSIZE), i, competitor;
        double fbest = -1.0e34;

        for (i = 0; i < Properties.TSIZE; i++) {
            competitor = Properties.rd.nextInt(Properties.POPSIZE);
            if (fitness[competitor] > fbest) {
                fbest = fitness[competitor];
                best = competitor;
            }
        }

        return (best);
    }

    int findWorst(double[] fitness) {
        int worst = Properties.rd.nextInt(Properties.POPSIZE), i, competitor;
        double fworst = 1e34;

        for (i = 0; i < Properties.TSIZE; i++) {
            competitor = Properties.rd.nextInt(Properties.POPSIZE);
            if (fitness[competitor] < fworst) {
                fworst = fitness[competitor];
                worst = competitor;
            }
        }
        return (worst);
    }

    Individual crossover(Individual parent1, Individual parent2) {
        int subTree1Start, subTree1End, subTree2Start, subTree2End;
        Individual child;
        int parent1Len = parent1.getTreeLength(0);
        int parent2Len = parent2.getTreeLength(0);
        int newLen;

        subTree1Start = Properties.rd.nextInt(parent1Len);
        subTree1End = parent1.getTreeLength(subTree1Start);

        subTree2Start = Properties.rd.nextInt(parent2Len);
        subTree2End = parent2.getTreeLength(subTree2Start);

        newLen = subTree1Start + (subTree2End - subTree2Start) + (parent1Len - subTree1End);

        child = new Individual(new char[newLen]);

        System.arraycopy(parent1.data, 0, child.data, 0, subTree1Start);
        System.arraycopy(parent2.data, subTree2Start, child.data, subTree1Start, (subTree2End - subTree2Start));
        System.arraycopy(parent1.data, subTree1End, child.data, subTree1Start + (subTree2End - subTree2Start), (parent1Len - subTree1End));

        return (child);
    }

    Individual mutation(Individual parent, double pmut) {
        int len = parent.getTreeLength(0);
        Individual parentCopy = new Individual(new char[len]);

        System.arraycopy(parent.data, 0, parentCopy.data, 0, len);

        for (int mutsite = 0; mutsite < len; mutsite++) {
            if (Properties.rd.nextDouble() < pmut) {
                if (!Properties.isOperation(parentCopy.data[mutsite]))
                    parentCopy.data[mutsite] = (char) Properties.rd.nextInt(Properties.varnumber + Properties.randomnumber);
                else
                    switch (parentCopy.data[mutsite]) {
                        case Properties.ADD, Properties.SUB, Properties.MUL, Properties.DIV ->
                                parentCopy.data[mutsite] = (char) (Properties.rd.nextInt(Properties.FUNCTION_SET_END - Properties.FUNCTION_SET_START + 1)
                                        + Properties.FUNCTION_SET_START);
                    }
            }
        }
        return (parentCopy);
    }

    public GeneticProgramming(String fname, long s) {
        Properties.fitness = new double[Properties.POPSIZE];
        Properties.seed = s;

        if (Properties.seed >= 0)
            Properties.rd.setSeed(Properties.seed);

        setup_fitness(fname);

        for (int i = 0; i < Properties.FUNCTION_SET_START; i++)
            Properties.x[i] = (Properties.maxrandom - Properties.minrandom) * Properties.rd.nextDouble() + Properties.minrandom;

        Population.population = createRandomPopulation(Properties.fitness);
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
                newInd = createNewIndividual();
                replaceWorstIndividual(newInd);
            }
            Properties.printStats(Properties.fitness, Population.population, gen);
        }
        System.out.print("PROBLEM *NOT* SOLVED\n");
    }

    public void replaceWorstIndividual(Individual newIndividual) {
        double newFitness = calculateFitness(newIndividual);
        int worstIndividual = findWorst(Properties.fitness);
        Population.population[worstIndividual] = newIndividual;
        Properties.fitness[worstIndividual] = newFitness;
    }

    public Individual createNewIndividual() {
        int parent1, parent2, parent;
        boolean doCrossover = Properties.rd.nextDouble() < Properties.CROSSOVER_PROB;
        if (doCrossover) {
            parent1 = findBest(Properties.fitness);
            parent2 = findBest(Properties.fitness);
            return crossover(Population.population[parent1], Population.population[parent2]);
        }
        // mutate
        parent = findBest(Properties.fitness);
        return mutation(Population.population[parent], Properties.PMUT_PER_NODE);
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
