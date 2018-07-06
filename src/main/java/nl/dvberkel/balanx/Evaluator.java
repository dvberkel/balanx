package nl.dvberkel.balanx;

public interface Evaluator<S, T> {
    Score<T> evaluate(S state);
}
