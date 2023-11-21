// Generated from neoGP.g4 by ANTLR 4.12.0
package neoGP.antlr.parser.model;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link neoGPParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface neoGPVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link neoGPParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(neoGPParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link neoGPParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(neoGPParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link neoGPParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(neoGPParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link neoGPParser#ifElse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfElse(neoGPParser.IfElseContext ctx);
	/**
	 * Visit a parse tree produced by {@link neoGPParser#loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoop(neoGPParser.LoopContext ctx);
	/**
	 * Visit a parse tree produced by {@link neoGPParser#if}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf(neoGPParser.IfContext ctx);
	/**
	 * Visit a parse tree produced by {@link neoGPParser#in}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIn(neoGPParser.InContext ctx);
	/**
	 * Visit a parse tree produced by {@link neoGPParser#print}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrint(neoGPParser.PrintContext ctx);
	/**
	 * Visit a parse tree produced by {@link neoGPParser#var}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar(neoGPParser.VarContext ctx);
	/**
	 * Visit a parse tree produced by {@link neoGPParser#varAssign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarAssign(neoGPParser.VarAssignContext ctx);
	/**
	 * Visit a parse tree produced by {@link neoGPParser#const}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConst(neoGPParser.ConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpression(neoGPParser.ParenthesizedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicOr}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicOr(neoGPParser.LogicOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Multiplication}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplication(neoGPParser.MultiplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Addition}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddition(neoGPParser.AdditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimaryExpression}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpression(neoGPParser.PrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegation(neoGPParser.NegationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Comparison}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparison(neoGPParser.ComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryMinus}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryMinus(neoGPParser.UnaryMinusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Equality}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquality(neoGPParser.EqualityContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicAnd}
	 * labeled alternative in {@link neoGPParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicAnd(neoGPParser.LogicAndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntLiteral}
	 * labeled alternative in {@link neoGPParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntLiteral(neoGPParser.IntLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FPNumberLiteral}
	 * labeled alternative in {@link neoGPParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFPNumberLiteral(neoGPParser.FPNumberLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link neoGPParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLiteral(neoGPParser.BooleanLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Identifier}
	 * labeled alternative in {@link neoGPParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(neoGPParser.IdentifierContext ctx);
}