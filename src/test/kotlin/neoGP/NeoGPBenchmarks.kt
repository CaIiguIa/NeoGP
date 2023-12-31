package neoGP

import org.junit.jupiter.api.BeforeEach
import java.nio.file.Paths
import kotlin.test.Test

class NeoGPBenchmarks {

    @BeforeEach
    fun setUp() {
//        NeoProperties.population = Population.generatePopulation()
        NeoProperties.inputsOutputs = listOf()
        NeoProperties.fitnessFunction = null
        NeoProperties.bestFitness = Int.MAX_VALUE
        NeoProperties.bestIndividual = null
        NeoProperties.BEST_FITNESS_THRESHOLD = 5
    }

    @Test
    fun testBench1() {
        //Number IO (Q 3.5.1) Given an integer and a float, print their sum.
        val path = Paths.get("").toAbsolutePath().toString() + "/src/test/resources/bench1.dat"

        TestService.testOutputExact(path)
    }

    @Test
    fun testBench2() {
        //Sum of Squares (Q 8.5.4) Given integer n, return the sum of squaring each integer in the range [1, n].
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

        TestService.testOutputExact(path)
    }

}