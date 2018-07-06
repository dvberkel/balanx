package nl.dvberkel.balanx.tictactoe;

import nl.dvberkel.balanx.Score;

import java.util.Optional;

import static nl.dvberkel.balanx.Score.*;

public class Evaluator implements nl.dvberkel.balanx.Evaluator<TicTacToe, TicTacToe.Token> {

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
