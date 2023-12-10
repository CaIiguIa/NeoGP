package neoGP

import java.io.File

class NeoGP {
    data class Params(
        val inputs: List<String>,
        val outputs: List<String>
    )
    companion object {
        fun loadParams(filePath: String): Params {
            // FILE FORMAT:
            // population_size max_gen_count max_depth time_limit
            // input_count output_count
            // input1 input2 ... inputN output1 output2 ... outputM

            val file = File(filePath)
            val lines = file.readLines()

            if (lines.size < 3)
                throw Exception("Error while parsing $filePath:  too few configuration lines, expected 3 got ${lines.size}")

            // Read NeoGP params

            val gpParams = lines[0].split(" ")

            if (gpParams.size != 4)
                throw Exception("Error while parsing $filePath:  wrong number of GP params, expected 4 got ${gpParams.size}")

            NeoProperties.POPULATION_SIZE = gpParams[0].toInt()
            NeoProperties.MAX_GEN = gpParams[1].toInt()
            NeoProperties.MAX_DEPTH = gpParams[2].toInt()
            NeoProperties.MAX_INSTRUCTIONS = gpParams[3].toInt()

            // Read count of inputs and outputs
            val ioParams = lines[1].split(" ")
            if (ioParams.size != 2)
                throw Exception("Error while parsing $filePath:  wrong number of IO counts, expected 2 got ${ioParams.size}")
            val inputCount = ioParams[0].toInt()
            val outputCount = ioParams[1].toInt()

            val ios = lines[2].split(" ")
            if (ios.size != inputCount + outputCount)
                throw Exception("Error while parsing $filePath:  wrong number of inputs/outputs, expected ${inputCount + outputCount} got ${ios.size}")

            val inputs = ios.subList(0, inputCount)
            val outputs = ios.subList(inputCount, inputCount + outputCount)

            return Params(inputs, outputs)
        }
    }
}