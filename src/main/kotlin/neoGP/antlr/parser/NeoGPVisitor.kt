package neoGP.antlr.parser

import neoGP.antlr.parser.model.neoGPBaseVisitor
import neoGP.antlr.parser.model.neoGPParser
import java.lang.IllegalStateException

class NeoGPVisitor : neoGPBaseVisitor<List<String>>() {
    val variables: MutableList<Variable> = mutableListOf()
    val inputs: MutableList<String> = mutableListOf()
    var inputIdx: Int = 0

    companion object {
        private const val INT = "[0-9]+"
        private const val FLOAT = "[0-9]+.[0-9]{8}"
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
        val left = getBoolean(leftExpr)
        val right = getBoolean(rightExpr)

        return listOf((left || right).toString())
    }

    override fun visitMultiplication(ctx: neoGPParser.MultiplicationContext?): List<String> {
        return super.visitMultiplication(ctx)
    }

    override fun visitAddition(ctx: neoGPParser.AdditionContext?): List<String> {
        return super.visitAddition(ctx)
    }

    override fun visitPrimaryExpression(ctx: neoGPParser.PrimaryExpressionContext?): List<String> {
        return super.visitPrimaryExpression(ctx)
    }

    override fun visitNegation(ctx: neoGPParser.NegationContext?): List<String> {
        return super.visitNegation(ctx)
    }

    override fun visitComparison(ctx: neoGPParser.ComparisonContext?): List<String> {
        return super.visitComparison(ctx)
    }

    override fun visitUnaryMinus(ctx: neoGPParser.UnaryMinusContext?): List<String> {
        return super.visitUnaryMinus(ctx)
    }

    override fun visitEquality(ctx: neoGPParser.EqualityContext?): List<String> {
        return super.visitEquality(ctx)
    }

    override fun visitLogicAnd(ctx: neoGPParser.LogicAndContext?): List<String> {
        val leftExpr = visit(ctx!!.expression(0)).first()
        val rightExpr = visit(ctx.expression(1)).first()
        val left = getBoolean(leftExpr)
        val right = getBoolean(rightExpr)

        return listOf((left && right).toString())
    }

    override fun visitIntLiteral(ctx: neoGPParser.IntLiteralContext?): List<String> {
        return super.visitIntLiteral(ctx)
    }

    override fun visitFPNumberLiteral(ctx: neoGPParser.FPNumberLiteralContext?): List<String> {
        return super.visitFPNumberLiteral(ctx)
    }

    override fun visitBooleanLiteral(ctx: neoGPParser.BooleanLiteralContext?): List<String> {
        return super.visitBooleanLiteral(ctx)
    }

    override fun visitIdentifier(ctx: neoGPParser.IdentifierContext?): List<String> {
        return super.visitIdentifier(ctx)
    }

    fun getBoolean(value: String): Boolean = when (value) {
        "true" -> true
        "false" -> false
        else -> value.toFloat() != 0F
    }
}

data class Variable(
    val name: String,
    var value: String?,
    val mutable: Boolean,
)