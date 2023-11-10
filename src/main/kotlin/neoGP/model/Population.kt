package neoGP.model

import neoGP.model.grammar.Individual
import neoGP.model.grammar.Statement
import kotlin.random.Random

class Population(
    val individuals: MutableList<Individual> = mutableListOf()
) {
    companion object {
        fun generatePopulation(size: Int, maxDepth: Int? = null): Population {
            val population = Population()
            for (i in 1..size)
                population.individuals.add(Individual.generateRandom(5))

            return population
        }

        const val CROSSOVER_PROBABILITY = 0.9
        const val COMPETITOR_NUMBER = 5
    }

    fun replaceWorstIndividual(newIndividual: Individual) {
        val worst = findWorst()
        val idx = individuals.indexOf(worst)
        individuals.removeAt(idx)
        individuals.add(idx, newIndividual)
    }

    fun createNewIndividual(): Individual {
        val crossover = Random.nextDouble() < CROSSOVER_PROBABILITY

        val parent1 = findBest()
        val parent2 = findBest()

        val newIndividual = if (crossover)
            crossover(parent1, parent2)
        else
            mutation(parent1)

        return newIndividual
    }

    private fun findBest(): Individual {
        var best = individuals.random()
        var bestFitness = -1e34
        var competitor: Individual
        var competitorFitness: Double

        for (i in 1 until COMPETITOR_NUMBER) {
            competitor = individuals.random()
            competitorFitness = competitor.fitness()

            if (competitorFitness > bestFitness) { //TODO: uwaga na znak fitness
                bestFitness = competitorFitness
                best = competitor
            }
        }

        return best
    }

    private fun findWorst(): Individual {
        var worst = individuals.random()
        var worstFitness = 1e34
        var competitor: Individual
        var competitorFitness: Double

        for (i in 1 until COMPETITOR_NUMBER) {
            competitor = individuals.random()
            competitorFitness = competitor.fitness()

            if (competitorFitness < worstFitness) { //TODO: uwaga na znak fitness
                worstFitness = competitorFitness
                worst = competitor
            }
        }

        return worst
    }

    fun getRandomChild(parent: Individual): Statement {
        val parentDepth = Random.nextInt(parent.depth())

        return parent.getChildrenAtDepth(parentDepth).randomOrNull()
            ?: throw IllegalStateException("Random child implementation fault")
    }

    private fun crossover(parent1: Individual, parent2: Individual): Individual {
        val children1 = parent1.getChildrenAtDepth(0)
        val children2 = parent2.getChildrenAtDepth(0)

        val parent1End = Random.nextInt(children1.size)
        val parent2Start = Random.nextInt(children2.size)

        val parent1Children = parent1.statements.filterIndexed { idx, _ -> idx <= parent1End }
        val parent2Children = parent2.statements.filterIndexed { idx, _ -> idx > parent2Start }

        val newInd = Individual()

        newInd.statements.addAll(
            parent1Children.map(Statement::copy)
        )
        newInd.statements.addAll(
            parent2Children.map(Statement::copy)
        )

        return newInd
    }

    private fun mutation(parent: Individual): Individual {
        val copy = parent.copy()
        val idxToReplace = Random.nextInt(copy.statements.size)
        copy.statements.removeAt(idxToReplace)
        copy.statements.add(idxToReplace, Statement.generateRandom())

        return copy
    }
}