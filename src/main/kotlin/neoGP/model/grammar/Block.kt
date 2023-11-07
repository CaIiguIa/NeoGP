package neoGP.model.grammar

class Block(
    val statements: List<Statement>,
) {
    companion object {
        fun generateRandom() =
            Block(listOf(Statement.generateRandom())) //TODO: Zrobić więcej, ale im więcej tym rzadsze
    }

    override fun toString(): String =
        statements.joinToString("\n")

    fun depth(): Int =
        statements.maxOf(Statement::depth) + 1

    fun copy() =
        Block(
            statements.map(Statement::copy)
        )
}