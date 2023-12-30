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
    }

    @Test
    fun test11A() {
        //1.1.A Program powinien wygenerować na wyjściu (na dowolnej pozycji w danych wyjściowych) liczbę 1. Poza liczbą 1 może też zwrócić inne liczby.
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/11A.dat"

        testOutputContains(path, listOf("1", "1.0"))
    }

    @Test
    fun test11B() {
        //1.1.B Program powinien wygenerować na wyjściu (na dowolnej pozycji w danych wyjściowych) liczbę 789. Poza liczbą 789 może też zwrócić inne liczby.
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/11B.dat"

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

    @Test
    fun test12A() {
        //1.2.A Program powinien odczytać dwie pierwsze liczy z wejścia i zwrócić na wyjściu (jedynie) ich sumę. Na wejściu mogą być tylko całkowite liczby dodatnie w zakresie [0,9]
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/12A.dat"


        testOutputExact(path)
    }

    @Test
    fun test12B() {
        //1.2.B Program powinien odczytać dwie pierwsze liczy z wejścia i zwrócić na wyjściu (jedynie) ich sumę. Na wejściu mogą być tylko całkowite liczby w zakresie [-9,9]
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/12B.dat"


        testOutputExact(path)
    }

    @Test
    fun test12C() {
        //1.2.C Program powinien odczytać dwie pierwsze liczy z wejścia i zwrócić na wyjściu (jedynie) ich sumę. Na wejściu mogą być tylko całkowite liczby dodatnie w zakresie [-9999,9999]
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/12C.dat"


        testOutputExact(path)
    }

    @Test
    fun test12D() {
        //1.2.D Program powinien odczytać dwie pierwsze liczy z wejścia i zwrócić na wyjściu (jedynie) ich różnicę. Na wejściu mogą być tylko całkowite liczby dodatnie w zakresie [-9999,9999]
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/12D.dat"


        testOutputExact(path)
    }

    @Test
    fun test12E() {
        //1.2.E Program powinien odczytać dwie pierwsze liczy z wejścia i zwrócić na wyjściu (jedynie) ich iloczyn. Na wejściu mogą być tylko całkowite liczby dodatnie w zakresie [-9999,9999]
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/12E.dat"


        testOutputExact(path)
    }

    @Test

    fun test13A() {
        //1.3.A Program powinien odczytać dwie pierwsze liczy z wejścia i zwrócić na wyjściu (jedynie) większą z nich. Na wejściu mogą być tylko całkowite liczby dodatnie w zakresie [0,9]
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/13A.dat"


        testOutputExact(path)
    }

    @Test

    fun test13B() {
        //1.3.B Program powinien odczytać dwie pierwsze liczy z wejścia i zwrócić na wyjściu (jedynie) większą z nich. Na wejściu mogą być tylko całkowite liczby w zakresie [-9999,9999]
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/13B.dat"


        testOutputExact(path)
    }

    @Test

    fun test14A() {
        //1.4.A Program powinien odczytać dziesięć pierwszych liczy z wejścia i zwrócić na wyjściu (jedynie) ich średnią arytmetyczną (zaokrągloną do pełnej liczby całkowitej). Na wejściu mogą być tylko całkowite liczby w zakresie [-99,99]
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/14A.dat"


        testOutputExact(path)
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


    private fun testOutputExact(filePath: String) {
        try {
            val data = NeoGP.loadParams(filePath)
            NeoProperties.inputsOutputs = data.params
            NeoProperties.fitnessFunction = data.fitnessFunction
            NeoGP.evolve()
            val outputs = getOutputsFor(NeoProperties.bestIndividual!!, data.params)

            assertTrue(containsExact(outputs, data.params.map { it.outputs }), "program does not find the best solution")
        } catch (e: Exception) {
            println("Program does not run properly, following exception has been thrown:")
            e.printStackTrace()
            fail()
        }
    }

    private fun containsExact(outputs: List<List<String>>, expectedValues: List<List<String>>): Boolean {
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

}