package newGP.model.grammar


abstract class Statement(
    open val value: String,
) {
    abstract override fun toString(): String
}

class Loop(
    override val value: String,
    val expression: Expression,
    val block: Block,
) : Statement(value) {
    override fun toString(): String =
        "while ( $expression ) { $block )"
}

class If(
    override val value: String,
    val expression: Expression,
    val block: Block,
) : Statement(value) {
    override fun toString(): String =
        "if ( $expression ) { $block }"
}

class IfElse(
    override val value: String,
    val expression: Expression,
    val block: Block,
    val elseBlock: Block,
) : Statement(value) {
    override fun toString(): String =
        "if ( $expression ) { $block } else { $elseBlock }"
}

class In(
    override val value: String,
    val id: Id,
) : Statement(value) {
    override fun toString(): String =
        "in $id;"
}

class Print(
    override val value: String,
    val expression: Expression,
) : Statement(value) {
    override fun toString(): String =
        "print ( $expression );"
}

class Var(
    override val value: String,
    val id: Id,
    val expression: Expression?,
) : Statement(value) {
    override fun toString(): String =
        "var $id ${expression?.let { "= $it" } ?: ""};"
}

class VarAssign(
    override val value: String,
    val id: Id,
    val expression: Expression,
) : Statement(value) {
    override fun toString(): String =
        "$id = $expression;"
}

class Const(
    override val value: String,
    val id: Id,
    val expression: Expression,
) : Statement(value) {
    override fun toString(): String =
        "const $id = $expression;"
}
