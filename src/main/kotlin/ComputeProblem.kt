import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.logging.Logger


class ComputeProblem {
    companion object {
        val log = Logger.getLogger(this::class.java.toString())

        fun fromFile(filePath: String): String {
            val p = Runtime.getRuntime().exec("tiny_gp.exe $filePath")

            val reader = BufferedReader(p.inputStream.reader())

            val lines = reader.lines().toList()
            return lines[lines.size - 3]
        }

        fun fromFileNative(filePath: String): String {
            val outputCapture = ByteArrayOutputStream()
            val customOut = PrintStream(outputCapture)
            val originalStdout = System.out;

            System.setOut(customOut)

            val gp = tiny_gp("test.dat", -1)

            gp.evolve()

            System.setOut(originalStdout)

            val capturedOutput = outputCapture.toString()

            val lines = capturedOutput.split("\n")
            return lines[lines.size - 3].replace("Best Individual: ", "")
        }
    }

    fun all() {}
}