package neoGP.model.grammar

import neoGP.NeoGP

class Individual(
    val statements: MutableList<Statement> = mutableListOf(),
) {
    private var stats: StatsOfIndividual = StatsOfIndividual()

    override fun toString(): String =
        statements.joinToString("\n")

    fun toOneLineString(): String =
        statements.joinToString(separator = " ", transform = { it.toOneLineString() })

    fun depth(): Int =
        statements.maxOf(Statement::depth) + 1

    fun fitness(): Double {
        if (stats.fitness != null)
            return stats.fitness!!

        stats = NeoGP.calculateFitness(this)
        return stats.fitness!!
    }

    fun instructions(): Int {
        if (stats.instructions != null && stats.testCases != null)
            return stats.instructions!! / stats.testCases!!

        stats = NeoGP.calculateFitness(this)
        return stats.instructions!! / stats.testCases!!
    }

    fun getChildrenAtDepth(depth: Int) = if (depth == 0)
        statements
    else
        statements.flatMap { it.getChildrenAtDepth(depth - 1) }

    fun getChildren(): Int =
        statements.sumOf(Statement::getChildren)

    fun copy() =
        Individual(
            statements.map(Statement::copy).toMutableList()
        )

    fun randomStatement(): Statement? =
        statements.flatMap(Statement::getStatements).randomOrNull()

    fun randomBlock(): Block? =
        statements.flatMap(Statement::getBlocks).randomOrNull()

    fun randomExpression(): Expression? =
        statements.flatMap(Statement::getExpressions).randomOrNull()

}

class StatsOfIndividual(
    var fitness: Double? = null,
    var instructions: Int? = null,
    var testCases: Int? = null,
)
