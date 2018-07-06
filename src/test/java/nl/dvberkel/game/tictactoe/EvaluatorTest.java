package nl.dvberkel.game.tictactoe;

import nl.dvberkel.game.Score;
import nl.dvberkel.game.tictactoe.exception.DuplicateTicTacToePositionPlacementException;
import org.junit.Test;

import static nl.dvberkel.game.Score.draw;
import static nl.dvberkel.game.Score.indeterminate;
import static nl.dvberkel.game.Score.winFor;
import static nl.dvberkel.game.tictactoe.TicTacToe.Position.*;
import static nl.dvberkel.game.tictactoe.TicTacToe.Token.Cross;
import static nl.dvberkel.game.tictactoe.TicTacToeBuilder.board;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EvaluatorTest {
    @Test
    public void threeCarossesInARowIsAWin() throws DuplicateTicTacToePositionPlacementException {
        nl.dvberkel.game.Evaluator evaluator = new Evaluator();
        TicTacToe state = board().crossAt(C).dotAt(NE).crossAt(NW).dotAt(E).crossAt(SE).build();

        Score<TicTacToe.Token> score = evaluator.evaluate(state);

        assertThat(score, is(winFor(Cross)));
    }

    @Test
    public void noRowOnAFullBoardIsADraw() throws DuplicateTicTacToePositionPlacementException {
        nl.dvberkel.game.Evaluator evaluator = new Evaluator();
        TicTacToe state = board()
                .dotAt(NW).dotAt(N).crossAt(NE)
                .crossAt(W).crossAt(C).dotAt(E)
                .dotAt(SW).crossAt(S).crossAt(SE)
                .build();

        Score<TicTacToe.Token> score = evaluator.evaluate(state);

        assertThat(score, is(draw()));
    }

    @Test
    public void noRowOnWithPositionsToPlayIsIndeterminate() throws DuplicateTicTacToePositionPlacementException {
        nl.dvberkel.game.Evaluator evaluator = new Evaluator();
        TicTacToe state = board().crossAt(C).build();

        Score<TicTacToe.Token> score = evaluator.evaluate(state);

        assertThat(score, is(indeterminate()));
    }}

