import java.io.File
import java.util.logging.Logger

class DataGenerator {

    companion object DataGenerator {

        val log = Logger.getLogger(this::class.java.toString())

        fun generateInputs() {
            val data = Properties.data
            val steps = Properties.steps
            val tinyGPProperties = Properties.tinyGPProperties

            data.forEach { function ->
                function.domains.forEach { domain ->
                    val file = File("${function.f.name}_${domain.name}")
                    file.writeText("")
                    file.appendText(tinyGPProperties)
                    val stepSize = domain.length() / steps
                }
            }
        }

    }

}