package nl.dvberkel.game.strategies;

import nl.dvberkel.game.Evaluator;
import nl.dvberkel.game.Score;
import nl.dvberkel.game.Strategy;
import nl.dvberkel.game.tictactoe.NodeGenerator;
import nl.dvberkel.game.tictactoe.TicTacToe;
import nl.dvberkel.game.tictactoe.TicTacToeBuilder;
import nl.dvberkel.game.tictactoe.exception.DuplicateTicTacToePositionPlacementException;
import org.junit.Before;
import org.junit.Test;

import static nl.dvberkel.game.Score.winFor;
import static nl.dvberkel.game.strategies.Random.randomStrategy;
import static nl.dvberkel.game.tictactoe.TicTacToe.Position.*;
import static nl.dvberkel.game.tictactoe.TicTacToe.Token.Cross;
import static nl.dvberkel.game.tictactoe.TicTacToeBuilder.board;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MinMaxTest {
    private Evaluator<TicTacToe, TicTacToe.Token> evaluator;
    private Strategy<TicTacToe> alternateStrategy;
    private Strategy<TicTacToe> strategy;

    @Before
    public void createStrategies() {
        evaluator = new nl.dvberkel.game.tictactoe.Evaluator();
        alternateStrategy = randomStrategy(NodeGenerator.instance());
        strategy = new MinMax(evaluator, alternateStrategy);
    }

    @Test
    public void winInOneShouldBeDetected() throws DuplicateTicTacToePositionPlacementException {
        TicTacToe initial = board().crossAt(C).dotAt(N).crossAt(NW).dotAt(NE).build();

        TicTacToe node = strategy.best(initial).get();

        Score<TicTacToe.Token> score = evaluator.evaluate(node);
        assertThat(score, is(winFor(Cross)));
    }

    @Test
    public void winInTwoShouldBeDetected() throws DuplicateTicTacToePositionPlacementException {
        TicTacToe initial = board().crossAt(C).dotAt(N).crossAt(NW).dotAt(SE).build();

        TicTacToe node = strategy.best(initial).get();
        node = alternateStrategy.best(node).get();
        node = strategy.best(node).get();

        Score<TicTacToe.Token> score = evaluator.evaluate(node);
        assertThat(score, is(winFor(Cross)));
    }
}
