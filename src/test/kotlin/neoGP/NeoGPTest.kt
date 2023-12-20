package neoGP

import neoGP.antlr.parser.NeoGPVisitor
import neoGP.model.Population
import neoGP.model.grammar.Individual
import org.junit.jupiter.api.BeforeEach
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

class NeoGPTest {
    @BeforeEach
    fun setUp() {
        NeoProperties.population = Population.generatePopulation()
        NeoProperties.inputsOutputs = listOf()
        NeoProperties.fitnessFunction = null
        NeoProperties.bestFitness = Int.MAX_VALUE
        NeoProperties.bestIndividual = null
        NeoProperties.BEST_FITNESS_THRESHOLD = 5
        NeoProperties.CONVERT_FLOAT_TO_INT = true
        NeoProperties.MAX_INT_VALUE = 100
        NeoProperties.MAX_FLOAT_VALUE = 100
    }

    @Test
    fun test11A() {
        //1.1.A Program powinien wygenerować na wyjściu (na dowolnej pozycji w danych wyjściowych) liczbę 1. Poza liczbą 1 może też zwrócić inne liczby.
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/11A.dat"
        NeoProperties.BEST_FITNESS_THRESHOLD = 0

        testOutputContains(path, listOf("1", "1.0"))
    }

    @Test
    fun test11B() {
        //1.1.B Program powinien wygenerować na wyjściu (na dowolnej pozycji w danych wyjściowych) liczbę 789. Poza liczbą 789 może też zwrócić inne liczby.
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/11B.dat"
//        NeoProperties.BEST_FITNESS_THRESHOLD = 0
        NeoProperties.MAX_INT_VALUE = 1000
        NeoProperties.MAX_FLOAT_VALUE = 1000

        testOutputContains(path, listOf("789", "789.0"))
    }

    @Test
    fun test11C() {
        //1.1.C Program powinien wygenerować na wyjściu (na dowolnej pozycji w danych wyjściowych) liczbę 31415. Poza liczbą 31415 może też zwrócić inne liczby.
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/11C.dat"
        NeoProperties.BEST_FITNESS_THRESHOLD = 0
        NeoProperties.MAX_INT_VALUE = 100000
        NeoProperties.MAX_FLOAT_VALUE = 100000

        testOutputContains(path, listOf("31415", "31415.0"))
    }

    @Test
    fun test11D() {
        //1.1.D Program powinien wygenerować na pierwszej pozycji na wyjściu liczbę 1. Poza liczbą 1 może też zwrócić inne liczby.
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/11D.dat"
        NeoProperties.MAX_INT_VALUE = 100
        NeoProperties.MAX_FLOAT_VALUE = 100

        testOutputContains(path, listOf("1", "1.0"))
    }

    @Test
    fun test11E() {
        //1.1.E Program powinien wygenerować na pierwszej pozycji na wyjściu liczbę 789. Poza liczbą 789 może też zwrócić inne liczby.
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/11E.dat"
        NeoProperties.MAX_INT_VALUE = 1000
        NeoProperties.MAX_FLOAT_VALUE = 1000

        testOutputContains(path, listOf("789", "789.0"))
    }

    @Test
    fun test11F() {
        //1.1.F Program powinien wygenerować na wyjściu liczbę jako jedyną liczbę 1. Poza liczbą 1 NIE powinien nic więcej wygenerować.
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/11F.dat"


        testOutputContains(path, listOf("1", "1.0"))
    }

    private fun testOutputContains(filePath: String, expectedValues: List<String>) {
        try {
            val data = NeoGP.loadParams(filePath)
            NeoProperties.inputsOutputs = data.params
            NeoProperties.fitnessFunction = data.fitnessFunction
            NeoGP.evolve()
            val outputs = getOutputsFor(NeoProperties.bestIndividual!!, data.params)

            assertTrue(contains(outputs, expectedValues), "program does not find the best solution")
        } catch (e: Exception) {
            println("Program does not run properly, following exception has been thrown:")
            e.printStackTrace()
            fail()
        }
    }

    private fun getOutputsFor(individual: Individual, ios: List<NeoGP.Params>): List<List<String>> {
        return ios.map { io ->
            val visitor = NeoGPVisitor(io.inputs)
            val tree = NeoGP.getParseTreeForIndividual(individual.toString())

            visitor.run(tree).first
        }
    }

    private fun contains(outputs: List<List<String>>, expectedValues: List<String>): Boolean =
        if (NeoProperties.CONVERT_FLOAT_TO_INT)
            outputs.all { out ->
                val o = out.map { it.toFloat().toInt() }
                expectedValues.any { value ->
                    value.toFloat().toInt() in o
                }
            }
        else
            outputs.all { out ->
                val o = out.map { it.toFloat() }
                expectedValues.any { value ->
                    value.toFloat() in o
                }
            }

}