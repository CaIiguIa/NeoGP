package TinyGP;

public class Individual {
    char[] data;

    Individual(char[] data) {
        this.data = data;
    }

    public int getTreeLength(int bufferCount) {
        if (!Properties.isOperation(this.data[bufferCount]))
            return (++bufferCount);

        return switch (this.data[bufferCount]) {
            case Properties.ADD, Properties.SUB, Properties.MUL, Properties.DIV -> (getTreeLength(getTreeLength(++bufferCount)));
            default -> (0);
        };
    }

    int printIndividual(int bufferCounter) {
        int a1 = 0, a2;

        if (!Properties.isOperation(this.data[bufferCounter])) {
            if (this.data[bufferCounter] < Properties.varNumber)
                System.out.print("X" + (this.data[bufferCounter] + 1) + " ");
            else
                System.out.print(Properties.x[this.data[bufferCounter]]);
            return (++bufferCounter);
        }

        System.out.print("(");


        switch (this.data[bufferCounter]) {
            case Properties.ADD -> {
                a1 = printIndividual(++bufferCounter);
                System.out.print(" + ");
            }
            case Properties.SUB -> {
                a1 = printIndividual(++bufferCounter);
                System.out.print(" - ");
            }
            case Properties.MUL -> {
                a1 = printIndividual(++bufferCounter);
                System.out.print(" * ");
            }
            case Properties.DIV -> {
                a1 = printIndividual(++bufferCounter);
                System.out.print(" / ");
            }
        }
        a2 = printIndividual(a1);
        System.out.print(")");

        return (a2);
    }

    double calculateFitness() {
        double result, fit = 0.0;

        for (int i = 0; i < Properties.fitnessCases; i++) {
            if (Properties.varNumber >= 0)
                System.arraycopy(Properties.targets[i], 0, Properties.x, 0, Properties.varNumber);
            Properties.program = this;
            Properties.PC = 0;
            result = run();
            fit += Math.abs(result - Properties.targets[i][Properties.varNumber]);
        }

        return (-fit);
    }

    private double run() {
        char primitive = Properties.program.data[Properties.PC++];

        if (primitive < Properties.FUNCTION_SET_START)
            return (Properties.x[primitive]);

        switch (primitive) {
            case Properties.ADD:
                return (run() + run());
            case Properties.SUB:
                return (run() - run());
            case Properties.MUL:
                return (run() * run());
            case Properties.DIV: {
                double num = run(), den = run();
                if (Math.abs(den) <= 0.001)
                    return (num);
                else
                    return (num / den);
            }
        }

        return (0.0); // should never get here
    }
}