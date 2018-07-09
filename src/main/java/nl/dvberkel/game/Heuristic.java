package nl.dvberkel.game;

public interface Heuristic<S> {
    class Value implements Comparable<Value> {
        public static Value of(int intrinsicValue) {
            return new Value(intrinsicValue);
        }

        private final int intrinsicValue;

        private Value(int intrinsicValue) {
            this.intrinsicValue = intrinsicValue;
        }

        @Override
        public int compareTo(Value other) {
            return this.intrinsicValue - other.intrinsicValue;
        }

        @Override
        public String toString() {
            return String.format("<%d>", intrinsicValue);
        }
    }
    Value evaluate(S node);
}
