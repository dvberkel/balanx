package nl.dvberkel.balanx.tictactoe;

import nl.dvberkel.balanx.tictactoe.exception.DuplicateTicTacToePositionPlacementException;
import org.junit.Test;

import java.util.Optional;

import static nl.dvberkel.balanx.tictactoe.Score.draw;
import static nl.dvberkel.balanx.tictactoe.Score.indeterminate;
import static nl.dvberkel.balanx.tictactoe.Score.winFor;
import static nl.dvberkel.balanx.tictactoe.TicTacToe.Position.*;
import static nl.dvberkel.balanx.tictactoe.TicTacToe.Token.Cross;
import static nl.dvberkel.balanx.tictactoe.TicTacToeBuilder.board;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StateEvaluatorTest {
    @Test
    public void threeCarossesInARowIsAWin() throws DuplicateTicTacToePositionPlacementException {
        Evaluator<TicTacToe, TicTacToe.Token> evaluator = new TicTacToeEvaluator();
        TicTacToe state = board().crossAt(C).dotAt(NE).crossAt(NW).dotAt(E).crossAt(SE).build();

        Score<TicTacToe.Token> score = evaluator.evaluate(state);

        assertThat(score, is(winFor(Cross)));
    }

    @Test
    public void noRowOnAFullBoardIsADraw() throws DuplicateTicTacToePositionPlacementException {
        Evaluator<TicTacToe, TicTacToe.Token> evaluator = new TicTacToeEvaluator();
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
        Evaluator<TicTacToe, TicTacToe.Token> evaluator = new TicTacToeEvaluator();
        TicTacToe state = board().crossAt(C).build();

        Score<TicTacToe.Token> score = evaluator.evaluate(state);

        assertThat(score, is(indeterminate()));
    }}

interface Evaluator<S, T> {
    Score<T> evaluate(S state);
}

class TicTacToeEvaluator implements Evaluator<TicTacToe, TicTacToe.Token> {

    @Override
    public Score<TicTacToe.Token> evaluate(TicTacToe state) {
        Optional<TicTacToe.Token> winner = state.won();
        if (winner.isPresent()) {
            return winFor(winner.get());
        } else {
            if (state.playablePositions().isEmpty()) {
                return draw();
            } else {
                return indeterminate();
            }
        }
    }
}

class Score<T> {
    public static <U> Score<U> winFor(U token) {
        return new Win(token);
    }

    public static <U> Score<U> draw() {
        return new Draw();
    }

    public static <U> Score<U> indeterminate() {
        return new Indeterminate();
    }

    static class Win<V> extends Score<V> {
        private final V token;

        Win(V token) {
            this.token = token;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Win<?> win = (Win<?>) o;

            return token.equals(win.token);
        }

        @Override
        public int hashCode() {
            return token.hashCode();
        }
    }

    static class Draw<V> extends Score<V> {
        Draw() {}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return 31;
        }
    }

    static class Indeterminate<V> extends Score<V> {
        Indeterminate() {}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return 7;
        }
    }}
