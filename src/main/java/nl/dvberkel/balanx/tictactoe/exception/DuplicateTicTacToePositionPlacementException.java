package nl.dvberkel.balanx.tictactoe.exception;

import nl.dvberkel.balanx.tictactoe.TicTacToe;

public class DuplicateTicTacToePositionPlacementException extends Exception {
    public DuplicateTicTacToePositionPlacementException(TicTacToe.Position position) {
        super(String.format("token is already placed at position %s", position));
    }
}
