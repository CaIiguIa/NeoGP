package TinyGP;

public class Individual {
    char[] data;

    Individual(char[] data) {
        this.data = data;
    }

    Individual copy() {
        Individual copy = new Individual(new char[data.length]);
        System.arraycopy(this.data, 0, copy.data, 0, this.data.length);
        return copy;
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
            if (this.data[bufferCounter] < Properties.varnumber)
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
}