package newGP.model.grammar

class Individual(
    val statements: MutableList<Statement> = mutableListOf()
) {
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
