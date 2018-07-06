package nl.dvberkel.game.strategies;

import nl.dvberkel.game.NodeGenerator;
import nl.dvberkel.game.Strategy;

import java.util.List;
import java.util.Optional;

public class Random<N> implements Strategy<N> {
    public static <T> Strategy<T> randomStrategy(NodeGenerator<T> generator) {
        return new Random(generator);
    }

    private final java.util.Random random = new  java.util.Random();
    private final NodeGenerator<N> generator;

    private Random(NodeGenerator<N> generator) {
        this.generator = generator;
    }

    @Override
    public Optional<N> best(N node) {
        List<N> candidates = generator.movesFor(node);
        if (!candidates.isEmpty()) {
            int winnerIndex = random.nextInt(candidates.size());
            return Optional.of(candidates.get(winnerIndex));
        } else {
            return Optional.empty();
        }
    }
}
