package nl.dvberkel.balanx;

public class Score<T> {
    public static <U> Score<U> winFor(U token) {
        return new Score.Win(token);
    }

    public static <U> Score<U> draw() {
        return new Score.Draw();
    }

    public static <U> Score<U> indeterminate() {
        return new Score.Indeterminate();
    }

    static class Win<V> extends Score<V> {
        private final V token;

        Win(V token) {
            this.token = token;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Score.Win<?> win = (Score.Win<?>) o;

            return token.equals(win.token);
        }

        @Override
        public int hashCode() {
            return token.hashCode();
        }
    }

    static class Draw<V> extends Score<V> {
        Draw() {}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return 31;
        }
    }

    static class Indeterminate<V> extends Score<V> {
        Indeterminate() {}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return 7;
        }
    }}
