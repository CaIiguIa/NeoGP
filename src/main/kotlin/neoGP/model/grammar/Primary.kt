package neoGP.model.grammar

import kotlin.random.Random

abstract class Primary(override val value: String) : Expression(value) {
    companion object {
        fun generateRandom() = when (Random.nextInt(4)) {
            0 -> Id.generateRandom()
            1 -> IntNumberToken.generateRandom()
            2 -> BooleanToken.generateRandom()
            else -> FloatNumberToken.generateRandom()
        }
    }


    override fun toString(): String = value

    abstract override fun copy(): Primary
}

class Id(override val value: String) : Primary(value) {
    companion object {
        fun generateRandom() = Id("variable${Random.nextInt(10)}")
    }

    private val regex = "[a-zA-Z_][a-zA-Z_0-9]*"

    init {
        check(Regex(regex).matches(value))
        { "The value does not match regex!" }
    }

    override fun copy(): Id = Id(value)

    override fun getChildren(): Int = 1
    override fun setChildExpression(exp: Expression) {}

}

class IntNumberToken(override val value: String) : Primary(value) {
    companion object {
        fun generateRandom() = IntNumberToken(Random.nextInt(0, 1000).toString())
    }

    private val regex = "[0-9]+"

    init {
        check(Regex(regex).matches(value))
        { "The value does not match regex! $value" }
    }

    override fun copy(): IntNumberToken = IntNumberToken(value)

    override fun getChildren(): Int = 1
    override fun setChildExpression(exp: Expression) {}

}

class FloatNumberToken(override val value: String) : Primary(value) {
    companion object {
        fun generateRandom(): FloatNumberToken {
            val float = Random.nextInt(1, 1000) * Random.nextFloat()
            return FloatNumberToken(String.format("%.8f", float).replace(',', '.'))
        }
    }

    private val regex = "[0-9]+.[0-9]{8}"

    init {
        check(Regex(regex).matches(value))
        { "The value does not match regex! $value" }
    }

    override fun copy(): FloatNumberToken = FloatNumberToken(value)

    override fun getChildren(): Int = 1
    override fun setChildExpression(exp: Expression) {}

}


class BooleanToken(override val value: String) : Primary(value) {
    companion object {
        fun generateRandom() = BooleanToken(listOf("false", "true").random())
    }

    init {
        check(value == "true" || value == "false")
        { "The value does not match regex!  $value" }
    }

    override fun copy(): BooleanToken = BooleanToken(value)

    override fun getChildren(): Int = 1
    override fun setChildExpression(exp: Expression) {}

}
