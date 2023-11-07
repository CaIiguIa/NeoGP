package newGP.model.grammar

abstract class Expression(
    open val value: String,
) {
    abstract override fun toString(): String
}