package newGP.model.grammar

import kotlin.random.Random


abstract class Statement(
    open val value: String,
) {
    companion object {
        fun generateRandom(): Statement = when (Random.nextInt(8)) {
            0 -> Loop.generateRandom()
            1 -> If.generateRandom()
            2 -> IfElse.generateRandom()
            3 -> In.generateRandom()
            4 -> Print.generateRandom()
            5 -> Var.generateRandom()
            6 -> VarAssign.generateRandom()
            else -> Const.generateRandom()
        }
    }

    abstract override fun toString(): String
    open fun depth(): Int = 0
    open fun getChildrenAtDepth(depth: Int): List<Statement> = listOf()

    abstract fun copy(): Statement


}

class Loop(
    override val value: String,
    val expression: Expression,
    val block: Block,
) : Statement(value) {
    companion object {
        fun generateRandom() = Loop("", Expression.generateRandom(), Block.generateRandom())
    }

    override fun toString(): String =
        "while ( $expression ) { \n$block \n}"

    override fun depth(): Int =
        block.depth() + 1

    override fun getChildrenAtDepth(depth: Int): List<Statement> =
        if (depth == 0)
            block.statements
        else
            block.statements.flatMap { it.getChildrenAtDepth(depth - 1) }

    override fun copy(): Statement =
        Loop(
            value,
            expression.copy(),
            block.copy()
        )
}

class If(
    override val value: String,
    val expression: Expression,
    val block: Block,
) : Statement(value) {
    companion object {
        fun generateRandom() = If("", Expression.generateRandom(), Block.generateRandom())
    }

    override fun toString(): String =
        "if ( $expression ) { \n$block \n}"

    override fun depth(): Int =
        block.depth() + 1

    override fun getChildrenAtDepth(depth: Int): List<Statement> =
        if (depth == 0)
            block.statements
        else
            block.statements.flatMap { it.getChildrenAtDepth(depth - 1) }

    override fun copy(): Statement =
        If(
            value,
            expression.copy(),
            block.copy()
        )
}

class IfElse(
    override val value: String,
    val expression: Expression,
    val block: Block,
    val elseBlock: Block,
) : Statement(value) {
    companion object {
        fun generateRandom() = IfElse("", Expression.generateRandom(), Block.generateRandom(), Block.generateRandom())
    }

    override fun toString(): String =
        "if ( $expression ) { \n$block \n} \nelse { \n$elseBlock \n}"

    override fun depth(): Int =
        maxOf(block.depth(), elseBlock.depth()) + 1

    override fun getChildrenAtDepth(depth: Int): List<Statement> =
        if (depth == 0)
            block.statements + elseBlock.statements
        else
            (block.statements + elseBlock.statements).flatMap { it.getChildrenAtDepth(depth - 1) }

    override fun copy(): Statement =
        IfElse(
            value,
            expression.copy(),
            block.copy(),
            elseBlock.copy()
        )
}

class In(
    override val value: String,
    val id: Id,
) : Statement(value) {
    companion object {
        fun generateRandom() = In("", Id.generateRandom())
    }

    override fun toString(): String =
        "in $id;"

    override fun copy(): Statement =
        In(
            value,
            id.copy()
        )
}

class Print(
    override val value: String,
    val expression: Expression,
) : Statement(value) {
    companion object {
        fun generateRandom() = Print("", Expression.generateRandom())
    }

    override fun toString(): String =
        "print ( $expression );"

    override fun copy(): Statement =
        Print(
            value,
            expression.copy()
        )
}

class Var(
    override val value: String,
    val id: Id,
    val expression: Expression?,
) : Statement(value) {
    companion object {
        fun generateRandom() = Var("", Id.generateRandom(), Expression.generateRandom())
    }

    override fun toString(): String =
        "var $id ${expression?.let { "= $it" } ?: ""};"

    override fun copy(): Statement =
        Var(
            value,
            id.copy(),
            expression?.copy()
        )
}

class VarAssign(
    override val value: String,
    val id: Id,
    val expression: Expression,
) : Statement(value) {
    companion object {
        fun generateRandom() = VarAssign("", Id.generateRandom(), Expression.generateRandom())
    }

    override fun toString(): String =
        "$id = $expression;"

    override fun copy(): Statement =
        VarAssign(
            value,
            id.copy(),
            expression.copy()
        )
}

class Const(
    override val value: String,
    val id: Id,
    val expression: Expression,
) : Statement(value) {
    companion object {
        fun generateRandom() = Const("", Id.generateRandom(), Expression.generateRandom())
    }

    override fun toString(): String =
        "const $id = $expression;"

    override fun copy(): Statement =
        Const(
            value,
            id.copy(),
            expression.copy()
        )
}
