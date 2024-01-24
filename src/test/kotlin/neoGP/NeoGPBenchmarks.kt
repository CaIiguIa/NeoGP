package neoGP

import org.junit.jupiter.api.BeforeEach
import java.io.File
import java.nio.file.Paths
import kotlin.math.pow
import kotlin.random.Random
import kotlin.test.Test

class NeoGPBenchmarks {

    @BeforeEach
    fun setUp() {
//        NeoProperties.population = Population.generatePopulation()
        NeoProperties.inputsOutputs = listOf()
        NeoProperties.fitnessFunction = null
        NeoProperties.bestFitness = Double.MAX_VALUE
        NeoProperties.bestIndividual = null
        NeoProperties.BEST_FITNESS_THRESHOLD = 5.0
    }

    @Test
    fun testBench1() {
        //Number IO (Q 3.5.1) Given an integer and a float, print their sum.
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/bench1.dat"

        TestService.testOutputExact(path, 0.01)
    }

    @Test
    fun testBench2() {
        //Collatz Numbers (P 4.2) Given an integer, find the number of terms in the Collatz (hailstone) sequence starting from that integer.
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/bench2.dat"

        TestService.testOutputExact(path)
    }

    @Test
    fun testBench3() {
        //Smallest - Given 4 integers, print the smallest of them.
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/bench3.dat"

        TestService.testOutputExact(path)
    }
    @Test
    fun testBenchRegression() {
        //Symbolic regression.
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/benchReg.dat"
//        printTruthTable(4, path)

        TestService.testOutputExact(path)
    }


    private fun printTruthTable(n: Int, filePath: String) {
        val file = File(filePath)
        var newText = file.readLines()[0] + "\n"

        val rows = (2.0).pow(n).toInt()


        for (i in 0 until rows) {
            for (j in n - 1 downTo 0) {
                newText +=(((i / (2.0).pow(j.toDouble()).toInt()) % 2).toString() + " ")
            }
            newText +=("; " + Random.nextInt(2) + "\n")
        }

        file.writeText(newText)
    }

}