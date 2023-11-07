// Generated from neoGP.g4 by ANTLR 4.12.0
package neoGP.antlr.parser.model;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link neoGPParser}.
 */
public interface neoGPListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link neoGPParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(neoGPParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link neoGPParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(neoGPParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link neoGPParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(neoGPParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link neoGPParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(neoGPParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link neoGPParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(neoGPParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link neoGPParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(neoGPParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link neoGPParser#loop}.
	 * @param ctx the parse tree
	 */
	void enterLoop(neoGPParser.LoopContext ctx);
	/**
	 * Exit a parse tree produced by {@link neoGPParser#loop}.
	 * @param ctx the parse tree
	 */
	void exitLoop(neoGPParser.LoopContext ctx);
	/**
	 * Enter a parse tree produced by {@link neoGPParser#if}.
	 * @param ctx the parse tree
	 */
	void enterIf(neoGPParser.IfContext ctx);
	/**
	 * Exit a parse tree produced by {@link neoGPParser#if}.
	 * @param ctx the parse tree
	 */
	void exitIf(neoGPParser.IfContext ctx);
	/**
	 * Enter a parse tree produced by {@link neoGPParser#ifElse}.
	 * @param ctx the parse tree
	 */
	void enterIfElse(neoGPParser.IfElseContext ctx);
	/**
	 * Exit a parse tree produced by {@link neoGPParser#ifElse}.
	 * @param ctx the parse tree
	 */
	void exitIfElse(neoGPParser.IfElseContext ctx);
	/**
	 * Enter a parse tree produced by {@link neoGPParser#in}.
	 * @param ctx the parse tree
	 */
	void enterIn(neoGPParser.InContext ctx);
	/**
	 * Exit a parse tree produced by {@link neoGPParser#in}.
	 * @param ctx the parse tree
	 */
	void exitIn(neoGPParser.InContext ctx);
	/**
	 * Enter a parse tree produced by {@link neoGPParser#print}.
	 * @param ctx the parse tree
	 */
	void enterPrint(neoGPParser.PrintContext ctx);
	/**
	 * Exit a parse tree produced by {@link neoGPParser#print}.
	 * @param ctx the parse tree
	 */
	void exitPrint(neoGPParser.PrintContext ctx);
	/**
	 * Enter a parse tree produced by {@link neoGPParser#var}.
	 * @param ctx the parse tree
	 */
	void enterVar(neoGPParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by {@link neoGPParser#var}.
	 * @param ctx the parse tree
	 */
	void exitVar(neoGPParser.VarContext ctx);
	/**
	 * Enter a parse tree produced by {@link neoGPParser#varAssign}.
	 * @param ctx the parse tree
	 */
	void enterVarAssign(neoGPParser.VarAssignContext ctx);
	/**
	 * Exit a parse tree produced by {@link neoGPParser#varAssign}.
	 * @param ctx the parse tree
	 */
	void exitVarAssign(neoGPParser.VarAssignContext ctx);
	/**
	 * Enter a parse tree produced by {@link neoGPParser#const}.
	 * @param ctx the parse tree
	 */
	void enterConst(neoGPParser.ConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link neoGPParser#const}.
	 * @param ctx the parse tree
	 */
	void exitConst(neoGPParser.ConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpression(neoGPParser.ParenthesizedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpression(neoGPParser.ParenthesizedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicOr}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicOr(neoGPParser.LogicOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicOr}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicOr(neoGPParser.LogicOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Multiplication}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplication(neoGPParser.MultiplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Multiplication}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplication(neoGPParser.MultiplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Addition}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAddition(neoGPParser.AdditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Addition}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAddition(neoGPParser.AdditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNegation(neoGPParser.NegationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNegation(neoGPParser.NegationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PrimaryExpression}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpression(neoGPParser.PrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PrimaryExpression}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpression(neoGPParser.PrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Comparison}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterComparison(neoGPParser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Comparison}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitComparison(neoGPParser.ComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryMinus}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryMinus(neoGPParser.UnaryMinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryMinus}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryMinus(neoGPParser.UnaryMinusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Equality}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterEquality(neoGPParser.EqualityContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Equality}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitEquality(neoGPParser.EqualityContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicAnd}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLogicAnd(neoGPParser.LogicAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicAnd}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLogicAnd(neoGPParser.LogicAndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Identifier}
	 * labeled alternative in {@link neoGPParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(neoGPParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Identifier}
	 * labeled alternative in {@link neoGPParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(neoGPParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NumberLiteral}
	 * labeled alternative in {@link neoGPParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterNumberLiteral(neoGPParser.NumberLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NumberLiteral}
	 * labeled alternative in {@link neoGPParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitNumberLiteral(neoGPParser.NumberLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link neoGPParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(neoGPParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link neoGPParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(neoGPParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link neoGPParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(neoGPParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link neoGPParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(neoGPParser.StringLiteralContext ctx);
}