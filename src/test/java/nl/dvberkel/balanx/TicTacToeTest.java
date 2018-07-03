package nl.dvberkel.balanx;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;

public class TicTacToeTest {
    @Test
    public void emptyBoardShouldHaveSomeMoves() {
        MoveGenerator<TicTacToe> generate = TicTacToeGenerator.instance();
        TicTacToe initialBoard = TicTacToe.empty();

        List<TicTacToe> result = generate.movesFor(initialBoard);

        assertThat(result, is(not(nullValue())));
        assertThat(result, is(not(empty())));
    }
}

class TicTacToe {

    public static TicTacToe empty() {
        return null;
    }
}

class TicTacToeGenerator implements MoveGenerator<TicTacToe> {
    public static MoveGenerator<TicTacToe> instance() {
        return new TicTacToeGenerator();
    }


    @Override
    public List<TicTacToe> movesFor(TicTacToe state) {
        return Arrays.asList(state);
    }
}

interface MoveGenerator<T> {
    List<T> movesFor(T state);
}