package neoGP.model.grammar

import kotlin.random.Random

abstract class Expression(
    open val value: String,
) {
    companion object {
        fun generateRandom(): Expression = when (Random.nextInt(14)) {
            0 -> ExpressionParenthesis.generateRandom()
            1 -> UnaryMinus.generateRandom()
            2 -> Mathematical.generateRandom()
            3 -> Comparison.generateRandom()
            4 -> Negation.generateRandom()
            5 -> Equality.generateRandom()
            6 -> Logical.generateRandom()

            else -> Primary.generateRandom() // 50% chance
        }
    }


    abstract override fun toString(): String

    abstract fun copy(): Expression
}

class ExpressionParenthesis(
    override val value: String,
    val expression: Expression,
) : Expression(value) {
    companion object {
        fun generateRandom() = ExpressionParenthesis("", Expression.generateRandom())
    }

    override fun toString(): String =
        "( $expression )"

    override fun copy(): Expression =
        ExpressionParenthesis(
            value,
            expression.copy()
        )
}

class UnaryMinus(
    override val value: String,
    val expression: Expression,
) : Expression(value) {
    companion object {
        fun generateRandom() = UnaryMinus("", Expression.generateRandom())
    }

    override fun toString(): String =
        "- $expression"

    override fun copy(): Expression =
        UnaryMinus(
            value,
            expression.copy()
        )
}

class Mathematical(
    override val value: String,
    val expression1: Expression,
    val expression2: Expression,
    val operator: Operator
) : Expression(value) {
    init {
        check(operator in operands)
        { "Invalid operator type for mathematical expression: $operator" }
    }

    companion object {
        private val operands = listOf(Operator.ADD, Operator.SUBTRACT, Operator.MULTIPLY, Operator.DIVIDE)

        fun generateRandom() =
            Mathematical("", Expression.generateRandom(), Expression.generateRandom(), operands.random())
    }

    override fun toString(): String =
        "$expression1 ${operator.value} $expression2"

    override fun copy(): Expression =
        Mathematical(
            value,
            expression1.copy(),
            expression2.copy(),
            operator
        )
}

class Comparison(
    override val value: String,
    val expression1: Expression,
    val expression2: Expression,
    val operator: Operator
) : Expression(value) {
    init {
        check(operator in operands)
        { "Invalid operator type for comparison expression: $operator" }
    }

    companion object {
        private val operands =
            listOf(Operator.LOWER_THAN, Operator.LOWER_EQUAL, Operator.GREATER_THAN, Operator.GREATER_EQUAL)

        fun generateRandom() =
            Comparison("", Expression.generateRandom(), Expression.generateRandom(), operands.random())
    }

    override fun toString(): String =
        "$expression1 ${operator.value} $expression2"

    override fun copy(): Expression =
        Comparison(
            value,
            expression1.copy(),
            expression2.copy(),
            operator
        )
}

class Negation(
    override val value: String,
    val expression: Expression,
) : Expression(value) {
    companion object {
        fun generateRandom() = Negation("", Expression.generateRandom())
    }

    override fun toString(): String =
        "!$expression"

    override fun copy(): Expression =
        Negation(
            value,
            expression.copy()
        )
}

class Equality(
    override val value: String,
    val expression1: Expression,
    val expression2: Expression,
    val operator: Operator
) : Expression(value) {
    init {
        check(operator in operands)
        { "Invalid operator type for equality expression: $operator" }
    }

    companion object {
        private val operands = listOf(Operator.EQUAL, Operator.NOT_EQUAL)

        fun generateRandom() =
            Equality("", Expression.generateRandom(), Expression.generateRandom(), operands.random())
    }

    override fun toString(): String =
        "$expression1 ${operator.value} $expression2"

    override fun copy(): Expression =
        Equality(
            value,
            expression1.copy(),
            expression2.copy(),
            operator
        )
}

class Logical(
    override val value: String,
    val expression1: Expression,
    val expression2: Expression,
    val operator: Operator
) : Expression(value) {
    init {
        check(operator in operands)
        { "Invalid operator type for logical expression: $operator" }
    }

    companion object {
        private val operands = listOf(Operator.AND, Operator.OR)
        fun generateRandom() =
            Logical("", Expression.generateRandom(), Expression.generateRandom(), operands.random())
    }

    override fun toString(): String =
        "$expression1 ${operator.value} $expression2"

    override fun copy(): Expression =
        Logical(
            value,
            expression1.copy(),
            expression2.copy(),
            operator
        )
}

enum class Operator(val value: String) {
    MULTIPLY("*"),
    DIVIDE("/"),
    ADD("+"),
    SUBTRACT("-"),
    LOWER_THAN("<"),
    GREATER_THAN(">"),
    LOWER_EQUAL("<="),
    GREATER_EQUAL(">="),
    EQUAL("=="),
    NOT_EQUAL("!="),
    AND("&&"),
    OR("||"),
}