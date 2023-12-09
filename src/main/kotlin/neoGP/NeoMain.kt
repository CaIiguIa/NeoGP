package neoGP

import neoGP.antlr.parser.NeoGPGenerator
import neoGP.antlr.parser.NeoGPParser
import neoGP.antlr.parser.NeoGPVisitor
import neoGP.antlr.parser.model.neoGPLexer
import neoGP.antlr.parser.model.neoGPParser
import neoGP.model.Population
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import kotlin.system.measureTimeMillis

fun main() {
    testRandomIndividual(true)

//    testMultipleRandomIndividuals()
//    testVisitor()
}

fun testParser() {
    var count = 0

    val time = measureTimeMillis {
        val population = Population.generatePopulation(1000)

        population.individuals.forEach { ind ->
            val indString = ind.toString()
            val lexer = neoGPLexer(CharStreams.fromString(indString))
            val tokens = CommonTokenStream(lexer)
            val parser = neoGPParser(tokens)
            val tree: neoGPParser.ProgramContext = parser.program()
            val translator = NeoGPParser()
            val sameInd = translator.parse(tree)
            check(indString == sameInd.toString()) { "Haha chciałbyś żeby działało" }
            count += indString.count { it == '\n' }
        }
    }
    println("Time: ${time / 1000.0}, avg instructions: ${count / 1000.0}")
}

fun testVisitor() {
    val nIter = 1000
    val time = measureTimeMillis {
        for (i in 0..nIter) {
            val ind = NeoGPGenerator.randomIndividual(5).toString()
            val visitor = NeoGPVisitor(listOf("11", "14"), 1000)
            try {
                visitor.run(getTreeForIndividual(ind))
            } catch (e: Exception) {
//                println("Error ${e.message}")
            }

        }
    }
    println("Time: ${time / 1000.0}, avg instructions: 7, number of individuals: $nIter")
}

fun testRandomIndividual(print: Boolean = false) {
    val individual = NeoGPGenerator.randomIndividual(5)
    if (print) println(individual)

    val visitor = NeoGPVisitor(listOf("11", "14"), 1000)
    val output = visitor.run(getTreeForIndividual(individual.toString()))
    if (print) println(output)
}

fun getTreeForIndividual(individual: String): neoGPParser.ProgramContext {
    val lexer = neoGPLexer(CharStreams.fromString(individual))
    val tokens = CommonTokenStream(lexer)
    val parser = neoGPParser(tokens)
    return parser.program()
}

fun testMultipleRandomIndividuals() {
    var flops = 0
    val time = measureTimeMillis {
        for (i in 1..1000) {
            try {
                testRandomIndividual()
            } catch (_: Exception) {
                flops += 1
            }
        }
    }
    println("Time: ${time / 1000.0}, number of individuals: 1000, flops: $flops")
}
