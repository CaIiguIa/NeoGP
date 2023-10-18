import java.io.File
import kotlin.math.sqrt
import java.nio.file.Paths
import java.util.logging.Logger

class DataGenerator {

    companion object DataGenerator {

        val log = Logger.getLogger(this::class.java.toString())

        fun generateInputs() {
            log.info("Generating input data for tinyGP")
            val data = Properties.data

            data.forEach { problem ->
                val function = problem.f
                problem.domains.forEach { domain ->
                    val file = prepareFile("/inputs/${problem.f.name}_${domain.name}.dat")

                    when (function) {
                        is SingleFunction ->
                            getFunctionPoints(function, domain, file)

                        is DoubleFunction ->
                            getFunctionPoints(function, domain, file)

                    }
                }
            }
        }

        private fun prepareFile(resourceFilePath: String): File {
            val resourcePath = Paths.get("").toAbsolutePath().toString() + "/src/main/resources"
            val file = File(resourcePath + resourceFilePath)
            file.parentFile.mkdirs()
            file.createNewFile()

            file.writeText("")
            return file
        }

        private fun getFunctionPoints(function: SingleFunction, domain: Domain, file: File) {
            file.appendText(Properties.getTinyGPProperties(1) + "\n")
            var currentValue = domain.lower
            val stepSize = domain.length() / Properties.numberOfSteps

            while (currentValue <= domain.upper) {
                file.appendText("$currentValue ${function.f(currentValue)}\n")
                currentValue += stepSize
            }
        }

        private fun getFunctionPoints(function: DoubleFunction, domain: Domain, file: File) {
            file.appendText(Properties.getTinyGPProperties(2) + "\n")
            var xValue = domain.lower
            val stepSize = domain.length() / sqrt(Properties.numberOfSteps.toDouble())

            while (xValue <= domain.upper) {
                var yValue = domain.lower

                while (yValue <= domain.upper) {
                    file.appendText("$xValue $yValue ${function.f(xValue, yValue)}\n")
                    yValue += stepSize
                }

                xValue += stepSize
            }
        }

    }

}