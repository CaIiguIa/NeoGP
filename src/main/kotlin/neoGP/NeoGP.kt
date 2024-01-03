package neoGP

import neoGP.antlr.parser.NeoGPVisitor
import neoGP.antlr.parser.model.neoGPLexer
import neoGP.antlr.parser.model.neoGPParser
import neoGP.model.Population
import neoGP.model.PopulationGenerationMethod
import neoGP.model.grammar.StatsOfIndividual
import neoGP.model.grammar.Individual
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File
import kotlin.math.abs
import kotlin.math.min
import kotlin.reflect.KFunction2

class NeoGP {
    data class Params(
        val inputs: List<String>,
        val outputs: List<String>
    )

    data class Data(
        val params: List<Params>,
        val fitnessFunction: KFunction2<List<String>, List<String>, Int>
    )

    companion object {
        fun loadParams(filePath: String): Data {
            // FILE FORMAT:
            // population_size=0, max_gen_count=0, max_grow_depth=0, instruction_limit=0, fit_function=fitExact, ...
            // input1 input2 ... inputN ; output1 output2 ... outputM
            // ...
            // input1 input2 ... inputK ; output1 output2 ... outputL

            val file = File(filePath)
            if (!file.exists())
                throw IllegalStateException("Input file does not exist!")
            val lines = file.readLines()

            if (lines.size < 2)
                throw Exception("Error while parsing $filePath:  too few configuration lines, expected 2+ got ${lines.size}")

            // Read NeoGP params

            val gpParams = lines[0].split(',')
                .associate {
                    val (key, value) = it.split('=')
                    key.trim() to value.trim()
                }

            if (gpParams["fit_function"] == null)
                throw Exception("Error while parsing $filePath:  fitness function should be provided")
            if (gpParams["population_generation_method"] != null &&
                gpParams["population_generation_method"]?.uppercase() !in PopulationGenerationMethod.values()
                    .map { it.toString() }
            )
                throw Exception("Error while parsing $filePath:  wrong population generation method. Use \"grow\", \"full\" or \"ramped\"")

            gpParams["population_size"]?.toInt()?.let { NeoProperties.POPULATION_SIZE = it }
            gpParams["max_gen_count"]?.toInt()?.let { NeoProperties.MAX_GEN = it }
            gpParams["max_grow_depth"]?.toInt()?.let { NeoProperties.MAX_GROW_DEPTH = it }
            gpParams["max_full_depth"]?.toInt()?.let { NeoProperties.MAX_FULL_DEPTH = it }
            gpParams["instruction_limit"]?.toInt()?.let { NeoProperties.MAX_INSTRUCTIONS = it }
            gpParams["convert_float_to_int"]?.toBoolean()?.let { NeoProperties.CONVERT_FLOAT_TO_INT = it }
            gpParams["max_float_value"]?.toInt()?.let { NeoProperties.MAX_FLOAT_VALUE = it }
            gpParams["max_int_value"]?.toInt()?.let { NeoProperties.MAX_INT_VALUE = it }
            gpParams["best_fitness_threshold"]?.toInt()?.let { NeoProperties.BEST_FITNESS_THRESHOLD = it }
            gpParams["crossover_prob"]?.toDouble()?.let { NeoProperties.CROSSOVER_PROBABILITY = it }
            gpParams["population_generation_method"]?.let {
                NeoProperties.POPULATION_GENERATION_METHOD = PopulationGenerationMethod.valueOf(it.uppercase())
            }

            val paramsList: MutableList<Params> = mutableListOf()

            // Read count of inputs and outputs
            for (line in lines.drop(1)) {
                if (line.isBlank())
                    break

                val ios = line.split(";")
                if (ios.size != 2)
                    throw Exception("Error while parsing $filePath:  wrong number of inputs/outputs, expected 2 got ${ios.size}")

                val inputs = ios[0].trim().split(" ").map { it.trim() }
                val outputs = ios[1].trim().split(" ").map { it.trim() }

                paramsList.add(Params(inputs, outputs))
            }

            val fitnessFunction = when (gpParams["fit_function"]) {
                "fitExact" -> ::fitExact
                "fitInclude" -> ::fitInclude
                "fitExactPercentage" -> ::fitExactPercentage
                "fitIncludePercentage" -> ::fitIncludePercentage
                "fitSquaredDistance" -> ::fitSquaredDistance
                else -> throw Exception("Error while parsing $filePath:  unknown fitness function ${gpParams["fit_function"]}")
            }

            return Data(paramsList, fitnessFunction)
        }

        // Fitness functions
        // 0 - for match
        // 1 - for no match
        // Sum fitness of all test cases to get total fitness
        private fun fitExact(correctOutput: List<String>, programOutput: List<String>): Int {
            if (NeoProperties.CONVERT_FLOAT_TO_INT) {
                val cout = correctOutput.map { it.toFloat().toInt() }
                val pout = programOutput.map { it.toFloat().toInt() }
                return if (cout == pout)
                    0
                else
                    1
            } else {
                val cout = correctOutput.map { it.toFloat() }
                val pout = programOutput.map { it.toFloat() }
                return if (cout == pout)
                    0
                else
                    1
            }

        }

        private fun fitInclude(correctOutput: List<String>, programOutput: List<String>): Int {
            if (NeoProperties.CONVERT_FLOAT_TO_INT) {
                val cout = correctOutput.map { it.toFloat().toInt() }
                val pout = programOutput.map { it.toFloat().toInt() }
                return if (cout.all { pout.contains(it) })
                    0
                else
                    1
            } else {
                val cout = correctOutput.map { it.toFloat() }
                val pout = programOutput.map { it.toFloat() }
                return if (cout.all { pout.contains(it) })
                    0
                else
                    1
            }


        }

        private fun fitExactPercentage(correctOutput: List<String>, programOutput: List<String>): Int {
            if (NeoProperties.CONVERT_FLOAT_TO_INT) {
                var correct = 0
                val out = programOutput.map { it.toFloat().toInt() }
                correctOutput.forEachIndexed { idx, value ->
                    correct += if (value.toFloat().toInt() == out.getOrNull(idx)) 1 else 0
                }

                return 10 - (correct / correctOutput.size * 10)
            } else {
                var correct = 0
                val out = programOutput.map { it.toFloat() }
                correctOutput.forEachIndexed { idx, value ->
                    correct += if (value.toFloat() == out.getOrNull(idx)) 1 else 0
                }

                return 10 - (correct / correctOutput.size * 10)
            }
        }

        private fun fitIncludePercentage(correctOutput: List<String>, programOutput: List<String>): Int {
            if (NeoProperties.CONVERT_FLOAT_TO_INT) {
                val out = programOutput.map { it.toFloat().toInt() }
                return 10 - correctOutput.map { it.toFloat().toInt() }.sumOf { value ->
                    if (value in out)
                        1 as Int
                    else
                        0 as Int
                }
            } else {
                val out = programOutput.map { it.toFloat() }
                return 10 - correctOutput.map { it.toFloat() }.sumOf { value ->
                    if (value in out)
                        1 as Int
                    else
                        0 as Int
                }
            }
        }

        private fun fitSquaredDistance(correctOutput: List<String>, programOutput: List<String>): Int {
            if (correctOutput.isEmpty() || programOutput.isEmpty())
                return Int.MAX_VALUE - 1000

            val sizeDiffers = correctOutput.size != programOutput.size

            val smaller = when {
                sizeDiffers -> listOf(correctOutput, programOutput).minBy { it.size }
                else -> correctOutput
            }

            val bigger = when {
                sizeDiffers -> listOf(correctOutput, programOutput).maxBy { it.size }
                else -> programOutput
            }

            var distance = 0.0
            smaller.forEachIndexed { idx, value ->
                distance += abs(value.toDouble() - bigger[idx].toDouble())
            }

            if (smaller.size != bigger.size) { //apply penalty because of different sizes
                val penalty: Double = bigger.size.toDouble() / smaller.size
                distance = distance * penalty + NeoProperties.BEST_FITNESS_THRESHOLD
            }

            return (distance * distance).toInt()
        }

        fun evolve() {
            NeoProperties.population = Population.generatePopulation()
            var individual: Int
            var newInd: Individual

            printGeneration(0)
            for (gen in 1 until NeoProperties.MAX_GEN) {
                if (NeoProperties.bestFitness <= NeoProperties.BEST_FITNESS_THRESHOLD) {
                    print("PROBLEM SOLVED\n")
                    return
                }

                individual = 0
                while (individual < NeoProperties.POPULATION_SIZE) {
                    newInd = NeoProperties.population.createNewIndividual()
                    NeoProperties.population.replaceWorstIndividual(newInd)
                    individual++
                }
                printGeneration(gen)
            }
            print("PROBLEM *NOT* SOLVED\n")
        }

        fun calculateFitness(individual: Individual): StatsOfIndividual {
            val tree = getParseTreeForIndividual(individual.toString())
            var fitness = 0L
            var instructions = 0
            NeoProperties.inputsOutputs.forEach {
                val visitor = NeoGPVisitor(it.inputs)
                val programOutput = visitor.run(tree)
                fitness += NeoProperties.fitnessFunction!!(it.outputs, programOutput.first)
                instructions += programOutput.second
            }
            fitness = min(Int.MAX_VALUE.toLong(), fitness)

            return StatsOfIndividual(fitness.toInt(), instructions, NeoProperties.inputsOutputs.size)
        }

        fun getParseTreeForIndividual(individual: String): neoGPParser.ProgramContext {
            val lexer = neoGPLexer(CharStreams.fromString(individual))
            val tokens = CommonTokenStream(lexer)
            val parser = neoGPParser(tokens)
            return parser.program()
        }

        private fun printGeneration(gen: Int) {
            var averageFitness = 0L
            var averageInstructions = 0

            NeoProperties.population.individuals.forEach { individual ->
                averageFitness += individual.fitness()
                averageInstructions += individual.instructions()
                when {
                    NeoProperties.bestFitness > individual.fitness() -> NeoProperties.bestIndividual = individual
                    NeoProperties.bestFitness == individual.fitness() ->
                        NeoProperties.bestIndividual =
                            NeoProperties.bestIndividual?.let { chooseShorterIndividual(it, individual) } ?: individual
                }
                NeoProperties.bestFitness = NeoProperties.bestIndividual?.fitness() ?: Int.MAX_VALUE
            }

            averageFitness /= NeoProperties.population.individuals.size
            averageInstructions /= NeoProperties.population.individuals.size

            print(
                """
Generation=$gen; 
Avg Fitness=${averageFitness}; 
Best Fitness=${NeoProperties.bestFitness}; 
Avg Size=${averageInstructions};
Best Individual: ${NeoProperties.bestIndividual?.toOneLineString()}
"""
            )
        }

        private fun chooseShorterIndividual(ind1: Individual, ind2: Individual): Individual {
            val len1 = ind1.getChildren()
            val len2 = ind2.getChildren()
            return when {
                len1 == len2 -> listOf(ind1, ind2).minBy { it.toOneLineString().length }
                len1 < len2 -> ind1
                else -> ind2
            }
        }

    }
}