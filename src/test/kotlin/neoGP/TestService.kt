package neoGP

import neoGP.antlr.parser.NeoGPVisitor
import neoGP.model.grammar.Individual
import kotlin.math.abs
import kotlin.test.assertTrue
import kotlin.test.fail

class TestService {
    companion object {

        fun testOutputContains(filePath: String, expectedValues: List<String>) {
            try {
                val data = NeoGP.loadParams(filePath)
                NeoProperties.inputsOutputs = data.params
                NeoProperties.fitnessFunction = data.fitnessFunction
                NeoGP.evolve()
                val outputs = getOutputsFor(NeoProperties.bestIndividual!!, data.params)

                println("\ninput -> (expected output | output)")
                outputs.forEachIndexed { idx, outs ->
                    println(data.params[idx].inputs.joinToString() + " -> ( " + data.params[idx].outputs.joinToString() + " | " + outs.joinToString() + " )")
                }

                assertTrue(contains(outputs, expectedValues), "program does not find the best solution")
            } catch (e: Exception) {
                println("Program does not run properly, following exception has been thrown:")
                e.printStackTrace()
                fail()
            }
        }

        fun getOutputsFor(individual: Individual, ios: List<NeoGP.Params>): List<List<String>> {
            return ios.map { io ->
                val visitor = NeoGPVisitor(io.inputs)
                val tree = NeoGP.getParseTreeForIndividual(individual.toString())

                visitor.run(tree).first
            }
        }

        fun contains(outputs: List<List<String>>, expectedValues: List<String>): Boolean =
            if (NeoProperties.CONVERT_FLOAT_TO_INT)
                outputs.all { out ->
                    val o = out.map { it.toFloat().toInt() }
                    val anyContains = expectedValues.any { value ->
                        value.toFloat().toInt() in o
                    }
                    if (!anyContains) println("Assertion error: not all values from $o are present in $expectedValues")

                    anyContains
                }
            else
                outputs.all { out ->
                    val o = out.map { it.toFloat() }
                    val anyContains = expectedValues.any { value ->
                        value.toFloat() in o
                    }
                    if (!anyContains) println("Assertion error: not all values from $o are present in [$expectedValues]")

                    anyContains
                }


        fun testOutputExact(filePath: String, threshold: Double? = null) {
            try {
                val data = NeoGP.loadParams(filePath)
                NeoProperties.inputsOutputs = data.params
                NeoProperties.fitnessFunction = data.fitnessFunction
                NeoGP.evolve()
                val outputs = getOutputsFor(NeoProperties.bestIndividual!!, data.params)

                println("\ninput -> (expected output | output)")
                outputs.forEachIndexed { idx, outs ->
                    println(data.params[idx].inputs.joinToString() + " -> ( " + data.params[idx].outputs.joinToString() + " | " + outs.joinToString() + " )")
                }

                assertTrue(
                    threshold?.let { containsExactWithThreshold(outputs, data.params.map { it.outputs }, threshold) }
                        ?: containsExact(outputs, data.params.map { it.outputs }),
                    "program does not find the best solution"
                )
            } catch (e: Exception) {
                println("Program does not run properly, following exception has been thrown:")
                e.printStackTrace()
                fail()
            }
        }

        fun containsExact(outputs: List<List<String>>, expectedValues: List<List<String>>): Boolean {
            if (outputs.size != expectedValues.size)
                return false
            if (NeoProperties.CONVERT_FLOAT_TO_INT)
                outputs.forEachIndexed { idx, _ ->
                    val out = outputs[idx].map { it.toFloat().toInt() }
                    val anyNotPresent = expectedValues[idx].any {
                        it.toFloat().toInt() !in out
                    }
                    if (anyNotPresent) {
                        return false
                    }
                }
            else
                outputs.forEachIndexed { idx, _ ->
                    val out = outputs[idx].map { it.toFloat() }
                    val anyNotPresent = expectedValues[idx].any {
                        it.toFloat() !in out
                    }
                    if (anyNotPresent) {
                        return false
                    }
                }

            return true
        }

        fun containsExactWithThreshold(
            outputs: List<List<String>>,
            expectedValues: List<List<String>>,
            threshold: Double
        ): Boolean {
            if (outputs.size != expectedValues.size)
                return false
            if (outputs.isEmpty())
                return true

            for (i in outputs.indices)
                for (j in outputs[0].indices)
                    if (abs(outputs[i][j].toDouble() - expectedValues[i][j].toDouble()) > threshold)
                        return false

            return true
        }
    }

}