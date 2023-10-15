
    fun main() {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1\$tF %1\$tT] [%4$-7s] %5\$s %n")
        DataGenerator.generateInputs()
    }

/* TODO:
* wygeneruj dane (dat?)
* uruchom program dla zestawu danych i output zapisz w pliku tekstowym
* wyszukaj najlepszy wynik
* narysuj wykresy wyniku i oryginału
* */