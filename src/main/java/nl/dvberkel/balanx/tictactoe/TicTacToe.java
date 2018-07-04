package nl.dvberkel.balanx.tictactoe;

import nl.dvberkel.balanx.tictactoe.exception.IllegalPositionIndexException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicTacToe {
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
