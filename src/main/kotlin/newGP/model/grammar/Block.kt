package newGP.model.grammar

class Block(
    val statements: List<Statement>,
) {
    override fun toString(): String =
        statements.joinToString("\n")

    fun depth(): Int =
        statements.maxOf(Statement::depth) + 1

    fun copy() =
        Block(
            statements.map(Statement::copy)
        )
}