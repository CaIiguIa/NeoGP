package neoGP.antlr.parser

import neoGP.antlr.parser.model.neoGPBaseListener
import neoGP.antlr.parser.model.neoGPParser
import neoGP.antlr.parser.model.neoGPParser.AdditionContext
import neoGP.antlr.parser.model.neoGPParser.BooleanLiteralContext
import neoGP.antlr.parser.model.neoGPParser.ComparisonContext
import neoGP.antlr.parser.model.neoGPParser.EqualityContext
import neoGP.antlr.parser.model.neoGPParser.FPNumberLiteralContext
import neoGP.antlr.parser.model.neoGPParser.IdentifierContext
import neoGP.antlr.parser.model.neoGPParser.IntLiteralContext
import neoGP.antlr.parser.model.neoGPParser.LogicAndContext
import neoGP.antlr.parser.model.neoGPParser.LogicOrContext
import neoGP.antlr.parser.model.neoGPParser.MultiplicationContext
import neoGP.antlr.parser.model.neoGPParser.NegationContext
import neoGP.antlr.parser.model.neoGPParser.ParenthesizedExpressionContext
import neoGP.antlr.parser.model.neoGPParser.PrimaryExpressionContext
import neoGP.antlr.parser.model.neoGPParser.UnaryMinusContext
import neoGP.model.grammar.*
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.TerminalNode

class NeoGPParser : neoGPBaseListener() {
    private val individual = Individual()
    private val statements = ArrayDeque<Statement>()
    private val blocks = ArrayDeque<Block>()
    private val expressions = ArrayDeque<Expression>()

    fun parse(ctx: neoGPParser.ProgramContext?): Individual {
        enterProgram(ctx)

        return individual
    }

    override fun enterProgram(ctx: neoGPParser.ProgramContext?) {
        check(ctx != null) { "context cannot be null!" }

        ctx.statement().forEach {
            enterStatement(it)
            individual.statements.add(statements.removeLast())
        }
    }

    override fun exitProgram(ctx: neoGPParser.ProgramContext?) {
        super.exitProgram(ctx)
    }

    override fun enterStatement(ctx: neoGPParser.StatementContext?) {
        check(ctx != null) { "context cannot be null!" }

        when {
            ctx.loop() != null -> enterLoop(ctx.loop())
            ctx.if_() != null -> enterIf(ctx.if_())
            ctx.ifElse() != null -> enterIfElse(ctx.ifElse())
            ctx.`in`() != null -> enterIn(ctx.`in`())
            ctx.print() != null -> enterPrint(ctx.print())
            ctx.`var`() != null -> enterVar(ctx.`var`())
            ctx.varAssign() != null -> enterVarAssign(ctx.varAssign())
            ctx.const_() != null -> enterConst(ctx.const_())
        }
    }

    override fun exitStatement(ctx: neoGPParser.StatementContext?) {
        super.exitStatement(ctx)
    }

    override fun enterBlock(ctx: neoGPParser.BlockContext?) {
        check(ctx != null) { "context cannot be null!" }
        blocks.add(Block())

        ctx.statement()?.forEach {
            enterStatement(it)
            blocks.last().statements.add(statements.removeLast())
        }
    }

    override fun exitBlock(ctx: neoGPParser.BlockContext?) {
        super.exitBlock(ctx)
    }

    override fun enterLoop(ctx: neoGPParser.LoopContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression())
        enterBlock(ctx.block())

        statements.add(Loop(ctx.text!!, expressions.removeLast(), blocks.removeLast()))
    }

    override fun exitLoop(ctx: neoGPParser.LoopContext?) {
        super.exitLoop(ctx)
    }

    override fun enterIf(ctx: neoGPParser.IfContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression())
        enterBlock(ctx.block())

        statements.add(If(ctx.text, expressions.removeLast(), blocks.removeLast()))
    }

    override fun exitIf(ctx: neoGPParser.IfContext?) {
        super.exitIf(ctx)
    }

    override fun enterIfElse(ctx: neoGPParser.IfElseContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression())
        val exp = expressions.removeLast()
        enterBlock(ctx.block()[0])
        val block1 = blocks.removeLast()
        enterBlock(ctx.block()[1])
        val block2 = blocks.removeLast()

        statements.add(IfElse(ctx.text, exp, block1, block2))
    }

    override fun exitIfElse(ctx: neoGPParser.IfElseContext?) {
        super.exitIfElse(ctx)
    }

    override fun enterIn(ctx: neoGPParser.InContext?) {
        check(ctx != null) { "context cannot be null!" }
        statements.add(In(ctx.text, Id(ctx.ID().text)))
    }

    override fun exitIn(ctx: neoGPParser.InContext?) {
        super.exitIn(ctx)
    }

    override fun enterPrint(ctx: neoGPParser.PrintContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression())

        statements.add(Print(ctx.text, expressions.removeLast()))
    }

    override fun exitPrint(ctx: neoGPParser.PrintContext?) {
        super.exitPrint(ctx)
    }

    override fun enterVar(ctx: neoGPParser.VarContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression())

        statements.add(Var(ctx.text, Id(ctx.ID().text), expressions.removeLast()))
    }

    override fun exitVar(ctx: neoGPParser.VarContext?) {
        super.exitVar(ctx)
    }

    override fun enterVarAssign(ctx: neoGPParser.VarAssignContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression())

        statements.add(VarAssign(ctx.text, Id(ctx.ID().text), expressions.removeLast()))
    }

    override fun exitVarAssign(ctx: neoGPParser.VarAssignContext?) {
        super.exitVarAssign(ctx)
    }

    override fun enterConst(ctx: neoGPParser.ConstContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression())

        statements.add(Const(ctx.text, Id(ctx.ID().text), expressions.removeLast()))
    }

    override fun exitConst(ctx: neoGPParser.ConstContext?) {
        super.exitConst(ctx)
    }

    override fun enterParenthesizedExpression(ctx: ParenthesizedExpressionContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression())

        expressions.add(ExpressionParenthesis(ctx.text, expressions.removeLast()))
    }

    override fun exitParenthesizedExpression(ctx: ParenthesizedExpressionContext?) {
        super.exitParenthesizedExpression(ctx)
    }

    override fun enterLogicOr(ctx: LogicOrContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression()[0])
        val exp1 = expressions.removeLast()
        enterExpression(ctx.expression()[1])
        val exp2 = expressions.removeLast()

        expressions.add(Logical(ctx.text, exp1, exp2, Operator.OR))
    }

    override fun exitLogicOr(ctx: LogicOrContext?) {
        super.exitLogicOr(ctx)
    }

    override fun enterMultiplication(ctx: MultiplicationContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression()[0])
        val exp1 = expressions.removeLast()
        enterExpression(ctx.expression()[1])
        val exp2 = expressions.removeLast()
        val operator = if (ctx.op.text == "*")
            Operator.MULTIPLY
        else Operator.DIVIDE

        expressions.add(Mathematical(ctx.text, exp1, exp2, operator))
    }

    override fun exitMultiplication(ctx: MultiplicationContext?) {
        super.exitMultiplication(ctx)
    }

    override fun enterAddition(ctx: AdditionContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression()[0])
        val exp1 = expressions.removeLast()
        enterExpression(ctx.expression()[1])
        val exp2 = expressions.removeLast()
        val operator = if (ctx.op.text == "+")
            Operator.ADD
        else Operator.SUBTRACT

        expressions.add(Mathematical(ctx.text, exp1, exp2, operator))
    }

    override fun exitAddition(ctx: AdditionContext?) {
        super.exitAddition(ctx)
    }

    override fun enterNegation(ctx: NegationContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression())

        expressions.add(Negation(ctx.text, expressions.removeLast()))
    }

    override fun exitNegation(ctx: NegationContext?) {
        super.exitNegation(ctx)
    }

    override fun enterPrimaryExpression(ctx: PrimaryExpressionContext?) {
        enterPrimary(ctx?.primary())
    }

    override fun exitPrimaryExpression(ctx: PrimaryExpressionContext?) {
        super.exitPrimaryExpression(ctx)
    }

    override fun enterComparison(ctx: ComparisonContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression()[0])
        val exp1 = expressions.removeLast()
        enterExpression(ctx.expression()[1])
        val exp2 = expressions.removeLast()
        val operator = when (ctx.op.text) {
            "<" -> Operator.LOWER_THAN
            "<=" -> Operator.LOWER_EQUAL
            ">" -> Operator.GREATER_THAN
            else -> Operator.GREATER_EQUAL
        }

        expressions.add(Comparison(ctx.text, exp1, exp2, operator))
    }

    override fun exitComparison(ctx: ComparisonContext?) {
        super.exitComparison(ctx)
    }

    override fun enterUnaryMinus(ctx: UnaryMinusContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression())

        expressions.add(UnaryMinus(ctx.text, expressions.removeLast()))
    }

    override fun exitUnaryMinus(ctx: UnaryMinusContext?) {
        super.exitUnaryMinus(ctx)
    }

    override fun enterEquality(ctx: EqualityContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression()[0])
        val exp1 = expressions.removeLast()
        enterExpression(ctx.expression()[1])
        val exp2 = expressions.removeLast()
        val operator = if (ctx.op.text == "==")
            Operator.EQUAL
        else
            Operator.NOT_EQUAL

        expressions.add(Equality(ctx.text, exp1, exp2, operator))
    }

    override fun exitEquality(ctx: EqualityContext?) {
        super.exitEquality(ctx)
    }

    override fun enterLogicAnd(ctx: LogicAndContext?) {
        check(ctx != null) { "context cannot be null!" }

        enterExpression(ctx.expression()[0])
        val exp1 = expressions.removeLast()
        enterExpression(ctx.expression()[1])
        val exp2 = expressions.removeLast()

        expressions.add(Logical(ctx.text, exp1, exp2, Operator.AND))
    }

    override fun exitLogicAnd(ctx: LogicAndContext?) {
        super.exitLogicAnd(ctx)
    }

    override fun enterIdentifier(ctx: IdentifierContext?) {
        check(ctx != null) { "context cannot be null!" }
        expressions.add(Id(ctx.text))
    }

    override fun exitIdentifier(ctx: IdentifierContext?) {
        super.exitIdentifier(ctx)
    }

    override fun enterIntLiteral(ctx: IntLiteralContext?) {
        check(ctx != null) { "context cannot be null!" }
        expressions.add(IntNumberToken(ctx.text))
    }

    override fun exitIntLiteral(ctx: IntLiteralContext?) {
        super.exitIntLiteral(ctx)
    }

    override fun enterBooleanLiteral(ctx: BooleanLiteralContext?) {
        check(ctx != null) { "context cannot be null!" }
        expressions.add(BooleanToken(ctx.text))
    }

    override fun exitBooleanLiteral(ctx: BooleanLiteralContext?) {
        super.exitBooleanLiteral(ctx)
    }

    override fun enterFPNumberLiteral(ctx: FPNumberLiteralContext?) {
        check(ctx != null) { "context cannot be null!" }
        expressions.add(FloatNumberToken(ctx.text))
    }

    private fun enterExpression(ctx: neoGPParser.ExpressionContext?) {
        check(ctx != null) { "context cannot be null!" }

        when (ctx) {
            is ParenthesizedExpressionContext -> enterParenthesizedExpression(ctx)
            is UnaryMinusContext -> enterUnaryMinus(ctx)
            is MultiplicationContext -> enterMultiplication(ctx)
            is AdditionContext -> enterAddition(ctx)
            is ComparisonContext -> enterComparison(ctx)
            is NegationContext -> enterNegation(ctx)
            is EqualityContext -> enterEquality(ctx)
            is LogicAndContext -> enterLogicAnd(ctx)
            is LogicOrContext -> enterLogicOr(ctx)
            is PrimaryExpressionContext -> enterPrimaryExpression(ctx)
        }
    }

    private fun enterPrimary(ctx: neoGPParser.PrimaryContext?) {
        check(ctx != null) { "context cannot be null!" }

        when (ctx) {
            is IdentifierContext -> enterIdentifier(ctx)
            is BooleanLiteralContext -> enterBooleanLiteral(ctx)
            is IntLiteralContext -> enterIntLiteral(ctx)
            is FPNumberLiteralContext -> enterFPNumberLiteral(ctx)
        }
    }

    override fun enterEveryRule(ctx: ParserRuleContext?) {
        super.enterEveryRule(ctx)
    }

    override fun exitEveryRule(ctx: ParserRuleContext?) {
        super.exitEveryRule(ctx)
    }

    override fun visitTerminal(node: TerminalNode?) {
        super.visitTerminal(node)
    }

    override fun visitErrorNode(node: ErrorNode?) {
        super.visitErrorNode(node)
    }
}