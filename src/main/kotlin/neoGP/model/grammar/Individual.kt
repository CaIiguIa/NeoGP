package neoGP.model.grammar

import neoGP.NeoGP

class Individual(
    val statements: MutableList<Statement> = mutableListOf(),
) {
    private var stats: StatsOfIndividual = StatsOfIndividual()

    override fun toString(): String =
        statements.joinToString("\n")

    fun toOneLineString(): String =
        statements.joinToString {it.toOneLineString()}

    fun depth(): Int =
        statements.maxOf(Statement::depth) + 1

    fun fitness(): Int {
        if (stats.fitness != null)
            return stats.fitness!!

        stats = NeoGP.calculateFitness(this)
        return stats.fitness!!
    }

    fun instructions(): Int {
        if (stats.instructions != null && stats.testCases != null)
            return stats.instructions!! /stats.testCases!!

        stats = NeoGP.calculateFitness(this)
        return stats.instructions!! /stats.testCases!!
    }

    fun getChildrenAtDepth(depth: Int) = statements.flatMap { it.getChildrenAtDepth(depth) }

    fun copy() =
        Individual(
            statements.map(Statement::copy).toMutableList()
        )

}

class StatsOfIndividual(
    var fitness: Int? = null,
    var instructions: Int? = null,
    var testCases: Int? = null,
)
