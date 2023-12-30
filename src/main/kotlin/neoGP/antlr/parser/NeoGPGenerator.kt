package neoGP.antlr.parser

import neoGP.NeoProperties
import neoGP.model.grammar.*
import java.lang.UnsupportedOperationException
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

class NeoGPGenerator() {
    companion object {

        private var variables: MutableList<Variable> = mutableListOf()
        private var variableIdx: Int = 0
        private var depth = 1

        fun randomIndividual(): Individual {
            variables = mutableListOf()
            variableIdx = 0
            depth = 1

            val individual = Individual()
            for (i in 1..NeoProperties.INIT_INSTRUCTION_NUMBER)
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

        private fun randomBlock(): Block {
            val lastVarBefore = variables.size
            val block = Block(mutableListOf())
            if (depth == NeoProperties.MAX_DEPTH)
                return block

            depth += 1
            val instructionNumber =
                Random.nextInt(NeoProperties.MIN_INSTRUCTION_BLOCK_SIZE, NeoProperties.MAX_INSTRUCTION_BLOCK_SIZE + 1)

            for (i in 1..instructionNumber)
                block.statements.add(generateRandomStatement())

            val lastVarAfter = variables.size
            if (lastVarAfter != lastVarBefore) {
                val toRemove = variables.filterIndexed { index, _ -> index >= lastVarBefore }
                variables.removeAll(toRemove) // prevent using variables not declared in this scope
            }
            depth -= 1
            return block
        }

        private fun randomIfElse(): IfElse =
            IfElse("", randomBooleanExpression(), randomBlock(), randomBlock())

        private fun randomLoop(): Loop {
            val lastVarBefore = variables.size
            val loop = Loop("", randomBooleanExpression(), randomBlock())
            val lastVarAfter = variables.size
            if (lastVarAfter != lastVarBefore) {
                val toRemove = variables.filterIndexed { index, _ -> index >= lastVarBefore }
                variables.removeAll(toRemove)
            }
            return loop
        }

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
            val random = Random.nextInt(12)

            return when {
                random == 0 -> randomLogical()
                random == 1 -> randomNegation()
                random == 2 -> randomComparison()
                random == 3 -> randomEquality()
                random in listOf(4, 5, 6, 7) && variables.any { it.value != null } -> randomVariable(
                    VariableType.BOOL
                )

                else -> randomBooleanPrimary()
            }
        }

        private fun randomIntExpression(): Expression {
            val random = Random.nextInt(6)

            return when {
                random == 0 -> randomMathematical(VariableType.INT)
                random == 1 -> randomUnaryMinus(VariableType.INT)
                random in listOf(2, 3) && variables.any { it.value != null } -> randomVariable(
                    VariableType.INT
                )

                else -> randomIntPrimary()
            }
        }

        private fun randomFloatExpression(): Expression {
            val random = Random.nextInt(6)

            return when {
                random == 0 -> randomMathematical(VariableType.FLOAT)
                random == 1 -> randomUnaryMinus(VariableType.FLOAT)
                random in listOf(2, 3) && variables.any { it.value != null } -> randomVariable(
                    VariableType.FLOAT
                )

                else -> randomFloatPrimary()
            }
        }

        private fun randomBooleanPrimary() = when (Random.nextInt(2)) {
            0 -> BooleanToken("true")
            else -> BooleanToken("false")
        }

        private fun randomIntPrimary() =
            IntNumberToken("${Random.nextInt(NeoProperties.MAX_INT_VALUE)}")

        private fun randomFloatPrimary(): FloatNumberToken {
            val number = Random.nextDouble() * NeoProperties.MAX_FLOAT_VALUE
            val roundedNumber = BigDecimal(number).setScale(8, RoundingMode.HALF_EVEN)
            return FloatNumberToken("$roundedNumber")
        }

        private fun randomVariable(ofType: VariableType) =
            Id(variables.filter { it.value != null && it.type == ofType }
                .randomOrNull()?.name
                ?: variables.randomOrNull()?.name
                ?: randomPrimaryValue()
            )

        private fun randomPrimaryValue(): String {
            val random = Random.nextInt(3)

            return when (random) {
                0 -> randomBooleanPrimary().value
                1 -> randomIntPrimary().value
                else -> randomFloatPrimary().value
            }
        }

    }
}
