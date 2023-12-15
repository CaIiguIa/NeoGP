package neoGP

import java.io.File
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
            // population_size=0, max_gen_count=0, max_depth=0, time_limit=0, fit_function=fitExact
            // input_count output_count
            // input1 input2 ... inputN output1 output2 ... outputM
            // ...
            // input1 input2 ... inputN output1 output2 ... outputM

            val file = File(filePath)
            val lines = file.readLines()

            if (lines.size < 3)
                throw Exception("Error while parsing $filePath:  too few configuration lines, expected 3+ got ${lines.size}")

            // Read NeoGP params

            val gpParams = lines[0].split(',')
                .associate {
                    val (key, value) = it.split('=')
                    key to value
                }

            if (gpParams.size != 4)
                throw Exception("Error while parsing $filePath:  wrong number of GP params, expected 4 got ${gpParams.size}")

            NeoProperties.POPULATION_SIZE = gpParams["population_size"]!!.toInt()
            NeoProperties.MAX_GEN = gpParams["max_gen_count"]!!.toInt()
            NeoProperties.MAX_DEPTH = gpParams["max_depth"]!!.toInt()
            NeoProperties.MAX_INSTRUCTIONS = gpParams["time_limit"]!!.toInt()

            val ioParams = lines[1].split(" ")
            if (ioParams.size != 2)
                throw Exception("Error while parsing $filePath:  wrong number of IO counts, expected 2 got ${ioParams.size}")
            val inputCount = ioParams[0].toInt()
            val outputCount = ioParams[1].toInt()

            val paramsList: MutableList<Params> = mutableListOf()

            // Read count of inputs and outputs
            for (line in lines.drop(2)) {
                if (line.isEmpty())
                    break

                val ios = lines[2].split(" ")
                if (ios.size != inputCount + outputCount)
                    throw Exception("Error while parsing $filePath:  wrong number of inputs/outputs, expected ${inputCount + outputCount} got ${ios.size}")

                val inputs = ios.subList(0, inputCount)
                val outputs = ios.subList(inputCount, inputCount + outputCount)

                paramsList.add(Params(inputs, outputs))
            }

            val fitnessFunction = when (gpParams["fit_function"]) {
                "fitExact" -> ::fitExact
                "fitInclude" -> ::fitInclude
                else -> throw Exception("Error while parsing $filePath:  unknown fitness function ${lines[2 + paramsList.size]}")
            }

            return Data(paramsList, fitnessFunction);
        }

        // Fitness functions
        // 0 - for match
        // 1 - for no match
        // Sum fitness of all test cases to get total fitness
        fun fitExact(correctOutput: List<String>, programOutput: List<String>): Int {
            return if (correctOutput == programOutput)
                0
            else
                1
        }

        fun fitInclude(correctOutput: List<String>, programOutput: List<String>): Int {
            return if (correctOutput.all { programOutput.contains(it) })
                0
            else
                1
        }
    }
}