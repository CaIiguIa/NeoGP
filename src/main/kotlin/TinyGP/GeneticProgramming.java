package TinyGP;/*
 * Program:   tiny_gp.java
 *
 * Author:    Riccardo Poli (email: rpoli@essex.ac.uk)
 *
 */

import java.util.*;
import java.io.*;

public class GeneticProgramming {
    double[] fitness;
    Individual[] pop;
    static Random rd = new Random();
    static final int
            ADD = 110,
            SUB = 111,
            MUL = 112,
            DIV = 113,
            FUNCTION_SET_START = ADD,
            FUNCTION_SET_END = DIV;
    static double[] x = new double[FUNCTION_SET_START];
    static double minrandom, maxrandom;
    static Individual program;
    static int PC;
    static int varnumber, fitnesscases, randomnumber;
    static double bestFitness = 0.0;
    static long seed;
    static double avg_len;
    static double bestFitnessThreshold = -1e-5;
    static final int
            MAX_LEN = 10000,
            POPSIZE = 100000,
            DEPTH = 5,
            GENERATIONS = 25,
            TSIZE = 2;
    public static final double
            PMUT_PER_NODE = 0.05,
            CROSSOVER_PROB = 0.9;
    static double[][] targets;

    double run() { /* Interpreter */
        char primitive = program.data[PC++];

        if (primitive < FUNCTION_SET_START)
            return (x[primitive]);

        switch (primitive) {
            case ADD:
                return (run() + run());
            case SUB:
                return (run() - run());
            case MUL:
                return (run() * run());
            case DIV: {
                double num = run(), den = run();
                if (Math.abs(den) <= 0.001)
                    return (num);
                else
                    return (num / den);
            }
        }

        return (0.0); // should never get here
    }

    int getTreeLength(Individual buffer, int buffercount) {
        if (!isOperation(buffer.data[buffercount]))
            return (++buffercount);

        return switch (buffer.data[buffercount]) {
            case ADD, SUB, MUL, DIV -> (getTreeLength(buffer, getTreeLength(buffer, ++buffercount)));
            default -> (0);
        };
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
            varnumber = Integer.parseInt(tokens.nextToken().trim());
            randomnumber = Integer.parseInt(tokens.nextToken().trim());
            minrandom = Double.parseDouble(tokens.nextToken().trim());
            maxrandom = Double.parseDouble(tokens.nextToken().trim());
            fitnesscases = Integer.parseInt(tokens.nextToken().trim());
            targets = new double[fitnesscases][varnumber + 1];

            if (isOperation(varnumber + randomnumber))
                System.out.println("too many variables and constants");

            for (i = 0; i < fitnesscases; i++) {
                line = in.readLine();
                tokens = new StringTokenizer(line);
                for (j = 0; j <= varnumber; j++) {
                    targets[i][j] = Double.parseDouble(tokens.nextToken().trim());
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

        for (int i = 0; i < fitnesscases; i++) {
            if (varnumber >= 0)
                System.arraycopy(targets[i], 0, x, 0, varnumber);
            program = individual;
            PC = 0;
            result = run();
            fit += Math.abs(result - targets[i][varnumber]);
        }

        return (-fit);
    }

    int grow(Individual buffer, int pos, int max, int depth) {
        char prim = (char) rd.nextInt(2);
        int one_child;

        if (pos >= max)
            return (-1);

        if (pos == 0)
            prim = 1;

        if (prim == 0 || depth == 0) {
            prim = (char) rd.nextInt(varnumber + randomnumber);
            buffer.data[pos] = prim;
            return (pos + 1);
        } else {
            prim = (char) (rd.nextInt(FUNCTION_SET_END - FUNCTION_SET_START + 1) + FUNCTION_SET_START);
            switch (prim) {
                case ADD, SUB, MUL, DIV -> {
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

    int printIndividual(Individual buffer, int bufferCounter) {
        int a1 = 0, a2;

        if (!isOperation(buffer.data[bufferCounter])) {
            if (buffer.data[bufferCounter] < varnumber)
                System.out.print("X" + (buffer.data[bufferCounter] + 1) + " ");
            else
                System.out.print(x[buffer.data[bufferCounter]]);
            return (++bufferCounter);
        }

        System.out.print("(");


        switch (buffer.data[bufferCounter]) {
            case ADD -> {
                a1 = printIndividual(buffer, ++bufferCounter);
                System.out.print(" + ");
            }
            case SUB -> {
                a1 = printIndividual(buffer, ++bufferCounter);
                System.out.print(" - ");
            }
            case MUL -> {
                a1 = printIndividual(buffer, ++bufferCounter);
                System.out.print(" * ");
            }
            case DIV -> {
                a1 = printIndividual(buffer, ++bufferCounter);
                System.out.print(" / ");
            }
        }
        a2 = printIndividual(buffer, a1);
        System.out.print(")");

        return (a2);
    }


    static Individual buffer = new Individual(new char[MAX_LEN]);

    Individual createRandomIndividual() {
        Individual ind;
        int len;

        len = grow(buffer, 0, MAX_LEN, GeneticProgramming.DEPTH);

        while (len < 0)
            len = grow(buffer, 0, MAX_LEN, GeneticProgramming.DEPTH);

        ind = new Individual(new char[len]);

        System.arraycopy(buffer.data, 0, ind.data, 0, len);

        return (ind);
    }

    Individual[] createRandomPopulation(double[] fitness) {
        Individual[] population = new Individual[GeneticProgramming.POPSIZE];
        int i;

        for (i = 0; i < GeneticProgramming.POPSIZE; i++) {
            population[i] = createRandomIndividual();
            fitness[i] = calculateFitness(population[i]);
        }

        return (population);
    }


    void printStats(double[] fitness, Individual[] pop, int gen) {
        int i, best = rd.nextInt(POPSIZE);
        int node_count = 0;
        bestFitness = fitness[best];
        double favgpop = 0.0;

        for (i = 0; i < POPSIZE; i++) {
            node_count += getTreeLength(pop[i], 0);
            favgpop += fitness[i];
            if (fitness[i] > bestFitness) {
                best = i;
                bestFitness = fitness[i];
            }
        }

        avg_len = (double) node_count / POPSIZE;
        favgpop /= POPSIZE;
        System.out.print("Generation=" + gen + " Avg Fitness=" + (-favgpop) +
                " Best Fitness=" + (-bestFitness) + " Avg Size=" + avg_len +
                "\nBest Individual: ");
        printIndividual(pop[best], 0);
        System.out.print("\n");
        System.out.flush();
    }

    int findBest(double[] fitness) {
        int best = rd.nextInt(POPSIZE), i, competitor;
        double fbest = -1.0e34;

        for (i = 0; i < GeneticProgramming.TSIZE; i++) {
            competitor = rd.nextInt(POPSIZE);
            if (fitness[competitor] > fbest) {
                fbest = fitness[competitor];
                best = competitor;
            }
        }

        return (best);
    }

    int findWorst(double[] fitness) {
        int worst = rd.nextInt(POPSIZE), i, competitor;
        double fworst = 1e34;

        for (i = 0; i < GeneticProgramming.TSIZE; i++) {
            competitor = rd.nextInt(POPSIZE);
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
        int parent1Len = getTreeLength(parent1, 0);
        int parent2Len = getTreeLength(parent2, 0);
        int newLen;

        subTree1Start = rd.nextInt(parent1Len);
        subTree1End = getTreeLength(parent1, subTree1Start);

        subTree2Start = rd.nextInt(parent2Len);
        subTree2End = getTreeLength(parent2, subTree2Start);

        newLen = subTree1Start + (subTree2End - subTree2Start) + (parent1Len - subTree1End);

        child = new Individual(new char[newLen]);

        System.arraycopy(parent1.data, 0, child.data, 0, subTree1Start);
        System.arraycopy(parent2.data, subTree2Start, child.data, subTree1Start, (subTree2End - subTree2Start));
        System.arraycopy(parent1.data, subTree1End, child.data, subTree1Start + (subTree2End - subTree2Start), (parent1Len - subTree1End));

        return (child);
    }

    Individual mutation(Individual parent, double pmut) {
        int len = getTreeLength(parent, 0);
        Individual parentCopy = new Individual(new char[len]);

        System.arraycopy(parent.data, 0, parentCopy.data, 0, len);

        for (int mutsite = 0; mutsite < len; mutsite++) {
            if (rd.nextDouble() < pmut) {
                if (!isOperation(parentCopy.data[mutsite]))
                    parentCopy.data[mutsite] = (char) rd.nextInt(varnumber + randomnumber);
                else
                    switch (parentCopy.data[mutsite]) {
                        case ADD, SUB, MUL, DIV -> parentCopy.data[mutsite] = (char) (rd.nextInt(FUNCTION_SET_END - FUNCTION_SET_START + 1)
                                + FUNCTION_SET_START);
                    }
            }
        }
        return (parentCopy);
    }

    void printParameters() {
        System.out.print("-- TINY GP (Java version) --\n");
        System.out.print("SEED=" + seed + "\nMAX_LEN=" + MAX_LEN +
                "\nPOPSIZE=" + POPSIZE + "\nDEPTH=" + DEPTH +
                "\nCROSSOVER_PROB=" + CROSSOVER_PROB +
                "\nPMUT_PER_NODE=" + PMUT_PER_NODE +
                "\nMIN_RANDOM=" + minrandom +
                "\nMAX_RANDOM=" + maxrandom +
                "\nGENERATIONS=" + GENERATIONS +
                "\nTSIZE=" + TSIZE +
                "\n----------------------------------\n");
    }

    public GeneticProgramming(String fname, long s) {
        fitness = new double[POPSIZE];
        seed = s;

        if (seed >= 0)
            rd.setSeed(seed);

        setup_fitness(fname);

        for (int i = 0; i < FUNCTION_SET_START; i++)
            x[i] = (maxrandom - minrandom) * rd.nextDouble() + minrandom;

        pop = createRandomPopulation(fitness);
    }

    public void evolve() {
        int individual;
        Individual newind;

        printParameters();
        printStats(fitness, pop, 0);

        for (int gen = 1; gen < GENERATIONS; gen++) {
            if (bestFitness > bestFitnessThreshold) {
                System.out.print("PROBLEM SOLVED\n");
                return;
            }

            for (individual = 0; individual < POPSIZE; individual++) {
                newind = createNewIndividual();
                replaceWorstIndividual(newind);
            }
            printStats(fitness, pop, gen);
        }
        System.out.print("PROBLEM *NOT* SOLVED\n");
    }

    public void replaceWorstIndividual(Individual newIndividual) {
        double newFitness = calculateFitness(newIndividual);
        int worstIndividual = findWorst(fitness);
        pop[worstIndividual] = newIndividual;
        fitness[worstIndividual] = newFitness;
    }

    public Individual createNewIndividual() {
        int parent1, parent2, parent;
        boolean doCrossover = rd.nextDouble() < CROSSOVER_PROB;
        if (doCrossover) {
            parent1 = findBest(fitness);
            parent2 = findBest(fitness);
            return crossover(pop[parent1], pop[parent2]);
        }
        // mutate
        parent = findBest(fitness);
        return mutation(pop[parent], PMUT_PER_NODE);
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

    public boolean isOperation(int i) {
        return i >= FUNCTION_SET_START;
    }
}
