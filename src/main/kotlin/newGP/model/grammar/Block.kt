package newGP.model.grammar

class Block(
    val statements: List<Statement>,
) {
    override fun toString(): String =
        statements.joinToString(separator = "\n")
}