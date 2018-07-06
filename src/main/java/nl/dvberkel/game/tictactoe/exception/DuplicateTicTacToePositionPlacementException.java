package nl.dvberkel.game.tictactoe.exception;

import nl.dvberkel.game.tictactoe.TicTacToe;

public class DuplicateTicTacToePositionPlacementException extends Exception {
    public DuplicateTicTacToePositionPlacementException(TicTacToe.Position position) {
        super(String.format("token is already placed at position %s", position));
    }
}
