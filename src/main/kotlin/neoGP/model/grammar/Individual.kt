package neoGP.model.grammar

class Individual(
    val statements: MutableList<Statement> = mutableListOf()
) {
    companion object {
        fun generateRandom(size: Int): Individual {
            val individual = Individual()
            for (i in 1..size)
                individual.statements.add(Statement.generateRandom())

            return individual
        }
    }


    override fun toString(): String =
        statements.joinToString("\n")

    fun depth(): Int =
        statements.maxOf(Statement::depth) + 1

    fun fitness(): Double = TODO()

    fun treeLength(): Int = TODO()

    fun getChildrenAtDepth(depth: Int) = statements.flatMap { it.getChildrenAtDepth(depth) }

    fun copy() =
        Individual(
            statements.map(Statement::copy).toMutableList()
        )

}
