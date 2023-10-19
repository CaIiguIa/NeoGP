import com.aspose.cells.ChartType
import com.aspose.cells.SaveFormat
import com.aspose.cells.Workbook
import java.io.File
import java.nio.file.Files
import java.util.logging.Logger


class ChartDrawer {

    companion object ChartDrawer {

        val log = Logger.getLogger(this::class.java.toString())

        fun drawChart(inputFile: File, solution: String) {
            val outCsvPath = inputFile.absolutePath.replace("input", "output").replace(".dat", ".csv")
            val outFilePath = inputFile.absolutePath.replace("input", "output").replace(".dat", ".xlsx")
            val content = Files.readString(inputFile.toPath(), Charsets.UTF_8)
                .replace(Properties.getTinyGPProperties(1) + "\n", "")
                .replace(Properties.getTinyGPProperties(2) + "\n", "")

            val csvFile = File(outCsvPath)
            csvFile.parentFile.mkdirs()
            csvFile.createNewFile()
            csvFile.writeText(content.replace(" ", ","))
            chart(csvFile)
        }


        private fun chart(csvFile: File) {
            val workbook = Workbook(csvFile.absolutePath)
            val worksheet = workbook.worksheets[0]
            val chartIndex = worksheet.charts.add(ChartType.COLUMN, 5, 5, 15, 15)
            val chart = worksheet.charts[chartIndex]

            chart.setChartDataRange("A1:C4", true)
            workbook.save(csvFile.absolutePath.replace(".csv", ".xlsx"), SaveFormat.XLSX)
        }

    }
}