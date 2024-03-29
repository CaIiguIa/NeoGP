import com.aspose.cells.*
import java.io.File
import java.nio.file.Files
import java.util.logging.Logger


class ChartDrawer {

    companion object ChartDrawer {

        val log = Logger.getLogger(this::class.java.toString())

        fun drawChart(inputFile: File, function: Function, solution: String) {
            val variableNumber = getVariableNumber(function)
            val outCsvPath = inputFile.absolutePath.replace("input", "output").replace(".dat", ".csv")
            val content = Files.readString(inputFile.toPath(), Charsets.UTF_8)
                .replace(Properties.getTinyGPProperties(variableNumber) + "\n", "")
                .replace(".", ",")
                .replace(" ", ";")

            val csvFile = File(outCsvPath)
            csvFile.parentFile.mkdirs()
            csvFile.createNewFile()
            csvFile.writeText(content)

            when (function) {
                is SingleFunction -> chartSingleFunction(csvFile, function, solution)
                is DoubleFunction -> chartDoubleFunction(csvFile, function, solution)
                else -> throw UnsupportedOperationException()
            }

        }

        private fun chartSingleFunction(csvFile: File, function: Function, solution: String) {
            val solutionCol = getSolutionExcelColumn(function)
            val cellRange = "A1:$solutionCol${Properties.numberOfSteps + 1}"

            //open Excel file
            val oTxtLoadOptions = TxtLoadOptions(LoadFormat.CSV)
            oTxtLoadOptions.separator = ';'
            oTxtLoadOptions.encoding = Encoding.getUTF8()
            val workbook = Workbook(csvFile.absolutePath, oTxtLoadOptions)
            val worksheet = workbook.worksheets[0]
            worksheet.cells[solutionCol + "1"].setSharedFormula(
                getSolutionFormula(solution),
                Properties.numberOfSteps,
                1
            )

            //chart
            val chartIndex = worksheet.charts.add(ChartType.COLUMN, 5, 5, 15, 15)
            val chart = worksheet.charts[chartIndex]
            chart.setChartDataRange(cellRange, true)

            workbook.save(csvFile.absolutePath.replace(".csv", ".xlsx"), SaveFormat.XLSX)
        }


        private fun chartDoubleFunction(csvFile: File, function: Function, solution: String) {
            val solutionCol = getSolutionExcelColumn(function)

            //open Excel file
            val oTxtLoadOptions = TxtLoadOptions(LoadFormat.CSV)
            oTxtLoadOptions.separator = ';'
            oTxtLoadOptions.encoding = Encoding.getUTF8()
            val workbook = Workbook(csvFile.absolutePath, oTxtLoadOptions)
            val worksheet = workbook.worksheets[0]

            //change shared formula so it takes multiple columns as input
            worksheet.cells[solutionCol + "1"].setSharedFormula(
                getSolutionFormula(solution),
                Properties.numberOfSteps,
                1
            )
            workbook.save(csvFile.absolutePath.replace(".csv", ".xlsx"), SaveFormat.XLSX)
        }

        private fun getVariableNumber(function: Function) = when (function) {
            is SingleFunction -> 1
            is DoubleFunction -> 2
            else -> throw UnsupportedOperationException()
        }

        private fun getSolutionExcelColumn(function: Function) = when (getVariableNumber(function)) {
            1 -> "C"
            2 -> "D"
            else -> throw UnsupportedOperationException()
        }

        private fun getSolutionFormula(solution: String) =
            solution
                .replace("X1", "A1")
                .replace("X2", "B1")

    }
}