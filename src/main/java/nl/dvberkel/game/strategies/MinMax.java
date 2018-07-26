package nl.dvberkel.game.strategies;

import nl.dvberkel.game.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MinMax<T, P extends Playable<N, T>, N extends Node<T, P>> implements Strategy<N> {
    private final Evaluator<N, T> evaluator;
    private final Strategy<N> alternateStrategy;

    public MinMax(Evaluator<N, T> evaluator, Strategy<N> alternateStrategy) {
        this.evaluator = evaluator;
        this.alternateStrategy = alternateStrategy;
    }

    @Override
    public Optional<N> best(N node) {
        T tokenToPlay = node.tokenToPlay();
        List<P> positions = node.playablePositions();

        List<Evaluator<N, T>> evaluators = Arrays.asList(
                evaluator,
                new MinMaxEvaluator(evaluator, tokenToPlay)
        );
        for (Evaluator<N, T> currentEvaluator: evaluators) {
            Optional<N> candidate = findAWin(currentEvaluator, node, tokenToPlay, positions);
            if (candidate.isPresent()) { return candidate; }
        }

        return alternateStrategy.best(node);
    }

    private Optional<N> findAWin(Evaluator<N, T> currentEvaluator, N node, T tokenToPlay, List<P> positions) {
        for (P position : positions) {
            N play = position.place(node, tokenToPlay).get();
            Score<T> score = currentEvaluator.evaluate(play);
            if (score.equals(Score.winFor(tokenToPlay))) {
                return Optional.of(play);
            }
        }
        return Optional.empty();
    }
}

class MinMaxEvaluator<T, P extends Playable<N, T>, N extends Node<T, P>> implements Evaluator<N, T> {
    private final Evaluator<N, T> evaluator;
    private final T tokenToPlay;

    MinMaxEvaluator(Evaluator<N, T> evaluator, T tokenToPlay) {
        this.evaluator = evaluator;
        this.tokenToPlay = tokenToPlay;
    }

    @Override
    public Score<T> evaluate(N node) {
        return evaluate(node, tokenToPlay);
    }
    private Score<T> evaluate(N node, T tokenThatPlayed) {
        Score<T> score = evaluator.evaluate(node);
        if (score.equals(Score.indeterminate())) {
            T tokenToPlay = node.tokenToPlay();
            List<P> positions = node.playablePositions();
            List<Score<T>> allNonLooseOutcomes = new ArrayList<>();
            for (P position: positions) {
                N aNode = position.place(node, tokenToPlay).get();
                Score<T> aScore = evaluate(aNode, tokenToPlay);
                if (aScore.equals(Score.winFor(tokenToPlay))) {
                    return aScore;
                } else if (!aScore.equals(Score.winFor(tokenThatPlayed))) {
                    allNonLooseOutcomes.add(aScore);
                }
            }
            if (!allNonLooseOutcomes.contains(Score.draw())) {
                return Score.winFor(tokenThatPlayed);
            } else {
                return Score.draw();
            }
        } else {
            return score;
        }
    }
}
