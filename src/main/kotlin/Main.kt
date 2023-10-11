import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin

class Main {
    fun main(args: Array<String>) {

    }

    val step: Int = 1000
    private val f1 = { x: Float -> 5 * x * x * x - 2 * x * x + 3 * x - 17 }
    private val f2 = { x: Float -> sin(x) + cos(x) }
    private val f3 = { x: Float -> 2 * ln(x + 1) }
    private val f4 = { x: Float, y: Float -> x + 2 * y }
    private val f5 = { x: Float, y: Float -> sin(x / 2) + 2 * cos(x) }
    private val f6 = { x: Float, y: Float -> x * x + 3 * x * y - 7 * y + 1 }

    val data: List<Problem> = listOf(
        Problem(
            SingleFunction(f1),
            listOf(
                Domain(-10,10),
                Domain(0, 100),
                Domain(-1, 1),
                Domain(-1000, 1000),
            )
        ),
        Problem(
            SingleFunction(f2),
            listOf(
                Domain(-3.14, 3.14),
                Domain(0, 7),
                Domain(0, 100),
                Domain(-100, 100),
            )
        ),
        Problem(
            SingleFunction(f3),
            listOf(
                Domain(0,4),
                Domain(0,9),
                Domain(0,99),
                Domain(0,999),
            )
        ),
        Problem(
            DoubleFunction(f4),
            listOf(
                Domain(0,1),
                Domain(-10,10),
                Domain(0,100),
                Domain(-1000, 1000),
            )
        ),
        Problem(
            DoubleFunction(f5),
            listOf(
                Domain(-3.14, 3.14),
                Domain(0,7),
                Domain(0,100),
                Domain(-100,100),
            )
        ),
        Problem(
            DoubleFunction(f6),
            listOf(
                Domain(-10,10),
                Domain(0,100),
                Domain(-1,1),
                Domain(-1000, 1000),
            )
        ),
    )

}

abstract class Function

class SingleFunction(
    val f: (Float) -> Float
): Function()

class DoubleFunction(
    val f: (Float, Float) -> Float
): Function()

class Problem(
    val f: Function,
    val domains: List<Domain>,
)

class Domain(
    val first: Float,
    val second: Float,
) {
    constructor(x: Double, y: Double) : this(x.toFloat(), y.toFloat())
    constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())
}

/* TODO:
* wygeneruj dane (dat?)
* uruchom program dla zestawu danych i output zapisz w pliku tekstowym
* wyszukaj najlepszy wynik
* narysuj wykresy wyniku i oryginału
* */