package nl.dvberkel.game.tictactoe;

import javafx.geometry.Pos;
import nl.dvberkel.game.Heuristic;
import nl.dvberkel.game.Node;
import nl.dvberkel.game.tictactoe.exception.IllegalPositionIndexException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static nl.dvberkel.game.tictactoe.TicTacToe.Position.*;
import static nl.dvberkel.game.tictactoe.TicTacToe.Token.Empty;
import static nl.dvberkel.game.tictactoe.TicTacToe.WinningRule.winWhenSameTokenAt;

public class TicTacToe implements Node<TicTacToe.Token, TicTacToe.Position> {
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

    public static enum Token {
        Cross {
            @Override
            public String toString() {
                return "x";
            }
        },
        Dot {
            @Override
            public String toString() {
                return "o";
            }
        },
        Empty {
            @Override
            public String toString() {
                return " ";
            }
        }
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
                Empty, Empty, Empty,
                Empty, Empty, Empty,
                Empty, Empty, Empty,
        });
    }

    private TicTacToe(Token[] tokens) {
        this.tokens = tokens;
    }

    private Optional<TicTacToe> place(int index, Token token) {
        if (tokens[index].equals(Empty)) {
            Token[] placedTokens = Arrays.copyOf(tokens, tokens.length);
            placedTokens[index] = token;
            return Optional.of(new TicTacToe(placedTokens));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Position> playablePositions() {
        return IntStream.range(0, tokens.length)
                .filter(index -> tokens[index].equals(Empty))
                .mapToObj(index -> Position.from(index))
                .collect(Collectors.toList());
    }

    @Override
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

    public String format() {
        return String.format("%s|%s|%s\n" +
                "-+-+-\n" +
                "%s|%s|%s\n" +
                "-+-+-\n" +
                "%s|%s|%s\n", tokens
        );
    }

    static class WinningRule {
        public static WinningRule winWhenSameTokenAt(Position... positions) {
            return new WinningRule(positions);
        }

        private final Position[] positions;

        private WinningRule(Position[] positions) {
            this.positions = positions;
        }

        public boolean applies(TicTacToe node) {
            Token target = node.tokens[positions[0].index];
            return !target.equals(Empty) && Arrays.stream(positions)
                    .allMatch(position -> node.tokens[position.index].equals(target));
        }

        public Token winner(TicTacToe node) {
            return node.tokens[positions[0].index];
        }
    }

    public class PositionHeuristic implements Heuristic<TicTacToe> {
        @Override
        public Value evaluate(TicTacToe node) {
            Token tokenToPlay = tokenToPlay();

            int score = 0;
            score += 1000 * contributionFor(tokenToPlay, C);
            score += 500 * contributionFor(tokenToPlay, NW, NE, SE, SW);
            score += 300 * contributionFor(tokenToPlay, N, E, S, W);

            return Value.of(score);
        }

        private int contributionFor(Token tokenToPlay, Position... positions) {
            int contribution = 0;
            for (Position position: positions) {
                Token token = tokens[position.index];
                if (!token.equals(Empty)) {
                    contribution += token.equals(tokenToPlay) ? 1 : -1;
                }
            }
            return contribution;
        }
    }

}
