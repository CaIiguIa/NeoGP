import TinyGP.GeneticProgramming
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.PrintStream


class ComputeProblem {
    companion object {

        fun fromFile(filePath: String): String {
            val p = Runtime.getRuntime().exec("tiny_gp.exe $filePath")

            val reader = BufferedReader(p.inputStream.reader())

            val lines = reader.lines().toList()
            return lines[lines.size - 3]
        }

        fun fromFileNative(filePath: String): String {
            val outputCapture = ByteArrayOutputStream()
            val customOut = PrintStream(outputCapture)
            val originalStdout = System.out

            System.setOut(customOut)

            val gp = GeneticProgramming(filePath, -1)

            gp.evolve()

            System.setOut(originalStdout)

            val capturedOutput = outputCapture.toString()

            val fitValues = mutableListOf<FloatArray>()

            val lines = capturedOutput.split("\n")
            for (line in lines) {
                if (line.startsWith("Generation")) {
                    val el = line.split(" ")
                    val avgFit = el[2].split("=")[1].toFloat()
                    val bestFit = el[4].split("=")[1].toFloat()
                    fitValues.add(floatArrayOf(avgFit, bestFit))
                }

            }

            return lines[lines.size - 3].replace("Best Individual: ", "")
        }
    }

    fun all() {}
}