package newGP.model.grammar

abstract class Token {
    abstract val value: String
}

class Id(override val value: String) : Token() {
    private val regex = "[a-zA-Z_][a-zA-Z_0-9]*"

    init {
        check (!Regex(regex).matches(value))
        {"The value does not match regex!" }
    }
}

class Number(override val value: String) : Token() {
    private val regex = "[0-9]+"

    init {
        check (!Regex(regex).matches(value))
        {"The value does not match regex!" }
    }
}

class Boolean(override val value: String) : Token() {
    private val regex = "(true|false)"

    init {
        check (!Regex(regex).matches(value))
        {"The value does not match regex!" }
    }
}

class StringToken(override val value: String) : Token() {
    private val regex = "\"~(\\r | \\n)* \""

    init {
        check (!Regex(regex).matches(value))
        {"The value does not match regex!" }
    }
}

