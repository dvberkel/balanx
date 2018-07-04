package nl.dvberkel.balanx.tictactoe;

import nl.dvberkel.balanx.tictactoe.exception.DuplicateTicTacToePositionPlacementException;
import org.junit.Test;

import static nl.dvberkel.balanx.tictactoe.Score.winFor;
import static nl.dvberkel.balanx.tictactoe.TicTacToe.Position.*;
import static nl.dvberkel.balanx.tictactoe.TicTacToe.Token.Cross;
import static nl.dvberkel.balanx.tictactoe.TicTacToeBuilder.board;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StateEvaluatorTest {
    @Test
    public void t() throws DuplicateTicTacToePositionPlacementException {
        Evaluator<TicTacToe, TicTacToe.Token> evaluator = new TicTacToeEvaluator();
        TicTacToe state = board().crossAt(C).dotAt(NE).crossAt(NW).dotAt(E).crossAt(SE).build();

        Score<TicTacToe.Token> score = evaluator.evaluate(state);

        assertThat(score, is(winFor(Cross)));
    }
}

interface Evaluator<S, T> {
    Score<T> evaluate(S state);
}

class TicTacToeEvaluator implements Evaluator<TicTacToe, TicTacToe.Token> {

    @Override
    public Score<TicTacToe.Token> evaluate(TicTacToe state) {
        return winFor(Cross);
    }
}

class Score<T> {
    public static <U> Score<U> winFor(U token) {
        return new Win(token);
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
}
