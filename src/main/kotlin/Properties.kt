import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin
import kotlin.math.tan

class Properties {

    companion object Properties {

        const val numberOfSteps: Int = 1000
        private const val numberOfConstants: Int = 25
        private const val constantLowerBound: Int = -5
        private const val constantUpperBound: Int = 5

        private val f1 = { x: Double -> 5 * x * x * x - 2 * x * x + 3 * x - 17 }
        private val f2 = { x: Double -> sin(x) + cos(x) }
        private val f3 = { x: Double -> 2 * ln(x + 1) }
        private val f4 = { x: Double, y: Double -> x + 2 * y }
        private val f5 = { x: Double, _: Double -> sin(x / 2) + 2 * cos(x) }
        private val f6 = { x: Double, y: Double -> x * x + 3 * x * y - 7 * y + 1 }
        private val f7 = { x: Double -> sin(x + 3.141592 / 2) }
        private val f8 = { x: Double -> tan(2 * x + 1) }


        val data: List<Problem> = listOf(
//            Problem(
//                SingleFunction(f1, "f1"),
//                listOf(
//                    Domain(-10, 10, "d1"),
//                    Domain(0, 100, "d2"),
//                    Domain(-1, 1, "d3"),
//                    Domain(-1000, 1000, "d4"),
//                )
//            ),
            Problem(
                SingleFunction(f2, "f2"),
                listOf(
                    Domain(-3.14, 3.14, "d1"),
//                    Domain(0, 7, "d2"),
//                    Domain(0, 100, "d3"),
//                    Domain(-100, 100, "d4"),
                )
            ),
//            Problem(
//                SingleFunction(f3, "f3"),
//                listOf(
//                    Domain(0, 4, "d1"),
//                    Domain(0, 9, "d2"),
//                    Domain(0, 99, "d3"),
//                    Domain(0, 999, "d4"),
//                )
//            ),
//            Problem(
//                DoubleFunction(f4, "f4"),
//                listOf(
//                    Domain(0, 1, "d1"),
//                    Domain(-10, 10, "d2"),
//                    Domain(0, 100, "d3"),
//                    Domain(-1000, 1000, "d4"),
//                )
//            ),
            Problem(
                DoubleFunction(f5, "f5"),
                listOf(
//                    Domain(-3.14, 3.14, "d1"),
//                    Domain(0, 7, "d2"),
//                    Domain(0, 100, "d3"),
                    Domain(-100, 100, "d4"),
                )
            ),
//            Problem(
//                DoubleFunction(f6, "f6"),
//                listOf(
//                    Domain(-10, 10, "d1"),
//                    Domain(0, 100, "d2"),
//                    Domain(-1, 1, "d3"),
//                    Domain(-1000, 1000, "d4"),
//                )
//            ),
            Problem(
                SingleFunction(f7, "f7"),
                listOf(
                    Domain(0.0, 2 * 3.14, "d1"),
                )
            ),
            Problem(
                SingleFunction(f8, "f8"),
                listOf(
                    Domain(0.001, 3.14, "d1"),
                )
            ),
        )

        fun getTinyGPProperties(numberOfVariables: Int): String =
            "$numberOfVariables $numberOfConstants $constantLowerBound $constantUpperBound $numberOfSteps"

    }

}

abstract class Function(
    val name: String
)

class SingleFunction(
    val f: (Double) -> Double,
    name: String,
) : Function(name)

class DoubleFunction(
    val f: (Double, Double) -> Double,
    name: String,
) : Function(name)

class Problem(
    val f: Function,
    val domains: List<Domain>,
)

class Domain(
    val lower: Double,
    val upper: Double,
    val name: String,
) {
    constructor(x1: Int, x2: Int, name: String) : this(x1.toDouble(), x2.toDouble(), name)

    fun length() = upper - lower
}