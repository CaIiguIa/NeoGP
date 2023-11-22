package neoGP.antlr.parser

import neoGP.model.grammar.*
import java.lang.UnsupportedOperationException
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

class NeoGPGenerator() {
    companion object {


        private var variables: MutableList<Variable> = mutableListOf()
        private var variableIdx: Int = 0


        fun randomIndividual(size: Int): Individual {
            variables = mutableListOf()
            variableIdx = 0

            val individual = Individual()
            for (i in 1..size)
                individual.statements.add(generateRandomStatement())

            return individual
        }

        fun randomStatement(): Statement {
            variables = mutableListOf()
            variableIdx = 0

            return generateRandomStatement()
        }

        private fun generateRandomStatement(): Statement {
            val random = Random.nextInt(8)

            return when {
                random == 0 -> randomLoop()
                random == 1 -> randomIf()
                random == 2 -> randomIfElse()
                random == 3 && variables.any { it.isMutable } -> randomIn()
                random == 4 -> randomPrint()
                random == 5 -> randomConst()
                random == 6 && variables.any { it.isMutable } -> randomVarAssign()
                else -> randomVar()
            }
        }

        private fun randomBlock(): Block =
            Block(mutableListOf(generateRandomStatement()))

        private fun randomIfElse(): IfElse =
            IfElse("", randomBooleanExpression(), randomBlock(), randomBlock())

        private fun randomLoop(): Loop =
            Loop("", randomBooleanExpression(), randomBlock())

        private fun randomIf(): If =
            If("", randomBooleanExpression(), randomBlock())

        private fun randomIn(): In {
            val variable = variables.filter { it.isMutable }.random()
            return In("", Id(variable.name))
        }

        private fun randomPrint(): Print =
            Print("", randomExpression().first)

        private fun randomVar(): Var {
            val newId = "variable$variableIdx"
            variableIdx += 1
            return when (Random.nextInt(2)) {
                0 -> {
                    val expr = randomExpression()
                    variables.add(Variable(newId, "", true, expr.second))
                    Var("", Id(newId), expr.first)
                }

                else -> {
                    variables.add(Variable(newId, null, true))
                    Var("", Id(newId), null)
                }
            }
        }

        private fun randomVarAssign(): VarAssign {
            val variable = variables.filter { it.isMutable }.random()
            variable.value = ""

            when (variable.type) {
                null -> {
                    val expr = randomExpression()
                    variable.type = expr.second
                    return VarAssign("", Id(variable.name), expr.first)
                }

                VariableType.BOOL -> {
                    val expr = randomBooleanExpression()
                    return VarAssign("", Id(variable.name), expr)
                }

                VariableType.INT -> {
                    val expr = randomIntExpression()
                    return VarAssign("", Id(variable.name), expr)
                }

                VariableType.FLOAT -> {
                    val expr = randomFloatExpression()
                    return VarAssign("", Id(variable.name), expr)
                }
            }
        }

        private fun randomConst(): Const {
            val newId = "variable$variableIdx"
            variableIdx += 1
            val expr = randomExpression()
            variables.add(Variable(newId, "", false, expr.second))
            return Const("", Id(newId), expr.first)
        }

        private fun randomLogical() =
            ExpressionParenthesis(
                "",
                Logical("", randomBooleanExpression(), randomBooleanExpression(), Logical.operands.random())
            )

        private fun randomMathematical(type: VariableType?): ExpressionParenthesis {
            val expr = when (type) {
                null -> Mathematical(
                    "",
                    randomNumericExpression().first,
                    randomNumericExpression().first,
                    Mathematical.operands.random()
                )

                VariableType.INT -> Mathematical(
                    "",
                    randomIntExpression(),
                    randomIntExpression(),
                    Mathematical.operands.random()
                )

                VariableType.FLOAT -> Mathematical(
                    "",
                    randomFloatExpression(),
                    randomFloatExpression(),
                    Mathematical.operands.random()
                )

                else -> throw UnsupportedOperationException("Mathematical cannot consist of boolean values")
            }

            return ExpressionParenthesis("", expr)
        }


        private fun randomNegation() =
            ExpressionParenthesis("", Negation("", randomBooleanExpression()))

        private fun randomComparison() =
            ExpressionParenthesis(
                "", Comparison(
                    "",
                    randomNumericExpression().first,
                    randomNumericExpression().first,
                    Comparison.operands.random()
                )
            )

        private fun randomUnaryMinus(type: VariableType?): ExpressionParenthesis {
            val expr = when (type) {
                null -> UnaryMinus("", randomNumericExpression().first)
                VariableType.INT -> UnaryMinus("", randomIntExpression())
                VariableType.FLOAT -> UnaryMinus("", randomFloatExpression())
                else -> throw UnsupportedOperationException("Unary minus operation cannot consist of boolean value")
            }

            return ExpressionParenthesis("", expr)
        }


        private fun randomEquality(): ExpressionParenthesis {
            val expr = when (Random.nextInt(3)) {
                0 -> {
                    Equality("", randomBooleanExpression(), randomBooleanExpression(), Equality.operands.random())
                }

                1 -> {
                    Equality("", randomIntExpression(), randomIntExpression(), Equality.operands.random())
                }

                else -> {
                    Equality("", randomFloatExpression(), randomFloatExpression(), Equality.operands.random())
                }
            }

            return ExpressionParenthesis("", expr)
        }

        private fun randomExpression(): Pair<Expression, VariableType> = when (Random.nextInt(3)) {
            0 -> Pair(randomBooleanExpression(), VariableType.BOOL)
            1 -> Pair(randomIntExpression(), VariableType.INT)
            else -> Pair(randomFloatExpression(), VariableType.FLOAT)
        }

        private fun randomNumericExpression(): Pair<Expression, VariableType> = when (Random.nextInt(2)) {
            0 -> Pair(randomIntExpression(), VariableType.INT)
            else -> Pair(randomFloatExpression(), VariableType.FLOAT)
        }

        private fun randomBooleanExpression(): Expression {
            val random = Random.nextInt(10)

            return when {
                random == 0 -> randomLogical()
                random == 1 -> randomNegation()
                random == 2 -> randomComparison()
                random == 3 -> randomEquality()
                random == 4 && variables.any { it.type == VariableType.BOOL } -> randomVariable(VariableType.BOOL)
                else -> randomBooleanPrimary()
            }
        }

        private fun randomIntExpression(): Expression {
            val random = Random.nextInt(6)

            return when {
                random == 0 -> randomMathematical(VariableType.INT)
                random == 1 -> randomUnaryMinus(VariableType.INT)
                random == 2 && variables.any { it.type == VariableType.INT } -> randomVariable(VariableType.INT)
                else -> randomIntPrimary()
            }
        }

        private fun randomFloatExpression(): Expression {
            val random = Random.nextInt(6)

            return when {
                random == 0 -> randomMathematical(VariableType.FLOAT)
                random == 1 -> randomUnaryMinus(VariableType.FLOAT)
                random == 2 && variables.any { it.type == VariableType.FLOAT } -> randomVariable(VariableType.FLOAT)
                else -> randomFloatPrimary()
            }
        }

        private fun randomPrimary(): Pair<Primary, VariableType> = when (Random.nextInt(3)) {
            0 -> Pair(randomBooleanPrimary(), VariableType.BOOL)
            1 -> Pair(randomIntPrimary(), VariableType.INT)
            else -> Pair(randomFloatPrimary(), VariableType.FLOAT)
        }

        private fun randomBooleanPrimary() = when (Random.nextInt(2)) {
            0 -> BooleanToken("true")
            else -> BooleanToken("false")
        }

        private fun randomIntPrimary() =
            IntNumberToken("${Random.nextInt(1000)}")

        private fun randomFloatPrimary(): FloatNumberToken {
            val number = Random.nextDouble() * 1000
            val roundedNumber = BigDecimal(number).setScale(8, RoundingMode.HALF_EVEN)
            return FloatNumberToken("$roundedNumber")
        }

        private fun randomVariable(ofType: VariableType) =
            Id(variables.first { it.type == ofType }.name)

    }
}
