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
}