import java.io.File
import java.nio.file.Paths

fun main() {
    System.setProperty("java.util.logging.SimpleFormatter.format", "[%1\$tF %1\$tT] [%4$-7s] %5\$s %n")
//    DataGenerator.generateInputs()
    File(Paths.get("").toAbsolutePath().toString() + "/src/main/resources/inputs").listFiles()?.forEach {
        ChartDrawer.drawChart(it, "")
    }
}

/* TODO:
* uruchomić program dla zestawu danych i wyszukać najlepszy wynik w outpucie
* narysować wykresy wyniku i oryginału
* */