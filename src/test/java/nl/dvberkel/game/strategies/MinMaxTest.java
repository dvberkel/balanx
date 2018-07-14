package nl.dvberkel.game.strategies;

import nl.dvberkel.game.Evaluator;
import nl.dvberkel.game.Score;
import nl.dvberkel.game.Strategy;
import nl.dvberkel.game.tictactoe.NodeGenerator;
import nl.dvberkel.game.tictactoe.TicTacToe;
import nl.dvberkel.game.tictactoe.exception.DuplicateTicTacToePositionPlacementException;
import org.junit.Test;

import static nl.dvberkel.game.Score.winFor;
import static nl.dvberkel.game.strategies.Random.randomStrategy;
import static nl.dvberkel.game.tictactoe.TicTacToe.Position.*;
import static nl.dvberkel.game.tictactoe.TicTacToe.Token.Cross;
import static nl.dvberkel.game.tictactoe.TicTacToeBuilder.board;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MinMaxTest {
    @Test
    public void winInOneShouldBeDetected() throws DuplicateTicTacToePositionPlacementException {
        Evaluator<TicTacToe, TicTacToe.Token> evaluator = new nl.dvberkel.game.tictactoe.Evaluator();
        Strategy<TicTacToe> strategy = new MinMax(evaluator, randomStrategy(NodeGenerator.instance()));
        TicTacToe initial = board().crossAt(C).dotAt(N).crossAt(NW).dotAt(NE).build();

        TicTacToe node = strategy.best(initial).get();

        Score<TicTacToe.Token> score = evaluator.evaluate(node);
        assertThat(score, is(winFor(Cross)));
    }

    @Test
    public void winInTwoShouldBeDetected() throws DuplicateTicTacToePositionPlacementException {
        Evaluator<TicTacToe, TicTacToe.Token> evaluator = new nl.dvberkel.game.tictactoe.Evaluator();
        Strategy<TicTacToe> alternateStrategy = randomStrategy(NodeGenerator.instance());
        Strategy<TicTacToe> strategy = new MinMax(evaluator, alternateStrategy);
        TicTacToe initial = board().crossAt(C).dotAt(N).crossAt(NW).dotAt(SE).build();

        TicTacToe node = strategy.best(initial).get();
        node = alternateStrategy.best(node).get();
        node = strategy.best(node).get();

        Score<TicTacToe.Token> score = evaluator.evaluate(node);
        assertThat(score, is(winFor(Cross)));
    }
}
