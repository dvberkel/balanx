package nl.dvberkel.balanx.tictactoe;

import nl.dvberkel.balanx.tictactoe.exception.IllegalPositionIndexException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static nl.dvberkel.balanx.tictactoe.TicTacToe.Position.*;
import static nl.dvberkel.balanx.tictactoe.TicTacToe.WinningRule.winWhenSameTokenAt;

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
    private final static List<WinningRule> winningRules = new ArrayList<>();
    private static List<WinningRule> winningRules() {
        if (winningRules.isEmpty()) {
            winningRules.add(winWhenSameTokenAt(NW, N, NE));
            winningRules.add(winWhenSameTokenAt(W, C, E));
            winningRules.add(winWhenSameTokenAt(SW, S, SE));
            winningRules.add(winWhenSameTokenAt(NW, W, SW));
            winningRules.add(winWhenSameTokenAt(N, C, S));
            winningRules.add(winWhenSameTokenAt(NE, E, SE));
            winningRules.add(winWhenSameTokenAt(NW, C, SE));
            winningRules.add(winWhenSameTokenAt(NE, C, SW));
        }
        return winningRules;
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

    public Optional<Token> won() {
        for (WinningRule rule: TicTacToe.winningRules()) {
            if (rule.applies(this)) {
                return Optional.of(rule.winner(this));
            }
        }
        return Optional.empty();
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

    static class WinningRule {
        public static WinningRule winWhenSameTokenAt(Position... positions) {
            return new WinningRule(positions);
        }

        private final Position[] positions;

        private WinningRule(Position[] positions) {
            this.positions = positions;
        }

        public boolean applies(TicTacToe state) {
            Token target = state.tokens[positions[0].index];
            return Arrays.stream(positions)
                    .allMatch(position -> state.tokens[position.index].equals(target));
        }

        public Token winner(TicTacToe state) {
            return state.tokens[positions[0].index];
        }
    }}
