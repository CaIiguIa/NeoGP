package neoGP.model.grammar

abstract class Statement(
    open val value: String,
) {
    abstract override fun toString(): String
    open fun depth(): Int = 0
    abstract fun getChildrenAtDepth(depth: Int): List<Statement>
    abstract fun copy(): Statement
    abstract fun toOneLineString(): String
    abstract fun getChildren(): Int
    abstract fun getStatements(): List<Statement>
    abstract fun getBlocks(): List<Block>
    abstract fun getExpressions(): List<Expression>
}

class Loop(
    override val value: String,
    val expression: Expression,
    val block: Block,
) : Statement(value) {

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

    override fun toOneLineString(): String =
        "while ( $expression ) { ${block.toOneLineString()} }"

    override fun getChildren(): Int =
        expression.getChildren() + block.getChildren() + 1

    override fun getStatements(): List<Statement> =
        block.childStatements()

    override fun getBlocks(): List<Block> =
        listOf(block) + block.getBlocks()

    override fun getExpressions(): List<Expression> =
        listOf(expression) + block.getExpressions()

}

class If(
    override val value: String,
    val expression: Expression,
    val block: Block,
) : Statement(value) {

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

    override fun toOneLineString(): String =
        "if ( $expression ) { ${block.toOneLineString()} }"

    override fun getChildren(): Int =
        expression.getChildren() + block.getChildren() + 1

    override fun getStatements(): List<Statement> =
        block.childStatements()

    override fun getBlocks(): List<Block> =
        listOf(block) + block.getBlocks()

    override fun getExpressions(): List<Expression> =
        listOf(expression) + block.getExpressions()

}

class IfElse(
    override val value: String,
    val expression: Expression,
    val block: Block,
    val elseBlock: Block,
) : Statement(value) {

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

    override fun toOneLineString(): String =
        "if ( $expression ) { ${block.toOneLineString()} } else { ${elseBlock.toOneLineString()} }"

    override fun getChildren(): Int =
        expression.getChildren() + block.getChildren() + elseBlock.getChildren() + 1

    override fun getStatements(): List<Statement> =
        block.childStatements() + elseBlock.childStatements()

    override fun getBlocks(): List<Block> =
        listOf(block, elseBlock)

    override fun getExpressions(): List<Expression> =
        block.getExpressions() + elseBlock.getExpressions() + listOf(expression)

}

class In(
    override val value: String,
    val id: Id,
) : Statement(value) {

    override fun toString(): String =
        "in $id;"

    override fun getChildrenAtDepth(depth: Int): List<Statement> = listOf()

    override fun copy(): Statement =
        In(
            value,
            id.copy()
        )

    override fun toOneLineString(): String =
        "in $id;"

    override fun getChildren(): Int =
        1

    override fun getStatements(): List<Statement> =
        listOf()

    override fun getBlocks(): List<Block> =
        listOf()

    override fun getExpressions(): List<Expression> =
        listOf()

}

class Print(
    override val value: String,
    val expression: Expression,
) : Statement(value) {

    override fun toString(): String =
        "print ( $expression );"

    override fun copy(): Statement =
        Print(
            value,
            expression.copy()
        )

    override fun toOneLineString(): String =
        "print ( $expression );"

    override fun getChildrenAtDepth(depth: Int): List<Statement> = listOf()

    override fun getChildren(): Int =
        expression.getChildren() + 1

    override fun getStatements(): List<Statement> =
        listOf()

    override fun getBlocks(): List<Block> =
        listOf()

    override fun getExpressions(): List<Expression> =
        listOf(expression)

}

class Var(
    override val value: String,
    val id: Id,
    val expression: Expression?,
) : Statement(value) {

    override fun toString(): String =
        "var $id ${expression?.let { "= $it" } ?: ""};"

    override fun copy(): Statement =
        Var(
            value,
            id.copy(),
            expression?.copy()
        )

    override fun toOneLineString(): String =
        "var $id ${expression?.let { "= $it" } ?: ""};"

    override fun getChildrenAtDepth(depth: Int): List<Statement> = listOf()

    override fun getChildren(): Int =
        (expression?.getChildren() ?: 0) + 1

    override fun getStatements(): List<Statement> =
        listOf()

    override fun getBlocks(): List<Block> =
        listOf()

    override fun getExpressions(): List<Expression> =
        listOfNotNull(expression)

}

class VarAssign(
    override val value: String,
    val id: Id,
    val expression: Expression,
) : Statement(value) {

    override fun toString(): String =
        "$id = $expression;"

    override fun copy(): Statement =
        VarAssign(
            value,
            id.copy(),
            expression.copy()
        )

    override fun toOneLineString(): String =
        "$id = $expression;"

    override fun getChildrenAtDepth(depth: Int): List<Statement> = listOf()

    override fun getChildren(): Int =
        expression.getChildren() + 1

    override fun getStatements(): List<Statement> =
        listOf()

    override fun getBlocks(): List<Block> =
        listOf()

    override fun getExpressions(): List<Expression> =
        listOf(expression)

}

class Const(
    override val value: String,
    val id: Id,
    val expression: Expression,
) : Statement(value) {

    override fun toString(): String =
        "const $id = $expression;"

    override fun copy(): Statement =
        Const(
            value,
            id.copy(),
            expression.copy()
        )

    override fun toOneLineString(): String =
        "const $id = $expression;"

    override fun getChildrenAtDepth(depth: Int): List<Statement> = listOf()

    override fun getChildren(): Int =
        expression.getChildren() + 1

    override fun getStatements(): List<Statement> =
        listOf()

    override fun getBlocks(): List<Block> =
        listOf()

    override fun getExpressions(): List<Expression> =
        listOf(expression)

}
