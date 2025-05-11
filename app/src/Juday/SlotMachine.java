package Juday;

import java.util.Random;

public class SlotMachine {
    private Random rand;

    public SlotMachine() {
        rand = new Random();
    }

    public Symbol[] spin() {
        Symbol[] result = new Symbol[3];
        for (int i = 0; i < 3; i++) {
            result[i] = randomSymbol();
        }
        return result;
    }

    private Symbol randomSymbol() {
        Symbol[] values = Symbol.values();
        return values[rand.nextInt(values.length)];
    }

    public int calculatePayout(Symbol[] result, int bet) {
        // Hitung frekuensi masing-masing simbol
        int[] counts = new int[Symbol.values().length];
        for (Symbol s : result) {
            counts[s.ordinal()]++;
        }

        for (int i = 0; i < counts.length; i++) {
            int count = counts[i];
            Symbol symbol = Symbol.values()[i];
            if (count == 3) {
                switch (symbol) {
                    case SEVEN:
                        return bet * 10;
                    case BELL:
                    case HEART:
                        return bet * 5;
                    case FRUIT:
                        return bet * 3;
                }
            } else if (count == 2 && symbol == Symbol.FRUIT) {
                return bet * 2;
            }
        }

        return 0; // Tidak ada kombinasi menang
    }
}

