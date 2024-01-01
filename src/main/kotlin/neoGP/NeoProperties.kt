package neoGP

import neoGP.model.Population
import neoGP.model.PopulationGenerationMethod
import neoGP.model.grammar.Individual
import kotlin.reflect.KFunction2

class NeoProperties {
    companion object {
        // Simulation
        var MAX_GEN = 50                    // Max number of generations of programs during learning
        var MAX_GROW_DEPTH = 3             // Max depth of code blocks a Program in grow generation method
        var MAX_FULL_DEPTH = 3             // Max depth of code blocks a Program in full generation method
        var CROSSOVER_PROBABILITY = 0.9
        var COMPETITOR_NUMBER = 5           // Number of competitors in a tournament

        //Population generation
        var POPULATION_GENERATION_METHOD = PopulationGenerationMethod.RAMPED
        var INIT_INSTRUCTION_NUMBER = 5     // Initial number of instructions in a Program
        var MIN_GROW_BLOCK_SIZE = 1         // Min number of instructions in a generated block of code in grow method
        var MAX_GROW_BLOCK_SIZE = 3         // Max number of instructions in a generated block of code in grow method
        var MAX_FULL_BLOCK_SIZE = 3         // Max number of instructions in a generated block of code in full method
        var MAX_EXPRESSION_DEPTH = 2        // Max number of  expression encapsulation
        var POPULATION_SIZE = 1000
        var MAX_INT_VALUE = 10              // Max value of int numbers in generated population
        var MAX_FLOAT_VALUE = 10            // Max value of float numbers in generated population

        // Fitness function
        var MAX_INSTRUCTIONS = 1000         // Max number of instructions in a Program
        var BEST_FITNESS_THRESHOLD = 5
        var CONVERT_FLOAT_TO_INT = false


        //Program variables
        var population = Population()
        var inputsOutputs: List<NeoGP.Params> = listOf()
        var fitnessFunction: KFunction2<List<String>, List<String>, Int>? = null
        var bestFitness = Int.MAX_VALUE
        var bestIndividual: Individual? = null
    }
}