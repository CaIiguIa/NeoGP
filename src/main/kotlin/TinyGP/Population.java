package TinyGP;

public class Population {
    static Individual[] population;

    private static final Individual buffer = new Individual(new char[Properties.MAX_LEN]);


    static Individual[] createRandomPopulation(double[] fitness) {
        Individual[] population = new Individual[Properties.POPSIZE];
        int i;

        for (i = 0; i < Properties.POPSIZE; i++) {
            population[i] = createRandomIndividual();
            fitness[i] = population[i].calculateFitness();
        }

        return (population);
    }

    static void replaceWorstIndividual(Individual newIndividual) {
        double newFitness = newIndividual.calculateFitness();
        int worstIndividual = findWorst(Properties.fitness);
        Population.population[worstIndividual] = newIndividual;
        Properties.fitness[worstIndividual] = newFitness;
    }

    static Individual createNewIndividual() {
        int parent1, parent2, parent;
        boolean doCrossover = Properties.rd.nextDouble() < Properties.CROSSOVER_PROB;
        if (doCrossover) {
            parent1 = findBest(Properties.fitness);
            parent2 = findBest(Properties.fitness);
            return crossover(Population.population[parent1], Population.population[parent2]);
        }
        // mutate
        parent = findBest(Properties.fitness);
        return mutation(Population.population[parent]);
    }

    static Individual createRandomIndividual() {
        Individual ind;
        int len;

        len = grow(buffer, 0, Properties.MAX_LEN, Properties.DEPTH);

        while (len < 0)
            len = grow(buffer, 0, Properties.MAX_LEN, Properties.DEPTH);

        ind = new Individual(new char[len]);

        System.arraycopy(buffer.data, 0, ind.data, 0, len);

        return (ind);
    }

    static int findBest(double[] fitness) {
        int best = Properties.rd.nextInt(Properties.POPSIZE), i, competitor;
        double bestFitness = -1.0e34;

        for (i = 0; i < Properties.COMPETITORS; i++) {
            competitor = Properties.rd.nextInt(Properties.POPSIZE);
            if (fitness[competitor] > bestFitness) {
                bestFitness = fitness[competitor];
                best = competitor;
            }
        }

        return (best);
    }

    static int findWorst(double[] fitness) {
        int worst = Properties.rd.nextInt(Properties.POPSIZE), i, competitor;
        double worstFitness = 1e34;

        for (i = 0; i < Properties.COMPETITORS; i++) {
            competitor = Properties.rd.nextInt(Properties.POPSIZE);
            if (fitness[competitor] < worstFitness) {
                worstFitness = fitness[competitor];
                worst = competitor;
            }
        }
        return (worst);
    }

    static Individual crossover(Individual parent1, Individual parent2) {
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

    static Individual mutation(Individual parent) {
        int len = parent.getTreeLength(0);
        Individual parentCopy = new Individual(new char[len]);

        System.arraycopy(parent.data, 0, parentCopy.data, 0, len);

        for (int mutsite = 0; mutsite < len; mutsite++) {
            if (Properties.rd.nextDouble() < Properties.PMUT_PER_NODE) {
                if (!Properties.isOperation(parentCopy.data[mutsite]))
                    parentCopy.data[mutsite] = (char) Properties.rd.nextInt(Properties.varNumber + Properties.randomNumber);
                else if (Properties.isFunction(parentCopy.data[mutsite]))
                    parentCopy.data[mutsite] = (char) (Properties.rd.nextInt(Properties.FUNCTION_SET_END - Properties.FUNCTION_SET_START + 1)
                            + Properties.FUNCTION_SET_START);
            }
        }
        return (parentCopy);
    }

    static int grow(Individual buffer, int pos, int max, int depth) {
        char prim = (char) Properties.rd.nextInt(2);
        int one_child;

        if (pos >= max)
            return (-1);

        if (pos == 0)
            prim = 1;

        if (prim == 0 || depth == 0) {
            prim = (char) Properties.rd.nextInt(Properties.varNumber + Properties.randomNumber);
            buffer.data[pos] = prim;
            return (pos + 1);
        } else {
            prim = (char) (Properties.rd.nextInt(Properties.FUNCTION_SET_END - Properties.FUNCTION_SET_START + 1) + Properties.FUNCTION_SET_START);
            if (Properties.isFunction(prim)) {
                buffer.data[pos] = prim;
                one_child = grow(buffer, pos + 1, max, depth - 1);
                if (one_child < 0)
                    return (-1);
                return (grow(buffer, one_child, max, depth - 1));
            }
        }

        return (0); // should never get here
    }

}
