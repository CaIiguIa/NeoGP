package neoGP.model

import neoGP.NeoProperties
import neoGP.antlr.parser.NeoGPGenerator
import neoGP.model.grammar.Individual
import neoGP.model.grammar.Statement
import kotlin.random.Random

class Population(
    val individuals: MutableList<Individual> = mutableListOf()
) {
    companion object {
        fun generatePopulation(
            method: PopulationGenerationMethod = NeoProperties.POPULATION_GENERATION_METHOD
        ): Population {
            println("Generating population...")

            return when (method) {
                PopulationGenerationMethod.GROW -> growMethodPopulation()
                PopulationGenerationMethod.FULL -> fullMethodPopulation()
                PopulationGenerationMethod.RAMPED -> rampedMethodPopulation()
            }.also {
                println("Generated population of ${NeoProperties.POPULATION_SIZE}")
            }
        }

        private fun growMethodPopulation(): Population {
            val population = Population()
            for (i in 1..NeoProperties.POPULATION_SIZE)
                population.individuals.add(
                    NeoGPGenerator.randomIndividual(growFullTree = false, NeoProperties.MAX_GROW_DEPTH)
                )

            return population
        }

        private fun fullMethodPopulation(): Population {
            val population = Population()
            for (i in 1..NeoProperties.POPULATION_SIZE)
                population.individuals.add(
                    NeoGPGenerator.randomIndividual(growFullTree = true, NeoProperties.MAX_FULL_DEPTH)
                )

            return population
        }

        private fun rampedMethodPopulation(): Population {
            val population = Population()
            val n = (NeoProperties.MAX_GROW_DEPTH * NeoProperties.MAX_FULL_DEPTH - 1)
            val avgSize = NeoProperties.POPULATION_SIZE / n
            val sizes = List(n) { avgSize }.toMutableList()

            var sizeLeft =
                NeoProperties.POPULATION_SIZE // make sure to generate population of exactly (NeoProperties.POPULATION_SIZE) size
            for (i in 0 until n)
                sizeLeft -= sizes[i]
            sizes[n - 1] += sizeLeft

            sizes.forEachIndexed { idx, size ->
                val half1 = size / 2
                val half2 = size - half1
                for (i in 0 until half1)
                    population.individuals.add(
                        NeoGPGenerator.randomIndividual(growFullTree = true, idx + 1)
                    )

                for (i in 0 until half2)
                    population.individuals.add(
                        NeoGPGenerator.randomIndividual(growFullTree = false, idx + 1)
                    )

            }
            return population
        }


    }

    fun replaceWorstIndividual(newIndividual: Individual) {
        val worst = findWorst()
        val idx = individuals.indexOf(worst)
        individuals.removeAt(idx)
        individuals.add(idx, newIndividual)
    }

    fun createNewIndividual(): Individual {
        val crossover = Random.nextDouble() < NeoProperties.CROSSOVER_PROBABILITY

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
        var bestFitness = Int.MAX_VALUE
        var competitor: Individual
        var competitorFitness: Int

        for (i in 1 until NeoProperties.COMPETITOR_NUMBER) {
            competitor = individuals.random()
            competitorFitness = competitor.fitness()

            if (competitorFitness < bestFitness) {
                bestFitness = competitorFitness
                best = competitor
            }
        }

        return best
    }

    private fun findWorst(): Individual {
        var worst = individuals.random()
        var worstFitness = 0
        var competitor: Individual
        var competitorFitness: Int

        for (i in 1 until NeoProperties.COMPETITOR_NUMBER) {
            competitor = individuals.random()
            competitorFitness = competitor.fitness()

            if (competitorFitness > worstFitness) {
                worstFitness = competitorFitness
                worst = competitor
            }
        }

        return worst
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

        return crossoverRandomElements(newInd, parent1, parent2)
    }

    private fun crossoverRandomElements(ind: Individual, parent1: Individual, parent2: Individual): Individual {
        for (parent in List(5) { listOf(parent1, parent2) }.flatten()) {
            ind.randomBlock()?.let { block ->
                parent.randomBlock()?.let { parentBlock ->
                    block.statements = parentBlock.statements.map(Statement::copy).toMutableList()
                }
            }

            ind.randomExpression()?.let { exp ->
                parent.randomExpression()?.let { parentExp ->
                    exp.setChildExpression(parentExp)
                }
            }
        }

        return ind
    }

    private fun mutation(parent: Individual): Individual {
        val copy = parent.copy()

        var idxToReplace = Random.nextInt(copy.statements.size)
        copy.statements.removeAt(idxToReplace)
        copy.statements.add(idxToReplace, NeoGPGenerator.randomStatement())

        for (i in 1..5) {
            val block= copy.randomBlock() ?: break

            idxToReplace = Random.nextInt(block.statements.size)
            block.statements.removeAt(idxToReplace)
            block.statements.add(idxToReplace, NeoGPGenerator.randomStatement())
        }

        return copy
    }
}

enum class PopulationGenerationMethod {
    GROW,
    FULL,
    RAMPED
}