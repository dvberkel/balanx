package nl.dvberkel.game;

public interface Evaluator<S, T> {
    Score<T> evaluate(S node);
}
