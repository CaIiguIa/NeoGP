package neoGP.antlr.parser

import neoGP.antlr.parser.model.neoGPBaseListener
import neoGP.antlr.parser.model.neoGPParser
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.TerminalNode

class NeoGPParser: neoGPBaseListener() {
    override fun enterProgram(ctx: neoGPParser.ProgramContext?) {
        super.enterProgram(ctx)
    }

    override fun exitProgram(ctx: neoGPParser.ProgramContext?) {
        super.exitProgram(ctx)
    }

    override fun enterStatement(ctx: neoGPParser.StatementContext?) {
        super.enterStatement(ctx)
    }

    override fun exitStatement(ctx: neoGPParser.StatementContext?) {
        super.exitStatement(ctx)
    }

    override fun enterBlock(ctx: neoGPParser.BlockContext?) {
        super.enterBlock(ctx)
    }

    override fun exitBlock(ctx: neoGPParser.BlockContext?) {
        super.exitBlock(ctx)
    }

    override fun enterLoop(ctx: neoGPParser.LoopContext?) {
        super.enterLoop(ctx)
    }

    override fun exitLoop(ctx: neoGPParser.LoopContext?) {
        super.exitLoop(ctx)
    }

    override fun enterIf(ctx: neoGPParser.IfContext?) {
        super.enterIf(ctx)
    }

    override fun exitIf(ctx: neoGPParser.IfContext?) {
        super.exitIf(ctx)
    }

    override fun enterIfElse(ctx: neoGPParser.IfElseContext?) {
        super.enterIfElse(ctx)
    }

    override fun exitIfElse(ctx: neoGPParser.IfElseContext?) {
        super.exitIfElse(ctx)
    }

    override fun enterIn(ctx: neoGPParser.InContext?) {
        super.enterIn(ctx)
    }

    override fun exitIn(ctx: neoGPParser.InContext?) {
        super.exitIn(ctx)
    }

    override fun enterPrint(ctx: neoGPParser.PrintContext?) {
        super.enterPrint(ctx)
    }

    override fun exitPrint(ctx: neoGPParser.PrintContext?) {
        super.exitPrint(ctx)
    }

    override fun enterVar(ctx: neoGPParser.VarContext?) {
        super.enterVar(ctx)
    }

    override fun exitVar(ctx: neoGPParser.VarContext?) {
        super.exitVar(ctx)
    }

    override fun enterVarAssign(ctx: neoGPParser.VarAssignContext?) {
        super.enterVarAssign(ctx)
    }

    override fun exitVarAssign(ctx: neoGPParser.VarAssignContext?) {
        super.exitVarAssign(ctx)
    }

    override fun enterConst(ctx: neoGPParser.ConstContext?) {
        super.enterConst(ctx)
    }

    override fun exitConst(ctx: neoGPParser.ConstContext?) {
        super.exitConst(ctx)
    }

    override fun enterParenthesizedExpression(ctx: neoGPParser.ParenthesizedExpressionContext?) {
        super.enterParenthesizedExpression(ctx)
    }

    override fun exitParenthesizedExpression(ctx: neoGPParser.ParenthesizedExpressionContext?) {
        super.exitParenthesizedExpression(ctx)
    }

    override fun enterLogicOr(ctx: neoGPParser.LogicOrContext?) {
        super.enterLogicOr(ctx)
    }

    override fun exitLogicOr(ctx: neoGPParser.LogicOrContext?) {
        super.exitLogicOr(ctx)
    }

    override fun enterMultiplication(ctx: neoGPParser.MultiplicationContext?) {
        super.enterMultiplication(ctx)
    }

    override fun exitMultiplication(ctx: neoGPParser.MultiplicationContext?) {
        super.exitMultiplication(ctx)
    }

    override fun enterAddition(ctx: neoGPParser.AdditionContext?) {
        super.enterAddition(ctx)
    }

    override fun exitAddition(ctx: neoGPParser.AdditionContext?) {
        super.exitAddition(ctx)
    }

    override fun enterNegation(ctx: neoGPParser.NegationContext?) {
        super.enterNegation(ctx)
    }

    override fun exitNegation(ctx: neoGPParser.NegationContext?) {
        super.exitNegation(ctx)
    }

    override fun enterPrimaryExpression(ctx: neoGPParser.PrimaryExpressionContext?) {
        super.enterPrimaryExpression(ctx)
    }

    override fun exitPrimaryExpression(ctx: neoGPParser.PrimaryExpressionContext?) {
        super.exitPrimaryExpression(ctx)
    }

    override fun enterComparison(ctx: neoGPParser.ComparisonContext?) {
        super.enterComparison(ctx)
    }

    override fun exitComparison(ctx: neoGPParser.ComparisonContext?) {
        super.exitComparison(ctx)
    }

    override fun enterUnaryMinus(ctx: neoGPParser.UnaryMinusContext?) {
        super.enterUnaryMinus(ctx)
    }

    override fun exitUnaryMinus(ctx: neoGPParser.UnaryMinusContext?) {
        super.exitUnaryMinus(ctx)
    }

    override fun enterEquality(ctx: neoGPParser.EqualityContext?) {
        super.enterEquality(ctx)
    }

    override fun exitEquality(ctx: neoGPParser.EqualityContext?) {
        super.exitEquality(ctx)
    }

    override fun enterLogicAnd(ctx: neoGPParser.LogicAndContext?) {
        super.enterLogicAnd(ctx)
    }

    override fun exitLogicAnd(ctx: neoGPParser.LogicAndContext?) {
        super.exitLogicAnd(ctx)
    }

    override fun enterIdentifier(ctx: neoGPParser.IdentifierContext?) {
        super.enterIdentifier(ctx)
    }

    override fun exitIdentifier(ctx: neoGPParser.IdentifierContext?) {
        super.exitIdentifier(ctx)
    }

    override fun enterNumberLiteral(ctx: neoGPParser.NumberLiteralContext?) {
        super.enterNumberLiteral(ctx)
    }

    override fun exitNumberLiteral(ctx: neoGPParser.NumberLiteralContext?) {
        super.exitNumberLiteral(ctx)
    }

    override fun enterBooleanLiteral(ctx: neoGPParser.BooleanLiteralContext?) {
        super.enterBooleanLiteral(ctx)
    }

    override fun exitBooleanLiteral(ctx: neoGPParser.BooleanLiteralContext?) {
        super.exitBooleanLiteral(ctx)
    }

    override fun enterStringLiteral(ctx: neoGPParser.StringLiteralContext?) {
        super.enterStringLiteral(ctx)
    }

    override fun exitStringLiteral(ctx: neoGPParser.StringLiteralContext?) {
        super.exitStringLiteral(ctx)
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