package nl.dvberkel.game.strategies;

import nl.dvberkel.game.Score;
import nl.dvberkel.game.Strategy;
import nl.dvberkel.game.Evaluator;
import nl.dvberkel.game.tictactoe.TicTacToe;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MinMax implements Strategy<TicTacToe> {
    private final Evaluator<TicTacToe, TicTacToe.Token> evaluator;
    private final Strategy<TicTacToe> alternateStrategy;

    public MinMax(Evaluator<TicTacToe, TicTacToe.Token> evaluator, Strategy<TicTacToe> alternateStrategy) {
        this.evaluator = evaluator;
        this.alternateStrategy = alternateStrategy;
    }

    @Override
    public Optional<TicTacToe> best(TicTacToe node) {
        TicTacToe.Token tokenToPlay = node.tokenToPlay();
        List<TicTacToe.Position> positions = node.playablePositions();

        List<Evaluator<TicTacToe, TicTacToe.Token>> evaluators = Arrays.asList(
                evaluator,
                new MinMaxEvaluator(evaluator, tokenToPlay)
        );
        for (Evaluator<TicTacToe, TicTacToe.Token> currentEvaluator: evaluators) {
            Optional<TicTacToe> candidate = findAWin(currentEvaluator, node, tokenToPlay, positions);
            if (candidate.isPresent()) { return candidate; }
        }

        return alternateStrategy.best(node);
    }

    private Optional<TicTacToe> findAWin(Evaluator<TicTacToe, TicTacToe.Token> currentEvaluator, TicTacToe node, TicTacToe.Token tokenToPlay, List<TicTacToe.Position> positions) {
        for (TicTacToe.Position position : positions) {
            TicTacToe play = position.place(node, tokenToPlay).get();
            Score<TicTacToe.Token> score = currentEvaluator.evaluate(play);
            if (score.equals(Score.winFor(tokenToPlay))) {
                return Optional.of(play);
            }
        }
        return Optional.empty();
    }
}

class MinMaxEvaluator implements Evaluator<TicTacToe, TicTacToe.Token> {
    private final Evaluator<TicTacToe, TicTacToe.Token> evaluator;
    private final TicTacToe.Token tokenToPlay;

    MinMaxEvaluator(Evaluator<TicTacToe, TicTacToe.Token> evaluator, TicTacToe.Token tokenToPlay) {
        this.evaluator = evaluator;
        this.tokenToPlay = tokenToPlay;
    }

    @Override
    public Score<TicTacToe.Token> evaluate(TicTacToe node) {
        return evaluate(node, tokenToPlay);
    }
    private Score<TicTacToe.Token> evaluate(TicTacToe node, TicTacToe.Token tokenThatPlayed) {
        Score<TicTacToe.Token> score = evaluator.evaluate(node);
        if (score.equals(Score.indeterminate())) {
            TicTacToe.Token tokenToPlay = node.tokenToPlay();
            List<TicTacToe.Position> positions = node.playablePositions();
            boolean allWinsForTokenThatPlayed = true;
            for (TicTacToe.Position position: positions) {
                TicTacToe aNode = position.place(node, tokenToPlay).get();
                Score<TicTacToe.Token> aScore = evaluate(aNode, tokenToPlay);
                if (aScore.equals(Score.winFor(tokenToPlay))) {
                    return aScore;
                } else if (!aScore.equals(Score.winFor(tokenThatPlayed))) {
                    allWinsForTokenThatPlayed = false;
                }
            }
            if (allWinsForTokenThatPlayed) {
                return Score.winFor(tokenThatPlayed);
            } else {
                return Score.draw();
            }
        } else {
            return score;
        }
    }
}
