package neoGP

import neoGP.antlr.parser.NeoGPParser
import neoGP.antlr.parser.model.neoGPLexer
import neoGP.antlr.parser.model.neoGPParser
import neoGP.model.Population
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun main() {
    val population = Population.generatePopulation(2)
    val ind1 = population.individuals.first()
    val ind2 = population.individuals[1]
    val mut = population.mutation(ind1)
    val cross = population.crossover(ind1, ind2)

    println("Parent1: \n$ind1")
    println("Parent2: \n$ind2")
    println("Parent1 mutation: \n$mut")
    println("crossover: \n$cross")

    val lexer = neoGPLexer(CharStreams.fromString(cross.toString()))
    val tokens = CommonTokenStream(lexer)
    val parser = neoGPParser(tokens)
    val tree: neoGPParser.ProgramContext = parser.program()
    val translator = NeoGPParser()
    val sameInd = translator.parse(tree)
    println(sameInd)
    check(cross.toString() == sameInd.toString()) { "Haha chciałbyś żeby działało" }
}