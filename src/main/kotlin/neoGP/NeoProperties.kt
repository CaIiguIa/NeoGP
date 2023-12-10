package neoGP

class NeoProperties {
    companion object {
        // Simulation
        var MAX_GEN = 50                    // Max number of generations of programs during learning
        var MAX_DEPTH = 10                  // Max depth of code blocks a Program
        var CROSSOVER_PROBABILITY = 0.9
        var COMPETITOR_NUMBER = 5           // Number of competitors in a tournament

        //Population generation
        var INIT_INSTRUCTION_NUMBER = 5     // Initial number of instructions in a Program
        var MIN_INSTRUCTION_BLOCK_SIZE = 2  // Min Number of instructions in a generated block of code
        var MAX_INSTRUCTION_BLOCK_SIZE = 2  // MaxNumber of instructions in a generated block of code, values above 2 will make the program veeeery slow
        var POPULATION_SIZE = 1000
        var MAX_INT_VALUE = 100             // Max value of int numbers in generated population
        var MAX_FLOAT_VALUE = 100           // Max value of float numbers in generated population

        // Fitness function
        var MAX_INSTRUCTIONS = 1000         // Max number of instructions in a Program
    }
}