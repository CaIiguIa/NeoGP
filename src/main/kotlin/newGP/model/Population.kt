package newGP.model

import newGP.model.grammar.Individual
import newGP.model.grammar.Statement
import kotlin.random.Random

class Population(
    val individuals: List<Individual>
) {
    companion object {
        fun generatePopulation(size: Int, maxDepth: Int? = null): Population = TODO()
    }

    fun replaceWorstIndividual(newIndividual: Individual): Nothing = TODO()

    fun createNewIndividual(): Individual = TODO()

    fun createRandomIndividual(): Individual = TODO()

    fun findBest(fitness: DoubleArray): Int = TODO()

    fun findWorst(fitness: DoubleArray): Int = TODO()

    fun getRandomChild(parent: Individual): Statement {
        val parentDepth = Random.nextInt(parent.depth())

        return parent.getChildrenAtDepth(parentDepth).randomOrNull()
            ?: throw IllegalStateException("Random child implementation fault")
    }

    fun crossover(parent1: Individual, parent2: Individual): Individual {
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

    fun mutation(parent: Individual): Individual {
        val copy = parent.copy()

        TODO("losowe dziecko na losowym poziomie zamieniamy na wygenerowanego, do tego dla każdej klasy metoda generująca losowego")
    }
}