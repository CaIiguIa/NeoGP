package neoGP.model.grammar

class Block(
    var statements: MutableList<Statement> = mutableListOf(),
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
        statements.joinToString(separator = " ", transform = { it.toOneLineString() })

    fun getChildren(): Int =
        statements.sumOf(Statement::getChildren) + 1

    fun childStatements(): List<Statement> =
        statements + statements.flatMap(Statement::getStatements)

    fun getBlocks(): List<Block> =
        statements.flatMap(Statement::getBlocks)

    fun getExpressions(): List<Expression> =
        statements.flatMap(Statement::getExpressions)

}