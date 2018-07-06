package nl.dvberkel.balanx.tictactoe;

import nl.dvberkel.balanx.Score;
import nl.dvberkel.balanx.tictactoe.exception.DuplicateTicTacToePositionPlacementException;
import org.junit.Test;

import static nl.dvberkel.balanx.Score.draw;
import static nl.dvberkel.balanx.Score.indeterminate;
import static nl.dvberkel.balanx.Score.winFor;
import static nl.dvberkel.balanx.tictactoe.TicTacToe.Position.*;
import static nl.dvberkel.balanx.tictactoe.TicTacToe.Token.Cross;
import static nl.dvberkel.balanx.tictactoe.TicTacToeBuilder.board;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StateEvaluatorTest {
    @Test
    public void threeCarossesInARowIsAWin() throws DuplicateTicTacToePositionPlacementException {
        nl.dvberkel.balanx.Evaluator evaluator = new Evaluator();
        TicTacToe state = board().crossAt(C).dotAt(NE).crossAt(NW).dotAt(E).crossAt(SE).build();

        Score<TicTacToe.Token> score = evaluator.evaluate(state);

        assertThat(score, is(winFor(Cross)));
    }

    @Test
    public void noRowOnAFullBoardIsADraw() throws DuplicateTicTacToePositionPlacementException {
        nl.dvberkel.balanx.Evaluator evaluator = new Evaluator();
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
        nl.dvberkel.balanx.Evaluator evaluator = new Evaluator();
        TicTacToe state = board().crossAt(C).build();

        Score<TicTacToe.Token> score = evaluator.evaluate(state);

        assertThat(score, is(indeterminate()));
    }}

