package neoGP


fun main(args: Array<String>) {
    if (args.size != 1)
        throw UnsupportedOperationException("Wrong number of program's input arguments! (${args.size})")

    val data = NeoGP.loadParams(args.first())
    NeoProperties.inputsOutputs = data.params
    NeoProperties.fitnessFunction = data.fitnessFunction
    NeoGP.evolve()
}
