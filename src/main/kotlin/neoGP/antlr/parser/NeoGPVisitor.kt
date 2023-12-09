package neoGP.antlr.parser

import neoGP.antlr.parser.model.neoGPBaseVisitor
import neoGP.antlr.parser.model.neoGPParser
import java.lang.IllegalStateException
import kotlin.math.abs

class NeoGPVisitor(
    private val inputs: List<String>,
    private val maxInstructions: Int = 1000,
) : neoGPBaseVisitor<List<String>>() {
    private var variables: MutableList<Variable> = mutableListOf()
    private var inputIdx: Int = 0
    private var instrNumber = 0

    fun run(program: neoGPParser.ProgramContext?): List<String> {
        instrNumber = 0
        inputIdx = 0
        variables = mutableListOf()

        return visitProgram(program)
    }

    companion object {
        private const val INT_REGEX = "-?[0-9]+"
        private const val FLOAT_REGEX = "-?([0-9]+(.|,)[0-9]+(E-?[0-9]+)?|Infinity)"
    }

    override fun visitProgram(ctx: neoGPParser.ProgramContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }
        if (instrNumber >= maxInstructions) return listOf()

        return ctx.statement().flatMap {
            visitStatement(it)
        }
    }

    override fun visitBlock(ctx: neoGPParser.BlockContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }
        if (instrNumber >= maxInstructions) return listOf()

        return ctx.statement().flatMap {
            visitStatement(it)
        }
    }

    override fun visitIfElse(ctx: neoGPParser.IfElseContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }
        if (instrNumber >= maxInstructions) return listOf()
        instrNumber += 1

        val expression = visitExpression(ctx.expression())

        return if (expression.first() == "true")
            visitBlock(ctx.block(0))
        else
            visitBlock(ctx.block(1))
    }

    override fun visitLoop(ctx: neoGPParser.LoopContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }
        if (instrNumber >= maxInstructions) return listOf()
        instrNumber += 1

        var expression = visitExpression(ctx.expression()).first()
        val outputs = mutableListOf<String>()

        while (expression == "true") {
            val lastVarBefore = variables.size

            outputs += visitBlock(ctx.block())
            expression = visitExpression(ctx.expression()).first()
            val lastVarAfter = variables.size
            if (lastVarAfter != lastVarBefore) {
                val toRemove = variables.filterIndexed { index, _ -> index >= lastVarBefore }
                variables.removeAll(toRemove)
            }
            if (instrNumber >= maxInstructions) break
        }

        return outputs
    }

    override fun visitIf(ctx: neoGPParser.IfContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }
        if (instrNumber >= maxInstructions) return listOf()
        instrNumber += 1

        val expression = visitExpression(ctx.expression())

        return if (expression.first() == "true")
            visitBlock(ctx.block())
        else
            listOf()
    }

    override fun visitIn(ctx: neoGPParser.InContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }
        if (instrNumber >= maxInstructions) return listOf()
        instrNumber += 1

        val name = ctx.ID().text
        val value = nextVariableValue()

        val variable = variables.firstOrNull { it.name == name }
            ?: Variable(name, null, false).also(variables::add)
        variable.value = value
        variable.type = getValueType(value)

        return listOf()
    }

    override fun visitPrint(ctx: neoGPParser.PrintContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }
        if (instrNumber >= maxInstructions) return listOf()
        instrNumber += 1

        return visitExpression(ctx.expression())
    }

    override fun visitVar(ctx: neoGPParser.VarContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        val name = ctx.ID().text
        val expression = ctx.expression()?.let { visitExpression(it) }?.first()

        return visitCreateVariable(name, expression)
    }

    override fun visitVarAssign(ctx: neoGPParser.VarAssignContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }


        val name = ctx.ID().text
        val expression = visitExpression(ctx.expression()).first()

        return visitCreateVariable(name, expression)
    }

    private fun visitCreateVariable(name: String, value: String?): List<String> {
        if (instrNumber >= maxInstructions) return listOf()
        instrNumber += 1

        val variable = variables.firstOrNull { it.name == name }
            ?: Variable(name, null, false).also(variables::add)
        variable.value = value
        value?.let {
            variable.type = getValueType(value)
        }

        return listOf()
    }

    override fun visitConst(ctx: neoGPParser.ConstContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        val name = ctx.ID().text
        val expression = visitExpression(ctx.expression()).first()

        return visitCreateVariable(name, expression)
    }

    override fun visitParenthesizedExpression(ctx: neoGPParser.ParenthesizedExpressionContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        return visitExpression(ctx.expression())
    }

    override fun visitLogicOr(ctx: neoGPParser.LogicOrContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        val leftExpr = visitExpression(ctx.expression(0)).first()
        val rightExpr = visitExpression(ctx.expression(1)).first()
        val left = leftExpr.toBooleanValue()
        val right = rightExpr.toBooleanValue()

        return listOf((left || right).toString())
    }

    override fun visitMultiplication(ctx: neoGPParser.MultiplicationContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        val leftExpr = visitExpression(ctx.expression(0)).first()
        val rightExpr = visitExpression(ctx.expression(1)).first()

        if (rightExpr.toIntValue() != 0 && leftExpr.isInt() && rightExpr.isInt()) {
            val left = leftExpr.toIntValue()
            val right = rightExpr.toIntValue()

            return when (ctx.op.text) {
                "*" -> listOf((left * right).toString())
                "/" -> listOf((left / right).toString())
                else -> throw IllegalStateException("Unsupported Multiplication operator: $leftExpr ${ctx.op.text} $rightExpr")
            }
        }

        val left = leftExpr.toFloatValue()
        var right = rightExpr.toFloatValue()

        if (ctx.op.text == "/" && abs(right) < 0.001)
            right = 0.001f

        return when (ctx.op.text) {
            "*" -> listOf((left * right).toString())
            "/" -> listOf((left / right).toString())
            else -> throw IllegalStateException("Unsupported Multiplication operator: $leftExpr ${ctx.op.text} $rightExpr")
        }
    }

    override fun visitAddition(ctx: neoGPParser.AdditionContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }
        val leftExpr = visitExpression(ctx.expression(0)).first()
        val rightExpr = visitExpression(ctx.expression(1)).first()

        if (leftExpr.isInt() || rightExpr.isInt()) {
            val left = leftExpr.toIntValue()
            val right = rightExpr.toIntValue()

            return when (ctx.op.text) {
                "+" -> listOf((left + right).toString())
                "-" -> listOf((left - right).toString())
                else -> throw IllegalStateException("Unsupported Addition operator: $leftExpr ${ctx.op.text} $rightExpr")
            }
        }

        val left = leftExpr.toFloatValue()
        val right = rightExpr.toFloatValue()

        return when (ctx.op.text) {
            "+" -> listOf((left + right).toString())
            "-" -> listOf((left - right).toString())
            else -> throw IllegalStateException("Unsupported Addition operator: $leftExpr ${ctx.op.text} $rightExpr")
        }
    }

    override fun visitPrimaryExpression(ctx: neoGPParser.PrimaryExpressionContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        return visitPrimary(ctx.primary())
    }

    override fun visitNegation(ctx: neoGPParser.NegationContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        val expression = visitExpression(ctx.expression()).first()
        return listOf(expression.toBooleanValue().toString())
    }

    override fun visitComparison(ctx: neoGPParser.ComparisonContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        val leftExpr = visitExpression(ctx.expression(0)).first()
        val rightExpr = visitExpression(ctx.expression(1)).first()

        val left = leftExpr.toFloatValue()
        val right = rightExpr.toFloatValue()

        val value = when (ctx.op.text) {
            ">" -> (left > right)
            ">=" -> (left >= right)
            "<" -> (left < right)
            "<=" -> (left <= right)
            else -> throw IllegalStateException("Unsupported comparison operator: $leftExpr ${ctx.op.text} $rightExpr")
        }

        return listOf(value.toString())
    }

    override fun visitUnaryMinus(ctx: neoGPParser.UnaryMinusContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        val expression = visitExpression(ctx.expression()).first()
        val value = when {
            expression.isBoolean() || expression.isInt() -> (expression.toIntValue() * -1).toString()
            expression.isFloat() -> (expression.toFloatValue() * -1).toString()
            else -> throw IllegalStateException("Value $expression is of unsupported type")
        }

        return listOf(value)
    }

    override fun visitEquality(ctx: neoGPParser.EqualityContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        val leftExpr = visitExpression(ctx.expression(0)).first()
        val rightExpr = visitExpression(ctx.expression(1)).first()
        val value = leftExpr.toFloatValue() == rightExpr.toFloatValue()

        return listOf(value.toString())
    }

    override fun visitLogicAnd(ctx: neoGPParser.LogicAndContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        val leftExpr = visitExpression(ctx.expression(0)).first()
        val rightExpr = visitExpression(ctx.expression(1)).first()
        val left = leftExpr.toBooleanValue()
        val right = rightExpr.toBooleanValue()

        return listOf((left && right).toString())
    }

    override fun visitIntLiteral(ctx: neoGPParser.IntLiteralContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        return listOf(ctx.INT().text)
    }

    override fun visitFPNumberLiteral(ctx: neoGPParser.FPNumberLiteralContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        return listOf(ctx.FPNUMBER().text)
    }

    override fun visitBooleanLiteral(ctx: neoGPParser.BooleanLiteralContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        return listOf(ctx.BOOL().text)
    }

    override fun visitIdentifier(ctx: neoGPParser.IdentifierContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        val variable = variables.firstOrNull { it.name == ctx.ID().text }
            ?: Variable(ctx.ID().text, null, true).also(variables::add)
        if (variable.value == null)
            variable.value = nextVariableValue()

        variable.type = getValueType(variable.value!!)
        return listOf(variable.value!!)
    }

    private fun String.toBooleanValue(): Boolean = when (this) {
        "true" -> true
        "false" -> false
        else -> this.toFloat() != 0F
    }

    private fun String.toIntValue(): Int = when (this) {
        "true" -> 1
        "false" -> 0
        else -> this.toFloat().toInt()
    }

    private fun String.toFloatValue(): Float = when (this) {
        "true" -> 1f
        "false" -> 0f
        else -> this.toFloat()
    }

    private fun String.isBoolean(): Boolean =
        this == "true" || this == "false"

    private fun String.isInt(): Boolean =
        Regex(INT_REGEX).matches(this)

    private fun String.isFloat(): Boolean =
        Regex(FLOAT_REGEX).matches(this)

    private fun String.isNumeric(): Boolean =
        this.isInt() || this.isFloat()

    private fun getValueType(value: String): VariableType = when {
        value.isBoolean() -> VariableType.BOOL
        value.isInt() -> VariableType.INT
        value.isFloat() -> VariableType.FLOAT
        else -> throw IllegalStateException("Value \"$value\" is of unsupported type")
    }

    private fun visitExpression(ctx: neoGPParser.ExpressionContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        return when (ctx) {
            is neoGPParser.ParenthesizedExpressionContext -> visitParenthesizedExpression(ctx)
            is neoGPParser.UnaryMinusContext -> visitUnaryMinus(ctx)
            is neoGPParser.MultiplicationContext -> visitMultiplication(ctx)
            is neoGPParser.AdditionContext -> visitAddition(ctx)
            is neoGPParser.ComparisonContext -> visitComparison(ctx)
            is neoGPParser.NegationContext -> visitNegation(ctx)
            is neoGPParser.EqualityContext -> visitEquality(ctx)
            is neoGPParser.LogicAndContext -> visitLogicAnd(ctx)
            is neoGPParser.LogicOrContext -> visitLogicOr(ctx)
            is neoGPParser.PrimaryExpressionContext -> visitPrimaryExpression(ctx)
            else -> throw IllegalStateException("Illegal expression: ${ctx.text}")
        }
    }

    private fun visitPrimary(ctx: neoGPParser.PrimaryContext?): List<String> {
        check(ctx != null) { "context cannot be null!" }

        return when (ctx) {
            is neoGPParser.IdentifierContext -> visitIdentifier(ctx)
            is neoGPParser.BooleanLiteralContext -> visitBooleanLiteral(ctx)
            is neoGPParser.IntLiteralContext -> visitIntLiteral(ctx)
            is neoGPParser.FPNumberLiteralContext -> visitFPNumberLiteral(ctx)
            else -> throw IllegalStateException("Illegal primary expression: ${ctx.text}")
        }
    }

    private fun nextVariableValue(): String {
        val value = inputs[inputIdx]
        inputIdx = (inputIdx + 1) % inputs.size
        return value
    }

}

data class Variable(
    val name: String,
    var value: String?,
    val isMutable: Boolean,
    var type: VariableType? = null
)

enum class VariableType {
    BOOL,
    INT,
    FLOAT,
}
