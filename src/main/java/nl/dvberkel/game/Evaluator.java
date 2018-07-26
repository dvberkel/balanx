package nl.dvberkel.game;

public interface Evaluator<N, T> {
    Score<T> evaluate(N node);
}
