package newGP.model.grammar

abstract class Primary(override val value: String) : Expression(value) {

    override fun toString(): String = value

    abstract override fun copy(): Primary
}

class Id(override val value: String) : Primary(value) {
    private val regex = "[a-zA-Z_][a-zA-Z_0-9]*"

    init {
        check(!Regex(regex).matches(value))
        { "The value does not match regex!" }
    }

    override fun copy(): Id = Id(value)
}

class NumberToken(override val value: String) : Primary(value) {
    private val regex = "[0-9]+"

    init {
        check(!Regex(regex).matches(value))
        { "The value does not match regex!" }
    }

    override fun copy(): NumberToken = NumberToken(value)
}

class Boolean(override val value: String) : Primary(value) {
    private val regex = "(true|false)"

    init {
        check(!Regex(regex).matches(value))
        { "The value does not match regex!" }
    }

    override fun copy(): Boolean = Boolean(value)
}

class StringToken(override val value: String) : Primary(value) {
    private val regex = "\"~(\\r | \\n)* \""

    init {
        check(!Regex(regex).matches(value))
        { "The value does not match regex!" }
    }

    override fun copy(): StringToken = StringToken(value)
}

