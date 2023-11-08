package neoGP

import neoGP.antlr.parser.NeoGPParser
import neoGP.antlr.parser.model.neoGPLexer
import neoGP.antlr.parser.model.neoGPParser
import neoGP.model.Population
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun main() {
    val ind = Population.generatePopulation(1).individuals.first()
    println(ind)

    //  Parser
    val lexer = neoGPLexer(CharStreams.fromString(ind.toString()))
    val tokens = CommonTokenStream(lexer)
    val parser = neoGPParser(tokens)
    val tree: neoGPParser.ProgramContext = parser.program()
    val translator = NeoGPParser()
    val sameInd = translator.parse(tree)
//    println(sameInd)
    check(ind.toString() == sameInd.toString()) { "Haha chciałbyś żeby działało" }
}