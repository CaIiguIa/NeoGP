package newGP.model.grammar

import kotlin.random.Random

abstract class Primary(override val value: String) : Expression(value) {
    companion object {
        fun generateRandom() = when (Random.nextInt(4)) {
            0 -> Id.generateRandom()
            1 -> NumberToken.generateRandom()
            2 -> BooleanToken.generateRandom()
            else -> StringToken.generateRandom()
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
}

class NumberToken(override val value: String) : Primary(value) {
    companion object {
        fun generateRandom() = NumberToken(Random.nextInt(0, 1000).toString())
    }

    private val regex = "[0-9]+"

    init {
        check(Regex(regex).matches(value))
        { "The value does not match regex!" }
    }

    override fun copy(): NumberToken = NumberToken(value)
}

class BooleanToken(override val value: String) : Primary(value) {
    companion object {
        fun generateRandom() = BooleanToken(listOf("false", "true").random())
    }

    init {
        check(value == "true" || value == "false")
        { "The value does not match regex!" }
    }

    override fun copy(): BooleanToken = BooleanToken(value)
}

class StringToken(override val value: String) : Primary(value) {
    companion object {
        fun generateRandom() = StringToken("string${Random.nextInt(10)}")
    }

    private val regex = """[^\r\n]*"""

    init {
        check(Regex(regex).matches(value))
        { "The value does not match regex!" }
    }

    override fun toString(): String =
        "\"$value\""

    override fun copy(): StringToken = StringToken(value)
}

