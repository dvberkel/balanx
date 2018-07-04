package nl.dvberkel.balanx;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static nl.dvberkel.balanx.TicTacToe.Position.*;
import static nl.dvberkel.balanx.TicTacToeBuilder.board;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class TicTacToeTest {
    private MoveGenerator<TicTacToe> generate;

    @Before
    public void createTicTacToeMoveGenerator() {
        generate = TicTacToeMoveGenerator.instance();
    }

    @Test
    public void emptyBoardShouldHaveEmptyPositionsPlayable() throws DuplicateTicTacToePositionPlacementException {
        TicTacToe emptyBoard = TicTacToe.empty();

        List<TicTacToe> nextMoves = generate.movesFor(emptyBoard);

        TicTacToeBuilder emptyBoardWith = board();
        assertThat(nextMoves, contains(
                emptyBoardWith.crossAt(NW).build(),
                emptyBoardWith.crossAt(N).build(),
                emptyBoardWith.crossAt(NE).build(),
                emptyBoardWith.crossAt(W).build(),
                emptyBoardWith.crossAt(C).build(),
                emptyBoardWith.crossAt(E).build(),
                emptyBoardWith.crossAt(SW).build(),
                emptyBoardWith.crossAt(S).build(),
                emptyBoardWith.crossAt(SE).build()
        ));
    }

    @Test
    public void boardWithOneTokenShouldHaveEmptyPositionsPlayable() throws DuplicateTicTacToePositionPlacementException {
        TicTacToeBuilder startingBoardWith = board().crossAt(C);
        TicTacToe startingBoard = startingBoardWith.build();

        List<TicTacToe> nextMoves = generate.movesFor(startingBoard);

        assertThat(nextMoves, contains(
                startingBoardWith.dotAt(NW).build(),
                startingBoardWith.dotAt(N).build(),
                startingBoardWith.dotAt(NE).build(),
                startingBoardWith.dotAt(W).build(),
                startingBoardWith.dotAt(E).build(),
                startingBoardWith.dotAt(SW).build(),
                startingBoardWith.dotAt(S).build(),
                startingBoardWith.dotAt(SE).build()
        ));
    }

    @Test
    public void boardWithTwoTokensShouldHaveEmptyPositionsPlayable() throws DuplicateTicTacToePositionPlacementException {
        TicTacToeBuilder startingBoardWith = board().crossAt(C).dotAt(NW);
        TicTacToe startingBoard = startingBoardWith.build();

        List<TicTacToe> nextMoves = generate.movesFor(startingBoard);

        assertThat(nextMoves, contains(
                startingBoardWith.crossAt(N).build(),
                startingBoardWith.crossAt(NE).build(),
                startingBoardWith.crossAt(W).build(),
                startingBoardWith.crossAt(E).build(),
                startingBoardWith.crossAt(SW).build(),
                startingBoardWith.crossAt(S).build(),
                startingBoardWith.crossAt(SE).build()
        ));
    }
}

class TicTacToeBuilder {
    public static TicTacToeBuilder board() {
        return new TicTacToeBuilder();
    }

    private final TicTacToe board;

    private TicTacToeBuilder() {
        this.board = TicTacToe.empty();
    }

    private TicTacToeBuilder(TicTacToe board) {
        this.board = board;
    }

    public TicTacToeBuilder crossAt(TicTacToe.Position position) throws DuplicateTicTacToePositionPlacementException {
        return place(position, TicTacToe.Token.Cross);
    }

    public TicTacToeBuilder dotAt(TicTacToe.Position position) throws DuplicateTicTacToePositionPlacementException {
        return place(position, TicTacToe.Token.Dot);
    }

    private TicTacToeBuilder place(TicTacToe.Position position, TicTacToe.Token token) throws DuplicateTicTacToePositionPlacementException {
        Optional<TicTacToe> candidate = position.place(this.board, token);
        if (candidate.isPresent()) {
            return new TicTacToeBuilder(candidate.get());
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

        public Optional<TicTacToe> place(TicTacToe board, Token token) {
            return board.place(index, token);
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

    private Optional<TicTacToe> place(int index, Token token) {
        if (tokens[index].equals(Token.Empty)) {
            Token[] placedTokens = Arrays.copyOf(tokens, tokens.length);
            placedTokens[index] = token;
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

    public Token tokenToPlay() {
        int crossCount = 0; int dotCount = 0;
        for (Token token : tokens) {
            if (token.equals(Token.Cross)) { crossCount++; }
            if (token.equals(Token.Dot)) {dotCount++; }
        }
        return dotCount >= crossCount ? Token.Cross: Token.Dot;
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

class TicTacToeMoveGenerator implements MoveGenerator<TicTacToe> {
    public static MoveGenerator<TicTacToe> instance() {
        return new TicTacToeMoveGenerator();
    }


    @Override
    public List<TicTacToe> movesFor(TicTacToe state) {
        List<TicTacToe.Position> playable = state.playablePositions();
        TicTacToe.Token tokenToPlay = state.tokenToPlay();
        return playable
                .stream()
                .map(position -> position.place(state, tokenToPlay).get())
                .collect(Collectors.toList());
    }
}

interface MoveGenerator<T> {
    List<T> movesFor(T state);
}