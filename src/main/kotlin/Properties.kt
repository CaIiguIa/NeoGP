import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin

class Properties {

    companion object Properties {

        const val tinyGPProperties = "1 100 -5 5 101 "
        const val steps: Int = 1000

        private val f1 = { x: Float -> 5 * x * x * x - 2 * x * x + 3 * x - 17 }
        private val f2 = { x: Float -> sin(x) + cos(x) }
        private val f3 = { x: Float -> 2 * ln(x + 1) }
        private val f4 = { x: Float, y: Float -> x + 2 * y }
        private val f5 = { x: Float, y: Float -> sin(x / 2) + 2 * cos(x) }
        private val f6 = { x: Float, y: Float -> x * x + 3 * x * y - 7 * y + 1 }


        val data: List<Problem> = listOf(
            Problem(
                SingleFunction(f1, "f1"),
                listOf(
                    Domain(-10, 10, "d1"),
                    Domain(0, 100, "d2"),
                    Domain(-1, 1, "d3"),
                    Domain(-1000, 1000, "d4"),
                )
            ),
            Problem(
                SingleFunction(f2, "f2"),
                listOf(
                    Domain(-3.14, 3.14, "d1"),
                    Domain(0, 7, "d2"),
                    Domain(0, 100, "d3"),
                    Domain(-100, 100, "d4"),
                )
            ),
            Problem(
                SingleFunction(f3, "f3"),
                listOf(
                    Domain(0, 4, "d1"),
                    Domain(0, 9, "d2"),
                    Domain(0, 99, "d3"),
                    Domain(0, 999, "d4"),
                )
            ),
            Problem(
                DoubleFunction(f4, "f4"),
                listOf(
                    Domain(0, 1, "d1"),
                    Domain(-10, 10, "d2"),
                    Domain(0, 100, "d3"),
                    Domain(-1000, 1000, "d4"),
                )
            ),
            Problem(
                DoubleFunction(f5, "f5"),
                listOf(
                    Domain(-3.14, 3.14, "d1"),
                    Domain(0, 7, "d2"),
                    Domain(0, 100, "d3"),
                    Domain(-100, 100, "d4"),
                )
            ),
            Problem(
                DoubleFunction(f6, "f6"),
                listOf(
                    Domain(-10, 10, "d1"),
                    Domain(0, 100, "d2"),
                    Domain(-1, 1, "d3"),
                    Domain(-1000, 1000, "d4"),
                )
            ),
        )
    }

}

abstract class Function(
    val name: String
)

class SingleFunction(
    val f: (Float) -> Float,
    name: String,
) : Function(name)

class DoubleFunction(
    val f: (Float, Float) -> Float,
    name: String,
) : Function(name)

class Problem(
    val f: Function,
    val domains: List<Domain>,
)

class Domain(
    val first: Float,
    val second: Float,
    val name: String,
) {
    constructor(x: Double, y: Double, name: String) : this(x.toFloat(), y.toFloat(), name)
    constructor(x: Int, y: Int, name: String) : this(x.toFloat(), y.toFloat(), name)

    fun length() = second-first
}