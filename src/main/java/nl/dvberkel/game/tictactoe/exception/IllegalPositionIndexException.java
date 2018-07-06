package nl.dvberkel.game.tictactoe.exception;

public class IllegalPositionIndexException extends RuntimeException {
    public IllegalPositionIndexException(int index) {
        super(String.format("index %d is not a valid Position index", index));
    }
}
