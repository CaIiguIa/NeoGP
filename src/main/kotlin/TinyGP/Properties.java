package TinyGP;

import java.util.Random;

public class Properties {
    static double[] fitness;
    static Random rd = new Random();
    static final int
            ADD = 110,
            SUB = 111,
            MUL = 112,
            DIV = 113,
            SIN = 114,
            COS = 115,
            FUNCTION_SET_START = ADD,
            FUNCTION_SET_END = COS;
    static double[] x = new double[FUNCTION_SET_START];
    static double minRandom, maxRandom;
    static Individual program;
    static int PC;
    static int varNumber, fitnessCases, randomNumber;
    static double bestFitness = 0.0;
    static long seed;
    static double avg_len;
    static double bestFitnessThreshold = -1e-5;
    static final int
            MAX_LEN = 10000,
            POPSIZE = 100000,
            DEPTH = 5,
            GENERATIONS = 25,
            COMPETITORS = 5;
    static final double
            PMUT_PER_NODE = 0.05,
            CROSSOVER_PROB = 0.9;
    static double[][] targets;


    static void printParameters() {
        System.out.print("-- TINY GP (Java version) --\n");
        System.out.print("SEED=" + Properties.seed + "\nMAX_LEN=" + Properties.MAX_LEN +
                "\nPOPSIZE=" + Properties.POPSIZE + "\nDEPTH=" + Properties.DEPTH +
                "\nCROSSOVER_PROB=" + Properties.CROSSOVER_PROB +
                "\nPMUT_PER_NODE=" + Properties.PMUT_PER_NODE +
                "\nMIN_RANDOM=" + Properties.minRandom +
                "\nMAX_RANDOM=" + Properties.maxRandom +
                "\nGENERATIONS=" + Properties.GENERATIONS +
                "\nTSIZE=" + Properties.COMPETITORS +
                "\n----------------------------------\n");
    }

    static void printStats(double[] fitness, Individual[] pop, int gen) {
        int i, best = Properties.rd.nextInt(Properties.POPSIZE);
        int node_count = 0;
        Properties.bestFitness = fitness[best];
        double favgpop = 0.0;

        for (i = 0; i < Properties.POPSIZE; i++) {
            node_count += pop[i].getTreeLength(0);
            favgpop += fitness[i];
            if (fitness[i] > Properties.bestFitness) {
                best = i;
                Properties.bestFitness = fitness[i];
            }
        }

        Properties.avg_len = (double) node_count / Properties.POPSIZE;
        favgpop /= Properties.POPSIZE;
        System.out.print("Generation=" + gen + " Avg Fitness=" + (-favgpop) +
                " Best Fitness=" + (-Properties.bestFitness) + " Avg Size=" + Properties.avg_len +
                "\nBest Individual: ");
        pop[best].printIndividual(0);
        System.out.print("\n");
        System.out.flush();
    }

    static boolean isOperation(int i) {
        return i >= FUNCTION_SET_START;
    }

    static boolean isFunction(int i) {
        return i >= Properties.FUNCTION_SET_START && i <= Properties.FUNCTION_SET_END;
    }
}
