package neoGP.model.grammar

class Block(
    val statements: MutableList<Statement> = mutableListOf(),
) {

    override fun toString(): String =
        statements.joinToString("\n")

    fun depth(): Int =
        statements.maxOf(Statement::depth) + 1

    fun copy() =
        Block(
            statements.map(Statement::copy).toMutableList()
        )

    fun toOneLineString(): String =
        statements.joinToString { it.toOneLineString() }

}