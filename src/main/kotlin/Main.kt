
fun main() {
    System.setProperty("java.util.logging.SimpleFormatter.format", "[%1\$tF %1\$tT] [%4$-7s] %5\$s %n")
    DataGenerator.generateInputs(draw = true)
}
