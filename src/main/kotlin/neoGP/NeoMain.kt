package neoGP

import neoGP.antlr.parser.NeoGPParser
import neoGP.antlr.parser.model.neoGPLexer
import neoGP.antlr.parser.model.neoGPParser
import neoGP.model.Population
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import kotlin.system.measureTimeMillis

fun main() {
    var count = 0

    val time = measureTimeMillis {
        val population = Population.generatePopulation(1000)

        population.individuals.forEach {ind ->
        val indString = ind.toString()
        val lexer = neoGPLexer(CharStreams.fromString(indString))
        val tokens = CommonTokenStream(lexer)
        val parser = neoGPParser(tokens)
        val tree: neoGPParser.ProgramContext = parser.program()
        val translator = NeoGPParser()
        val sameInd = translator.parse(tree)
        check(indString == sameInd.toString()) { "Haha chciałbyś żeby działało" }
            count += indString.count { it =='\n' }
        }
    }
    println("Time: ${time/1000.0}, avg instructions: ${count/1000.0}")

}