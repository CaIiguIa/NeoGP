package newGP.model.grammar

abstract class Primary(override val value: String) : Expression(value) {

    override fun toString(): String = value
}

class Id(override val value: String) : Primary(value) {
    private val regex = "[a-zA-Z_][a-zA-Z_0-9]*"

    init {
        check(!Regex(regex).matches(value))
        { "The value does not match regex!" }
    }
}

class Number(override val value: String) : Primary(value) {
    private val regex = "[0-9]+"

    init {
        check(!Regex(regex).matches(value))
        { "The value does not match regex!" }
    }
}

class Boolean(override val value: String) : Primary(value) {
    private val regex = "(true|false)"

    init {
        check(!Regex(regex).matches(value))
        { "The value does not match regex!" }
    }
}

class StringToken(override val value: String) : Primary(value) {
    private val regex = "\"~(\\r | \\n)* \""

    init {
        check(!Regex(regex).matches(value))
        { "The value does not match regex!" }
    }
}

