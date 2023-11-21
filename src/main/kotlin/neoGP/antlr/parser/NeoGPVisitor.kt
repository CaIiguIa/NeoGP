package neoGP.antlr.parser

import neoGP.antlr.parser.model.neoGPBaseVisitor
import neoGP.antlr.parser.model.neoGPParser
import java.lang.IllegalStateException

class NeoGPVisitor : neoGPBaseVisitor<List<String>>() {
    val variables: MutableList<Variable> = mutableListOf()
    val inputs: MutableList<String> = mutableListOf()
    var inputIdx: Int = 0

    companion object {
        private const val INT_REGEX = "-?[0-9]+"
        private const val FLOAT_REGEX = "-?[0-9]+.[0-9]{8}"
    }

    override fun visitProgram(ctx: neoGPParser.ProgramContext?): List<String> {
        return ctx?.statement()?.flatMap {
            visit(it)
        } ?: throw IllegalStateException("context cannot be null!")
    }

    override fun visitBlock(ctx: neoGPParser.BlockContext?): List<String> {
        return ctx?.statement()?.flatMap {
            visit(it)
        } ?: listOf()
    }

    override fun visitIfElse(ctx: neoGPParser.IfElseContext?): List<String> {
        val expression = visit(ctx!!.expression())

        return if (expression.first() == "true")
            visit(ctx.block(0))
        else
            visit(ctx.block(1))
    }

    override fun visitLoop(ctx: neoGPParser.LoopContext?): List<String> {
        var expression = visit(ctx!!.expression()).first()
        val outputs = mutableListOf<String>()

        while (expression == "true") {
            outputs += visit(ctx.block())
            expression = visit(ctx.expression()).first()
        }

        return outputs
    }

    override fun visitIf(ctx: neoGPParser.IfContext?): List<String> {
        val expression = visit(ctx!!.expression())

        return if (expression.first() == "true")
            visit(ctx.block())
        else
            listOf()
    }

    override fun visitIn(ctx: neoGPParser.InContext?): List<String> {
        val name = ctx!!.ID().text
        val value = inputs[inputIdx]
        inputIdx = (inputIdx + 1) % inputs.size

        if (variables.none { it.name == name })
            throw IllegalStateException("Variable $name is not defined in the scope")
        if (!variables.first().mutable)
            throw IllegalStateException("Const value cannot be reassigned")

        variables.first { it.name == name }.value = value

        return listOf()
    }

    override fun visitPrint(ctx: neoGPParser.PrintContext?): List<String> {
        return visit(ctx!!.expression())
    }

    override fun visitVar(ctx: neoGPParser.VarContext?): List<String> {
        val name = ctx!!.ID().text
        val expression = ctx.expression()?.let { visit(it) }?.first()

        if (variables.any { it.name == name })
            throw IllegalStateException("Variable $name is already defined")

        variables.add(Variable(name, expression, true))

        return listOf()
    }

    override fun visitVarAssign(ctx: neoGPParser.VarAssignContext?): List<String> {
        val name = ctx!!.ID().text

        if (variables.none { it.name == name })
            throw IllegalStateException("Variable $name is not defined in the scope")
        if (!variables.first().mutable)
            throw IllegalStateException("Const value cannot be reassigned")

        val expression = visit(ctx.expression()).first()
        variables.first { it.name == name }.value = expression

        return listOf()
    }

    override fun visitConst(ctx: neoGPParser.ConstContext?): List<String> {
        val name = ctx!!.ID().text
        val expression = visit(ctx.expression()).first()

        if (variables.any { it.name == name })
            throw IllegalStateException("Variable $name is already defined")

        variables.add(Variable(name, expression, false))

        return listOf()
    }

    override fun visitParenthesizedExpression(ctx: neoGPParser.ParenthesizedExpressionContext?): List<String> {
        return visit(ctx!!.expression())
    }

    override fun visitLogicOr(ctx: neoGPParser.LogicOrContext?): List<String> {
        val leftExpr = visit(ctx!!.expression(0)).first()
        val rightExpr = visit(ctx.expression(1)).first()
        val left = leftExpr.toBoolean()
        val right = rightExpr.toBoolean()

        return listOf((left || right).toString())
    }

    override fun visitMultiplication(ctx: neoGPParser.MultiplicationContext?): List<String> {
        val leftExpr = visit(ctx!!.expression(0)).first()
        val rightExpr = visit(ctx.expression(1)).first()

        if (!leftExpr.isNumeric() || !rightExpr.isNumeric())
            throw IllegalStateException("Unsupported operation on Boolean value: ${ctx.op.text}")

        if (leftExpr.isFloat() || rightExpr.isFloat()) {
            val left = leftExpr.toFloat()
            val right = rightExpr.toFloat()

            if (right == 0F)
                throw IllegalStateException("Cannot divide by zero")

            return when (ctx.op.text) {
                "*" -> listOf((left * right).toString())
                "/" -> listOf((left / right).toString())
                else -> throw IllegalStateException("Unsupported Multiplication operator: ${ctx.op.text}")
            }
        }

        val left = leftExpr.toInt()
        val right = rightExpr.toInt()

        if (right == 0)
            throw IllegalStateException("Cannot divide by zero")

        return when (ctx.op.text) {
            "*" -> listOf((left * right).toString())
            "/" -> listOf((left / right).toString())
            else -> throw IllegalStateException("Unsupported Multiplication operator: ${ctx.op.text}")
        }
    }

    override fun visitAddition(ctx: neoGPParser.AdditionContext?): List<String> {
        val leftExpr = visit(ctx!!.expression(0)).first()
        val rightExpr = visit(ctx.expression(1)).first()

        if (!leftExpr.isNumeric() || !rightExpr.isNumeric())
            throw IllegalStateException("Unsupported operation on Boolean value: ${ctx.op.text}")

        if (leftExpr.isFloat() || rightExpr.isFloat()) {
            val left = leftExpr.toFloat()
            val right = rightExpr.toFloat()

            return when (ctx.op.text) {
                "+" -> listOf((left + right).toString())
                "-" -> listOf((left - right).toString())
                else -> throw IllegalStateException("Unsupported Addition operator: ${ctx.op.text}")
            }
        }

        val left = leftExpr.toInt()
        val right = rightExpr.toInt()

        return when (ctx.op.text) {
            "+" -> listOf((left + right).toString())
            "-" -> listOf((left - right).toString())
            else -> throw IllegalStateException("Unsupported Addition operator: ${ctx.op.text}")
        }
    }

    override fun visitPrimaryExpression(ctx: neoGPParser.PrimaryExpressionContext?): List<String> {
        return super.visitPrimaryExpression(ctx)
    }

    override fun visitNegation(ctx: neoGPParser.NegationContext?): List<String> {
        val expression = visit(ctx!!.expression()).first()
        return listOf(expression.toBoolean().toString())
    }

    override fun visitComparison(ctx: neoGPParser.ComparisonContext?): List<String> {
        val leftExpr = visit(ctx!!.expression(0)).first()
        val rightExpr = visit(ctx.expression(1)).first()

        if (!leftExpr.isNumeric() || !rightExpr.isNumeric())
            throw IllegalStateException("Unsupported operation on Boolean value: ${ctx.op.text}")

        val left = leftExpr.toFloat()
        val right = rightExpr.toFloat()

        val value = when (ctx.op.text) {
            ">" -> (left > right)
            ">=" -> (left >= right)
            "<" -> (left < right)
            "<=" -> (left <= right)
            else -> throw IllegalStateException("Unsupported comparison operator: ${ctx.op.text}")
        }

        return listOf(value.toString())
    }

    override fun visitUnaryMinus(ctx: neoGPParser.UnaryMinusContext?): List<String> {
        val expression = visit(ctx!!.expression()).first()
        val value = when {
            expression.isBoolean() -> throw IllegalStateException("Unsupported operation on Boolean value: unary minus")
            expression.isInt() -> (expression.toInt() * -1).toString()
            expression.isFloat() -> (expression.toFloat() * -1).toString()
            else -> throw IllegalStateException("Value $expression is of unsupported type")
        }

        return listOf(value)
    }

    override fun visitEquality(ctx: neoGPParser.EqualityContext?): List<String> {
        val left = visit(ctx!!.expression(0)).first()
        val right = visit(ctx.expression(1)).first()
        val value = when {
            left.isBoolean() && right.isBoolean() -> left == right
            left.isInt() && right.isInt() -> left == right
            left.isFloat() && right.isFloat() -> left == right
            else -> throw IllegalStateException("Values $left and $right are not comparable")
        }

        return listOf(value.toString())
    }

    override fun visitLogicAnd(ctx: neoGPParser.LogicAndContext?): List<String> {
        val leftExpr = visit(ctx!!.expression(0)).first()
        val rightExpr = visit(ctx.expression(1)).first()
        val left = leftExpr.toBoolean()
        val right = rightExpr.toBoolean()

        return listOf((left && right).toString())
    }

    override fun visitIntLiteral(ctx: neoGPParser.IntLiteralContext?): List<String> {
        return listOf(ctx!!.INT().text)
    }

    override fun visitFPNumberLiteral(ctx: neoGPParser.FPNumberLiteralContext?): List<String> {
        return listOf(ctx!!.FPNUMBER().text)
    }

    override fun visitBooleanLiteral(ctx: neoGPParser.BooleanLiteralContext?): List<String> {
        return listOf(ctx!!.BOOL().text)
    }

    override fun visitIdentifier(ctx: neoGPParser.IdentifierContext?): List<String> {
        val value = variables.firstOrNull { it.name == ctx!!.ID().text }?.value
        return listOf(value ?: "null")
    }

    private fun String.toBoolean(): Boolean = when (this) {
        "true" -> true
        "false" -> false
        else -> this.toFloat() != 0F
    }

    private fun String.isBoolean(): Boolean =
        this == "true" || this == "false"

    private fun String.isInt(): Boolean =
        Regex(INT_REGEX).matches(this)

    private fun String.isFloat(): Boolean =
        Regex(FLOAT_REGEX).matches(this)

    private fun String.isNumeric(): Boolean =
        this.isInt() || this.isFloat()

}

data class Variable(
    val name: String,
    var value: String?,
    val mutable: Boolean,
)