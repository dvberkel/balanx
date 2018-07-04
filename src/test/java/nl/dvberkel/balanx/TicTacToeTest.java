package nl.dvberkel.balanx;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static nl.dvberkel.balanx.TicTacToe.Position.*;
import static nl.dvberkel.balanx.TicTacToeBuilder.board;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;

public class TicTacToeTest {
    @Test
    public void emptyBoardShouldHaveAllPositionsPlayable() throws DuplicateTicTacToePositionPlacementException {
        MoveGenerator<TicTacToe> generate = TicTacToeGenerator.instance();
        TicTacToe emptyBoard = TicTacToe.empty();

        List<TicTacToe> nextMoves = generate.movesFor(emptyBoard);

        assertThat(nextMoves, is(not(nullValue())));
        assertThat(nextMoves, is(not(empty())));
        assertThat(nextMoves, contains(
                board().crossAt(NW).build(),
                board().crossAt(N).build(),
                board().crossAt(NE).build(),
                board().crossAt(W).build(),
                board().crossAt(C).build(),
                board().crossAt(E).build(),
                board().crossAt(SW).build(),
                board().crossAt(S).build(),
                board().crossAt(SE).build()
        ));
    }
}

class TicTacToeBuilder {
    public static TicTacToeBuilder board() {
        return new TicTacToeBuilder();
    }

    private TicTacToe board;

    private TicTacToeBuilder() {
        this.board = TicTacToe.empty();
    }

    public TicTacToeBuilder crossAt(TicTacToe.Position position) throws DuplicateTicTacToePositionPlacementException {
        Optional<TicTacToe> candidate = position.place(this.board);
        if (candidate.isPresent()) {
            this.board = candidate.get();
            return this;
        } else {
            throw new DuplicateTicTacToePositionPlacementException(position);
        }
    }

    public TicTacToe build() {
        return this.board;
    }
}

class DuplicateTicTacToePositionPlacementException extends Exception {
    public DuplicateTicTacToePositionPlacementException(TicTacToe.Position position) {
        super(String.format("token is already placed at position %s", position));
    }
}

class IllegalPositionIndexException extends RuntimeException {
    public IllegalPositionIndexException(int index) {
        super(String.format("index %d is not a valid Position index", index));
    }
}

class TicTacToe {
    public static enum Position {
        NW(0), N(1), NE(2),
        W(3),  C(4), E(5),
        SW(6), S(7), SE(8);

        private int index;

        Position(int index) {
            this.index = index;
        }

        public Optional<TicTacToe> place(TicTacToe board) {
            return board.place(index);
        }

        public static Position from(int index) {
            for (Position candidate : Position.values()) {
                if (candidate.index == index) {
                    return candidate;
                }
            }
            throw new IllegalPositionIndexException(index);
        }
    }

    static enum Token {
        Cross,
        Dot,
        Empty
    }

    public static TicTacToe empty() {
        return new TicTacToe();
    }

    private final Token[] tokens;

    private TicTacToe() {
        this(new Token[]{
                Token.Empty, Token.Empty, Token.Empty,
                Token.Empty, Token.Empty, Token.Empty,
                Token.Empty, Token.Empty, Token.Empty,
        });
    }

    private TicTacToe(Token[] tokens) {
        this.tokens = tokens;
    }

    private Optional<TicTacToe> place(int index) {
        if (tokens[index].equals(Token.Empty)) {
            Token[] placedTokens = Arrays.copyOf(tokens, tokens.length);
            placedTokens[index] = Token.Cross;
            return Optional.of(new TicTacToe(placedTokens));
        } else {
            return Optional.empty();
        }
    }

    public List<Position> playablePositions() {
        return IntStream.range(0, tokens.length)
                .filter(index -> tokens[index].equals(Token.Empty))
                .mapToObj(index -> Position.from(index))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicTacToe ticTacToe = (TicTacToe) o;

        return IntStream.range(0, tokens.length)
                .filter(index -> !tokens[index].equals(ticTacToe.tokens[index]))
                .count() == 0;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(tokens);
    }

    @Override
    public String toString() {
        return "TicTacToe{" +
                "tokens=" + Arrays.toString(tokens) +
                '}';
    }
}

class TicTacToeGenerator implements MoveGenerator<TicTacToe> {
    public static MoveGenerator<TicTacToe> instance() {
        return new TicTacToeGenerator();
    }


    @Override
    public List<TicTacToe> movesFor(TicTacToe state) {
        List<TicTacToe.Position> playable = state.playablePositions();
        return playable
                .stream()
                .map(position -> position.place(state).get())
                .collect(Collectors.toList());
    }
}

interface MoveGenerator<T> {
    List<T> movesFor(T state);
}