package neoGP

import neoGP.antlr.parser.NeoGPGenerator
import neoGP.antlr.parser.NeoGPParser
import neoGP.antlr.parser.NeoGPVisitor
import neoGP.model.Population
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
//    testRandomIndividual(true)
//    testMultipleRandomIndividuals()
//    testVisitor()
//    testParser()
    if (args.size != 1)
        throw UnsupportedOperationException("Wrong number of program's input arguments! (${args.size})")

    val data = NeoGP.loadParams(args.first())
    NeoProperties.inputsOutputs = data.params
    NeoProperties.fitnessFunction = data.fitnessFunction
    NeoGP.evolve()
}

fun testParser() {
    var count = 0

    val time = measureTimeMillis {
        val population = Population.generatePopulation()

        population.individuals.forEach { ind ->
            val indString = ind.toString()
            val translator = NeoGPParser()
            val sameInd = translator.toIndividual(indString)
            check(indString == sameInd.toString()) { "Haha chciałbyś żeby działało" }
            count += indString.count { it == '\n' }
        }
    }
    println("Time: ${time / 1000.0}, avg instructions: ${count / NeoProperties.POPULATION_SIZE.toFloat()}")
}

fun testVisitor() {
    val nIter = 1000
    val time = measureTimeMillis {
        for (i in 0..nIter) {
            val ind = NeoGPGenerator.randomIndividual().toString()
            val visitor = NeoGPVisitor(listOf("11", "14"))
            try {
                visitor.run(NeoGP.getParseTreeForIndividual(ind))
            } catch (e: Exception) {
                println("Error ${e.message}")
//                println(e.printStackTrace())
            }
        }
    }
    println("Time: ${time / 1000.0}, number of individuals: $nIter")
}

fun testRandomIndividual(print: Boolean = false) {
    val individual = NeoGPGenerator.randomIndividual()
    if (print) println(individual)

    val visitor = NeoGPVisitor(listOf("11", "14"))
    val output = visitor.run(NeoGP.getParseTreeForIndividual(individual.toString()))
    if (print) println(output)
}

fun testMultipleRandomIndividuals() {
    var flops = 0
    val time = measureTimeMillis {
        for (i in 1..NeoProperties.POPULATION_SIZE) {
            try {
                testRandomIndividual()
            } catch (_: Exception) {
                flops += 1
            }
        }
    }
    println("Time: ${time / 1000.0}, number of individuals: 1000, flops: $flops")
}
