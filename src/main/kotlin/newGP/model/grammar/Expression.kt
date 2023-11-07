package newGP.model.grammar

abstract class Expression(
    open val value: String,
) {
    abstract override fun toString(): String
}

class ExpressionParenthesis(
    override val value: String,
    val expression: Expression,
) : Expression(value) {
    override fun toString(): String =
        "( $expression )"
}

class UnaryMinus(
    override val value: String,
    val expression: Expression,
) : Expression(value) {
    override fun toString(): String =
        "- $expression"
}

class Mathematical(
    override val value: String,
    val expression1: Expression,
    val expression2: Expression,
    val operator: Operator
) : Expression(value) {
    init {
        check(
            operator in listOf(
                Operator.ADD,
                Operator.SUBTRACT,
                Operator.MULTIPLY,
                Operator.DIVIDE
            )
        )
        { "Invalid operator type for mathematical expression: $operator" }
    }

    override fun toString(): String =
        "$expression1 ${operator.value} $expression2"
}

class Comparison(
    override val value: String,
    val expression1: Expression,
    val expression2: Expression,
    val operator: Operator
) : Expression(value) {
    init {
        check(
            operator in listOf(
                Operator.LOWER_THAN,
                Operator.LOWER_EQUAL,
                Operator.GREATER_THAN,
                Operator.GREATER_EQUAL
            )
        )
        { "Invalid operator type for comparison expression: $operator" }
    }

    override fun toString(): String =
        "$expression1 ${operator.value} $expression2"
}

class Negation(
    override val value: String,
    val expression: Expression,
) : Expression(value) {
    override fun toString(): String =
        "!$expression"
}

class Equality(
    override val value: String,
    val expression1: Expression,
    val expression2: Expression,
    val operator: Operator
) : Expression(value) {
    init {
        check(
            operator in listOf(
                Operator.EQUAL,
                Operator.NOT_EQUAL
            )
        )
        { "Invalid operator type for equality expression: $operator" }
    }

    override fun toString(): String =
        "$expression1 ${operator.value} $expression2"
}

class Logical(
    override val value: String,
    val expression1: Expression,
    val expression2: Expression,
    val operator: Operator
) : Expression(value) {
    init {
        check(
            operator in listOf(
                Operator.AND,
                Operator.OR
            )
        )
        { "Invalid operator type for logical expression: $operator" }
    }

    override fun toString(): String =
        "$expression1 ${operator.value} $expression2"
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